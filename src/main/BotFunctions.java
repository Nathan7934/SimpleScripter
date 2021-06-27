package main;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class BotFunctions {
    static Robot robot;
	static {
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	//delay between clicks in ms
    static int delay = 300;

	public BotFunctions() {}

    public static void clickMouse(int button) {
        try {
            robot.mousePress(button);
            robot.delay(100);
            robot.mouseRelease(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void clickHold(int button) {
        try {
            robot.mousePress(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void clickRelease(int button) {
        try {
            robot.mouseRelease(button);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void doubleClick(int button) {
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

    public static void pressKey(int keycode) {
        try {
            robot.keyPress(keycode);
            robot.delay(100);
            robot.keyRelease(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void holdKey(int keycode) {
        try {
            robot.keyPress(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void releaseKey(int keycode) {
        try {
            robot.keyRelease(keycode);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void moveMouse(int x, int y) {
        try {
            robot.mouseMove(x, y);
            robot.delay(delay);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void randomWait(int lower, int upper) {
    	int rwait_time = ThreadLocalRandom.current().nextInt(lower, upper + 1);
		robot.delay(rwait_time);
    }

    public static void setDelay(int ms) {
        delay = ms;
    }

    public static void wait(int ms) {
        robot.delay(ms);
    }
}
