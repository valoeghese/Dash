package valoeghese.dash.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import valoeghese.dash.Dash;

// Yes. Polling every frame seemed like an easier solution to getting a double tap than making a new keybind that mimicks the old one and using the vanilla system.
// Rebinding that would be annoying. Could have used consume click on the original but you never know if other mods are trying to use that. Thus, this soln.
public class DoubleTapHandler {
	public DoubleTapHandler(KeyMapping mapping) {
		this.mapping = mapping;
		// to reduce very occasional weird triggering issues at random instances that are extremely unlikely but possible to happen
		this.reset();
	}

	private final KeyMapping mapping;

	private boolean wasDown;
	private long[] downTimes = new long[2];
	private int selected; // actually a bit

	public void measure() {
		boolean isDown = this.mapping.isDown();

		if (isDown && !this.wasDown) { // if newly pressed
			this.downTimes[this.selected] = System.currentTimeMillis(); // mark time
			this.selected = (this.selected + 1) & 0b1; // flip bit
		}

		this.wasDown = isDown;
	}

	public boolean doubleTapped() {
		long dt = this.downTimes[0] - this.downTimes[1];
		return dt <= maxTimeDelayMillis && dt >= -maxTimeDelayMillis; // probably marginally faster than abs
	}

	public void reset() {
		long wayBack = System.currentTimeMillis() - 100000L; // 100 seconds ago is way back at this scale
		this.downTimes[0] = wayBack;
		this.downTimes[1] = wayBack - 100000L;
	}

	private static final long maxTimeDelayMillis = Dash.config.sensitivity();

	public static final DoubleTapHandler FORWARD_DASH = new DoubleTapHandler(Minecraft.getInstance().options.keyUp);
	public static final DoubleTapHandler BACKWARDS_DASH = new DoubleTapHandler(Minecraft.getInstance().options.keyDown);
	public static final DoubleTapHandler LEFT_DASH = new DoubleTapHandler(Minecraft.getInstance().options.keyLeft);
	public static final DoubleTapHandler RIGHT_DASH = new DoubleTapHandler(Minecraft.getInstance().options.keyRight);
}
