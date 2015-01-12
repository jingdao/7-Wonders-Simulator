package controller;

import model.Wonder;

public class Simulator {
	
	public Controller con;

	public Simulator() {
		Controller.manualSimulation=false;
		con = new Controller(null);
		simulateRandomForceWonder(10000);
	}

	public void simulateRandom(int numSimulations) {
		int[] wins = new int[7];
		int[] sumScore = new int[7];
		float[] sumScoreSquared = new float[7];
		for (int i=0;i<numSimulations;i++) {
			con.newGame(7);
			for (int j=0;j<7;j++) {
				sumScore[j]+=con.lastScore.get(j);
				sumScoreSquared[j]+=con.lastScore.get(j)*con.lastScore.get(j);
			}
			for (int j:con.lastWinner) wins[j]++;
		}
		System.out.println("Wonder        |  Avg StdDev Wins");
		for (int i=0;i<7;i++) {
			double avg = 1.0*sumScore[i]/numSimulations;
			double stdDev = Math.sqrt(1.0*sumScoreSquared[i]/numSimulations-avg*avg);
			System.out.printf("%13s | %4.1f %6.1f %4d\n",Wonder.wonders[i].name,avg,stdDev,wins[i]);
		}
	}

	public void simulateRandomForceWonder(int numSimulations) {
		int[] wins = new int[14];
		int[] sumScore = new int[14];
		float[] sumScoreSquared = new float[14];
		int[] numTrials = new int[14];
		for (int i=0;i<numSimulations;i++) {
			int k;
			int numPlayers = Controller.random.nextInt(5)+3;
			con.newGame(numPlayers);
			for (int j=0;j<numPlayers;j++) {
				for (k=0;k<7;k++) if (Wonder.wonders[k]==con.lastPlayers[j].wonder) break;
				if (con.lastPlayers[j].isWonderBSide) {
					numTrials[2*k+1]++;
					sumScore[2*k+1]+=con.lastScore.get(j);
					sumScoreSquared[2*k+1]+=con.lastScore.get(j)*con.lastScore.get(j);
				} else {
					numTrials[2*k]++;
					sumScore[2*k]+=con.lastScore.get(j);
					sumScoreSquared[2*k]+=con.lastScore.get(j)*con.lastScore.get(j);
				}
			}
			for (int j:con.lastWinner) {
				for (k=0;k<7;k++) if (Wonder.wonders[k]==con.lastPlayers[j].wonder) break;
				if (con.lastPlayers[j].isWonderBSide) wins[2*k+1]++;
				else wins[2*k]++;
			}
		}
		System.out.println("Wonder           | Trials  Avg StdDev Wins WinRate");
		for (int i=0;i<14;i++) {
			double avg = 1.0*sumScore[i]/numTrials[i];
			double stdDev = Math.sqrt(1.0*sumScoreSquared[i]/numTrials[i]-avg*avg);
			double winRate = 1.0*wins[i]/numTrials[i];
			System.out.printf("%13s%3s | %6d %4.1f %6.1f %4d %7.4f\n",Wonder.wonders[i/2].name,i%2==0?"(A)":"(B)",numTrials[i],avg,stdDev,wins[i],winRate);
		}
	}

	public static void main(String[] args) {
		Simulator sim = new Simulator();
	}
}
