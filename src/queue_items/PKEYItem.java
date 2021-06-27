package queue_items;

import main.BotFunctions;

public class PKEYItem extends CQItem {
	private final int key;
	public PKEYItem(String command, int key) {
		super(command);
		this.key = key;
	}
	public void execute() {
		BotFunctions.pressKey(this.key);
	}
}
