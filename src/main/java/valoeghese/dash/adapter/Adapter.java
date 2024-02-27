package valoeghese.dash.adapter;

/**
 * Adapt forge and fabric specific things.
 */
public interface Adapter {
	/**
	 * Get whether this is the dedicated server.
	 * @return whether we are on the dedicated server.
	 */
	boolean isDedicatedServer();

	/**
	 * The adapter instance for this current platform.
	 */
	Adapter INSTANCE = new FabricAdapter();
}
