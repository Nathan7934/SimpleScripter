import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AppFrame extends JFrame {
    /* The primary GUI frame. Every other component is a sub-component of this frame.
    */

    private CommandHandler ch = new CommandHandler(this);

    private SimpleCommands simple_coms = new SimpleCommands(ch);
    private ParameterCommands para_coms = new ParameterCommands(ch, this);
    private CommandsList coms_list = new CommandsList(ch, this);
    private JLabel m_info = new JLabel("MP: (0,0)");

    public AppFrame(){
        super("SimpleScripter Prototype");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.5; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.NONE;
        add(simple_coms, gc);
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.insets = new Insets(0,5,5,0);
        add(m_info, gc);
        gc.gridx = 1; gc.gridy = 0; gc.weightx = 0.5; gc.weighty = 1.0; // Repeated assignments for sake of clarity.
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0,20,0,0);
        add(para_coms, gc);
        gc.gridx = 2; gc.gridy = 0; gc.weightx = 1.0; gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,0);
        add(coms_list, gc);

        // Giving myself credit lmao
        gc.gridx = 1; gc.anchor = GridBagConstraints.LAST_LINE_END;
        gc.insets = new Insets(0,0,5,1);
        add(new JLabel("By: Nathan Raymant"), gc);

        // Start thread that updates mouse position label.
        Thread m_thread = new Thread(new MPThread(m_info));
        m_thread.start();
    }

    public void addCommand(String command){
        coms_list.addToList(command);
    }

    public void delCommand(int index){ coms_list.removeFromList(index);}
}

class SimpleCommands extends JPanel implements ActionListener{
    /* A sub panel of AppFrame. Contains all simple command buttons and handles their click actions.
     */

    // Define panel components
    private JButton click_btn = new JButton("Left Click");
    private JButton rclick_btn = new JButton("Right Click");
    private JButton dclick_btn = new JButton("Double Click");
    private JButton copy_btn = new JButton("Copy");
    private JButton paste_btn = new JButton("Paste");
    private JButton clickh_btn = new JButton("Hold Click");

    private CommandHandler ch;

    public SimpleCommands(CommandHandler command_handler){

        ch = command_handler;
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Simple Commands"));

        Dimension size = new Dimension(125,20);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(3,10,3,10);
        click_btn.setPreferredSize(size);
        add(click_btn, gc);
        gc.gridy = 1;
        rclick_btn.setPreferredSize(size);
        add(rclick_btn, gc);
        gc.gridy = 2;
        dclick_btn.setPreferredSize(size);
        add(dclick_btn, gc);
        gc.gridy = 3;
        copy_btn.setPreferredSize(size);
        add(copy_btn, gc);
        gc.gridy = 4;
        paste_btn.setPreferredSize(size);
        add(paste_btn, gc);
        gc.gridy = 5;
        clickh_btn.setPreferredSize(size);
        add(clickh_btn, gc);
        gc.insets = new Insets(3, 10, 8, 10);

        // Add actionListeners
        click_btn.addActionListener(this);
        rclick_btn.addActionListener(this);
        dclick_btn.addActionListener(this);
        copy_btn.addActionListener(this);
        paste_btn.addActionListener(this);
        clickh_btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        /* This frame itself behaves as an action listener. It distinguishes which button is pressed, and adds the
        correct corresponding command to CommandHandler's queue.
        */
        JButton stim = (JButton)e.getSource();

        if(stim == click_btn){ ch.addCommand(CommandHandler.CLICK_COM, -1); }
        else if(stim == rclick_btn){ ch.addCommand(CommandHandler.RCLICK_COM, -1); }
        else if(stim == dclick_btn){ ch.addCommand(CommandHandler.DCLICK_COM, -1); }
        else if(stim == copy_btn){ ch.addCommand(CommandHandler.COPY_COM, -1); }
        else if(stim == paste_btn){ ch.addCommand(CommandHandler.PASTE_COM, -1); }
        else{ ch.addCommand(CommandHandler.CLICKH_COM, -1); }
    }
}

class ParameterCommands extends JPanel implements ActionListener{
    /* A sub panel of AppFrame. Contains all parameter command buttons and handles click actions for them.
       Creates KeyCatcherDialog boxes for taking in key input parameters.
     */

    private JButton pkey_btn = new JButton("Press Key");
    private JButton hkey_btn = new JButton("Hold Key");
    private JButton mm_btn = new JButton("Move Mouse");
    private JButton wait_btn = new JButton("Wait");
    private JButton loop_btn = new JButton("Loop");

    private CommandHandler ch;
    private JFrame app_frame;

    public ParameterCommands(CommandHandler command_handler, JFrame app_frame){
        this.ch = command_handler;
        this.app_frame = app_frame;
        setLayout(new GridBagLayout());
        setBorder(new TitledBorder("Parameter Commands"));

        Dimension size = new Dimension(125,20);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1; gc.weighty = 1;
        gc.fill = GridBagConstraints.NONE;
        gc.insets = new Insets(3,10,3,10);
        pkey_btn.setPreferredSize(size);
        add(pkey_btn, gc);
        gc.gridy = 1;
        hkey_btn.setPreferredSize(size);
        add(hkey_btn, gc);
        gc.gridy = 2;
        mm_btn.setPreferredSize(size);
        add(mm_btn, gc);
        gc.gridy = 3;
        loop_btn.setPreferredSize(size);
        add(loop_btn, gc);
        gc.gridy = 4;
        gc.insets = new Insets(3, 10, 8, 10);
        wait_btn.setPreferredSize(size);
        add(wait_btn, gc);

        pkey_btn.addActionListener(this);
        hkey_btn.addActionListener(this);
        mm_btn.addActionListener(this);
        loop_btn.addActionListener(this);
        wait_btn.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        /* Similar to simple commands, except the input is taken by dialog boxes. These dialogs are where the commands
        are sent to CommandHandler.
        */
        JButton stim = (JButton) e.getSource();

        if (stim == pkey_btn) {
            keyCatcherDialog kc = new keyCatcherDialog(app_frame, ch, ch.PKEY_COM);
            kc.setVisible(true);
        } else if (stim == hkey_btn) {
            keyCatcherDialog kc = new keyCatcherDialog(app_frame, ch, ch.HKEY_COM);
            kc.setVisible(true);
        } else if (stim == mm_btn) {
            dualIntCatcherDialog dic = new dualIntCatcherDialog(app_frame, ch, ch.MM_COM);
            dic.setVisible(true);
        } else if (stim == wait_btn) {
            intCatcherDialog ic = new intCatcherDialog(app_frame, ch, ch.WAIT_COM);
            ic.setVisible(true);
        } else {
        	intCatcherDialog ic = new intCatcherDialog(app_frame, ch, ch.SLOOP_COM);
        	ic.setVisible(true);
        }
    }
}

class CommandsList extends JPanel implements ActionListener{
    /* A sub panel of AppFrame that displays the queue list, additionally handles actions on nearby buttons (Execute,
    swap up, swap down, and delete).
     */

    private CommandHandler ch;
    private JFrame app_frame;
    private ListItem curr_pointed;

    private DefaultListModel list_model = new DefaultListModel();
    private JList q_lst = new JList(list_model);
    private JLabel title_lbl = new JLabel("Queued Commands");
    private JButton exe_btn = new JButton("Execute");
    private JButton up = new JButton();
    private JButton down = new JButton();
    private JButton del = new JButton();

    private int curr_index = 0;

    public CommandsList(CommandHandler command_handler, JFrame app_frame){
        this.ch = command_handler; this.app_frame = app_frame;
        this.curr_pointed = null;

        setBackground(new Color(204,204,204));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 1.0; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.NONE;
        add(title_lbl, gc);
        gc.gridy = 1;
        q_lst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        q_lst.setCellRenderer(new QueueListRenderer());

        JScrollPane q_pane = new JScrollPane(q_lst);
        q_pane.setPreferredSize(new Dimension(200, 300));
        add(q_pane, gc);
        gc.gridy = 2;
        exe_btn.setPreferredSize(new Dimension(100, 40));
        add(exe_btn, gc);

        gc.insets = new Insets(0, 5,0,0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        up.setPreferredSize(new Dimension(20,20));
        add(up, gc);
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        down.setPreferredSize(new Dimension(20,20));
        add(down, gc);
        gc.insets = new Insets(0, 0, 0, 5);
        gc.anchor = GridBagConstraints.LINE_END;
        del.setPreferredSize(new Dimension(20,20));
        add(del, gc);

        exe_btn.addActionListener(this);
        up.addActionListener(this);
        down.addActionListener(this);
        del.addActionListener(this);
        q_lst.addListSelectionListener(new ListSelectionListener(){
            // This listener is defined here as an anonymous inner class. Keeps track of which list item is selected.
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting() && q_lst.getSelectedIndex() != -1) { // fixes bug where event fires multiple times with mouse click.
                	// for some reason, an action is caught when the del button is pressed. The -1 comparison fixes this...
	                // should examine further.
                    curr_index = q_lst.getSelectedIndex();
                    updatePointed();
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        JButton stim = (JButton) e.getSource();

        if (stim == up) {
            ch.swapUp(curr_index);
            swapUp(curr_index);
        } else if (stim == down) {
            ch.swapDown(curr_index);
            swapDown(curr_index);
        } else if (stim == del) {
            ch.delCommand(curr_index);
        } else {
			ExecutionLauncher il = new ExecutionLauncher(app_frame, ch);
			il.setVisible(true);
			Thread timer_thread = new Thread(new ExecutionTimer(il.getTimer(), il, ch));
			timer_thread.start();
        }
    }

	// Add <command> to list model.
    public void addToList(String command){
    	ListItem new_item = new ListItem(command);
    	list_model.addElement(new_item);
    }

    public void removeFromList(int index){
        // Removes command at <index> from the display. Also modifies the selection based on where <index> is.
        if(!list_model.isEmpty()) {
            list_model.remove(index);
            if (index < list_model.size()) {
                q_lst.setSelectedIndex(index);
                curr_index = index;
            } else {
                q_lst.setSelectedIndex(index - 1);
                curr_index = index-1;
            }
            updatePointed();
        }
    }

    public void swapUp(int index){
        // Swaps element in list model at index <index> with element above it.
        if(index > 0){
        	ListItem temp = (ListItem) list_model.get(index-1);
            list_model.set(index-1, list_model.get(index));
            list_model.set(index, temp);
            q_lst.setSelectedIndex(index-1);
            curr_index = index-1;
            updatePointed();
        }
    }

    public void swapDown(int index){
        // Swaps element in list model at index <index> with element below it.
        if(index < list_model.size()-1){
	        ListItem temp = (ListItem) list_model.get(index+1);
            list_model.set(index+1, list_model.get(index));
            list_model.set(index, temp);
            q_lst.setSelectedIndex(index+1);
            curr_index = index+1;
            updatePointed();
        }
    }

    public void updatePointed() {
    	/* Updates the pointer information related to the selected ListItem.
    	   This is important for the pair-highlighting functionality between
    	   paired commands (such as loop and hold click). */
	    if (curr_pointed != null) {
		    curr_pointed.setPairIsSelected(false);
	    }
	    if (ch.getPointerIndex(curr_index) != -1) {
		    ListItem pointed = (ListItem) list_model.get(ch.getPointerIndex(curr_index));
		    pointed.setPairIsSelected(true);
		    curr_pointed = pointed;
	    }
	    q_lst.repaint();
    }

	class QueueListRenderer extends JLabel implements ListCellRenderer {
    	/* A custom list renderer. Using this to overwrite the default
    	   list renderer allows us to change the background color for
    	   specific elements in the list. */
		public QueueListRenderer() {
			setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value, int index,
		                                              boolean isSelected, boolean cellHasFocus) {
			ListItem item = (ListItem) value;
			setText(item.toString());
			Color background;
			if (isSelected) {
				background = list.getSelectionBackground();
			} else if (item.getPairIsSelected()) {
				background = new Color(122, 203, 124, 191);
			} else {
				background = list.getBackground();
			}
			setBackground(background);
			return this;
		}
	}

	class ListItem {
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
	    public boolean getPairIsSelected() {
    		return this.pairIsSelected;
	    }
	    public String toString() {
    		return command;
	    }
	}
}

class ExecutionLauncher extends JDialog {
	/* A window that appears after the user clicks the "execute" button. It displays a three
    second countdown warning the user that execution is about to occur. */
	private JLabel warning = new JLabel("            Execution begins in:            ");
	private JLabel timer = new JLabel("3");
	private CommandHandler ch;

	public ExecutionLauncher(JFrame app_frame, CommandHandler command_handler) {
		super(app_frame, "");
		this.ch = command_handler;
		setSize(new Dimension(225, 125));
		setLayout(new FlowLayout());
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		timer.setFont(new Font(timer.getFont().getName(), timer.getFont().getStyle(), 36));
		add(warning);
		add(timer);
		setLocationRelativeTo(app_frame);
		setResizable(false);
	}

	public JLabel getTimer() {
		return timer;
	}
}

class keyCatcherDialog extends JDialog implements KeyListener {
    /* A Dialog who's purpose is to wait for and collect a user input key.
    Meant to be used while defining parameter commands involving key presses.*/
    private JLabel instruction = new JLabel(" [Press the appropriate key] ");
    private CommandHandler ch;
    private String command;

    public keyCatcherDialog(JFrame app_frame, CommandHandler command_handler, String command){
        super(app_frame, "Key Catcher");
        this.ch = command_handler; this.command = command;
        setFocusTraversalKeysEnabled(false);
        setSize(new Dimension(225, 75));
        setLayout(new BorderLayout());
        instruction.setFont(new Font(instruction.getFont().getName(), instruction.getFont().getStyle(), 14));
        add(instruction, BorderLayout.CENTER);
        setLocationRelativeTo(app_frame);
        setResizable(false);

        addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        int KeyInt = e.getKeyCode();
        ch.addCommand(command, KeyInt);
        dispose();
    }


    // Unimplemented key listener methods. Leave empty.
    public void keyTyped(KeyEvent e) { }
    public void keyReleased(KeyEvent e) {}
}

class intCatcherDialog extends JDialog implements ActionListener{
    /* A Dialog who's purpose is to take a user input of a single integer (via input text field).
    Main usage is planned for wait commands (for inputting ms) and loop commands (for inputting no. of loops). */
    private JLabel instruction = new JLabel();
    private JTextField input = new JTextField(12);
    private JButton enter = new JButton("Enter");
    private CommandHandler ch;
    private String command;

    public intCatcherDialog(JFrame app_frame, CommandHandler command_handler, String command){
        super(app_frame, "Int Catcher");
        this.ch = command_handler; this.command = command;
        setFocusTraversalKeysEnabled(false);
        setSize(new Dimension(225, 125));
        setLayout(new FlowLayout());
        if (command == CommandHandler.WAIT_COM) {
        	instruction.setText("Enter the desired delay (ms): ");
        } else if (command == CommandHandler.SLOOP_COM) {
        	instruction.setText("Enter the number of iterations: ");
        }
        add(instruction);
        add(input);
        add(enter);
        setLocationRelativeTo(app_frame);
        setResizable(false);

        enter.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if ((StringUtils.isNumeric(input.getText())) && (StringUtils.isNotEmpty(input.getText()))) {
        	ch.addCommand(command, Integer.parseInt(input.getText()));
            dispose();
        }
    }
}

class dualIntCatcherDialog extends JDialog implements ActionListener{
    /* A Dialog who's purpose is to take a user input of two integers (via input text fields).
    Main usage is only planned for move mouse command (for getting coords).*/
    private JLabel instruction = new JLabel("               Enter coordinates:               "); //TODO: Stop being super lazy about layouts lol
    private JLabel x_lbl = new JLabel("x: ");
    private JLabel y_lbl = new JLabel(" y: ");
    private JTextField x_input = new JTextField(6);
    private JTextField y_input = new JTextField(6);
    private JButton enter = new JButton("Enter");
    private CommandHandler ch;
    private String command;

    public dualIntCatcherDialog(JFrame app_frame, CommandHandler command_handler, String command){
        super(app_frame, "Dual Int Catcher");
        this.ch = command_handler; this.command = command;
        setFocusTraversalKeysEnabled(false);
        setSize(new Dimension(225, 125));
        setLayout(new FlowLayout());
        add(instruction);
        add(x_lbl);
        add(x_input);
        add(y_lbl);
        add(y_input);
        add(enter);
        setLocationRelativeTo(app_frame);
        setResizable(false);

        enter.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String x_val = x_input.getText();
        String y_val = y_input.getText();
        if ((StringUtils.isNumeric(x_val)) && (StringUtils.isNotEmpty(x_val)) && (StringUtils.isNumeric(y_val)) && (StringUtils.isNotEmpty(y_val))){
            ch.addCommand(command, Integer.parseInt(x_val), Integer.parseInt(y_val));
            dispose();
        }
    }
}

class ExecutionTimer implements Runnable {
	private JLabel timer;
	private JDialog warning_dialog;
	private CommandHandler ch;
	public ExecutionTimer(JLabel timer, JDialog warning_dialog, CommandHandler command_handler) {
		this.timer = timer;
		this.warning_dialog = warning_dialog;
		this.ch = command_handler;
	}
	public void run() {
		for (int i = 2; i >= 0; i--) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			timer.setText(String.valueOf(i));
		}
		warning_dialog.dispose();
		ch.execute();
	}
}

class MPThread implements Runnable{
    // A separate thread whose purpose is to continuously update the mouse pointer info on the frame.
    private JLabel m_info;
    public MPThread(JLabel m_info){
        this.m_info =  m_info;
    }
    public void run(){
        PointerInfo mpi;
        Point p;
        int x, y;
        while(true){
            try{
                Thread.sleep(100);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            mpi = MouseInfo.getPointerInfo();
            p = mpi.getLocation();
            x = (int) p.getX();
            y = (int) p.getY();
            m_info.setText("MP: (" + x + "," + y + ")");
        }
    }
}


