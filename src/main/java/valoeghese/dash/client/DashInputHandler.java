package valoeghese.dash.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import valoeghese.dash.Dash;

import java.util.function.BooleanSupplier;

// Yes. Polling every frame seemed like an easier solution to getting a double tap than making a new keybind that mimicks the old one and using the vanilla system.
// Rebinding that would be annoying. Could have used consume click on the original but you never know if other mods are trying to use that. Thus, this soln.
public class DashInputHandler {
	public DashInputHandler(KeyMapping mapping, BooleanSupplier enabledGetter) {
		this.mapping = mapping;
		this.enabled = enabledGetter;

		// to reduce very occasional weird triggering issues at random instances that are extremely unlikely but possible to happen
		this.reset();
	}

	private final KeyMapping mapping;
	private final BooleanSupplier enabled;

	private boolean wasDown;
	private long[] downTimes = new long[2];
	private int selected; // actually a bit

	/**
	 * Measures the taps in order to detect a double tap.
	 */
	public void measure() {
		// disable if not enabled or double tapping is not enabled.
		if (!this.enabled.getAsBoolean() || !Dash.localConfig.doubleTapDash.get()) return;

		boolean isDown = this.mapping.isDown();

		if (isDown && !this.wasDown) { // if newly pressed
			this.downTimes[this.selected] = System.currentTimeMillis(); // mark time
			this.selected ^= 1; // flip bit
		}

		this.wasDown = isDown;
	}

	/**
	 * Check if the player should dash.
	 * @param dashKeyPressed whether the dash key was pressed.
	 * @return whether the player should dash. This does not check whether the player <i>is able to</i> dash. For that,
	 * check {@link Dash#canDash(Player)}.
	 */
	public boolean shouldDash(boolean dashKeyPressed) {
		if (!this.enabled.getAsBoolean()) return false; // disable!
		// if dash key pressed or double tapped
		return (dashKeyPressed && this.mapping.isDown())
				|| this.doubleTapped();
	}

	private boolean doubleTapped() {
		if (!Dash.localConfig.doubleTapDash.get()) return false; // if double tap is disabled, don't bother checking!

		long dt = this.downTimes[0] - this.downTimes[1];
		final long maxTimeDelayMillis = Dash.localConfig.sensitivity.get();
		return dt <= maxTimeDelayMillis && dt >= -maxTimeDelayMillis; // probably marginally faster than abs
	}

	public void reset() {
		long wayBack = System.currentTimeMillis() - 100000L; // 100 seconds ago is way back at this scale
		this.downTimes[0] = wayBack;
		this.downTimes[1] = wayBack - 100000L;
	}

	public static final DashInputHandler FORWARD_DASH = new DashInputHandler(Minecraft.getInstance().options.keyUp, () -> Dash.activeConfig.forwardDash.get());
	public static final DashInputHandler BACKWARDS_DASH = new DashInputHandler(Minecraft.getInstance().options.keyDown, () -> Dash.activeConfig.backwardsDash.get());
	public static final DashInputHandler LEFT_DASH = new DashInputHandler(Minecraft.getInstance().options.keyLeft, () -> Dash.activeConfig.leftDash.get());
	public static final DashInputHandler RIGHT_DASH = new DashInputHandler(Minecraft.getInstance().options.keyRight, () -> Dash.activeConfig.rightDash.get());
}
