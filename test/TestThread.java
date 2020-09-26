import alx.ALXOnePlayerController;

public class TestThread extends Thread{
    ALXOnePlayerController c;
    public TestThread(ALXOnePlayerController c) {
        super();
        this.c = c;
    }
    public void run()  {
        System.out.println("Start thread");
        c.checkIfStarted();
    }
}
