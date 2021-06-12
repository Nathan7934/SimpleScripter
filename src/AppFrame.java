import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

public class AppFrame extends JFrame implements ActionListener{
    /* The primary GUI frame. Every other component is a sub-component of this frame.
    */

    private CommandHandler ch = new CommandHandler(this);

    private SimpleCommands simple_coms = new SimpleCommands(ch);
    private ParameterCommands para_coms = new ParameterCommands(ch, this);
    private CommandsList coms_list;
    private JButton menu;
    private MenuList menu_list = new MenuList(this);
    private JLabel m_info = new JLabel("MP: (0,0)");

    private int[] settings; // An array for storing the user's settings.
    // Boolean settings, like 'display pointer position', are stored as 1's (true) and 0's (false)
    // Declaration of settings array indices as constants:
    public static final int INTERCOM_DELAY = 0, EXEC_DELAY = 1, MIN_ON_EXEC = 2, AUTOSAVE_INTERVAL = 3, SHOW_ADVANCED = 4,
    MANUAL_COORDS = 5, DISPLAY_POS = 6, DISPLAY_FILE = 7;
    private static int[] DEFAULT_SETTINGS = new int[]{300, 3, 0, 15, 0, 0, 0, 1};

    public AppFrame() {
        super("SimpleScripter Prototype");

        // Settings configuration
        this.settings = Arrays.copyOf(DEFAULT_SETTINGS, 8); // These are the defaults. Order of settings in array
        //  correlates to the order in which the private settings variables are declared in SettingsDialog.
        // TODO: Make it so that the user's settings persist when the program closes

        // UI Configuration
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setResizable(false);
        menu = new JButton(new ImageIcon("./icons/menu.png"));

        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.5; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.NONE;
        add(simple_coms, gc);
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        gc.insets = new Insets(0,5,5,0);
        add(m_info, gc);

        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        gc.insets = new Insets(0, 0, 0, 0);
        menu.setPreferredSize(new Dimension(30, 30));
        menu.setBorder(BorderFactory.createEmptyBorder());
        menu.setBackground(new Color(238, 238, 238));
        // Next 3 lines fix an obscure bug where clicking the menu button doesn't close the popup menu.
        JComboBox prop = new JComboBox();
        Object preventHide = prop.getClientProperty("doNotCancelPopup");
        menu.putClientProperty("doNotCancelPopup", preventHide);
        add(menu, gc);
        menu.addActionListener(this);

        gc.gridx = 1; gc.gridy = 0; gc.weightx = 0.5; gc.weighty = 1.0; // Repeated assignments for sake of clarity.
        gc.anchor = GridBagConstraints.CENTER;
        gc.insets = new Insets(0,20,0,0);
        add(para_coms, gc);
        gc.gridx = 2; gc.gridy = 0; gc.weightx = 1.0; gc.weighty = 1.0;
        gc.anchor = GridBagConstraints.LINE_END;
        gc.insets = new Insets(0,0,0,0);
        coms_list = new CommandsList(ch, this, getSettingVal(EXEC_DELAY));
        add(coms_list, gc);

	    applySettings(this.settings);

        // Start thread that updates mouse position label.
        Thread m_thread = new Thread(new MPThread(m_info));
        m_thread.start();
    }

    public void actionPerformed(ActionEvent e) {
    	if (!menu_list.isVisible()) {
		    menu_list.show(this, 39, 31); // Hardcoded values should be removed if window scaling is added
	    } else {
    		menu_list.setVisible(false);
	    }
	}

    public void addCommand(String command){
        coms_list.addToList(command);
    }

    public void delCommand(int index){ coms_list.removeFromList(index);}

	public void setExecutionIndex(int index) {
    	coms_list.setExecutionIndex(index);
	}

	public void applySettings(int[] new_settings) {
        // Applies the settings based on their values in the settings array.
		for (int val = 0; val < settings.length; val++) {
			switch(val) {
				case INTERCOM_DELAY:
					settings[INTERCOM_DELAY] = new_settings[INTERCOM_DELAY];
					ch.setDelay(new_settings[INTERCOM_DELAY]);
					break;
				case EXEC_DELAY:
					settings[EXEC_DELAY] = new_settings[EXEC_DELAY];
					coms_list.setExecutionDelay(new_settings[EXEC_DELAY]);
					break;
				case MIN_ON_EXEC:
					settings[MIN_ON_EXEC] = new_settings[MIN_ON_EXEC];
					ch.setMinOnExec(new_settings[MIN_ON_EXEC] == 1);
					break;
				case AUTOSAVE_INTERVAL:
					// TODO: Implement

					break;
				case SHOW_ADVANCED:
					// TODO: Implement

					break;
				case MANUAL_COORDS:
					// TODO: Implement

					break;
				case DISPLAY_POS:
					// TODO: Implement

					break;
				case DISPLAY_FILE:
					// TODO: Implement

					break;
			}
		}
    }

    public int getSettingVal(int index) {
        // Gets a setting's value from the settings array. Takes the index of value in the array. Index should be passed
        // using the defined static constants.
        return settings[index];
    }

	class MenuList extends JPopupMenu implements ActionListener{
    	private JFrame app_frame;
    	private JMenuItem save = new JMenuItem("Save");
    	private JMenuItem load = new JMenuItem("Load");
    	private JMenuItem settings = new JMenuItem("Settings");
    	private JMenuItem about = new JMenuItem("About");
    	public static final int WIDTH = 60;
    	public static final int HEIGHT = 80;

    	public MenuList(JFrame app_frame) {
    		this.app_frame = app_frame;
    		setPopupSize(WIDTH, HEIGHT);
    		add(save);
    		add(load);
    		add(settings);
    		add(about);
    		save.addActionListener(this);
    		load.addActionListener(this);
    		settings.addActionListener(this);
    		about.addActionListener(this);
	    }

    	public void actionPerformed(ActionEvent e) {
			JMenuItem stim = (JMenuItem) e.getSource();
			if (stim == save) {
				// TODO: Add save functionality
			} else if (stim == load) {
				// TODO: Add load functionality
			} else if (stim == settings) {
				SettingsDialog od = new SettingsDialog(app_frame);
				od.setVisible(true);
			} else {
				AboutDialog ad = new AboutDialog(app_frame);
				ad.setVisible(true);
			}
	    }
	}

	class AboutDialog extends JDialog {
    	/* A dialog window that appears when the user clicks the "about" option in the menu dropdown. Displays a
    	brief description of the program along with a hyperlink to the source code repository. */
    	public AboutDialog(JFrame app_frame) {
		    super(app_frame, "About the Program");

		    final String auth_text =
		    "By: Nathan C. Raymant<br/>" +
		    "Undergraduate student at the University of Toronto<br/>" +
		    "May 2021";
		    final JLabel auth = new JLabel("<html><div style='text-align: center;'>" + auth_text + "</div></html>");
		    final String name_text = "<b>SimpleScripter</b>";
		    final JLabel name = new JLabel("<html><div style='text-align: center;'>" + name_text + "</div></html>");
		    final String body_text =
		    "<br/>This program serves to act as a simple macro<br/>" +
		    "scripting tool that can be used by anyone.<br/>" +
		    "Emulate a variety of desktop inputs to create<br/>" +
		    "your own automated sequences and let your<br/>" +
		    "computer perform repetitive tasks on your behalf.<br/>" +
		    "<br/>This is a publicly available portfolio piece.<br/>" +
		    "To view the source code, visit:<br/>";
		    final JLabel body = new JLabel("<html><div style='text-align: center;'>" + body_text + "</div></html>");

    		setSize(325, 350);
    		setLocationRelativeTo(app_frame);
    		setResizable(false);
    		setLayout(new FlowLayout());
		    name.setFont(new Font(name.getFont().getName(), name.getFont().getStyle(), 28));
		    name.setForeground(new Color(14, 6, 146));
		    add(name);
    		add(auth);
    		add(body);

    		// Add the GitHub hyperlink
		    String github_text = "<div style='text-align: center;'>The GitHub Repository</div>";
		    JLabel github = new JLabel("<html>" + github_text + "</html>");
		    github.setForeground(Color.BLUE.darker());
		    github.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    github.setFont(new Font(github.getFont().getName(), github.getFont().getStyle(), 16));
		    github.setBorder(new EmptyBorder(15, 10, 10, 10));
		    github.addMouseListener(new MouseAdapter() {
		    	public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI("https://github.com/Nathan7934/SimpleScripter"));
					} catch (IOException | URISyntaxException err) {
						err.printStackTrace();
					}
			    }
			    public void mouseEntered(MouseEvent e) {
					// TODO: Figure out how to get the github link to underline on hover. All solutions found do not
				    // work for apparently no reason.
			    }
			    public void mouseExited(MouseEvent e) {
			    }
		    });
		    add(github);

	    }
	}

	class SettingsDialog extends JDialog implements ActionListener {
		/*  A dialog that allows the user to adjust the settings of the program. Appears when the user selects the
		'settings' menu option from the drop down. */

		private JTextField dd_field; // Inter-command delay (millisecond)
        private JTextField ed_field; // Execution delay (seconds)
        private JCheckBox min_chk; // minimize program on execution
        private JTextField sas_field; // autosave interval (minutes)
        private JCheckBox sao_chk; // show advanced options
        private JCheckBox mmc_chk; // manually set mouse coords
        private JCheckBox dpp_chk; // display pointer position
        private JCheckBox dof_chk; // display open file name
        private JButton apply;
        private JButton defaults;

        private boolean monitoring; // variable to prevent JTextFields' DocumentListener from triggering during the
		// build phase of the dialog.

		private JTextField[] field_settings;
		private JCheckBox[] chkbox_settings;

    	public SettingsDialog(JFrame app_frame) {
    		super(app_frame, "Settings");
		    setSize(325, 375);
		    setLocationRelativeTo(app_frame);
		    setResizable(false);
		    SpringLayout layout = new SpringLayout();
		    setLayout(layout);
		    this.monitoring = false;

            // ================ EXECUTION SETTINGS ====================================
            JSeparator exec_sep1 = new JSeparator();
            exec_sep1.setPreferredSize(new Dimension(110, 5));
            layout.putConstraint(SpringLayout.NORTH, exec_sep1, 18, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.WEST, exec_sep1, 10, SpringLayout.WEST, this);
            add(exec_sep1);
		    JLabel exec_lbl = new JLabel("Execution");
		    exec_lbl.setForeground(new Color(84, 111, 156, 255));
		    layout.putConstraint(SpringLayout.WEST, exec_lbl, 5, SpringLayout.EAST, exec_sep1);
		    layout.putConstraint(SpringLayout.NORTH, exec_lbl, 10, SpringLayout.NORTH, this);
		    add(exec_lbl);
		    JSeparator exec_sep2 = new JSeparator();
		    exec_sep2.setPreferredSize(new Dimension(110, 5));
		    layout.putConstraint(SpringLayout.NORTH, exec_sep2, 18, SpringLayout.NORTH, this);
		    layout.putConstraint(SpringLayout.WEST, exec_sep2, 5, SpringLayout.EAST, exec_lbl);
		    add(exec_sep2);

		    JLabel dd_lbl = new JLabel("Delay between commands (ms):");
		    layout.putConstraint(SpringLayout.WEST, dd_lbl, 10, SpringLayout.WEST, this);
		    layout.putConstraint(SpringLayout.NORTH, dd_lbl, 10, SpringLayout.SOUTH, exec_lbl);
		    add(dd_lbl);
		    dd_field = new JTextField(7);
		    layout.putConstraint(SpringLayout.WEST, dd_field, 15, SpringLayout.EAST, dd_lbl);
		    layout.putConstraint(SpringLayout.NORTH, dd_field, 10, SpringLayout.SOUTH, exec_lbl);
		    dd_field.getDocument().addDocumentListener(new TextChangedListener(this));
		    add(dd_field);

		    JLabel ed_lbl = new JLabel("Execution delay (seconds):");
		    layout.putConstraint(SpringLayout.WEST, ed_lbl, 10, SpringLayout.WEST, this);
		    layout.putConstraint(SpringLayout.NORTH, ed_lbl, 7, SpringLayout.SOUTH, dd_lbl);
		    add(ed_lbl);
		    ed_field = new JTextField(7);
		    layout.putConstraint(SpringLayout.WEST, ed_field, 0, SpringLayout.WEST, dd_field);
		    layout.putConstraint(SpringLayout.NORTH, ed_field, 7, SpringLayout.SOUTH, dd_lbl);
		    ed_field.getDocument().addDocumentListener(new TextChangedListener(this));
		    add(ed_field);

		    JLabel min_lbl = new JLabel("Minimize program on execution:");
		    layout.putConstraint(SpringLayout.WEST, min_lbl, 10, SpringLayout.WEST, this);
		    layout.putConstraint(SpringLayout.NORTH, min_lbl, 7, SpringLayout.SOUTH, ed_lbl);
		    add(min_lbl);
		    min_chk = new JCheckBox();
		    layout.putConstraint(SpringLayout.WEST, min_chk, 12, SpringLayout.EAST, min_lbl); // 8 or 38??
		    layout.putConstraint(SpringLayout.NORTH, min_chk, 5, SpringLayout.SOUTH, ed_lbl);
		    min_chk.addActionListener(this);
		    add(min_chk);

		    // ========================== UTILITY SETTINGS ========================================
            JSeparator util_sep1 = new JSeparator();
            util_sep1.setPreferredSize(new Dimension(123, 5));
            layout.putConstraint(SpringLayout.NORTH, util_sep1, 38, SpringLayout.NORTH, min_lbl);
            layout.putConstraint(SpringLayout.WEST, util_sep1, 10, SpringLayout.WEST, this);
            add(util_sep1);
            JLabel util_lbl = new JLabel("Utility");
            util_lbl.setForeground(new Color(84, 111, 156, 255));
            layout.putConstraint(SpringLayout.WEST, util_lbl, 5, SpringLayout.EAST, util_sep1);
            layout.putConstraint(SpringLayout.NORTH, util_lbl, 30, SpringLayout.NORTH, min_lbl);
            add(util_lbl);
            JSeparator util_sep2 = new JSeparator();
            util_sep2.setPreferredSize(new Dimension(123, 5));
            layout.putConstraint(SpringLayout.NORTH, util_sep2, 38, SpringLayout.NORTH, min_lbl);
            layout.putConstraint(SpringLayout.WEST, util_sep2, 5, SpringLayout.EAST, util_lbl);
            add(util_sep2);

            JLabel sas_lbl = new JLabel("Autosave interval (minutes):");
            layout.putConstraint(SpringLayout.WEST, sas_lbl, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, sas_lbl, 10, SpringLayout.SOUTH, util_lbl);
            add(sas_lbl);
            sas_field = new JTextField(7);
            layout.putConstraint(SpringLayout.WEST, sas_field, 0, SpringLayout.WEST, dd_field);
            layout.putConstraint(SpringLayout.NORTH, sas_field, 10, SpringLayout.SOUTH, util_lbl);
            sas_field.getDocument().addDocumentListener(new TextChangedListener(this));
            add(sas_field);

            JLabel sao_lbl = new JLabel("Show advanced options:");
            layout.putConstraint(SpringLayout.WEST, sao_lbl, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, sao_lbl, 7, SpringLayout.SOUTH, sas_lbl);
            add(sao_lbl);
            sao_chk = new JCheckBox();
            layout.putConstraint(SpringLayout.WEST, sao_chk, 0, SpringLayout.WEST, min_chk);
            layout.putConstraint(SpringLayout.NORTH, sao_chk, 5, SpringLayout.SOUTH, sas_lbl);
            sao_chk.addActionListener(this);
            add(sao_chk);

            JLabel mmc_lbl = new JLabel("Manually enter coordinates:");
            layout.putConstraint(SpringLayout.WEST, mmc_lbl, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, mmc_lbl, 7, SpringLayout.SOUTH, sao_lbl);
            add(mmc_lbl);
            mmc_chk = new JCheckBox();
            layout.putConstraint(SpringLayout.WEST, mmc_chk, 0, SpringLayout.WEST, min_chk);
            layout.putConstraint(SpringLayout.NORTH, mmc_chk, 5, SpringLayout.SOUTH, sao_lbl);
            mmc_chk.addActionListener(this);
            add(mmc_chk);

            // ========================== APPEARANCE SETTINGS =================================
            JSeparator aprnc_sep1 = new JSeparator();
            aprnc_sep1.setPreferredSize(new Dimension(106, 5));
            layout.putConstraint(SpringLayout.NORTH, aprnc_sep1, 38, SpringLayout.NORTH, mmc_lbl);
            layout.putConstraint(SpringLayout.WEST, aprnc_sep1, 10, SpringLayout.WEST, this);
            add(aprnc_sep1);
            JLabel aprnc_lbl = new JLabel("Appearance");
            aprnc_lbl.setForeground(new Color(84, 111, 156, 255));
            layout.putConstraint(SpringLayout.WEST, aprnc_lbl, 5, SpringLayout.EAST, aprnc_sep1);
            layout.putConstraint(SpringLayout.NORTH, aprnc_lbl, 30, SpringLayout.NORTH, mmc_lbl);
            add(aprnc_lbl);
            JSeparator aprnc_sep2 = new JSeparator();
            aprnc_sep2.setPreferredSize(new Dimension(107, 5));
            layout.putConstraint(SpringLayout.NORTH, aprnc_sep2, 38, SpringLayout.NORTH, mmc_lbl);
            layout.putConstraint(SpringLayout.WEST, aprnc_sep2, 5, SpringLayout.EAST, aprnc_lbl);
            add(aprnc_sep2);

            JLabel dpp_lbl = new JLabel("Display pointer position:");
            layout.putConstraint(SpringLayout.WEST, dpp_lbl, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, dpp_lbl, 10, SpringLayout.SOUTH, aprnc_lbl);
            add(dpp_lbl);
            dpp_chk = new JCheckBox();
            layout.putConstraint(SpringLayout.WEST, dpp_chk, 0, SpringLayout.WEST, min_chk);
            layout.putConstraint(SpringLayout.NORTH, dpp_chk, 8, SpringLayout.SOUTH, aprnc_lbl);
            dpp_chk.addActionListener(this);
            add(dpp_chk);

            JLabel dof_lbl = new JLabel("Display opened file name:"); // maybe make a dropdown option with options
            // for always and only when working on a saved doc
            layout.putConstraint(SpringLayout.WEST, dof_lbl, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, dof_lbl, 7, SpringLayout.SOUTH, dpp_lbl);
            add(dof_lbl);
            dof_chk = new JCheckBox();
            layout.putConstraint(SpringLayout.WEST, dof_chk, 0, SpringLayout.WEST, min_chk);
            layout.putConstraint(SpringLayout.NORTH, dof_chk, 5, SpringLayout.SOUTH, dpp_lbl);
            dof_chk.addActionListener(this);
            add(dof_chk);

            // ===================== OTHER UI ELEMENTS ============================
            apply = new JButton("Apply");
            apply.setPreferredSize(new Dimension(70, 30));
            layout.putConstraint(SpringLayout.WEST, apply, 232, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, apply, 300, SpringLayout.NORTH, this);
            apply.setEnabled(false);
            apply.addActionListener(this);
            add(apply);

            defaults = new JButton("Defaults");
            defaults.setPreferredSize(new Dimension(85, 30));
            layout.putConstraint(SpringLayout.WEST, defaults, 10, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, defaults, 300, SpringLayout.NORTH, this);
            defaults.addActionListener(this);
            add(defaults);

		    field_settings = new JTextField[] {dd_field, ed_field, sas_field};
		    chkbox_settings = new JCheckBox[] {min_chk, sao_chk, mmc_chk, dpp_chk, dof_chk};
            updateSettingsDisplay("current");
            monitoring = true;
        }

    	public void actionPerformed(ActionEvent e) {
            Object stim = e.getSource();
            if (stim instanceof JButton) {
                if (stim == apply) {
                	int[] new_settings = generateSettingsFromFields();
                	if (new_settings != null) {
                		applySettings(new_settings);
		                apply.setEnabled(false);
	                }
                } else if (stim == defaults) {
                    updateSettingsDisplay("default");
                    apply.setEnabled(true);
                }
            } else if (stim instanceof JCheckBox){
                apply.setEnabled(true);
            }
    	}

    	private int[] generateSettingsFromFields() {
    		// Generates and returns a settings array based on the current field values in the settings dialog
		    // Return value should be used as an argument for applySettings().
    		int total_len = field_settings.length + chkbox_settings.length;
    		int[] new_settings = new int[total_len];
		    int field_index = 0, chkbox_index = 0;
		    for (int i = 0; i < total_len; i++) {
			    if (i == INTERCOM_DELAY || i == EXEC_DELAY || i == AUTOSAVE_INTERVAL) {
			    	String field_text = field_settings[field_index].getText();
			    	if (!StringUtils.isNumeric(field_text)) {
			    		return null;
				    }
			    	new_settings[i] = Integer.parseInt(field_text);
			    	field_index++;
			    } else {
			    	boolean val = chkbox_settings[chkbox_index].isSelected();
			    	if (val) {
			    		new_settings[i] = 1;
				    } else {
			    		new_settings[i] = 0;
				    }
			    	chkbox_index++;
			    }
		    }
		    return new_settings;
	    }

        public void updateSettingsDisplay(String to_what) {
    		/* This method is meant to either display the current settings when the settings dialog is opened, or
    		display the default settings when the 'Defaults' button is clicked. The parameter <to_what> should be either
    		"default" or "current" to determine which functionality is employed. */
    		int field_index = 0, chkbox_index = 0;
    		for (int i = 0; i < (field_settings.length + chkbox_settings.length); i++) {
    			if (i == INTERCOM_DELAY || i == EXEC_DELAY || i == AUTOSAVE_INTERVAL) {
				    if (to_what.equals("default")) {
					    field_settings[field_index].setText(Integer.toString(DEFAULT_SETTINGS[i]));
				    } else {
					    field_settings[field_index].setText(Integer.toString(getSettingVal(i)));
				    }
				    field_index++;
			    } else {
				    if (to_what.equals("default")) {
					    chkbox_settings[chkbox_index].setSelected(DEFAULT_SETTINGS[i] == 1);
				    } else {
					    chkbox_settings[chkbox_index].setSelected(getSettingVal(i) == 1);
				    }
				    chkbox_index++;
			    }
		    }
        }

        public boolean isMonitoring() {
    		return monitoring;
        }

    	class TextChangedListener implements DocumentListener {
    		private SettingsDialog sd;
    		public TextChangedListener (SettingsDialog sd) {
    			this.sd = sd;
		    }
            public void insertUpdate(DocumentEvent e) {
    			if (sd.isMonitoring()) { apply.setEnabled(true); }
            }
            public void removeUpdate(DocumentEvent e) {
	            if (sd.isMonitoring()) { apply.setEnabled(true); }
            }
            public void changedUpdate(DocumentEvent e) {
	            if (sd.isMonitoring()) { apply.setEnabled(true); }
            }
        }
	}
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
	    clickh_btn.setPreferredSize(size);
        add(clickh_btn, gc);
        gc.gridy = 4;
	    copy_btn.setPreferredSize(size);
        add(copy_btn, gc);
        gc.gridy = 5;
	    paste_btn.setPreferredSize(size);
        add(paste_btn, gc);
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
    private JButton rwait_btn = new JButton("Random Wait");
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
        wait_btn.setPreferredSize(size);
        add(wait_btn, gc);
        gc.gridy = 5;
	    gc.insets = new Insets(3, 10, 8, 10);
        rwait_btn.setPreferredSize(size);
        add(rwait_btn, gc);

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
    private boolean is_executing = false;
    private int exec_delay;

    private DefaultListModel list_model = new DefaultListModel();
    private JList q_lst = new JList(list_model);
    private JLabel title_lbl = new JLabel("Queued Commands");
    private JButton exe_btn = new JButton("Execute");
    private JButton up;
    private JButton down;
    private JButton del;

    private int curr_index = 0;

    public CommandsList(CommandHandler command_handler, JFrame app_frame, int exec_delay){
        this.ch = command_handler; this.app_frame = app_frame;
        this.curr_pointed = null;
        this.exec_delay = exec_delay;

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

        gc.insets = new Insets(0, 15,0,0);
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        up = new JButton(new ImageIcon("./icons/up.png"));
	    up.setBorder(BorderFactory.createEmptyBorder());
	    up.setBackground(new Color(204, 204, 204));
        up.setPreferredSize(new Dimension(20,20));
        add(up, gc);
        gc.anchor = GridBagConstraints.LAST_LINE_START;
        down = new JButton(new ImageIcon("./icons/down.png"));
	    down.setBorder(BorderFactory.createEmptyBorder());
	    down.setBackground(new Color(204, 204, 204));
        down.setPreferredSize(new Dimension(20,20));
        add(down, gc);
        gc.insets = new Insets(0, 0, 0, 15);
        gc.anchor = GridBagConstraints.LINE_END;
        del = new JButton(new ImageIcon("./icons/delete.png"));
	    del.setBorder(BorderFactory.createEmptyBorder());
	    del.setBackground(new Color(204, 204, 204));
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
			Thread timer_thread = new Thread(new ExecutionTimer(il.getTimer(), il, ch, exec_delay));
			timer_thread.start();
        }
    }

	// Add <command> to list model.
    public void addToList(String command){
    	ListItem new_item = new ListItem(command);
    	list_model.addElement(new_item);
    	indentNested();
    }

    public void removeFromList(int index){
        // Removes command at <index> from the display. Also modifies the selection based on where <index> is.
        if(!list_model.isEmpty()) {
            list_model.remove(index);
            if (index < list_model.size()) {
	            curr_index = index;
            } else {
	            curr_index = index-1;
            }
	        q_lst.setSelectedIndex(curr_index);
	        if (curr_index != -1) {
	            indentNested();
	            updatePointed();
            }
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
            indentNested();
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
            indentNested();
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
	    int pointer_index = ch.getPointerIndex(curr_index);
	    if (pointer_index > -1 && pointer_index < list_model.size()) {
		    ListItem pointed = (ListItem) list_model.get(pointer_index);
		    pointed.setPairIsSelected(true);
		    curr_pointed = pointed;
	    }
	    q_lst.repaint();
    }

    public void indentNested() {
    	/* Adds indentation to nested loops in the list model. */
    	int nested_level = 0;
    	ListItem item;
    	for (int i = 0; i < list_model.size(); i++) {
    		item = (ListItem) list_model.get(i);
    		item.setCommand(item.toString().trim()); // remove previous indent
			if (item.toString().contains(ch.ELOOP_COM)) {
				nested_level--;
			}
			String indent = "";
			for (int j = 0; j < nested_level; j++) {
				indent = indent + "   ";
			}
			item.setCommand(indent + item.toString());
			list_model.set(i, item);
		    if (item.toString().contains(ch.SLOOP_COM)) {
			    nested_level++;
		    }
	    }
    	q_lst.repaint();
    }

    public void setExecutionIndex (int index) {
    	/* A method meant to be used during CommandHandler's execution process, selecting the item that is currently
    	executing in the queue. Pass -1 when execution is finished. */
    	if (index < 0 || index >= list_model.size()) {
		    is_executing = false;
    		q_lst.clearSelection();
    		curr_index = -1;
	    } else {
    		is_executing = true;
    		q_lst.setSelectedIndex(index);
	    }
    	q_lst.repaint();
    }

    public void setExecutionDelay(int exec_delay) {
    	this.exec_delay = exec_delay;
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
			Color background; Color foreground;
			if (is_executing && isSelected) {
				foreground = Color.WHITE;
				background = Color.BLACK;
			} else {
				foreground = Color.BLACK;
				if (isSelected) {
					background = list.getSelectionBackground();
				} else if (item.getPairIsSelected() && !is_executing) {
					background = new Color(122, 203, 124, 191);
				} else {
					background = list.getBackground();
				}
			}
			setForeground(foreground);
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
}

class ExecutionLauncher extends JDialog {
	/* A window that appears after the user clicks the "execute" button. It displays a three
    second countdown warning the user that execution is about to occur. */
	private JLabel warning = new JLabel("            Execution begins in:            ");
	private JLabel timer = new JLabel("");
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
        super(app_frame, "");
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
        if (StringUtils.isNumeric(input.getText())) {
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
        super(app_frame, "");
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
        String can_contain = "-0123456789";
        if (StringUtils.containsOnly(x_val, can_contain) && StringUtils.containsOnly(y_val, can_contain)){
            ch.addCommand(command, Integer.parseInt(x_val), Integer.parseInt(y_val));
            dispose();
        }
    }
}

class ExecutionTimer implements Runnable {
	private JLabel timer;
	private JDialog warning_dialog;
	private CommandHandler ch;
	private int delay;
	public ExecutionTimer(JLabel timer, JDialog warning_dialog, CommandHandler command_handler, int delay) {
		this.timer = timer;
		this.warning_dialog = warning_dialog;
		this.ch = command_handler;
		this.delay = delay;
	}
	public void run() {
		timer.setText(String.valueOf(delay));
		for (int i = delay-1; i >= 0; i--) {
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