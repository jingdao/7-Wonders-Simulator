package controller;

import model.Wonder;

public class Simulator {
	
	public Controller con;

	public Simulator() {
		Controller.manualSimulation=false;
		con = new Controller(null);
		simulateRandom(1000);
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

	public static void main(String[] args) {
		Simulator sim = new Simulator();
	}
}
