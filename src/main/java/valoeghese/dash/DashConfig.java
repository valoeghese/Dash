package valoeghese.dash;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public record DashConfig(double strength, double yVelocity, float cooldown, boolean resetAttack, long sensitivity, // V1.0 Config Options
						 float exhaustion, boolean doubleTapDash, boolean[] dashDirections, boolean dashMidair /*Ordered by Dash constants*/, // V1.1 Config Options
						 boolean dashWhileGliding, boolean dashWhileSwimming, ScreenPosition screenPosition // V1.2 Config Options
						 ) {
	public static DashConfig loadOrCreate() {
		// V1.0
		properties.setProperty("strength", "1.3");
		properties.setProperty("y_velocity", "0.4");
		properties.setProperty("cooldown", "1.0");
		properties.setProperty("double_tap_sensitivity", "0.2");
		properties.setProperty("resets_attack", "true");
		// V1.1
		properties.setProperty("exhaustion", "0.0");
		properties.setProperty("double_tap_dash", "true");
		properties.setProperty("forward_dash", "true");
		properties.setProperty("backwards_dash", "true");
		properties.setProperty("left_dash", "true");
		properties.setProperty("right_dash", "true");
		properties.setProperty("dash_midair", "false");
		// V1.2
		properties.setProperty("dash_while_gliding", "false");
		properties.setProperty("dash_while_swimming", "false");
		properties.setProperty("icon_x", "8");
		properties.setProperty("icon_y", "100%-32");

		File file = new File(FabricLoader.getInstance().getConfigDir().toFile(), "dash.properties");

		try {
			if (file.isFile()) {
				Properties loaded = new Properties(properties);

				try (FileReader reader = new FileReader(file)) {
					loaded.load(reader);
					properties = loaded;
				}
			}

			file.createNewFile();

			try (FileWriter writer = new FileWriter(file)) {
				properties.store(writer, "Double-Tap Dash mod config. Make sure the server and client have the same settings as the cooldown is not synced.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// V1.0
		double strength = 1.3;
		double yVelocity = 0.3;
		float cooldown = 20.0f;
		boolean resetAttack = true;
		long sensitivity = 200;
		// V1.1
		float exhaustion = 0.0f;
		boolean doubleTapDash = true;
		boolean[] dashDirections = {true, true, true, true};
		boolean dashMidair = false;
		boolean dashWhileGliding = false;
		boolean dashWhileSwimming = false;
		ScreenPosition screenPosition = new ScreenPosition(0, 0, 8, 0, 1, -32);

		try {
			// V1.0
			strength = Double.parseDouble(properties.getProperty("strength"));
			yVelocity = Double.parseDouble(properties.getProperty("y_velocity"));
			cooldown = 20 * Float.parseFloat(properties.getProperty("cooldown"));
			resetAttack = Boolean.parseBoolean(properties.getProperty("resets_attack"));
			sensitivity = (long) (1000 * Double.parseDouble(properties.getProperty("double_tap_sensitivity")));
			// V1.1
			exhaustion = Float.parseFloat(properties.getProperty("exhaustion"));
			doubleTapDash = Boolean.parseBoolean(properties.getProperty("double_tap_dash"));
			dashDirections = new boolean[] {
					Boolean.parseBoolean(properties.getProperty("forward_dash")),
					Boolean.parseBoolean(properties.getProperty("backwards_dash")),
					Boolean.parseBoolean(properties.getProperty("left_dash")),
					Boolean.parseBoolean(properties.getProperty("right_dash"))
			};
			dashMidair = Boolean.parseBoolean(properties.getProperty("dash_midair"));
			// V1.2
			dashWhileGliding = Boolean.parseBoolean(properties.getProperty("dash_while_gliding"));
			dashWhileSwimming = Boolean.parseBoolean(properties.getProperty("dash_while_swimming"));
			screenPosition = ScreenPosition.parse(properties.getProperty("icon_x"), properties.getProperty("icon_y"));
		} catch (Exception e) {
			Dash.LOGGER.error("Error parsing dash config:");
			e.printStackTrace();
		}

		return new DashConfig(strength, yVelocity, cooldown, resetAttack, sensitivity, // V1.0
				exhaustion, doubleTapDash, dashDirections, dashMidair, // V1.1
				dashWhileGliding, dashWhileSwimming, screenPosition); // V1.2
	}

	private static Properties properties = new Properties();
}
