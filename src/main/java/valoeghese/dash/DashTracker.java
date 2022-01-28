package valoeghese.dash;

// for keeping track of dash by player without map caches
public interface DashTracker {
	long getLastDash();
	void setLastDash(long time);
}
