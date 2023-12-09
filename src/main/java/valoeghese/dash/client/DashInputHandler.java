package valoeghese.dash.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import valoeghese.dash.Dash;

// Yes. Polling every frame seemed like an easier solution to getting a double tap than making a new keybind that mimicks the old one and using the vanilla system.
// Rebinding that would be annoying. Could have used consume click on the original but you never know if other mods are trying to use that. Thus, this soln.
public class DashInputHandler {
	public DashInputHandler(KeyMapping mapping) {
		this.mapping = mapping;
		// to reduce very occasional weird triggering issues at random instances that are extremely unlikely but possible to happen
		this.reset();
	}

	private final KeyMapping mapping;

	private boolean enabled = true; // whether the dash direction specified by this input handler is enabled
	private boolean wasDown;
	private long[] downTimes = new long[2];
	private int selected; // actually a bit

	/**
	 * Set whether functionality is enabled.
	 */
	public void setEnabled(boolean enabled) {
		if (this.enabled = enabled) { // set value and check
			this.reset(); // if it's been just now enabled, reset just in case to make sure it's in a 'ready' state
		}
	}

	/**
	 * Measures the taps in order to detect a double tap.
	 */
	public void measure() {
		if (!this.enabled || !Dash.activeConfig.doubleTapDash.get()) return; // disable if not enabled or double tapping is not enabled.

		boolean isDown = this.mapping.isDown();

		if (isDown && !this.wasDown) { // if newly pressed
			this.downTimes[this.selected] = System.currentTimeMillis(); // mark time
			this.selected = (this.selected + 1) & 0b1; // flip bit
		}

		this.wasDown = isDown;
	}

	public boolean shouldDash(boolean dashKeyPressed) {
		if (!this.enabled) return false; // disable!
		// if dash key pressed or double tapped
		return (dashKeyPressed && this.mapping.isDown() && Dash.canDash(Minecraft.getInstance().player) /*This check is done here for key, and in measure() for double-tap*/)
				|| this.doubleTapped();
	}

	private boolean doubleTapped() {
		if (!Dash.activeConfig.doubleTapDash.get()) return false; // if double tap is disabled, don't bother checking!
		long dt = this.downTimes[0] - this.downTimes[1];
		final long maxTimeDelayMillis = Dash.activeConfig.sensitivity.get();
		return dt <= maxTimeDelayMillis && dt >= -maxTimeDelayMillis; // probably marginally faster than abs
	}

	public void reset() {
		long wayBack = System.currentTimeMillis() - 100000L; // 100 seconds ago is way back at this scale
		this.downTimes[0] = wayBack;
		this.downTimes[1] = wayBack - 100000L;
	}

	public static final DashInputHandler FORWARD_DASH = new DashInputHandler(DashClient.options.keyUp);
	public static final DashInputHandler BACKWARDS_DASH = new DashInputHandler(DashClient.options.keyDown);
	public static final DashInputHandler LEFT_DASH = new DashInputHandler(DashClient.options.keyLeft);
	public static final DashInputHandler RIGHT_DASH = new DashInputHandler(DashClient.options.keyRight);
}
