package model;

import java.util.ArrayList;
import java.io.Console;
import view.CardView;
import controller.Controller;
import java.util.HashMap;
import java.util.HashSet;

public class Player {

	public CardView view;
	public Player left,right;
	public int id,numClay,numOre,numStone,numWood,numGlass,numLoom,numPapyrus;
	public int numCoin,numShield,numWonderStages;
	public int numGear,numCompass,numTablet,numVariableScience;
	public int victoryToken,defeatToken;
	public int numBrown,numGray,numYellow,numBlue,numGreen,numRed,numPurple;
	public int leftTradingCostRaw=2,rightTradingCostRaw=2,tradingCostManufactured=2;
	public int leftCost,rightCost;
	public Cards lastCard;
	public PlayerAction action;
	public Wonder wonder;
	public ArrayList<Cards> playedCards;
	public HashMap<Integer,ArrayList<NeighborResource>> resourceMap = new HashMap<Integer,ArrayList<NeighborResource>>();
	public String resourceDescription="",commerceDescription="";
	public ArrayList<ArrayList<Integer>> resourceOptions;
	public ArrayList<Integer> wonderOptions;
	public int[] playableCost;
	public boolean canBuildWonder;
	public boolean isWonderBSide=false;
	public int hasFreeBuild=-1;
	public boolean canCopyGuild=false;
	public boolean canPlayLastCard=false;
	public boolean canPlayFromDiscard=false;

	public Player(int id,Wonder w,CardView v) {
		this.id=id;
		this.wonder=w;
		this.view=v;
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
		if (id==0&&Controller.manualSimulation) {
			view.selectWonderSide(this);
		} else {
			isWonderBSide=id%2==0;
		}
	}

	public void getAction(ArrayList<Cards> cards) {
		checkResources(cards);
		canBuildWonder=checkWonder();
		action = PlayerAction.CARD;
		int cardPlayed = -1;
		if (id==0&&Controller.manualSimulation) {
			view.displayCards(cards,playableCost);
			view.displayResources(this);
			view.selectAction(this,cards);
		} else {
			if (canBuildWonder) {
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
//				if (canBuildWonder) action=PlayerAction.WONDER;
//				else
					action=PlayerAction.COIN;
			}
			lastCard = cards.remove(cardPlayed);
		};
	}

	public void checkResources(ArrayList<Cards> cards) {
//		if (id==0) {
//			for (Integer j:resourceMap.keySet()) {
//				String s="";
//				int ii=j;
//				for (int i=0;i<7;i++) {
//					s+=(ii%5)+",";
//					ii=ii/5;
//				}
//				System.out.print(s+":");
//				if (resourceMap.get(j)!=null) {
//					for (NeighborResource n:resourceMap.get(j)) {
//						System.out.print(n.leftRaw+""+n.leftManufactured+""+n.rightRaw+""+n.rightManufactured+":"+NeighborResource.getStringFromResourceCode(n.prerequisite)+",");
//					}
//				}
//				System.out.println();
//			}
//		}
		playableCost=new int[cards.size()];
		resourceOptions = new ArrayList<ArrayList<Integer>>();
		leftCost=0;
		rightCost=0;
		for (int i=0;i<cards.size();i++) {
			playableCost[i]=-1;
			if (playedCards.contains(cards.get(i))) {playableCost[i]=-2;resourceOptions.add(null);continue;}
			if (cards.get(i).dependency!=null) {
				for (Cards c:playedCards) {
					if (c.name.endsWith(cards.get(i).dependency)) {
						playableCost[i]=0;
						break;
					}
				}
				if (playableCost[i]==0) {resourceOptions.add(null);continue;}
			}
			if (cards.get(i).costCoin>numCoin) {playableCost[i]=-1;resourceOptions.add(null);continue;}
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
				ArrayList<Integer> a = new ArrayList<Integer>(NeighborResource.getCost(k,leftTradingCostRaw,rightTradingCostRaw,tradingCostManufactured,resourceMap,new HashSet<Integer>()));
				int minCost=100;
//				if (id==0) System.out.print(cards.get(i).name+":"+needClay+""+needOre+""+needStone+""+needWood+""+needGlass+""+needLoom+""+needPapyrus+":");
				for (int j:a) minCost=Math.min(minCost,j/100+j%100);
//				ArrayList<Integer> b = new ArrayList<Integer>();
//				for (int j:a) if (j/100+j%100==minCost) b.add(j);
				resourceOptions.add(a);
				playableCost[i]=minCost;
			}
			if (playableCost[i]>numCoin) playableCost[i]=-1;
//			if (id==0) System.out.println(k+":"+playableCost[i]);
		}
//		if (id==0) for (ArrayList<Integer> a:resourceOptions) System.out.println(a);
	}

	public boolean checkWonder() {
		wonderOptions=null;
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
		if (k==0) return true;
		else if (!resourceMap.containsKey(k)) return false;
		else if (resourceMap.get(k)==null) return true;
		else {
			wonderOptions=new ArrayList<Integer>(NeighborResource.getCost(k,leftTradingCostRaw,rightTradingCostRaw,tradingCostManufactured,resourceMap,new HashSet<Integer>()));
			int minCost=100;
			for (int i:wonderOptions) {
				minCost=Math.min(minCost,i/100+i%100);
			}
			if (minCost==0) wonderOptions=null;
			return minCost<=numCoin;
		}
	}

	public void applyCardEffect(Cards c) {
		int dCoin=0;
		if (c.name=="VINEYARD") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.BROWN) count++;
			for (Cards cc:left.playedCards) if (cc.type==CardType.BROWN) count++;
			for (Cards cc:right.playedCards) if (cc.type==CardType.BROWN) count++;
			dCoin=count;
		} else if (c.name=="HAVEN") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.BROWN) count++;
			dCoin=count;
		} else if (c.name=="BAZAR") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.GRAY) count++;
			for (Cards cc:left.playedCards) if (cc.type==CardType.GRAY) count++;
			for (Cards cc:right.playedCards) if (cc.type==CardType.GRAY) count++;
			dCoin=count*2;
		} else if (c.name=="CHAMBER OF COMMERCE") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.GRAY) count++;
			dCoin=count*2;
		} else if (c.name=="LIGHTHOUSE") {
			int count=0;
			for (Cards cc:playedCards) if (cc.type==CardType.YELLOW) count++;
			dCoin=count;
		} else if (c.name=="ARENA") {
			dCoin=numWonderStages*3;
		} else if (c.name=="CARAVANSERY") {int[] r={1,5,25,125};addDualResource(r);commerceDescription+="CLAY/ORE/STONE/WOOD,";}
		else if (c.name=="FORUM") {int[] r={625,3125,15625};addDualResource(r);commerceDescription+="GLASS/LOOM/PAPYRUS,";}
		else if (c.name=="MARKETPLACE") tradingCostManufactured=1;
		else if (c.name=="WEST TRADING POST") leftTradingCostRaw=1;
		else if (c.name=="EAST TRADING POST") rightTradingCostRaw=1;
		else if (c.name=="SCIENTISTS GUILD") numVariableScience++;
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
		numCoin+=dCoin;
		view.showCardAction(c.name,dCoin,"Player "+id);
	}

	public void applyWonderEffect(WonderStage w) {
		numWonderStages++;
		numCoin+=w.numCoin;
		numShield+=w.numShield;
		view.showWonderAction(w,numWonderStages,"Player "+id);
		if (w.special==SpecialResource.RAW_MATERIALS) {int[] r={1,5,25,125};addDualResource(r);commerceDescription+="CLAY/ORE/STONE/WOOD,";}
		else if (w.special==SpecialResource.MANUFACTURED_GOODS) {int[] r={625,3125,15625};addDualResource(r);commerceDescription+="GLASS/LOOM/PAPYRUS,";}
		else if (w.special==SpecialResource.TRADING) {leftTradingCostRaw=1; rightTradingCostRaw=1;}
		else if (w.special==SpecialResource.SCIENCE) numVariableScience++;
		else if (w.special==SpecialResource.FREE_BUILD) hasFreeBuild=1;
		else if (w.special==SpecialResource.GUILD) canCopyGuild=true;
		else if (w.special==SpecialResource.PLAY_LAST_CARD) canPlayLastCard=true;
		else if (w.special==SpecialResource.PLAY_FROM_DISCARD) canPlayFromDiscard=true;
	}

	public void addDualResource(int[] r) {
		NeighborResource.nid++;
		HashMap<Integer,ArrayList<NeighborResource>> newResource = new HashMap<Integer,ArrayList<NeighborResource>>();
		for (Integer i:resourceMap.keySet()) {
			for (int j:r) {
//				if (!(i%(j*5)>=j*4)) newResource.put(i+j,null);
				if (!(i%(j*5)>=j*4)) {
					if(resourceMap.containsKey(i+j)) {
						if (resourceMap.get(i+j)!=null) {
							ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
							if (resourceMap.get(i)==null) a.add(new NeighborResource(0));
							else a.add(new NeighborResource(i));
							newResource.put(i+j,a);
						}
					} else {
						ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
						if (resourceMap.get(i)==null) a.add(new NeighborResource(0));
						else a.add(new NeighborResource(i));
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
//			resourceMap.put(i,null);
			if (resourceMap.containsKey(i)) {
				if (resourceMap.get(i)!=null)
					resourceMap.get(i).add(new NeighborResource(0));
			} else {
				ArrayList<NeighborResource> a = new ArrayList<NeighborResource>();
				a.add(new NeighborResource(0));
				resourceMap.put(i,a);
			}
		}
	}

	public void addNeighborResource(int[] r,boolean leftOrRight,boolean rawOrManufactured) {
		NeighborResource.nid++;
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

	public void copyGuild() {
		ArrayList<Cards> guildChoices = new ArrayList<Cards>();
		for (Cards c:left.playedCards) if (c.type==CardType.PURPLE) guildChoices.add(c);
		for (Cards c:right.playedCards) if (c.type==CardType.PURPLE) guildChoices.add(c);
		if (id==0&&Controller.manualSimulation) {
			if (guildChoices.size()==0) {
				view.message("There are no neighbouring guilds to copy");
				return;
			} else view.selectGuild(this,guildChoices);
		} else {
			if (guildChoices.size()>0) playedCards.add(guildChoices.get(0));
		}
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
		if (id==0&&Controller.manualSimulation) {
			if (selected.size()==0) {
				view.message("There are no playable cards from the discard pile");
				return;
			} else view.selectFromDiscard(this,discardPile,selection);
		} else {
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
