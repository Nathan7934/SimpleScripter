import java.awt.Robot;
import java.util.concurrent.ThreadLocalRandom;

public class BotFunctions {
    private Robot robot;

    //delay between clicks in ms
    private int delay;
    public BotFunctions() {
        try {
            robot = new Robot();
        }catch(Exception e) {
            e.printStackTrace();
        }
        delay = 300;
    }

    public void clickMouse(int button) {
        try {
            robot.mousePress(button);
            robot.delay(250);
            robot.mouseRelease(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void clickHold(int button) {
        try {
            robot.mousePress(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void clickRelease(int button) {
        try {
            robot.mouseRelease(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void doubleClick(int button) {
        try {
            robot.mousePress(button);
            robot.delay(50);
            robot.mouseRelease(button);
            robot.delay(50);
            robot.mousePress(button);
            robot.delay(50);
            robot.mouseRelease(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void pressKey(int keycode) {
        try {
            robot.keyPress(keycode);
            robot.delay(250);
            robot.keyRelease(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void holdKey(int keycode) {
        try {
            robot.keyPress(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void releaseKey(int keycode) {
        try {
            robot.keyRelease(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void moveMouse(int x, int y) {
        try {
            robot.mouseMove(x, y);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public void randomWait(int lower, int upper) {
    	int rwait_time = ThreadLocalRandom.current().nextInt(lower, upper + 1);
		robot.delay(rwait_time);
    }

    public void setDelay(int ms) {
        this.delay = ms;
    }

    public void wait(int ms) {
        robot.delay(ms);
    }
}
