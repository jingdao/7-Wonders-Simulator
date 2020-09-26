import alx.ALXController;
import controller.ALXSimulator;
import controller.Controller;

class TestALXModif{
    public static void main(String[] args) {
        Controller con = new Controller(null);
        Thread t = new TestThread(con.alxCon);
        t.start();
        con.newGame(7);

    }

}
