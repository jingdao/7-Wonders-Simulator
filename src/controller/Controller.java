package controller;

import alx.ALXController;
import alx.ALXDefaultController;
import alx.ALXOnePlayerController;
import alx.ALXPlayer;
import javafx.util.Pair;
import model.Cards;
import model.CardType;
import model.Wonder;
import model.Player;
import model.PlayerAction;
import model.Bot;
import model.WonderStage;
import model.ResourceType;
import view.CommandLine;
import view.CardView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Controller {

	public static Random random = new Random();
	public CardView com;
	public static boolean manualSimulation=false;
	public static boolean alxSimulation=true;
	public static int defaultNumPlayers=7;
	public static String defaultWonder = "ALEXANDRIA";
	public ArrayList<Integer> lastScore;
	public ArrayList<Integer> lastWinner;
	public Player[] lastPlayers;
	public ALXOnePlayerController alxCon;

	public Controller(CardView cv) {
		Cards.buildDependencyMap();
		alxCon = new ALXOnePlayerController();
		if (cv!=null) com=cv;
		if (manualSimulation) {
			if (cv==null) com = new CommandLine(true);
			newGame(defaultNumPlayers);
		} else if (cv==null) com = new CommandLine(false);

	}

	public void newGame(int numPlayers) {
		Wonder[] w = new Wonder[numPlayers];
		Cards[][] c=new Cards[numPlayers][7];
		Player[] p = new Player[numPlayers];
		lastPlayers=p;
		ArrayList<Cards> discardPile = new ArrayList<Cards>();
		shuffleWonders(w);
		if (manualSimulation) {
			p[0]=new Player(0,w[0],com);
			System.out.println("Player has "+w[0].name);
		} else if (alxSimulation){
			p[0] = new ALXPlayer(0, w[0], com, alxCon);
		}
		else p[0]=new Bot(0,w[0],com);
		for (int i=1;i<numPlayers;i++) p[i]=new Bot(i,w[i],com);
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
			com.displayAge(age);
			shuffleCards(c,age);
			ArrayList<Cards>[] cc = new ArrayList[numPlayers];
			for (int j=0;j<numPlayers;j++) {
				cc[j]=new ArrayList<Cards>();
				cc[j].addAll(Arrays.asList(c[j]));
				if (p[j].hasFreeBuild==0) p[j].hasFreeBuild=1;
			}
			for (int j=0;j<6;j++) {
				HashMap<String, Pair<PlayerAction, Cards>> plays = new  HashMap<String, Pair<PlayerAction, Cards>>();
				com.displayTurn(j+1);
				for(int k=0;k<numPlayers;k++) {
					com.displayPlayerName(""+k);
					if (age==1||age==3){
						p[k].getAction(cc[(j+numPlayers-k)%numPlayers]);
					}
					else p[k].getAction(cc[(k+j)%numPlayers]);
					Cards kCard = null;
					if(p[k].action==PlayerAction.CARD){
						kCard = p[k].playedCard;
					}
					plays.put(p[k].wonder.name, new Pair<PlayerAction, Cards>(p[k].action, kCard));
				}
				alxCon.setPlays(plays);
				for (Player pp:p) resolvePlayerOutcome(pp,discardPile);
				if (j!=5) {
					for (Player pp:p) {
						if (pp.canPlayFromDiscard) {
							com.message("Player "+pp.id+" gets to play from the discard pile (wonder effect)");
							pp.playFromDiscard(discardPile);
						}
					}
					com.updateView();
				}
			}
			for (int k=0;k<numPlayers;k++) {
				ArrayList<Cards> remainingCards;
				if (age==1||age==3) remainingCards=cc[(5+numPlayers-k)%numPlayers];
				else remainingCards=cc[(k+5)%numPlayers];
				if (!p[k].canPlayLastCard) discardPile.add(remainingCards.get(0));
				else {
					com.message("Player "+k+" gets to play the last card (wonder effect)");
					p[k].getAction(remainingCards);
					resolvePlayerOutcome(p[k],discardPile);
				}
			}
			for (Player pp:p) {
				if (pp.canPlayFromDiscard) {
					com.message("Player "+pp.id+" gets to play from the discard pile (wonder effect)");
					pp.playFromDiscard(discardPile);
				}
			}
			if (age==3) {
				for (Player pp:p) {
					if (pp.canCopyGuild) {
						com.message("Player "+pp.id+" gets to copy a guild (wonder effect)");
						pp.copyGuild();
					}
				}
			}
			com.updateView();
			com.displayDiscardPile(discardPile);
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
			com.displayWarResults(p,warResult);
		}
		countScore(p);
	}

	public void shuffleWonders(Wonder[] w) {
		int numPlayers=w.length;
		ArrayList<Wonder> wonders = new ArrayList<Wonder>();
		wonders.addAll(Arrays.asList(Wonder.wonders));
		if (defaultWonder!=null) {
			int k;
			for (k=0;k<wonders.size();k++) {
				if (wonders.get(k).name.equals(defaultWonder)) break;
			}
			w[0]=wonders.remove(k);
			for (int i=0;i<numPlayers-1;i++) {
				if (manualSimulation) {
					int j=random.nextInt(wonders.size());
					w[i+1]=wonders.remove(j);
				} else w[i+1]=wonders.remove(0);
			}
		} else {
			for (int i=0;i<numPlayers;i++) {
//				if (manualSimulation) {
					int j=random.nextInt(wonders.size());
					w[i]=wonders.remove(j);
//				} else w[i]=wonders.remove(0);
			}
		}
		com.displayWonders(w);

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
			}
		}
	}
 
	public void resolvePlayerOutcome(Player pp,ArrayList<Cards> discardPile) {
		if (pp.leftCost>0) {
			pp.numCoin-=pp.leftCost;
			pp.left.numCoin+=pp.leftCost;
			com.displayPayment("Player "+pp.id,"Player "+pp.left.id,pp.leftCost);
		}
		if (pp.rightCost>0) {
			pp.numCoin-=pp.rightCost;
			pp.right.numCoin+=pp.rightCost;
			com.displayPayment("Player "+pp.id,"Player "+pp.right.id,pp.rightCost);
		}
		if (pp.action==PlayerAction.CARD) {
			pp.numCoin-=pp.lastCard.costCoin;
			pp.playedCards.add(pp.lastCard);
			pp.applyCardEffect(pp.lastCard);
		} else if (pp.action==PlayerAction.WONDER) {
			WonderStage[] wonderSide;
			if (pp.isWonderBSide) wonderSide=pp.wonder.stagesB;
			else wonderSide=pp.wonder.stagesA;
			pp.applyWonderEffect(wonderSide[pp.numWonderStages]);
		}
		else if (pp.action==PlayerAction.COIN) {
			discardPile.add(pp.lastCard);
			pp.numCoin+=3;
			com.showDiscardAction("Player "+pp.id);
		}
	}

	public void countScore(Player[] p) {
		int highScore=0;
		int highestCoin=0;
		ArrayList<Integer> winner = new ArrayList<Integer>();
		lastScore=new ArrayList<Integer>();
		for (Player pp:p) {
			pp.countCards();
		}
		int[] militaryScore=new int[p.length],
			  coinScore=new int[p.length],
			  wonderScore=new int[p.length],
			  civilianScore=new int[p.length],
			  scienceScore=new int[p.length],
			  commercialScore=new int[p.length],
			  guildScore=new int[p.length];
		int[][] scoreCategories = {militaryScore,coinScore,wonderScore,civilianScore,scienceScore,commercialScore,guildScore};
		for (int i=0;i<p.length;i++) {
			int totalScore;
			militaryScore[i]=p[i].victoryToken-p[i].defeatToken;
			coinScore[i]=p[i].numCoin/3;
			WonderStage[] wonderSide;
			if (p[i].isWonderBSide) wonderSide=p[i].wonder.stagesB;
			else wonderSide=p[i].wonder.stagesA;
			for (int j=0;j<p[i].numWonderStages;j++) wonderScore[i]+=wonderSide[j].numVictory;
			for (Cards c:p[i].playedCards) {
				if (c.type==CardType.BLUE) civilianScore[i]+=c.resourceValue;
				else if (c.name=="HAVEN") {
					commercialScore[i]+=p[i].numBrown;
				} else if (c.name=="LIGHTHOUSE") {
					commercialScore[i]+=p[i].numYellow;
				} else if (c.name=="CHAMBER OF COMMERCE") {
					commercialScore[i]+=p[i].numGray*2;
				} else if (c.name=="ARENA") commercialScore[i]+=p[i].numWonderStages;
				else if (c.name=="WORKERS GUILD") {
					guildScore[i]+=p[i].left.numBrown+p[i].right.numBrown;
				} else if (c.name=="CRAFTMENS GUILD") {
					guildScore[i]+=(p[i].left.numGray+p[i].right.numGray)*2;
				} else if (c.name=="TRADERS GUILD") {
					guildScore[i]+=p[i].left.numYellow+p[i].right.numYellow;
				} else if (c.name=="PHILOSOPHERS GUILD") {
					guildScore[i]+=p[i].left.numGreen+p[i].right.numGreen;
				} else if (c.name=="SPY GUILD") {
					guildScore[i]+=p[i].left.numRed+p[i].right.numRed;
				} else if (c.name=="MAGISTRATES GUILD") {
					guildScore[i]+=p[i].left.numBlue+p[i].right.numBlue;
				} else if (c.name=="SHIPOWNERS GUILD") {
					guildScore[i]+=p[i].numBrown+p[i].numGray+p[i].numBlue;
				} else if (c.name=="STRATEGY GUILD") {
					guildScore[i]+=p[i].left.defeatToken+p[i].right.defeatToken;
				} else if (c.name=="BUILDERS GUILD") {
					guildScore[i]+=p[i].numWonderStages+p[i].left.numWonderStages+p[i].right.numWonderStages;
				}
			}
			scienceScore[i]=getMaxScienceScore(p[i].numGear,p[i].numCompass,p[i].numTablet,p[i].numVariableScience);
			totalScore = militaryScore[i]+coinScore[i]+wonderScore[i]+civilianScore[i]+scienceScore[i]+commercialScore[i]+guildScore[i];
			lastScore.add(totalScore);
			if (totalScore>highScore||totalScore==highScore&&p[i].numCoin>highestCoin) {
				highScore=totalScore;
				highestCoin=p[i].numCoin;
				winner.clear();
				winner.add(i);
			} else if (totalScore==highScore&&p[i].numCoin==highestCoin) winner.add(i);
		}
		if (!manualSimulation) lastWinner=winner;
		com.displayScore(p,winner,lastScore,scoreCategories);
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
		Controller con = new Controller(null);
	}
}
