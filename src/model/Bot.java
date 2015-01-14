package model;

import java.util.ArrayList;
import view.CardView;
import controller.Controller;

public class Bot extends Player {
	
	public boolean forceWonder=true;

	public Bot(int id,Wonder w,CardView v) {
		super(id,w,v);
	}

	public void getAction(ArrayList<Cards> cards) {
		checkResources(cards);
		canBuildWonder=checkWonder();
		action = PlayerAction.CARD;
		int cardPlayed = -1;
		if (forceWonder&&canBuildWonder) {
			action=PlayerAction.WONDER;
			cardPlayed=0;
			if (wonderOptions!=null) {
				int minLeft=50,minRight=50;
				for (int j:wonderOptions) {
					if (j/100+j%100<minLeft+minRight) {
						minLeft=j/100;
						minRight=j%100;
					}
				}
				leftCost=minLeft;
				rightCost=minRight;
			}
			lastCard = cards.remove(cardPlayed);
			return;
		}
		for (int i=0;i<cards.size();i++) {
			if (hasFreeBuild>0&&playableCost[i]!=-2) {hasFreeBuild=0;cardPlayed=i;break;}
			else if (playableCost[i]==0) {cardPlayed=i; break;}
			else if (playableCost[i]>0) {
				ArrayList<Integer> a = resourceOptions.get(i);
				for (int j:a) {
					if ((j/100+j%100)==playableCost[i]) {
						leftCost=j/100;
						rightCost=j%100;
						break;
					}
				}
				cardPlayed=i;
				break;
			}
		}
		if (cardPlayed==-1) {
			cardPlayed=0;
			if (canBuildWonder) {
				action=PlayerAction.WONDER;
				if (wonderOptions!=null) {
					int minLeft=50,minRight=50;
					for (int j:wonderOptions) {
						if (j/100+j%100<minLeft+minRight) {
							minLeft=j/100;
							minRight=j%100;
						}
					}
					leftCost=minLeft;
					rightCost=minRight;
				}
			} else
				action=PlayerAction.COIN;
		}
		lastCard = cards.remove(cardPlayed);
	}
}
