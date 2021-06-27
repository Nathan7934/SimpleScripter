package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class MCLICKItem extends CQItem {
	public MCLICKItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.clickMouse(InputEvent.BUTTON2_DOWN_MASK);
	}
}
