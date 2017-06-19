package Main;

public class ValidateThread implements Runnable {
    public void run() {
        ArffConv.validate();
    }
}
