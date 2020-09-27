import alx.ALXOnePlayerController;
import alx.Action;
import model.Player;
import model.PlayerAction;

import java.util.Random;

public class TestThread extends Thread{
    ALXOnePlayerController c;
    Random r;
    public TestThread(ALXOnePlayerController c) {
        super();
        this.c = c;
        r = new Random();
    }
    public void run()  {
        System.out.println("Start thread");
        c.checkIfStarted();
        Action a;
        while (!c.isGameOver()){
            a = c.getWaitedAction();
            //System.out.println("Waited : "+a);
            if(a== Action.PLAY){
                c.playCard(c.getHand().get(r.nextInt(c.getHand().size())), PlayerAction.CARD);
                System.out.println("Plays :" + c.getPlays());
            }
            if(a== Action.WONDER_SIDE){
                c.setSide(true);
            }
        }
        System.out.println("Thread done");

    }
}
