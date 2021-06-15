import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class CommandHandler {

    // Command Constants for addCommand()
    public static final String CLICK_COM = "Left Click";
    public static final String RCLICK_COM = "Right Click";
    public static final String DCLICK_COM = "Double Click";
    public static final String MCLICK_COM = "Middle Click";
    public static final String CLICKH_COM = "Hold Click";
    public static final String CLICKR_COM = "Release Click";
    public static final String PKEY_COM = "Press Key ";
    public static final String HKEY_COM = "Hold Key ";
    public static final String RKEY_COM = "Release Key ";
    public static final String WAIT_COM = "Wait ";
    public static final String RWAIT_COM = "Random Wait ";
    public static final String MM_COM = "Move Mouse ";
    public static final String SLOOP_COM = "Start Loop";
    public static final String ELOOP_COM = "End Loop";

	private boolean min_on_exec = false;

    private AppFrame app_frame;
    private BotFunctions bot = new BotFunctions();
    private ArrayList<CQItem> queue = new ArrayList<CQItem>();
    private Hashtable<Integer, String> keyStrings = new Hashtable<Integer, String>();

    // -------- Leftover from weirdness with interfaces, kept for the sake of faster calling of bot functions ----------
    // TODO: These probably aren't necessary. Remove later?

    public void leftClick() { bot.clickMouse(InputEvent.BUTTON1_DOWN_MASK); }
    public void rightClick() { bot.clickMouse(InputEvent.BUTTON3_DOWN_MASK); }
    public void doubleClick() { bot.doubleClick(InputEvent.BUTTON1_DOWN_MASK); }
	public void middleClick() { bot.clickMouse(InputEvent.BUTTON2_DOWN_MASK); }
    public void clickHold() { bot.clickHold(InputEvent.BUTTON1_DOWN_MASK); }
    public void clickRelease() { bot.clickRelease(InputEvent.BUTTON1_DOWN_MASK); }

    // -----------------------------------------------------------------------------------------------------------------

    public CommandHandler(AppFrame app_frame){

        this.app_frame = app_frame; // Storing reference to AppFrame to make GUI changes when queue is altered.

        // -----------------Adding values to KeyInt to String converter hashtable.--------------------------------------
        keyStrings.put(KeyEvent.VK_Q, "Q"); keyStrings.put(KeyEvent.VK_A, "A"); keyStrings.put(KeyEvent.VK_Z, "Z"); // alphabet
        keyStrings.put(KeyEvent.VK_W, "W"); keyStrings.put(KeyEvent.VK_S, "S"); keyStrings.put(KeyEvent.VK_X, "X");
        keyStrings.put(KeyEvent.VK_E, "E"); keyStrings.put(KeyEvent.VK_D, "D"); keyStrings.put(KeyEvent.VK_C, "C");
        keyStrings.put(KeyEvent.VK_R, "R"); keyStrings.put(KeyEvent.VK_F, "F"); keyStrings.put(KeyEvent.VK_V, "V");
        keyStrings.put(KeyEvent.VK_T, "T"); keyStrings.put(KeyEvent.VK_G, "G"); keyStrings.put(KeyEvent.VK_B, "B");
        keyStrings.put(KeyEvent.VK_Y, "Y"); keyStrings.put(KeyEvent.VK_H, "H"); keyStrings.put(KeyEvent.VK_N, "N");
        keyStrings.put(KeyEvent.VK_U, "U"); keyStrings.put(KeyEvent.VK_J, "J"); keyStrings.put(KeyEvent.VK_M, "M");
        keyStrings.put(KeyEvent.VK_I, "I"); keyStrings.put(KeyEvent.VK_K, "K"); keyStrings.put(KeyEvent.VK_O, "O");
        keyStrings.put(KeyEvent.VK_L, "L"); keyStrings.put(KeyEvent.VK_P, "P");

        keyStrings.put(KeyEvent.VK_1, "1"); keyStrings.put(KeyEvent.VK_2, "2"); keyStrings.put(KeyEvent.VK_3, "3"); // numbers
        keyStrings.put(KeyEvent.VK_4, "4"); keyStrings.put(KeyEvent.VK_5, "5"); keyStrings.put(KeyEvent.VK_6, "6");
        keyStrings.put(KeyEvent.VK_7, "7"); keyStrings.put(KeyEvent.VK_8, "8"); keyStrings.put(KeyEvent.VK_9, "9");
        keyStrings.put(KeyEvent.VK_0, "0");

        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('`'), "`"); keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('-'), "-"); // punctuation
        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('='), "="); keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('['), "[");
        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar(']'), "]"); keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('\\'), "\\");
        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar(';'), ";"); keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('\''), "\'");
        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar(','), ","); keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('.'), ".");
        keyStrings.put(KeyEvent.getExtendedKeyCodeForChar('/'), "/");

        keyStrings.put(KeyEvent.VK_TAB, "Tab"); keyStrings.put(KeyEvent.VK_CAPS_LOCK, "Caps Lock"); keyStrings.put(KeyEvent.VK_SHIFT, "Shift"); // named keys
        keyStrings.put(KeyEvent.VK_CONTROL, "Control"); keyStrings.put(KeyEvent.VK_WINDOWS, "Windows"); keyStrings.put(KeyEvent.VK_ALT, "Alt");
        keyStrings.put(KeyEvent.VK_ENTER, "Enter"); keyStrings.put(KeyEvent.VK_BACK_SPACE, "Backspace"); keyStrings.put(KeyEvent.VK_DELETE, "Delete");
        keyStrings.put(KeyEvent.VK_UP, "Up"); keyStrings.put(KeyEvent.VK_DOWN, "Down"); keyStrings.put(KeyEvent.VK_LEFT, "Left");
        keyStrings.put(KeyEvent.VK_RIGHT, "Right"); keyStrings.put(KeyEvent.VK_PRINTSCREEN, "Print screen"); keyStrings.put(KeyEvent.VK_ESCAPE, "Escape");
        keyStrings.put(KeyEvent.VK_INSERT, "Insert"); keyStrings.put(KeyEvent.VK_HOME, "Home"); keyStrings.put(KeyEvent.VK_END, "End");
        keyStrings.put(KeyEvent.VK_PAGE_UP, "Page up"); keyStrings.put(KeyEvent.VK_PAGE_DOWN, "Page down"); keyStrings.put(KeyEvent.VK_SPACE, "Space");

        keyStrings.put(KeyEvent.VK_F1, "F1"); keyStrings.put(KeyEvent.VK_F2, "F2"); keyStrings.put(KeyEvent.VK_F3, "F3"); // function keys
        keyStrings.put(KeyEvent.VK_F4, "F4"); keyStrings.put(KeyEvent.VK_F5, "F5"); keyStrings.put(KeyEvent.VK_F6, "F6");
        keyStrings.put(KeyEvent.VK_F7, "F7"); keyStrings.put(KeyEvent.VK_F8, "F8"); keyStrings.put(KeyEvent.VK_F9, "F9");
        keyStrings.put(KeyEvent.VK_F10, "F10"); keyStrings.put(KeyEvent.VK_F11, "F11"); keyStrings.put(KeyEvent.VK_F12, "F12");
        // ---------------------------- End of KeyInt to String mapping ------------------------------------------------
    }

    public void addCommand(String command, int InfoInt){
        /* Adds a command to the queue. <command> tells us which command to add. <InfoInt> can refer to several things,
        such as a key code for key related commands, a time in ms for wait commands, or the number of loops for a loop
        command. */
        switch(command){
            case CLICK_COM:
                queue.add(new CQItem(command) { public void execute(){ leftClick(); } });
                break;
            case RCLICK_COM:
                queue.add(new CQItem(command) { public void execute(){ rightClick(); } });
                break;
            case DCLICK_COM:
                queue.add(new CQItem(command) { public void execute(){ doubleClick(); } });
                break;
	        case MCLICK_COM:
	        	queue.add(new CQItem(command) { public void execute(){ middleClick(); } });
	        	break;
            case CLICKH_COM:
                // Since hold click MUST be followed by a release click, we add a release click command as well and
                // create pointers for each command to each other (allows for synchronized deletion).
                CQItem clickHolder = new CQItem(command){ public void execute(){ clickHold(); } };
                CQItem clickReleaser = new CQItem(CLICKR_COM){ public void execute(){ clickRelease(); } };
                clickHolder.setPointer(clickReleaser);
                clickReleaser.setPointer(clickHolder);
                queue.add(clickHolder);
                queue.add(clickReleaser);
                app_frame.addCommand(command);
                app_frame.addCommand(CLICKR_COM);
                break;
            case PKEY_COM:
                queue.add(new CQItem(command){
                    private final int key = InfoInt;
                    public void execute(){
                        bot.pressKey(this.key);
                    }
                } );
                app_frame.addCommand(command + "(" + keyStrings.get(InfoInt) + ")");
                break;
            case HKEY_COM:
                CQItem keyHolder = new CQItem(command){
                    private final int key = InfoInt;
                    public void execute(){
                        bot.holdKey(this.key);
                    }
                };
                CQItem keyReleaser = new CQItem(RKEY_COM){
                    private final int key = InfoInt;
                    public void execute(){
                        bot.releaseKey(this.key);
                    }
                };
                keyHolder.setPointer(keyReleaser); // pointers are necessary for synchronized deleting of these paired commands.
                keyReleaser.setPointer(keyHolder);
                queue.add(keyHolder);
                queue.add(keyReleaser);
                app_frame.addCommand(command + "(" + keyStrings.get(InfoInt) + ")");
                app_frame.addCommand(RKEY_COM + "(" + keyStrings.get(InfoInt) + ")");
                break;
	        case SLOOP_COM:
	        	String loop_count = String.format("%s %s", command, String.valueOf(InfoInt));
                CQItem loopStart = new CQItem(loop_count);
                CQItem loopEnd = new CQItem(ELOOP_COM);
                loopStart.setPointer(loopEnd);
                loopEnd.setPointer(loopStart);
                queue.add(loopStart);
                queue.add(loopEnd);
                app_frame.addCommand(String.format("%s (%d) {", command, InfoInt));
                app_frame.addCommand("} " + ELOOP_COM);
	        	break;
            case WAIT_COM:
                queue.add(new CQItem(command){ private final int time = InfoInt; public void execute(){ bot.wait(this.time); } } );
                app_frame.addCommand(command + "(" + InfoInt + ")");
                break;
        }
        if((InfoInt == -1) && (!command.equals(CLICKH_COM))) { app_frame.addCommand(command); } // Updates the list model on the GUI.
    }

    public void execute() {
    	/* Executes the command sequence in the locally stored <queue> ArrayList. Uses a recursive algorithm structure
    	to allow for nested loop functionality.*/
	    app_frame.setTitle("SimpleScripter Prototype (Executing...)");
	    if (min_on_exec) {
	    	app_frame.setState(Frame.ICONIFIED);
	    }

		int index = 0;
		CQItem item;
		while (index < queue.size()) {
			item = queue.get(index);
			app_frame.setExecutionIndex(index);
			if (!item.getCommand().contains(SLOOP_COM)) {
				item.execute();
				index++;
			} else {
				// TODO: Add proper error handling
				int repeats = Integer.parseInt(StringUtils.substringAfterLast(item.getCommand(), " "));
				index += recursiveExecute(index + 1, repeats) + 2; // TODO: Check for off-by-one's
			}
		}

		app_frame.setExecutionIndex(-1);
	    app_frame.setTitle("SimpleScripter Prototype");
	    if (min_on_exec) {
	    	app_frame.setState(Frame.NORMAL);
	    }
    }

    public int recursiveExecute(int start_index, int repeats) {
		int comms_in_loop = 0, index;
		CQItem item = queue.get(start_index);
		for (int i = 0; i < repeats; i++) {
			index = start_index;
			app_frame.setExecutionIndex(start_index);
			while (!item.getCommand().equals(ELOOP_COM)) {
				if (!item.getCommand().contains(SLOOP_COM)) {
					item.execute();
					index++;
					if (i == 0) { comms_in_loop++; }
				} else {
					int new_repeats = Integer.parseInt(StringUtils.substringAfterLast(item.getCommand(), " "));
					int increment = recursiveExecute(index + 1, new_repeats) + 2;
					index += increment;
					if (i == 0) { comms_in_loop += increment; }
				}

				item = queue.get(index);
				app_frame.setExecutionIndex(index);
			}
			item = queue.get(start_index);
		}
    	return comms_in_loop;
    }

    public void addCommand(String command, int left, int right){
        /* An addCommand override that takes two inputs. Meant to be used for adding move mouse commands, but could
        potentially handle other future commands that might take two integer parameters.
        Added functionality to include random wait command.*/
        if(command.equals(MM_COM)) {
            queue.add(new CQItem(command) {
               private final int x_coord = left;
               private final int y_coord = right;
               public void execute(){
                   bot.moveMouse(this.x_coord, this.y_coord);
               }
            });
            app_frame.addCommand(String.format("%s(%d,%d)", command, left, right));
        } else if (command.equals(RWAIT_COM)) {
	        queue.add(new CQItem(command) {
		        private final int lower = left;
		        private final int upper = right;
		        public void execute(){
			        bot.randomWait(this.lower, this.upper);
		        }
	        });
	        app_frame.addCommand(String.format("%s(%d-%d)", command, left, right));
        }
    }

    public void delCommand(int index){
        /* Deletes a command from the queue and updates the GUI. If the command being deleted is paired to another
        (i.e. "hold" commands), then deletes both queue items simultaneously. */
        if (!queue.isEmpty() && index != -1) {
            CQItem item = queue.get(index);
            CQItem pointer = item.getPointerRef();
            app_frame.delCommand(index);
            queue.remove(index);
            if (pointer != null) {
                int pointer_index = queue.indexOf(pointer);
                app_frame.delCommand(pointer_index);
                queue.remove(pointer_index);
            }
            repairPointers();
        }
    }

    public void swapUp(int index){
        // Swaps element in queue at index <index> with element above it.
        if(index > 0){
            Collections.swap(queue, index, index-1);
	        repairPointers();
        }
    }

    public void swapDown(int index){
        // Swaps element in queue at index <index> with element below it.
        if(index < queue.size()-1){
            Collections.swap(queue, index, index+1);
	        repairPointers();
        }
    }

    public int getPointerIndex(int index) {
    	/* Return the index of the pointer for the CQItem at <index>. If the
	       CQItem at <index> does not have a pointer, return -1. */
    	CQItem pointer = queue.get(index).getPointerRef();
    	if (pointer != null) {
		    return queue.indexOf(pointer);
	    }
    	return -1;
    }

    public void repairPointers() {
    	/* Iterates through the queue and points start loop commands to their appropriate end loop commands based on
    	their nested depth (in terms of nested loops). This prevents the user from inadvertently putting multiple start
    	and end commands in improper order. */
    	ArrayList<Integer> awaiting_pair = new ArrayList<>();
    	for (int i = 0; i < queue.size(); i++) {
    		CQItem item = queue.get(i);
    		if (item.getCommand().contains(SLOOP_COM)) {
    			awaiting_pair.add(i);
		    } else if (item.getCommand().contains(ELOOP_COM)) {
    			// Precondition: If this condition is met, <awaiting_pair> should never be empty.
			    int start_index = awaiting_pair.size() - 1;
    			CQItem start_ptr = queue.get(awaiting_pair.get(start_index));
    			item.setPointer(start_ptr);
    			start_ptr.setPointer(item);
    			awaiting_pair.remove(start_index);
		    }
	    }
    }

    public void setDelay(int delay) {
    	bot.setDelay(delay);
    }

    public void setMinOnExec(boolean val) {
    	this.min_on_exec = val;
    }
}

class CQItem{
    // Abstract object type for queue items.
    private String command;
    private CQItem pointer; // Pointer ref, only to be set when dealing with pairing commands, such as press key.
    private boolean has_pointer = false;

    public CQItem(String command){
        this.command = command;
    }

    public void execute(){
        // To be implemented through lambda expressions.
    }

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
