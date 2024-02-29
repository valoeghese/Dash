package valoeghese.dash.adapter.client;

import valoeghese.dash.adapter.Adapter;

/**
 * {@link Adapter} for client. Can reference client specific classes as it will only be used by the client.
 */
public interface ClientAdapter {
	/**
	 * The adapter instance for this current platform.
	 */
	ClientAdapter INSTANCE = new FabricClientAdapter();
}
