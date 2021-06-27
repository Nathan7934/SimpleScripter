package queue_items;

import main.BotFunctions;

public class HKEYItem extends CQItem {
	private final int key;
	public HKEYItem(String command, int key) {
		super(command);
		this.key = key;
	}
	public void execute() {
		BotFunctions.holdKey(this.key);
	}
}
