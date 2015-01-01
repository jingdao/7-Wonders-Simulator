package view;

import java.util.ArrayList;
import model.Wonder;
import model.WonderStage;
import model.Cards;
import model.Player;
import model.PlayerAction;

public class CommandLine implements CardView {

	public boolean debugLog;

	public CommandLine(boolean debugLog) {
		this.debugLog=debugLog;
	}

	public void message(String s) {
		if (debugLog) System.out.println(s);
	}

	public void displayWonders(Wonder[] w) {
		if (debugLog)for (Wonder ww:w) System.out.println(ww.name);
	}

	public void displayAge(int age) {
		if (debugLog) System.out.println("Age "+age);
	}

	public void displayPlayerName(String s) {
//		if (debugLog) System.out.println("Player "+s);
	}

	public void displayTurn(int turn) {
		if (debugLog) System.out.println("Turn "+turn);
	}

	public void displayDiscardPile(ArrayList<Cards> discardPile) {
		if (debugLog) {
			System.out.print("Discard pile: ");
			for (Cards d:discardPile) System.out.print(d.name+",");
			System.out.println();
		}
	}

	public void displayWarResults(Player[] p,int[] warResult) {
		if (debugLog) {
			int numPlayers=p.length;
			System.out.print("War results: (");
			for (int j=0;j<numPlayers;j++) System.out.print(p[j].numShield+",");
			System.out.print(") (");
			for (int j=0;j<numPlayers;j++) System.out.print(warResult[j]+",");
			System.out.println(")");
		}
	}

	public void displayPayment(String src,String dest,int amount){
		if (debugLog) System.out.println(src+" payed "+amount+" coin to "+dest);
	}

	public void displayScore(Player[] p,ArrayList<Integer> winner,ArrayList<Integer> totalScore,int[][] scoreCategories) {
		if (debugLog) {
			System.out.println("Player | BROWN GRAY YELLOW BLUE GREEN RED PURPLE | VICTORY DEFEAT");
			for (Player pp:p) {
				if (debugLog) System.out.printf("%6d | %5d %4d %6d %4d %5d %3d %6d | %7d %6d\n",pp.id,pp.numBrown,pp.numGray,pp.numYellow,pp.numBlue,pp.numGreen,pp.numRed,pp.numPurple,pp.victoryToken,pp.defeatToken);
			}
			System.out.println("Player | Military Coin Wonder Civilian Science Commercial Guild | Total");
			for (int i=0;i<p.length;i++) {
				System.out.printf("%6d | %8d %4d %6d %8d %7d %10d %5d | %5d\n",i,
					scoreCategories[0][i],
					scoreCategories[1][i],
					scoreCategories[2][i],
					scoreCategories[3][i],
					scoreCategories[4][i],
					scoreCategories[5][i],
					scoreCategories[6][i],
					totalScore.get(i));
			}
			if (winner.size()==1) System.out.println("The winner is Player "+winner.get(0)+"!");
			else {
				System.out.print("The winners are");
				for (Integer i:winner) {
					System.out.print(" Player "+i);
				}
				System.out.println("!");
			}
		}

	}

	public void displayCards(ArrayList<Cards> cards,int[] playableCost) {
		int i=0;
		for(Cards c:cards) {
			if (playableCost[i]==0) System.out.print(c.name+",");
			else if (playableCost[i]<0) System.out.print(c.name+"(x),");
			else System.out.print(c.name+"("+playableCost[i]+"),");
			i++;
		}
		System.out.println("");
	}

	public void displayResources(Player p) {
		System.out.println("Resources: "+p.numClay+","+p.numOre+","+p.numStone+","+p.numWood+","+p.numGlass+","+p.numLoom+","+p.numPapyrus);
		System.out.println("           "+p.numCoin+","+p.numShield+","+p.numGear+","+p.numCompass+","+p.numTablet+","+p.numWonderStages);
		System.out.println(p.resourceDescription+p.commerceDescription);
		if (p.hasFreeBuild>0) System.out.println("FREE BUILD");
	}

	public void displayNeighborResources(String name,Player p) {
		System.out.println(name);
		System.out.println("Resources: "+p.numClay+","+p.numOre+","+p.numStone+","+p.numWood+","+p.numGlass+","+p.numLoom+","+p.numPapyrus);
		System.out.println("           "+p.numCoin+","+p.numShield+","+p.numGear+","+p.numCompass+","+p.numTablet+","+p.numWonderStages);
		for (Cards c:p.playedCards) System.out.print(c.name+",");
		System.out.println();
		System.out.println(p.resourceDescription);
	}

	public void showDiscardAction(String src) {
		if (debugLog) System.out.println(src+" discarded a card for 3 coin");
	}

	public void showWonderAction(WonderStage w,int numWonderStages,String name) {
		if (debugLog) {
			System.out.print(name+" built Wonder Stage "+numWonderStages+" ");
			if (w.numCoin>0) {
				System.out.print("+"+w.numCoin+" COIN ");
			}
			if (w.numShield>0) {
				System.out.print("+"+w.numShield+" SHIELD ");
			}
			System.out.println();
		}
	}

	public void showCardAction(String cardName,int dCoin,String playerName) {
		if (debugLog) {
			System.out.print(playerName+" played "+cardName);
			if (dCoin>0) System.out.print(" for "+dCoin+" coin");
			System.out.println();
		}
	}

	public void selectWonderSide(Player p) {
		while (true) {
			System.out.print("Choose Wonder side (0: side A, 1: side B)>>>");
			try { 
				int wonderSide = Integer.parseInt(System.console().readLine());
				if (wonderSide==0) p.isWonderBSide=false;
				else if (wonderSide==1) p.isWonderBSide=true;
				else continue;
				break;
			}
			catch (NumberFormatException e) {}
		}
	}

	public void selectFromDiscard(Player p,ArrayList<Cards> discardPile, ArrayList<Cards> selection) {
		System.out.print("Choose a card from the discard pile: (");
		for (int i=0;i<discardPile.size();i++) if (selection.get(i)!=null) System.out.print(i+":"+discardPile.get(i).name+" ");
		System.out.println(")");
		while (true) {
			System.out.print(">>>");
			try {
				int choice = Integer.parseInt(System.console().readLine());
				if (choice>=0&&choice<discardPile.size()&&selection.get(choice)!=null) {
					if (debugLog) System.out.println("Extra turn playing from discard");
					Cards c=discardPile.remove(choice);
					p.playedCards.add(c);
					p.applyCardEffect(c);
					break;
				}
			}
			catch (NumberFormatException e) {}
		}
	}

	public void selectGuild(Player p,ArrayList<Cards> guildChoices) {
		System.out.print("Choose a guild to copy: (");
		for (int i=0;i<guildChoices.size();i++) System.out.print(i+":"+guildChoices.get(i).name+" ");
		System.out.println(")");
		while (true) {
			System.out.print(">>>");
			try {
				int choice = Integer.parseInt(System.console().readLine());
				if (choice>=0&&choice<guildChoices.size()) {
					p.playedCards.add(guildChoices.get(choice));
					break;
				}
			}
			catch (NumberFormatException e) {}
		}
	}

	public void selectAction(Player p,ArrayList<Cards> cards) {
		while (true) {
			System.out.print("Choose action (0:card 1:wonder 2:discard 3:look)>>>");
			try { 
				p.action = PlayerAction.values()[Integer.parseInt(System.console().readLine())];
				if (p.action==PlayerAction.WONDER) { 
					if (p.canBuildWonder) {
						if (p.wonderOptions!=null) selectTrading(p,p.wonderOptions);
						break;
					} else {
						WonderStage[] wonderSide;
						if (p.isWonderBSide) wonderSide=p.wonder.stagesB;
						else wonderSide=p.wonder.stagesA;
						if (p.numWonderStages>=wonderSide.length) System.out.println("You have already constructed all "+wonderSide.length+" stages of your wonder.");
						else System.out.println("You do not have enough resources to construct your next wonder");
					}
				} else if (p.action==PlayerAction.CARD||p.action==PlayerAction.COIN) {
					break;
				} else if (p.action==PlayerAction.NUMTYPES) selectLookAction(p,cards);
			}
			catch (ArrayIndexOutOfBoundsException e) {}
			catch (NumberFormatException e) {}
		}
		selectCard(p,cards);
	}

	public void selectLookAction(Player p,ArrayList<Cards> cards) {
		while (true) {
			System.out.print("Look at (-1:neighbors 0:cardsPlayed 1-"+cards.size()+":hand)>>>");
			try { 
				int subAction = Integer.parseInt(System.console().readLine());
				if (subAction==-1) {
					displayNeighborResources("Left",p.left);
					displayNeighborResources("Right",p.right);
					break;
				} else if (subAction==0) {
					for (Cards c:p.playedCards) System.out.print(c.name+",");
					System.out.println();
					break;
				} else if (subAction<=cards.size()) {
					System.out.println(cards.get(subAction-1).getDescription());
					break;
				}
			}
			catch (NumberFormatException e) {}
		}
	}

	public void selectTrading(Player p, ArrayList<Integer> options) {
		System.out.print("Choose trading option (");
		for (int j=0;j<options.size();j++) {
			System.out.print(j+":"+(options.get(j)/100)+"&"+(options.get(j)%100)+" ");
		}
		System.out.println(")");
		while (true) {
			System.out.print(">>>");
			try {
				int tradingOption = Integer.parseInt(System.console().readLine());
				if (tradingOption>=0&&tradingOption<options.size()) {
					p.leftCost=options.get(tradingOption)/100;
					p.rightCost=options.get(tradingOption)%100;
					break;
				}
			} catch (NumberFormatException e) {}
		}
	}

	public void selectCard(Player p,ArrayList<Cards> cards) {
		int cardPlayed = -1;
		while (true) {
			System.out.print("Choose card (1-"+cards.size()+")>>>");
			try { 
				cardPlayed = Integer.parseInt(System.console().readLine())-1;
				if (cardPlayed>=0&&cardPlayed<cards.size()) {
					if (p.action!=PlayerAction.CARD) break;
					if (p.playableCost[cardPlayed]!=-2&&p.hasFreeBuild>0) {
						System.out.print("Use free build? (1:yes 0:no)>>>");
						try {
							if(Integer.parseInt(System.console().readLine())==1) {
								p.hasFreeBuild=0; break;
							}
						}
						catch (NumberFormatException e) {}
					}
					if (p.playableCost[cardPlayed]<0) {
						if (p.playedCards.contains(cards.get(cardPlayed))) System.out.println("You cannot build 2 identical structures");
						else System.out.println("You do not have enough resources");
					}
					else if (p.playableCost[cardPlayed]>0){
						ArrayList<Integer> a = p.resourceOptions.get(cardPlayed);
						selectTrading(p,a);
						break;
					} else break;
				}			
			}
			catch (NumberFormatException e) {}
		}
		p.lastCard = cards.remove(cardPlayed);
	}
}
