package model;

import java.util.ArrayList;
import java.io.Console;
import controller.Controller;

public class Player {

	public Player left,right;
	public int id,numClay,numOre,numStone,numWood,numGlass,numLoom,numPapyrus;
	public int numCoin,numShield,numWonderStages;
	public int numGear,numCompass,numTablet;
	public int victoryToken,defeatToken;
	public int numBrown,numGray,numYellow,numBlue,numGreen,numRed,numPurple;
	public int numRawMaterials,numManufacturedGoods;
	public int numDualResource,numDualClay,numDualOre,numDualStone,numDualWood;
	public Cards lastCard;
	public PlayerAction action;
	public Wonder wonder;
	public ArrayList<Cards> playedCards;
	boolean[] playable;
	public boolean isWonderBSide;

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
		if (id==0&&Controller.debugLog) {
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

	public void getAction(ArrayList<Cards> cards,boolean debugLog) {
		checkResources(cards);
		action = PlayerAction.CARD;
		int cardPlayed = -1;
		if (id==0&&debugLog) {
			int i=0;
			for(Cards c:cards) {
				if (playable[i++]) System.out.print(c.name+",");
				else System.out.print(c.name+"(x),");
			}
			System.out.println("");
			System.out.println("Resources: "+numClay+","+numOre+","+numStone+","+numWood+","+numGlass+","+numLoom+","+numPapyrus);
			System.out.println("           "+numCoin+","+numShield+","+numGear+","+numCompass+","+numTablet+","+numWonderStages);
			while (true) {
				System.out.print("Choose action (0:card 1:wonder 2:discard 3:look)>>>");
				try { 
					action = PlayerAction.values()[Integer.parseInt(System.console().readLine())];
					if (action==PlayerAction.WONDER) { 
						if (checkWonder(true)) break;
					} else if (action==PlayerAction.CARD||action==PlayerAction.COIN) {
						break;
					} else if (action==PlayerAction.NUMTYPES) {
						while (true) {
							System.out.print("Look at (-1:neighbors 0:cardsPlayed 1-"+cards.size()+":hand)>>>");
							try { 
								int subAction = Integer.parseInt(System.console().readLine());
								if (subAction==-1) {
									System.out.println("Left");
									System.out.println("Resources: "+left.numClay+","+left.numOre+","+left.numStone+","+left.numWood+","+left.numGlass+","+left.numLoom+","+left.numPapyrus);
									for (Cards c:left.playedCards) System.out.print(c.name+",");
									System.out.println("\nRight");
									System.out.println("Resources: "+right.numClay+","+right.numOre+","+right.numStone+","+right.numWood+","+right.numGlass+","+right.numLoom+","+right.numPapyrus);
									for (Cards c:right.playedCards) System.out.print(c.name+",");
									System.out.println();
									break;
								} else if (subAction==0) {
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
			if (cardPlayed==-1) {
				cardPlayed=0;
				if (checkWonder(false)) action=PlayerAction.WONDER;
				else action=PlayerAction.COIN;
			}
		};

		lastCard = cards.remove(cardPlayed);
		if (action==PlayerAction.CARD) {
//			playedCards.add(c);
			numCoin-=lastCard.costCoin;
			applyCardEffect(lastCard,debugLog);
		} else if (action==PlayerAction.WONDER) {
			WonderStage[] wonderSide;
			if (isWonderBSide) wonderSide=wonder.stagesB;
			else wonderSide=wonder.stagesA;
			numWonderStages++;
			if (debugLog) System.out.print("Player "+id+" built Wonder Stage "+numWonderStages+"      ");
			if (wonderSide[numWonderStages-1].numCoin>0) {
				numCoin+=wonderSide[numWonderStages-1].numCoin;
				if (debugLog) System.out.print("+"+wonderSide[numWonderStages-1].numCoin+" COIN ");
			}
			if (wonderSide[numWonderStages-1].numShield>0) {
				numShield+=wonderSide[numWonderStages-1].numShield;
				if (debugLog) System.out.print("+"+wonderSide[numWonderStages-1].numShield+" SHIELD ");
			}
			if (wonderSide[numWonderStages-1].special==SpecialResource.RAW_MATERIALS) numRawMaterials++;
			else if (wonderSide[numWonderStages-1].special==SpecialResource.MANUFACTURED_GOODS) numManufacturedGoods++;
			if (debugLog) System.out.println();
		} else if (action==PlayerAction.COIN) {
			numCoin+=3;
			if (debugLog) System.out.println("Player "+id+" discarded a card for 3 coin");
		}
	}

	public void checkResources(ArrayList<Cards> cards) {
		playable=new boolean[cards.size()];
		for (int i=0;i<cards.size();i++) {
			if (playedCards.contains(cards.get(i))) continue;
			if (cards.get(i).dependency!=null) {
				for (Cards c:playedCards) {
					if (c.name.endsWith(cards.get(i).dependency)) {
						playable[i]=true;
						break;
					}
				}
				if (playable[i]) continue;
			}
			if (cards.get(i).costCoin>numCoin) continue;
			int needClay=cards.get(i).costClay, needOre=cards.get(i).costOre,
				needStone=cards.get(i).costStone, needWood=cards.get(i).costWood,
				needGlass=cards.get(i).costGlass, needLoom=cards.get(i).costLoom, needPapyrus=cards.get(i).costPapyrus;
			needClay=Math.max(0,needClay-numClay);
			needOre=Math.max(0,needOre-numOre);
			needStone=Math.max(0,needStone-numStone);
			needWood=Math.max(0,needWood-numWood);
			needGlass=Math.max(0,needGlass-numGlass);
			needLoom=Math.max(0,needLoom-numLoom);
			needPapyrus=Math.max(0,needPapyrus-numPapyrus);
//			if (id==0) System.out.println(needClay+","+needOre+","+needStone+","+needWood+","+needGlass+","+needLoom+","+needPapyrus);
			if (needGlass+needLoom+needPapyrus>numManufacturedGoods) continue;
			if (needClay+needOre+needStone+needWood>numRawMaterials) continue;
			playable[i]=true;
		}
	}

	public boolean checkWonder(boolean debugLog) {
		WonderStage[] wonderSide;
		if (isWonderBSide) wonderSide=wonder.stagesB;
		else wonderSide=wonder.stagesA;
		if (numWonderStages>=wonderSide.length) {
			if (debugLog) System.out.println("You have already constructed all "+wonderSide.length+" stages of your wonder.");
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
			if (debugLog) System.out.println("You do not have enough resources to construct your next wonder.");
			return false;
		}
		return true;
	}

	public void applyCardEffect(Cards c,boolean debugLog) {
		if (c.name=="VINEYARD") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.BROWN) count++;
			for (Cards cc:left.playedCards) if (cc.type==CardType.BROWN) count++;
			for (Cards cc:right.playedCards) if (cc.type==CardType.BROWN) count++;
			numCoin+=count;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+count+" coin");
			return;
		} else if (c.name=="HAVEN") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.BROWN) count++;
			numCoin+=count;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+count+" coin");
			return;
		} else if (c.name=="BAZAR") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.GRAY) count++;
			for (Cards cc:left.playedCards) if (cc.type==CardType.GRAY) count++;
			for (Cards cc:right.playedCards) if (cc.type==CardType.GRAY) count++;
			numCoin+=count*2;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+(count*2)+" coin");
			return;
		} else if (c.name=="CHAMBER OF COMMERCE") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.GRAY) count++;
			numCoin+=count*2;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+(count*2)+" coin");
			return;
		} else if (c.name=="LIGHTHOUSE") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.YELLOW) count++;
			numCoin+=count;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+count+" coin");
			return;
		} else if (c.name=="ARENA") {
			numCoin+=numWonderStages*3;
			if (debugLog) System.out.println("Player "+id+" played "+c.name+" for "+(numWonderStages*3)+" coin");
			return;
		} else if (c.name=="CARAVANSERY") numRawMaterials++;
		else if (c.name=="FORUM") numManufacturedGoods++;
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
		if (debugLog) System.out.println("Player "+id+" played "+c.name);
	}

	public void countCards() {
		for (Cards c:playedCards) {
			if (c.type==CardType.BROWN) numBrown++;
			else if (c.type==CardType.GRAY) numGray++;
			else if (c.type==CardType.YELLOW) numYellow++;
			else if (c.type==CardType.BLUE) numBlue++;
			else if (c.type==CardType.GREEN) numGreen++;
			else if (c.type==CardType.RED) numRed++;
			else  numPurple++;
		}
	}
}
