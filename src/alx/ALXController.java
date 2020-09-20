package alx;

import javafx.util.Pair;
import model.Cards;
import model.Player;
import model.PlayerAction;
import model.Wonder;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.concurrent.Future;

public class ALXController {
    HashMap<String, ALXPlayer> simPlayers;
    public ALXController(){
        simPlayers = new HashMap<String, ALXPlayer>();
    }

    public void registerSimPlayer(ALXPlayer player){
        simPlayers.put(player.wonder.name, player);
    }

    public Pair<PlayerAction, Cards> getSimPlayAction(String wonderName, ArrayList<Cards> hand){
        System.out.println("Controller picks action");
        return defaultAction(simPlayers.get(wonderName),hand);
    }


    public Boolean getWonderSide(Wonder w){
        return defaultWonderSide();
    }

    public Cards getCopyGuild(ArrayList<Cards> guildChoices){
        return defaultCopyGuild(guildChoices);
    }

    public Cards getPlayFromDiscard(ArrayList<Cards> playable){
        return defaultPlayFromDiscard(playable);
    }




    public Pair<PlayerAction,Cards> defaultAction(Player p , ArrayList<Cards> cards) {
        PlayerAction action;
        Cards lastCard;
        p.checkResources(cards);
        p.canBuildWonder=p.checkWonder();
        action = PlayerAction.CARD;
        int cardPlayed = -1;
        if (p.canBuildWonder) {
            action=PlayerAction.WONDER;
            cardPlayed=0;
            if (p.wonderOptions!=null) {
                int minLeft=50,minRight=50;
                for (int j:p.wonderOptions) {
                    if (j/100+j%100<minLeft+minRight) {
                        minLeft=j/100;
                        minRight=j%100;
                    }
                }
                p.leftCost=minLeft;
                p.rightCost=minRight;
            }
            lastCard = cards.remove(cardPlayed);
            return new Pair<PlayerAction, Cards>(action, lastCard);
        }
        for (int i=0;i<cards.size();i++) {
            if (p.hasFreeBuild>0&&p.playableCost[i]!=-2) {p.hasFreeBuild=0;cardPlayed=i;break;}
            else if (p.playableCost[i]==0) {cardPlayed=i; break;}
            else if (p.playableCost[i]>0) {
                ArrayList<Integer> a = p.resourceOptions.get(i);
                for (int j:a) {
                    if ((j/100+j%100)==p.playableCost[i]) {
                        p.leftCost=j/100;
                        p.rightCost=j%100;
                        break;
                    }
                }
                cardPlayed=i;
                break;
            }
        }
        if (cardPlayed==-1) {
            cardPlayed=0;
            if (p.canBuildWonder) {
                action=PlayerAction.WONDER;
                if (p.wonderOptions!=null) {
                    int minLeft=50,minRight=50;
                    for (int j:p.wonderOptions) {
                        if (j/100+j%100<minLeft+minRight) {
                            minLeft=j/100;
                            minRight=j%100;
                        }
                    }
                    p.leftCost=minLeft;
                    p.rightCost=minRight;
                }
            } else
                action=PlayerAction.COIN;
        }
        lastCard = cards.remove(cardPlayed);
        return new Pair<PlayerAction, Cards>(action, lastCard);
    }

    public boolean defaultWonderSide(){
        return true;
    }

    public Cards defaultCopyGuild(ArrayList<Cards> guildChoices){
        if(guildChoices.isEmpty()){
            return null;
        }
        return guildChoices.get(0);
    }

    public Cards defaultPlayFromDiscard(ArrayList<Cards> playable){
        if(playable.isEmpty()){
            return null;
        }
        return playable.get(0);
    }

}
