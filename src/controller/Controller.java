package controller;

import model.Cards;
import model.Wonder;
import model.Player;
import model.PlayerAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Controller {

	public Random random;

	public Controller() {
		random = new Random();
		Cards.buildDependencyMap();
		int numPlayers=7;
		newGame(numPlayers);
	}

	public void newGame(int numPlayers) {
		Wonder[] w = new Wonder[numPlayers];
		Cards[][] c=new Cards[numPlayers][7];
		Player[] p = new Player[numPlayers];
		ArrayList<Cards> discardPile = new ArrayList<Cards>();
		shuffleWonders(w);
		for (int i=0;i<numPlayers;i++) p[i]=new Player(i,w[i]);
		p[0].right=p[numPlayers-1]; p[numPlayers-1].left=p[0];
		for (int i=0;i<numPlayers-1;i++) {p[i].left=p[i+1]; p[i+1].right=p[i];}
		for (int age=1;age<=3;age++) {
			System.out.println("Age "+age);
			shuffleCards(c,age);
			ArrayList<Cards>[] cc = new ArrayList[numPlayers];
			for (int j=0;j<numPlayers;j++) {
				cc[j]=new ArrayList<Cards>();
				cc[j].addAll(Arrays.asList(c[j]));
			}
			for (int j=0;j<6;j++) {
				System.out.println("Turn "+(j+1));
				for(int k=0;k<numPlayers;k++) {
//					System.out.println("Player "+(k+1));
					if (age==1||age==3) p[k].getAction(cc[(j+numPlayers-k)%numPlayers]);
					else p[k].getAction(cc[(k+j)%numPlayers]);
				}
				for (Player pp:p) {
					if (pp.action==PlayerAction.CARD) pp.playedCards.add(pp.lastCard);
					else if (pp.action==PlayerAction.COIN) discardPile.add(pp.lastCard);
				}
			}
			for (int k=0;k<numPlayers;k++) discardPile.add(cc[k].get(0));
			System.out.print("Discard pile: ");
			for (Cards d:discardPile) System.out.print(d.name+",");
			System.out.println();
			int[] warResult = new int[numPlayers];
			for (int j=0;j<numPlayers-1;j++) {
				if (p[j].numShield>p[j+1].numShield) {
					p[j].victoryToken+=2*age-1;p[j+1].defeatToken++;
					warResult[j]+=2*age-1;warResult[j+1]--;
				} else if (p[j+1].numShield>p[j].numShield) {
					p[j+1].victoryToken+=2*age-1;p[j].defeatToken++;
					warResult[j+1]+=2*age-1;warResult[j]--;
				}
			}
			if (p[0].numShield>p[numPlayers-1].numShield) {
				p[0].victoryToken+=2*age-1;p[numPlayers-1].defeatToken++;
				warResult[0]+=2*age-1;warResult[numPlayers-1]--;
			} else if (p[numPlayers-1].numShield>p[0].numShield) {
				p[numPlayers-1].victoryToken+=2*age-1;p[0].defeatToken++;
				warResult[numPlayers-1]+=2*age-1;warResult[0]--;
			}
			System.out.print("War results: (");
			for (int j=0;j<numPlayers;j++) System.out.print(p[j].numShield+",");
			System.out.print(") (");
			for (int j=0;j<numPlayers;j++) System.out.print(warResult[j]+",");
			System.out.println(")");
		}
	}

	public void shuffleWonders(Wonder[] w) {
		int numPlayers=w.length;
		ArrayList<Wonder> wonders = new ArrayList<Wonder>();
		wonders.addAll(Arrays.asList(Wonder.wonders));
		for (int i=0;i<numPlayers;i++) {
			int j=random.nextInt(wonders.size());
			w[i]=wonders.remove(j);
			System.out.println(w[i].name);
		}

	}

	public void shuffleCards(Cards[][] c,int age) {
		int numPlayers=c.length;
		int groups=numPlayers-2;
		ArrayList<Cards> ls = new ArrayList<Cards>();
		ArrayList<Cards> guilds = new ArrayList<Cards>();
		
		if (age==1) {
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age1group3)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age1group4)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age1group5)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age1group6)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age1group7)); groups--;}
		} else if (age==2) {
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age2group3)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age2group4)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age2group5)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age2group6)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age2group7)); groups--;}
		} else if (age==3) {
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age3group3)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age3group4)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age3group5)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age3group6)); groups--;}
			if (groups!=0) { ls.addAll(Arrays.asList(Cards.age3group7)); groups--;}
			guilds.addAll(Arrays.asList(Cards.age3guilds));
			for (int i=0;i<numPlayers+2;i++) {
				int j=random.nextInt(guilds.size());
				ls.add(guilds.remove(j));
			}
		}
		for (int i=0;i<numPlayers;i++) {
			for (int k=0;k<7;k++) {
				int j=random.nextInt(ls.size());
				c[i][k]=ls.remove(j);
//				System.out.println(i+" "+c[i][k].name);
			}
		}

	}

	public static void main(String[] args ) {
		Controller con = new Controller();

	}
}
