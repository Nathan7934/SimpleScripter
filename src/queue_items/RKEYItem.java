package queue_items;

import main.BotFunctions;

public class RKEYItem extends CQItem {
	private final int key;
	public RKEYItem(String command, int key) {
		super(command);
		this.key = key;
	}
	public void execute() {
		BotFunctions.releaseKey(this.key);
	}
}
