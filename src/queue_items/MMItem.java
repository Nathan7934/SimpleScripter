package queue_items;

import main.BotFunctions;

public class MMItem extends CQItem {
	private final int x_coord;
	private final int y_coord;
	public MMItem(String command, int x_coord, int y_coord) {
		super(command);
		this.x_coord = x_coord;
		this.y_coord = y_coord;
	}
	public void execute() {
		BotFunctions.moveMouse(this.x_coord, this.y_coord);
	}
}
