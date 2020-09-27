package model;

import java.util.ArrayList;
import view.CardView;
import java.util.HashSet;
import controller.Controller;

public class Bot extends Player {
	
	public boolean forceWonder=true;

	public Bot(int id,Wonder w,CardView v) {
		super(id,w,v);
	}

	public void selectWonderSide() {
		isWonderBSide=Controller.random.nextInt(2)==0;
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
		playedCard = cards.get(cardPlayed);
		lastCard = cards.remove(cardPlayed);
	}
	
	public void copyGuild() {
		ArrayList<Cards> guildChoices = new ArrayList<Cards>();
		for (Cards c:left.playedCards) if (c.type==CardType.PURPLE) guildChoices.add(c);
		for (Cards c:right.playedCards) if (c.type==CardType.PURPLE) guildChoices.add(c);
		if (guildChoices.size()>0) playedCards.add(guildChoices.get(0));
	}

	public void playFromDiscard(ArrayList<Cards> discardPile) {
		canPlayFromDiscard=false;
		ArrayList<Cards> selection = new ArrayList<Cards>();
		HashSet<Cards> selected = new HashSet<Cards>();
		for (Cards c:discardPile) {
			if (playedCards.contains(c)) selection.add(null);
			else if (selected.contains(c)) selection.add(null);
			else {selection.add(c); selected.add(c);}
		}
		if (selected.size()>0) {
			for (int i=0;i<discardPile.size();i++){
				if (selection.get(i)!=null) {
					Cards c = discardPile.remove(i);
					playedCards.add(c);
					applyCardEffect(c);
					break;
				}
			}
		}
	}
}
