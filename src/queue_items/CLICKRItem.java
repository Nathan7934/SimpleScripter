package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class CLICKRItem extends CQItem {
	public CLICKRItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.clickRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
}
