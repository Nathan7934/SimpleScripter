package queue_items;

import main.BotFunctions;

public class RWAITItem extends CQItem {
	private final int lower;
	private final int upper;
	public RWAITItem(String command, int lower, int upper) {
		super(command);
		this.lower = lower;
		this.upper = upper;
	}
	public void execute() {
		BotFunctions.moveMouse(this.lower, this.upper);
	}
}