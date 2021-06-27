package queue_items;

import main.BotFunctions;

import java.awt.event.InputEvent;

public class DCLICKItem extends CQItem {
	public DCLICKItem(String command) {
		super(command);
	}
	public void execute() {
		BotFunctions.doubleClick(InputEvent.BUTTON1_DOWN_MASK);
	}
}
