package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class CLICKHItem extends CQItem {
	public CLICKHItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.clickHold(InputEvent.BUTTON1_DOWN_MASK);
	}
}
