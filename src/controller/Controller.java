package controller;

import model.Cards;
import model.CardType;
import model.Wonder;
import model.Player;
import model.PlayerAction;
import model.WonderStage;
import model.ResourceType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Controller {

	public Random random;
	public static boolean debugLog=true;

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
		//w[0]=Wonder.Alexandria;
		for (int i=0;i<numPlayers;i++) p[i]=new Player(i,w[i]);
		p[0].right=p[numPlayers-1]; p[numPlayers-1].left=p[0];
		for (int i=0;i<numPlayers-1;i++) {p[i].left=p[i+1]; p[i+1].right=p[i];}
		for (Player pp:p) {
			if (pp.wonder.startingResource==ResourceType.CLAY) {int[] r={1};pp.left.addNeighborResource(r,false,true);pp.right.addNeighborResource(r,true,true);}
			else if (pp.wonder.startingResource==ResourceType.ORE) {int[] r={5};pp.left.addNeighborResource(r,false,true);pp.right.addNeighborResource(r,true,true);}
			else if (pp.wonder.startingResource==ResourceType.STONE) {int[] r={25};pp.left.addNeighborResource(r,false,true);pp.right.addNeighborResource(r,true,true);}
			else if (pp.wonder.startingResource==ResourceType.WOOD) {int[] r={125};pp.left.addNeighborResource(r,false,true);pp.right.addNeighborResource(r,true,true);}
			else if (pp.wonder.startingResource==ResourceType.GLASS) {int[] r={625};pp.left.addNeighborResource(r,false,false);pp.right.addNeighborResource(r,true,false);}
			else if (pp.wonder.startingResource==ResourceType.LOOM) {int[] r={3125};pp.left.addNeighborResource(r,false,false);pp.right.addNeighborResource(r,true,false);}
			else if (pp.wonder.startingResource==ResourceType.PAPYRUS) {int[] r={15625};pp.left.addNeighborResource(r,false,false);pp.right.addNeighborResource(r,true,false);}
		}
		for (int age=1;age<=3;age++) {
			if (debugLog) System.out.println("Age "+age);
			shuffleCards(c,age);
			ArrayList<Cards>[] cc = new ArrayList[numPlayers];
			for (int j=0;j<numPlayers;j++) {
				cc[j]=new ArrayList<Cards>();
				cc[j].addAll(Arrays.asList(c[j]));
				if (p[j].hasFreeBuild==0) p[j].hasFreeBuild=1;
			}
			for (int j=0;j<6;j++) {
				if (debugLog) System.out.println("Turn "+(j+1));
				for(int k=0;k<numPlayers;k++) {
//					System.out.println("Player "+(k+1));
					if (age==1||age==3) p[k].getAction(cc[(j+numPlayers-k)%numPlayers],debugLog);
					else p[k].getAction(cc[(k+j)%numPlayers],debugLog);
				}
				for (Player pp:p) resolvePlayerOutcome(pp,discardPile);
				if (j!=5) for (Player pp:p) if (pp.canPlayFromDiscard) pp.playFromDiscard(discardPile);
			}
			for (int k=0;k<numPlayers;k++) {
				ArrayList<Cards> remainingCards;
				if (age==1||age==3) remainingCards=cc[(5+numPlayers-k)%numPlayers];
				else remainingCards=cc[(k+5)%numPlayers];
				if (!p[k].canPlayLastCard) discardPile.add(remainingCards.get(0));
				else {
					System.out.println("Extra turn from wonder effect");
					p[k].getAction(remainingCards,debugLog);
					resolvePlayerOutcome(p[k],discardPile);
				}
			}
			for (Player pp:p) if (pp.canPlayFromDiscard) pp.playFromDiscard(discardPile);
//			System.out.print("Discard pile: ");
//			for (Cards d:discardPile) System.out.print(d.name+",");
//			System.out.println();
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
			if (debugLog) {
				System.out.print("War results: (");
				for (int j=0;j<numPlayers;j++) System.out.print(p[j].numShield+",");
				System.out.print(") (");
				for (int j=0;j<numPlayers;j++) System.out.print(warResult[j]+",");
				System.out.println(")");
			}
		}
		for (Player pp:p) if (pp.canCopyGuild) pp.copyGuild();
		countScore(p);
	}

	public void shuffleWonders(Wonder[] w) {
		int numPlayers=w.length;
		ArrayList<Wonder> wonders = new ArrayList<Wonder>();
		wonders.addAll(Arrays.asList(Wonder.wonders));
		for (int i=0;i<numPlayers;i++) {
			int j=random.nextInt(wonders.size());
			w[i]=wonders.remove(j);
//			w[i]=wonders.remove(0);
			if (debugLog) System.out.println(w[i].name);
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
 
	public void resolvePlayerOutcome(Player pp,ArrayList<Cards> discardPile) {
		if (pp.leftCost>0) {
			pp.numCoin-=pp.leftCost;
			pp.left.numCoin+=pp.leftCost;
			if (debugLog) System.out.println("Player "+pp.id+" payed "+pp.leftCost+" coin to Player "+pp.left.id);
		}
		if (pp.rightCost>0) {
			pp.numCoin-=pp.rightCost;
			pp.right.numCoin+=pp.rightCost;
			if (debugLog) System.out.println("Player "+pp.id+" payed "+pp.rightCost+" coin to Player "+pp.right.id);
		}
		if (pp.action==PlayerAction.CARD) {
			pp.numCoin-=pp.lastCard.costCoin;
			pp.playedCards.add(pp.lastCard);
			pp.applyCardEffect(pp.lastCard,debugLog);
		} else if (pp.action==PlayerAction.WONDER) {
			WonderStage[] wonderSide;
			if (pp.isWonderBSide) wonderSide=pp.wonder.stagesB;
			else wonderSide=pp.wonder.stagesA;
			pp.applyWonderEffect(wonderSide[pp.numWonderStages],debugLog);
		}
		else if (pp.action==PlayerAction.COIN) {
			discardPile.add(pp.lastCard);
			pp.numCoin+=3;
			if (debugLog) System.out.println("Player "+pp.id+" discarded a card for 3 coin");
		}
	}

	public void countScore(Player[] p) {
		int highScore=0;
		int highestCoin=0;
		ArrayList<Integer> winner = new ArrayList<Integer>();
		System.out.println("Player | BROWN GRAY YELLOW BLUE GREEN RED PURPLE | VICTORY DEFEAT");
		for (Player pp:p) {
			pp.countCards();
			System.out.printf("%6d | %5d %4d %6d %4d %5d %3d %6d | %7d %6d\n",pp.id,pp.numBrown,pp.numGray,pp.numYellow,pp.numBlue,pp.numGreen,pp.numRed,pp.numPurple,pp.victoryToken,pp.defeatToken);
		}
		System.out.println("Player | Military Coin Wonder Civilian Science Commercial Guild | Total");
		for (int i=0;i<p.length;i++) {
			int militaryScore=0,coinScore=0,wonderScore=0,civilianScore=0,scienceScore=0,commercialScore=0,guildScore=0,totalScore;
			militaryScore=p[i].victoryToken-p[i].defeatToken;
			coinScore=p[i].numCoin/3;
			WonderStage[] wonderSide;
			if (p[i].isWonderBSide) wonderSide=p[i].wonder.stagesB;
			else wonderSide=p[i].wonder.stagesA;
			for (int j=0;j<p[i].numWonderStages;j++) wonderScore+=wonderSide[j].numVictory;
			for (Cards c:p[i].playedCards) {
				if (c.type==CardType.BLUE) civilianScore+=c.resourceValue;
				else if (c.name=="HAVEN") {
					commercialScore+=p[i].numBrown;
				} else if (c.name=="LIGHTHOUSE") {
					commercialScore+=p[i].numYellow;
				} else if (c.name=="CHAMBER OF COMMERCE") {
					commercialScore+=p[i].numGray*2;
				} else if (c.name=="ARENA") commercialScore+=p[i].numWonderStages;
				else if (c.name=="WORKERS GUILD") {
					guildScore+=p[i].left.numBrown+p[i].right.numBrown;
				} else if (c.name=="CRAFTSMENS GUILD") {
					guildScore+=p[i].left.numGray+p[i].right.numGray;
				} else if (c.name=="TRADERS GUILD") {
					guildScore+=p[i].left.numYellow+p[i].right.numYellow;
				} else if (c.name=="PHILOSOPHERS GUILD") {
					guildScore+=p[i].left.numGreen+p[i].right.numGreen;
				} else if (c.name=="SPY GUILD") {
					guildScore+=p[i].left.numRed+p[i].right.numRed;
				} else if (c.name=="MAGISTRATES GUILD") {
					guildScore+=p[i].left.numBlue+p[i].right.numBlue;
				} else if (c.name=="SHIPOWNERS GUILD") {
					guildScore+=p[i].numBrown+p[i].numGray+p[i].numBlue;
				} else if (c.name=="STRATEGY GUILD") {
					guildScore+=p[i].left.defeatToken+p[i].right.defeatToken;
				} else if (c.name=="BUILDERS GUILD") {
					guildScore+=p[i].numWonderStages+p[i].left.numWonderStages+p[i].right.numWonderStages;
				}
			}
			scienceScore=getMaxScienceScore(p[i].numGear,p[i].numCompass,p[i].numTablet,p[i].numVariableScience);
			totalScore = militaryScore+coinScore+wonderScore+civilianScore+scienceScore+commercialScore+guildScore;
			System.out.printf("%6d | %8d %4d %6d %8d %7d %10d %5d | %5d\n",i,militaryScore,coinScore,wonderScore,civilianScore,scienceScore,commercialScore,guildScore,totalScore);
			if (totalScore>highScore||totalScore==highScore&&p[i].numCoin>highestCoin) {
				highScore=totalScore;
				highestCoin=p[i].numCoin;
				winner.clear();
				winner.add(i);
			} else if (totalScore==highScore&&p[i].numCoin==highestCoin) winner.add(i);
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

	public int getMaxScienceScore(int numGear,int numCompass,int numTablet,int numVariableScience) {
		int maxScore=0,j,n1,n2,n3;
		for (int i=0;i<Math.pow(3,numVariableScience);i++) {
			j=i;
			n1=numGear;
			n2=numCompass;
			n3=numTablet;
			for(int k=0;k<numVariableScience;k++) {
				if (j%3==0) n1++;
				else if (j%3==1) n2++;
				else if (j%3==2) n3++;
				j/=3;
			}
			maxScore=Math.max(maxScore,getScienceScore(n1,n2,n3));
		}
		return maxScore;
	}

	public int getScienceScore(int numGear,int numCompass,int numTablet) {
		int sum=0;
		sum+=numGear*numGear+numCompass*numCompass+numTablet*numTablet;
		int numSets=Math.min(numGear,Math.min(numCompass,numTablet));
		sum+=numSets*7;
		return sum;
	}

	public static void main(String[] args ) {
		Controller con = new Controller();

	}
}
