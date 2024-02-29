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

	// TODO network system that is able to be implemented on any platform

	/**
	 * The adapter instance for this current platform.
	 */
	Adapter INSTANCE = new FabricAdapter();
}
