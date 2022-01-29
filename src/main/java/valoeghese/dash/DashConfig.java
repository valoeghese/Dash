package valoeghese.dash;

import net.minecraftforge.fml.loading.FMLConfig;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public record DashConfig(double strength, double yVelocity, float cooldown, boolean resetAttack, long sensitivity) {
	public static DashConfig loadOrCreate() {
		properties.setProperty("strength", "1.3");
		properties.setProperty("y_velocity", "0.3");
		properties.setProperty("cooldown", "1.0");
		properties.setProperty("double_tap_sensitivity", "0.2");
		properties.setProperty("resets_attack", "true");

		File file = new File(FMLConfig.defaultConfigPath(), "dash.properties");

		try {
			if (file.isFile()) {
				Properties loaded = new Properties(properties);

				try (FileReader reader = new FileReader(file)) {
					loaded.load(reader);
					properties = loaded;
				}
			} else {
				file.createNewFile();

				try (FileWriter writer = new FileWriter(file)) {
					properties.store(writer, "Dash mod config. Make sure the server and client have the same settings as the cooldown is not synced.");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		double strength = 1.3;
		double yVelocity = 0.3;
		float cooldown = 20.0f;
		boolean resetAttack = true;
		long sensitivity = 200;

		try {
			strength = Double.parseDouble(properties.getProperty("strength"));
			yVelocity = Double.parseDouble(properties.getProperty("y_velocity"));
			cooldown = 20 * Float.parseFloat(properties.getProperty("cooldown"));
			resetAttack = Boolean.parseBoolean(properties.getProperty("resets_attack"));
			sensitivity = (long) (1000 * Double.parseDouble(properties.getProperty("double_tap_sensitivity")));
		} catch (Exception e) {
			Dash.LOGGER.error("Error parsing dash config:");
			e.printStackTrace();
		}

		return new DashConfig(strength, yVelocity, cooldown, resetAttack, sensitivity);
	}

	private static Properties properties = new Properties();
}
