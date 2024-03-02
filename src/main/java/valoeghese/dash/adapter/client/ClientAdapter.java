package valoeghese.dash.adapter.client;

import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.Packet;
import valoeghese.dash.fabric.FabricClientAdapter;

import java.util.function.BiConsumer;

/**
 * {@link Adapter} for client. Can reference client specific classes as it will only be used by the client.
 */
public interface ClientAdapter {
	// Network

	/**
	 * Register a serverbound packet handler.
	 * @param packet the packet to register the receiver for.
	 * @param handler the function to run on the server when a packet is received.
	 */
	<T> void registerClientboundReceiver(Packet<T> packet, BiConsumer<T, S2CContext> handler);

	/**
	 * Send a packet to the server.
	 * @param packet the packet to send to the server.
	 * @throws IllegalArgumentException if the object is not a recognised dash packet.
	 */
	void sendToServer(Object packet);

	/**
	 * The adapter instance for this current platform.
	 */
	ClientAdapter INSTANCE = new FabricClientAdapter();
}
