package valoeghese.dash.config;

import net.fabricmc.loader.api.FabricLoader;
import valoeghese.dash.Dash;
import valoeghese.dash.MomentumMode;
import valoeghese.dash.ScreenPosition;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class DashConfig extends SynchronisedConfig {
	private DashConfig() {
	}

	// Client Side
	public final TimeOption sensitivity = new TimeOption(this.options, "double_tap_sensitivity", 0.2);

	public final ScreenPositionOption iconPosition = new ScreenPositionOption(
			this.options,
			"icon",
			new ScreenPosition(0, 0, 8, 0, 1, -32)
	);

	public static DashConfig loadOrCreate() {
		Properties properties = new Properties();
		DashConfig config = new DashConfig();

		// add defaults (these will be overridden by anything read in)
		config.save(properties);

		File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "dash.properties");

		try {
			if (file.isFile()) {
				Properties loaded = new Properties();

				try (FileReader reader = new FileReader(file)) {
					loaded.load(reader);
				}

				// overwrite defaults
				properties.putAll(loaded);
			}

			file.createNewFile();

			try (FileWriter writer = new FileWriter(file)) {
				properties.store(writer, "Double-Tap Dash mod config.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		config.read(properties);
		return config;
	}
}
