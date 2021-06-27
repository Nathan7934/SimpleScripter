package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class CLICKItem extends CQItem {
	public CLICKItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.clickMouse(InputEvent.BUTTON1_DOWN_MASK);
	}
}