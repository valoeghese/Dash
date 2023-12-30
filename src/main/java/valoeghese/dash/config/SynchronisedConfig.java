package valoeghese.dash.config;

import valoeghese.dash.Dash;
import valoeghese.dash.MomentumMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * Contains everything that is synchronised between the client and server configs.
 */
public class SynchronisedConfig {
	protected final Collection<Option<?>> options = new ArrayList<>();

	// Dash itself
	public final DoubleOption strength = new DoubleOption(this.options, "strength", 1.3);
	public final DoubleOption yVelocity = new DoubleOption(this.options, "y_velocity", 0.4);
	public final EnumOption<MomentumMode> momentumMode = new EnumOption<>(this.options, "momentum_mode", MomentumMode.ADD, MomentumMode::parse);

	// Meta-properties of the dash
	public final FloatOption cooldown = new FloatOption(this.options, "cooldown", 1.0f);
	public final BooleanOption resetAttack = new BooleanOption(this.options, "resets_attack", true);
	public final FloatOption exhaustion = new FloatOption(this.options, "exhaustion", 0.0f);

	// What kinds of dash / trigger
	public final BooleanOption doubleTapDash = new BooleanOption(this.options, "double_tap_dash", true);

	public final BooleanOption forwardDash = new BooleanOption(this.options, "forward_dash", true);
	public final BooleanOption backwardsDash = new BooleanOption(this.options, "backwards_dash", true);
	public final BooleanOption leftDash = new BooleanOption(this.options, "left_dash", true);
	public final BooleanOption rightDash = new BooleanOption(this.options, "right_dash", true);

	/**
	 * Allow any combination of enabled dash directions
	 */
	public final BooleanOption diagonalDash = new BooleanOption(this.options, "diagonal_dash", true);

	// Where dash
	public final BooleanOption dashMidair = new BooleanOption(this.options, "dash_midair", false);
	public final BooleanOption dashWhileGliding = new BooleanOption(this.options, "dash_while_gliding", false);
	public final BooleanOption dashWhileSwimming = new BooleanOption(this.options, "dash_while_swimming", false);
	public final BooleanOption dashWhileFloating = new BooleanOption(this.options, "dash_while_floating", false);

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
}
