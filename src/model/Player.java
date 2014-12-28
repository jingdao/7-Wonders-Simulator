package model;

import java.util.ArrayList;
import java.io.Console;
import controller.Controller;
import java.util.HashMap;
import java.util.HashSet;

public class Player {

	public Player left,right;
	public int id,numClay,numOre,numStone,numWood,numGlass,numLoom,numPapyrus;
	public int numCoin,numShield,numWonderStages;
	public int numGear,numCompass,numTablet;
	public int victoryToken,defeatToken;
	public int numBrown,numGray,numYellow,numBlue,numGreen,numRed,numPurple;
	public int leftTradingCostRaw=2,leftTradingCostManufactured=2,rightTradingCostRaw=2,rightTradingCostManufactured=2;
	public int leftCost,rightCost;
	public Cards lastCard;
	public PlayerAction action;
	public Wonder wonder;
	public ArrayList<Cards> playedCards;
	public HashMap<Integer,ArrayList<NeighborResource>> resourceMap = new HashMap<Integer,ArrayList<NeighborResource>>();
	public String resourceDescription="",commerceDescription="";
	public ArrayList<ArrayList<Integer>> resourceOptions = new ArrayList<ArrayList<Integer>>();
	int[] playableCost;
	boolean canBuildWonder;
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
		canBuildWonder=checkWonder();
		action = PlayerAction.CARD;
		int cardPlayed = -1;
		if (id==0&&debugLog) {
			int i=0;
			for(Cards c:cards) {
				if (playableCost[i]==0) System.out.print(c.name+",");
				else if (playableCost[i]<0) System.out.print(c.name+"(x),");
				else System.out.print(c.name+"("+playableCost[i]+"),");
				i++;
			}
			System.out.println("");
			System.out.println("Resources: "+numClay+","+numOre+","+numStone+","+numWood+","+numGlass+","+numLoom+","+numPapyrus);
			System.out.println("           "+numCoin+","+numShield+","+numGear+","+numCompass+","+numTablet+","+numWonderStages);
			System.out.println(resourceDescription+commerceDescription);
			while (true) {
				System.out.print("Choose action (0:card 1:wonder 2:discard 3:look)>>>");
				try { 
					action = PlayerAction.values()[Integer.parseInt(System.console().readLine())];
					if (action==PlayerAction.WONDER) { 
						if (canBuildWonder) break;
						else {
							WonderStage[] wonderSide;
							if (isWonderBSide) wonderSide=wonder.stagesB;
							else wonderSide=wonder.stagesA;
							if (numWonderStages>=wonderSide.length) System.out.println("You have already constructed all "+wonderSide.length+" stages of your wonder.");
							else System.out.println("You do not have enough resources to construct your next wonder");
						}
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
									System.out.println();
									System.out.println(left.resourceDescription);
									System.out.println("Right");
									System.out.println("Resources: "+right.numClay+","+right.numOre+","+right.numStone+","+right.numWood+","+right.numGlass+","+right.numLoom+","+right.numPapyrus);
									for (Cards c:right.playedCards) System.out.print(c.name+",");
									System.out.println();
									System.out.println(right.resourceDescription);
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
						else if (playableCost[cardPlayed]<0) {
							if (playedCards.contains(cards.get(cardPlayed))) System.out.println("You cannot build 2 identical structures");
							else System.out.println("You do not have enough resources");
						}
						else if (playableCost[cardPlayed]>0){
							System.out.print("Choose trading option (");
							ArrayList<Integer> a = resourceOptions.get(cardPlayed);
							for (int j=0;j<a.size();j++) {
								System.out.print(j+":"+(a.get(j)/100)+"&"+(a.get(j)%100)+" ");
							}
//							ArrayList<NeighborResource> a = (ArrayList<NeighborResource>)neighborResourceOptions[cardPlayed];
//							HashSet<Integer> h = new HashSet<Integer>();
//							for (int j=0;j<a.size();j++) {
//								NeighborResource n = a.get(j);
//								int lc=n.leftRaw*leftTradingCostRaw+n.leftManufactured*leftTradingCostManufactured;
//								int rc=n.rightRaw*rightTradingCostRaw+n.rightManufactured*rightTradingCostManufactured;
//								if (!h.contains(lc*100+rc)) {
//									h.add(lc*100+rc);
//									System.out.print(j+":"+lc+"&"+rc+" ");
//								}
//							}
							System.out.println(")");
							while (true) {
								System.out.print(">>>");
								try {
									int tradingOption = Integer.parseInt(System.console().readLine());
									if (tradingOption>=0&&tradingOption<a.size()) {
//										NeighborResource n = a.get(tradingOption);
//										leftCost=n.leftRaw*leftTradingCostRaw+n.leftManufactured*leftTradingCostManufactured;
//										rightCost=n.rightRaw*rightTradingCostRaw+n.rightManufactured*rightTradingCostManufactured;
										leftCost=a.get(tradingOption)/100;
										rightCost=a.get(tradingOption)%100;
										break;
									}
								} catch (NumberFormatException e) {}
							}
							break;
						} else break;
					}			
				}
				catch (NumberFormatException e) {}
			}

		} else {
			for (int i=0;i<cards.size();i++) {
				if (playableCost[i]==0) {cardPlayed=i; break;}
			}
			if (cardPlayed==-1) {
				cardPlayed=0;
				if (canBuildWonder) action=PlayerAction.WONDER;
				else action=PlayerAction.COIN;
			}
		};

		lastCard = cards.remove(cardPlayed);
		if (action==PlayerAction.CARD) {
//			playedCards.add(c);
			numCoin-=lastCard.costCoin;
//			applyCardEffect(lastCard,debugLog);
		} else if (action==PlayerAction.WONDER) {
			WonderStage[] wonderSide;
			if (isWonderBSide) wonderSide=wonder.stagesB;
			else wonderSide=wonder.stagesA;
			applyWonderEffect(wonderSide[numWonderStages],debugLog);
		} else if (action==PlayerAction.COIN) {
			numCoin+=3;
			if (debugLog) System.out.println("Player "+id+" discarded a card for 3 coin");
		}
	}

	public void checkResources(ArrayList<Cards> cards) {
		if (id==0) {
			for (Integer j:resourceMap.keySet()) {
				String s="";
				int ii=j;
				for (int i=0;i<7;i++) {
					s+=(ii%5)+",";
					ii=ii/5;
				}
				System.out.print(s+":");
				if (resourceMap.get(j)!=null) {
					for (NeighborResource n:resourceMap.get(j)) {
						System.out.print(n.leftRaw+""+n.leftManufactured+""+n.rightRaw+""+n.rightManufactured+":"+NeighborResource.getStringFromResourceCode(n.prerequisite)+",");
					}
				}
				System.out.println();
			}
		}
		playableCost=new int[cards.size()];
		resourceOptions = new ArrayList<ArrayList<Integer>>();
		leftCost=0;
		rightCost=0;
		for (int i=0;i<cards.size();i++) {
			playableCost[i]=-1;
			if (playedCards.contains(cards.get(i))) {playableCost[i]=-1;continue;}
			if (cards.get(i).dependency!=null) {
				for (Cards c:playedCards) {
					if (c.name.endsWith(cards.get(i).dependency)) {
						playableCost[i]=0;
						break;
					}
				}
				if (playableCost[i]==0) continue;
			}
			if (cards.get(i).costCoin>numCoin) {playableCost[i]=-1;continue;}
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
//			if (needGlass+needLoom+needPapyrus>numManufacturedGoods) continue;
//			if (needClay+needOre+needStone+needWood>numRawMaterials) continue;
			int k=needClay+needOre*5+needStone*25+needWood*125+needGlass*625+needLoom*3125+needPapyrus*15625;
			if (k==0) {playableCost[i]=0; resourceOptions.add(null);}
			else if (!resourceMap.containsKey(k)) {playableCost[i]=-1; resourceOptions.add(null);}
			else if (resourceMap.get(k)==null) {playableCost[i]=0; resourceOptions.add(null);}
			else {
//				neighborResourceOptions[i]=resourceMap.get(k);
//				playableCost[i]=100;
//				for (NeighborResource n:resourceMap.get(k)) {
//					int cost = n.leftRaw*leftTradingCostRaw+n.leftManufactured*leftTradingCostManufactured+
//								n.rightRaw*rightTradingCostRaw+n.rightManufactured*rightTradingCostManufactured;
//					playableCost[i]=Math.min(playableCost[i],cost);
//				}
				ArrayList<Integer> a = new ArrayList<Integer>(NeighborResource.getCost(k,leftTradingCostRaw,leftTradingCostManufactured,rightTradingCostRaw,rightTradingCostManufactured,resourceMap));
				int minCost=100;
				if (id==0) System.out.print(cards.get(i).name+":"+needClay+""+needOre+""+needStone+""+needWood+""+needGlass+""+needLoom+""+needPapyrus+":");
				for (int j:a) {
					if (id==0) System.out.print((j/100)+" "+(j%100)+",");
					minCost=Math.min(minCost,j/100+j%100);
				}
				if (id==0) System.out.println();
				resourceOptions.add(a);
				playableCost[i]=minCost;
			}
			if (playableCost[i]>numCoin) playableCost[i]=-1;
//			if (id==0) System.out.println(k+":"+playableCost[i]);
		}
	}

	public boolean checkWonder() {
		WonderStage[] wonderSide;
		if (isWonderBSide) wonderSide=wonder.stagesB;
		else wonderSide=wonder.stagesA;
		if (numWonderStages>=wonderSide.length) return false;
		int needClay=wonderSide[numWonderStages].costClay, needOre=wonderSide[numWonderStages].costOre,
			needStone=wonderSide[numWonderStages].costStone, needWood=wonderSide[numWonderStages].costWood,
			needGlass=wonderSide[numWonderStages].costGlass, needLoom=wonderSide[numWonderStages].costLoom, needPapyrus=wonderSide[numWonderStages].costPapyrus;
		needClay=Math.max(0,needClay-numClay);
		needOre=Math.max(0,needOre-numOre);
		needStone=Math.max(0,needStone-numStone);
		needWood=Math.max(0,needWood-numWood);
		needGlass=Math.max(0,needGlass-numGlass);
		needLoom=Math.max(0,needLoom-numLoom);
		needPapyrus=Math.max(0,needPapyrus-numPapyrus);
		int k=needClay+needOre*5+needStone*25+needWood*125+needGlass*625+needLoom*3125+needPapyrus*15625;
		if (k==0||resourceMap.containsKey(k)) return true;
		else return false;
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
		} else if (c.name=="CARAVANSERY") {int[] r={1,5,25,125};addDualResource(r);commerceDescription+="CLAY/ORE/STONE/WOOD,";}
		else if (c.name=="FORUM") {int[] r={625,3125,15625};addDualResource(r);commerceDescription+="GLASS/LOOM/PAPYRUS,";}
		if (c.rtype==ResourceType.COIN) numCoin+=c.resourceValue;
		else if (c.rtype==ResourceType.CLAY) {numClay+=c.resourceValue;for (int j=0;j<c.resourceValue;j++){int[] r={1};left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}}
		else if (c.rtype==ResourceType.ORE) {numOre+=c.resourceValue;for (int j=0;j<c.resourceValue;j++){int[] r={5};left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}}
		else if (c.rtype==ResourceType.STONE) {numStone+=c.resourceValue;for (int j=0;j<c.resourceValue;j++){int[] r={25};left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}}
		else if (c.rtype==ResourceType.WOOD) {numWood+=c.resourceValue;for (int j=0;j<c.resourceValue;j++){int[] r={125};left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}}
		else if (c.rtype==ResourceType.GLASS) {numGlass++;int[] r={625};left.addNeighborResource(r,false,false);right.addNeighborResource(r,true,false);}
		else if (c.rtype==ResourceType.LOOM) {numLoom++;int[] r={3125};left.addNeighborResource(r,false,false);right.addNeighborResource(r,true,false);}
		else if (c.rtype==ResourceType.PAPYRUS) {numPapyrus++;int[] r={15625};left.addNeighborResource(r,false,false);right.addNeighborResource(r,true,false);}
		else if (c.rtype==ResourceType.CLAYORE) {int[] r={1,5};addDualResource(r);resourceDescription+="CLAY/ORE,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.CLAYSTONE) {int[] r={1,25};addDualResource(r);resourceDescription+="CLAY/STONE,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.CLAYWOOD) {int[] r={1,125};addDualResource(r);resourceDescription+="CLAY/WOOD,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.ORESTONE) {int[] r={5,25};addDualResource(r);resourceDescription+="ORE/STONE,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.OREWOOD) {int[] r={5,125};addDualResource(r);resourceDescription+="ORE/WOOD,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.STONEWOOD) {int[] r={25,125};addDualResource(r);resourceDescription+="STONE/WOOD,";left.addNeighborResource(r,false,true);right.addNeighborResource(r,true,true);}
		else if (c.rtype==ResourceType.SHIELD) numShield+=c.resourceValue;
		else if (c.rtype==ResourceType.SCIENCE) {
			if (c.resourceValue==ScienceType.GEAR.ordinal()) numGear++;
			else if (c.resourceValue==ScienceType.TABLET.ordinal()) numTablet++;
			else if (c.resourceValue==ScienceType.COMPASS.ordinal()) numCompass++;
		}
		if (debugLog) System.out.println("Player "+id+" played "+c.name);
	}

	public void applyWonderEffect(WonderStage w,boolean debugLog) {
		numWonderStages++;
		if (debugLog) System.out.print("Player "+id+" built Wonder Stage "+numWonderStages+"      ");
		if (w.numCoin>0) {
			numCoin+=w.numCoin;
			if (debugLog) System.out.print("+"+w.numCoin+" COIN ");
		}
		if (w.numShield>0) {
			numShield+=w.numShield;
			if (debugLog) System.out.print("+"+w.numShield+" SHIELD ");
		}
		if (w.special==SpecialResource.RAW_MATERIALS) {int[] r={1,5,25,125};addDualResource(r);commerceDescription+="CLAY/ORE/STONE/WOOD,";}
		else if (w.special==SpecialResource.MANUFACTURED_GOODS) {int[] r={625,3125,15625};addDualResource(r);commerceDescription+="GLASS/LOOM/PAPYRUS,";}
		if (debugLog) System.out.println();
	}

	public void addDualResource(int[] r) {
		HashMap<Integer,ArrayList<NeighborResource>> newResource = new HashMap<Integer,ArrayList<NeighborResource>>();
		for (Integer i:resourceMap.keySet()) {
			for (int j:r) {
				if (!(i%(j*5)>=j*4)) newResource.put(i+j,null);
			}
		}
		resourceMap.putAll(newResource);
		for (int i:r) {
			resourceMap.put(i,null);
		}
	}

	public void addNeighborResource(int[] r,boolean leftOrRight,boolean rawOrManufactured) {
		HashMap<Integer,ArrayList<NeighborResource>> newResource = new HashMap<Integer,ArrayList<NeighborResource>>();
		for (Integer i:resourceMap.keySet()) {
			for (int j:r) {
				if (!(i%(j*5)>=j*4)) {
					if(resourceMap.containsKey(i+j)) {
						if (resourceMap.get(i+j)!=null) {
							ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
							if (resourceMap.get(i)==null) a.add(new NeighborResource(leftOrRight,rawOrManufactured));
							else a.add(new NeighborResource(leftOrRight,rawOrManufactured,i));
							newResource.put(i+j,a);
						}
					} else {
						ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
						if (resourceMap.get(i)==null) a.add(new NeighborResource(leftOrRight,rawOrManufactured));
						else a.add(new NeighborResource(leftOrRight,rawOrManufactured,i));
						newResource.put(i+j,a);
					}
				}
			}
		}
//		resourceMap.putAll(newResource);
		for (Integer i:newResource.keySet()) {
			if (resourceMap.containsKey(i)) for (NeighborResource n:newResource.get(i)) resourceMap.get(i).add(n); 
			else resourceMap.put(i,newResource.get(i));
			
		}
		for (int i:r) {
			if (resourceMap.containsKey(i)) {
				if (resourceMap.get(i)!=null)
					resourceMap.get(i).add(new NeighborResource(leftOrRight,rawOrManufactured));
			} else {
				ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
				a.add(new NeighborResource(leftOrRight,rawOrManufactured));
				resourceMap.put(i,a);
			}
		}

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
