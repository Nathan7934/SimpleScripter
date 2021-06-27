package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class RCLICKItem extends CQItem {
	public RCLICKItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.clickMouse(InputEvent.BUTTON3_DOWN_MASK);
	}
}
