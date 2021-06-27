package queue_items;

import main.BotFunctions;

public class WAITItem extends CQItem {
	private final int time;
	public WAITItem(String command, int time) {
		super(command);
		this.time = time;
	}
	public void execute() {
		BotFunctions.wait(this.time);
	}
}