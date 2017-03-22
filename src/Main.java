import java.util.TimerTask;

public class Main extends TimerTask{
    public static void main(String[] args) {
        java.util.Timer timer = new java.util.Timer();
        timer.schedule(new Logic(), 0, 30000);
    }

    @Override
    public void run() {
        new Logic();
    }
}
