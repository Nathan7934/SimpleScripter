package main;

import java.io.Serializable;

public class ListItem implements Serializable {
	// list_model is composed of elements of this type. Used to store additional data.
	private String command;
	private boolean pairIsSelected;
	public ListItem(String command) {
		this.command = command;
		this.pairIsSelected = false;
	}
	public void setPairIsSelected(boolean val) {
		this.pairIsSelected = val;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public boolean getPairIsSelected() {
		return this.pairIsSelected;
	}
	public String toString() {
		return command;
	}
}
