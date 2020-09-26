package alx;

import javafx.util.Pair;
import model.Cards;
import model.Player;
import model.PlayerAction;
import model.Wonder;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class ALXOnePlayerController extends ALXController{
    // Use make notification between controller and player outside of the simulation
    private Object bell;
    private Action waitedAction;
    private Cards played;
    private ReentrantLock actionLock;
    private ArrayList<Cards> hand;

    public Object getBell() {
        return bell;
    }

    public Action getWaitedAction() {
        actionLock.lock();
        Action a = waitedAction;
        actionLock.unlock();
        return a;
    }

    public void setWaitedAction(Action a) {
        actionLock.lock();
        waitedAction=a;
        actionLock.unlock();
    }

    public Cards getPlayed() {
        return played;
    }

    public void setPlayed(Cards played) {
        this.played = played;
    }


    public ALXOnePlayerController() {
        super();
        bell = new Object();
        actionLock = new ReentrantLock();
        waitedAction = Action.NONE;
    }

    public void registerSimPlayer(ALXPlayer player){
        simPlayers.put(player.wonder.name, player);
    }

    public Pair<PlayerAction, Cards> getSimPlayAction(String wonderName, ArrayList<Cards> hand){
        actionLock.lock();
        waitedAction = Action.PLAY;
        this.hand = hand;
        actionLock.unlock();
        synchronized (bell){
            bell.notify();
        }

        try {
            synchronized (bell){
                bell.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return (new Pair(wonderName, played));
    }

    public void checkIfStarted(){
        boolean started = false;
        System.out.println("checkStart");
        while (!started){
            actionLock.lock();
            if(waitedAction!=Action.NONE){
                started = true;
            }
            actionLock.unlock();
            if(!started){
                System.out.println("Not started");
                try {
                    synchronized (bell){
                        bell.wait(100);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("started");
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
