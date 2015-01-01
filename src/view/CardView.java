package view;

import java.util.ArrayList;
import model.Wonder;
import model.WonderStage;
import model.Cards;
import model.Player;
import model.PlayerAction;

public interface CardView {

	public void message(String s);
	public void displayWonders(Wonder[] w);
	public void displayAge(int age);
	public void displayPlayerName(String s);
	public void displayTurn(int turn);
	public void displayDiscardPile(ArrayList<Cards> discardPile);
	public void displayWarResults(Player[] p,int[] warResult);
	public void displayPayment(String src,String dest,int amount);
	public void displayScore(Player[] p,ArrayList<Integer> winner,ArrayList<Integer> totalScore,int[][] scoreCategories);
	public void displayCards(ArrayList<Cards> cards,int[] playableCost);
	public void displayResources(Player p);
	public void displayNeighborResources(String name,Player p);
	public void showDiscardAction(String src);
	public void showWonderAction(WonderStage w,int numWonderStages,String name);
	public void showCardAction(String cardName,int dCoin,String playerName); 
	public void selectWonderSide(Player p);
	public void selectFromDiscard(Player p,ArrayList<Cards> discardPile, ArrayList<Cards> selection);
	public void selectGuild(Player p,ArrayList<Cards> guildChoices);
	public void selectAction(Player p,ArrayList<Cards> cards); 
	public void selectLookAction(Player p,ArrayList<Cards> cards); 
	public void selectTrading(Player p, ArrayList<Integer> options); 
	public void selectCard(Player p,ArrayList<Cards> cards);

}
