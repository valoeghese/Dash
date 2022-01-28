package valoeghese.dash;

// for keeping track of dash by player without map caches
public interface DashTracker {
	/**
	 * Calculates and returns the dash coodlown percentage.
	 * @return get the dash cooldown (unclamped). 0.0 = reset, 1.0 = can dash again.
	 */
	float getDashCooldown();

	/**
	 * Set the last dash time.
	 * @param time the time in ticks.
	 */
	void setLastDash(long time);
}
