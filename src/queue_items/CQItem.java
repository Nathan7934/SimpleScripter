package queue_items;

import java.io.Serializable;

public abstract class CQItem implements Serializable {
	// Abstract object type for queue items.
	private static final long serialVersionUID = 1337L;

	private final String command;
	private CQItem pointer; // Pointer ref, only to be set when dealing with pairing commands, such as press key.
	private boolean has_pointer = false;

	public CQItem(String command){
		this.command = command;
	}

	public abstract void execute();

	public String getCommand(){
		return command;
	}

	public void setPointer(CQItem pointer){
		this.pointer = pointer;
		has_pointer = true;
	}

	public CQItem getPointerRef(){
		if(has_pointer){ return pointer; }
		else{ return null; }
	}
}