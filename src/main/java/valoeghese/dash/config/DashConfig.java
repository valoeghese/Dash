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
import java.util.Map;
import java.util.Properties;

public class DashConfig {
	private DashConfig() {
	}

	private final Collection<Option<?>> options = new ArrayList<>();

	// Dash itself
	public final DoubleOption strength = new DoubleOption(this.options, "strength", 1.3);
	public final DoubleOption yVelocity = new DoubleOption(this.options, "y_velocity", 0.4);
	public final EnumOption<MomentumMode> momentumMode = new EnumOption<>(this.options, "momentum_mode", MomentumMode.ADD, MomentumMode::parse);

	// Meta-properties of the dash
	public final FloatOption cooldown = new FloatOption(this.options, "cooldown", 1.0f);
	public final BooleanOption resetAttack = new BooleanOption(this.options, "resets_attack", true);
	public final TimeOption sensitivity = new TimeOption(this.options, "double_tap_sensitivity", 0.2);
	public final FloatOption exhaustion = new FloatOption(this.options, "exhaustion", 0.0f);

	// What kinds of dash / trigger
	public final BooleanOption doubleTapDash = new BooleanOption(this.options, "double_tap_dash", true);

	/**
	 * Ordered by Dash constants.
	 * 0 - forwards
	 * 1 - backwards
	 * 2 - left
	 * 3 - right
	 */
	public final BooleanOption[] dashDirections = new BooleanOption[] {
			new BooleanOption(this.options, "forward_dash", true),
			new BooleanOption(this.options, "backwards_dash", true),
			new BooleanOption(this.options, "left_dash", true),
			new BooleanOption(this.options, "right_dash", true)
	};

	/**
	 * Allow any combination of enabled dash directions
	 */
	public final BooleanOption diagonalDash = new BooleanOption(this.options, "diagonal_dash", true);

	// Where dash
	public final BooleanOption dashMidair = new BooleanOption(this.options, "dash_midair", false);
	public final BooleanOption dashWhileGliding = new BooleanOption(this.options, "dash_while_gliding", false);
	public final BooleanOption dashWhileSwimming = new BooleanOption(this.options, "dash_while_swimming", false);
	public final BooleanOption dashWhileFloating = new BooleanOption(this.options, "dash_while_floating", false);

	// GUI
	public final ScreenPositionOption screenPosition = new ScreenPositionOption(
			this.options,
			"icon",
			new ScreenPosition(0, 0, 8, 0, 1, -32)
	);

	public void read(Properties properties) {
		for (Option<?> option : this.options) {
			try {
				option.deserialise(properties);
			} catch (IllegalArgumentException e) {
				Dash.LOGGER.error("Could not parse value for dash option \"" + option.name + "\". Reverting to default!");
			}
		}
	}

	public void save(Properties properties) {
		for (Option<?> option : this.options) {
			option.serialise(properties);
		}
	}

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
