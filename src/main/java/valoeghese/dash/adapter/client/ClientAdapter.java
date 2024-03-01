package valoeghese.dash.adapter.client;

import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.fabric.FabricClientAdapter;

import java.util.function.BiConsumer;

/**
 * {@link Adapter} for client. Can reference client specific classes as it will only be used by the client.
 */
public interface ClientAdapter {
	/**
	 * The adapter instance for this current platform.
	 */
	ClientAdapter INSTANCE = new FabricClientAdapter();

	// Network

	/**
	 * Register a serverbound packet handler.
	 * @param id the id of the packet to register a receiver for.
	 * @param clazz the class of the packet to register a receiver for.
	 * @param handler the function to run on the server when a packet is received.
	 */
	<T> void registerClientboundReceiver(ResourceLocation id, Class<T> clazz, BiConsumer<T, S2CContext> handler);

	/**
	 * Send a packet to the server.
	 * @param packet the packet to send to the server.
	 * @throws IllegalArgumentException if the object is not a recognised dash packet.
	 */
	void sendServerboundPacket(Object packet);
}
