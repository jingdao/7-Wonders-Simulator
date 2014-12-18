package model;

import java.util.ArrayList;
import java.io.Console;

public class Player {

	public int id,numClay,numOre,numStone,numWood,numGlass,numLoom,numPapyrus;
	public int numCoin,numShield,numWonderStages;
	public int numCompass,numGear,numTablet;
	public int victoryToken,defeatToken;
	Wonder wonder;
	ArrayList<Cards> playedCards;
	boolean[] playable;
	boolean isWonderBSide;

	public Player(int id,Wonder w) {
		this.id=id;
		this.wonder=w;
		this.playedCards=new ArrayList<Cards>();
		this.numCoin=3;
		switch (w.startingResource) {
			case CLAY: numClay++; break;
			case ORE: numOre++; break;
			case STONE: numStone++; break;
			case WOOD: numWood++; break;
			case GLASS: numGlass++; break;
			case LOOM: numLoom++; break;
			case PAPYRUS: numPapyrus++; break;
		}
		if (id==0) {
			while (true) {
				System.out.print("Choose Wonder side (0: side A, 1: side B)>>>");
				try { 
					int wonderSide = Integer.parseInt(System.console().readLine());
					if (wonderSide==0) isWonderBSide=false;
					else if (wonderSide==1) isWonderBSide=true;
					else continue;
					break;
				}
				catch (NumberFormatException e) {}
			}
		}
	}

	public PlayerAction getAction(ArrayList<Cards> cards) {
		checkResources(cards);
		PlayerAction action = PlayerAction.CARD;
		int cardPlayed = -1;
		if (id==0) {
			int i=0;
			for(Cards c:cards) {
				if (playable[i++]) System.out.print(c.name+",");
				else System.out.print(c.name+"(x),");
			}
			System.out.println("");
			System.out.println("Resources: "+numClay+","+numOre+","+numStone+","+numWood+","+numGlass+","+numLoom+","+numPapyrus);
			System.out.println("           "+numCoin+","+numShield+","+numGear+","+numTablet+","+numCompass+","+numWonderStages);
			while (true) {
				System.out.print("Choose action (0:card 1:wonder 2:discard 3:look)>>>");
				try { 
					action = PlayerAction.values()[Integer.parseInt(System.console().readLine())];
					if (action==PlayerAction.WONDER) { 
						if (checkWonder()) break;
					} else if (action==PlayerAction.CARD||action==PlayerAction.COIN) {
						break;
					} else if (action==PlayerAction.NUMTYPES) {
						while (true) {
							System.out.print("Look at (0:cardsPlayed 1-"+cards.size()+":hand)>>>");
							try { 
								int subAction = Integer.parseInt(System.console().readLine());
								if (subAction==0) {
									for (Cards c:playedCards) System.out.print(c.name+",");
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
				}
				catch (ArrayIndexOutOfBoundsException e) {}
				catch (NumberFormatException e) {}
			}
			while (true) {
				System.out.print("Choose card (1-"+cards.size()+")>>>");
				try { 
					cardPlayed = Integer.parseInt(System.console().readLine())-1;
					if (cardPlayed>=0&&cardPlayed<cards.size()) {
						if (action!=PlayerAction.CARD) break;
						else if (!playable[cardPlayed]) {
							if (playedCards.contains(cards.get(cardPlayed))) System.out.println("You cannot build 2 identical structures");
							else System.out.println("You do not have enough resources");
						}
						else break;
					}			
				}
				catch (NumberFormatException e) {}
			}

		} else {
			for (int i=0;i<cards.size();i++) {
				if (playable[i]) {cardPlayed=i; break;}
			}
			if (cardPlayed==-1) {action=PlayerAction.COIN; cardPlayed=0;}
		};

		Cards c = cards.remove(cardPlayed);
		if (action==PlayerAction.CARD) {
			playedCards.add(c);
			numCoin-=c.costCoin;
			applyCardEffect(c);
			System.out.println("Player "+id+" played "+c.name);
		} else if (action==PlayerAction.WONDER) {
			numWonderStages++;
			System.out.println("Player "+id+" built wonder stage "+numWonderStages);
		} else if (action==PlayerAction.COIN) {
			numCoin+=3;
			System.out.println("Player "+id+" discarded a card for 3 coin");
		}
		return action; 
	}

	public void checkResources(ArrayList<Cards> cards) {
		playable=new boolean[cards.size()];
		for (int i=0;i<cards.size();i++) {
			if (playedCards.contains(cards.get(i))) continue;
			if (cards.get(i).costCoin>numCoin) continue;
			if (cards.get(i).costClay>numClay) continue;
			if (cards.get(i).costOre>numOre) continue;
			if (cards.get(i).costStone>numStone) continue;
			if (cards.get(i).costWood>numWood) continue;
			if (cards.get(i).costGlass>numGlass) continue;
			if (cards.get(i).costLoom>numLoom) continue;
			if (cards.get(i).costPapyrus>numPapyrus) continue;
			playable[i]=true;
		}
	}

	public boolean checkWonder() {
		WonderStage[] wonderSide;
		if (isWonderBSide) wonderSide=wonder.stagesB;
		else wonderSide=wonder.stagesA;
		if (numWonderStages>=wonderSide.length) {
			System.out.println("You have already constructed all "+wonderSide.length+" stages of your wonder.");
			return false;
		}
		boolean enoughResources=true;
		if (wonderSide[numWonderStages].costClay>numClay) enoughResources=false;
		if (wonderSide[numWonderStages].costOre>numOre) enoughResources=false;
		if (wonderSide[numWonderStages].costStone>numStone) enoughResources=false;
		if (wonderSide[numWonderStages].costWood>numWood) enoughResources=false;
		if (wonderSide[numWonderStages].costGlass>numGlass) enoughResources=false;
		if (wonderSide[numWonderStages].costLoom>numLoom) enoughResources=false;
		if (wonderSide[numWonderStages].costPapyrus>numPapyrus) enoughResources=false;
		if (!enoughResources) {
			System.out.println("You do not have enough resources to construct your next wonder.");
			return false;
		}
		return true;
	}

	public void applyCardEffect(Cards c) {
		if (c.rtype==ResourceType.COIN) numCoin+=c.resourceValue;
		else if (c.rtype==ResourceType.WOOD) numWood+=c.resourceValue;
		else if (c.rtype==ResourceType.CLAY) numClay+=c.resourceValue;
		else if (c.rtype==ResourceType.ORE) numOre+=c.resourceValue;
		else if (c.rtype==ResourceType.STONE) numStone+=c.resourceValue;
		else if (c.rtype==ResourceType.LOOM) numLoom+=c.resourceValue;
		else if (c.rtype==ResourceType.GLASS) numGlass+=c.resourceValue;
		else if (c.rtype==ResourceType.PAPYRUS) numPapyrus+=c.resourceValue;
		else if (c.rtype==ResourceType.SHIELD) numShield+=c.resourceValue;
		else if (c.rtype==ResourceType.SCIENCE) {
			if (c.resourceValue==ScienceType.GEAR.ordinal()) numGear++;
			else if (c.resourceValue==ScienceType.TABLET.ordinal()) numTablet++;
			else if (c.resourceValue==ScienceType.COMPASS.ordinal()) numCompass++;
		}
	}
}
