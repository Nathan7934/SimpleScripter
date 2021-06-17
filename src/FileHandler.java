import org.apache.commons.lang3.StringUtils;

import java.io.*;

public class FileHandler {
	/* Class consisting of static methods for file handling. Used as a pipeline for settings data between the program
	and config.txt as well as enaabling the storage of scripts as Serializables. */

	public static void writeSettingsToFile(int[] settings) {
		/* A static method for writing the currently applied user settings to config.txt. */

		final String[] SETTING_PREFIXES = {"DelayBetweenCommands", "ExecutionDelay",
				"MinimizeProgramOnExecution", "AutosaveInterval", "ShowAdvancedOptions", "ManuallyEnterCoordinates",
				"DisplayPointerPosition", "DisplayOpenedFileName"};
		try {
			FileWriter writer = new FileWriter("config.txt");
			for (int i = 0; i < settings.length; i++) {
				String to_write = "SET " + SETTING_PREFIXES[i] + " \"" + settings[i] + "\"";
				writer.write(to_write + "\r\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int[] readSettingsFromFile() {
		/* A static method for reading in the user defined settings from config.txt. If config.txt does not exist, it
		will be created (by calling writeSettingsToFile()) containing the default settings. */

		int[] settings = new int[AppFrame.DEFAULT_SETTINGS.length];
		BufferedReader reader = null;
		File f = new File("config.txt");
		if (!f.exists() || f.isDirectory()) {
			writeSettingsToFile(AppFrame.DEFAULT_SETTINGS);
			return AppFrame.DEFAULT_SETTINGS;
		}
		try {
			reader = new BufferedReader(new FileReader(f));
			String line;
			int index = 0;
			while ((line = reader.readLine()) != null) {
				line = StringUtils.substringBetween(line, "\"");
				if (StringUtils.isNumeric(line)) {
					settings[index] = Integer.parseInt(line);
				} else if (index < AppFrame.DEFAULT_SETTINGS.length){
					// If user changes values by manually editing config.txt - and enters an invalid entry, then the
					// program will just boot with default settings
					return AppFrame.DEFAULT_SETTINGS;
				}
				index++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ee) {
				ee.printStackTrace();
			}
		}
		return settings;
	}
}
