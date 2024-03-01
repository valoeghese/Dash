package valoeghese.dash.adapter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import valoeghese.dash.adapter.fabric.FabricAdapter;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Adapt forge and fabric specific things.
 */
public interface Adapter {
	/**
	 * Get whether this is the dedicated server.
	 * @return whether we are on the dedicated server.
	 */
	boolean isDedicatedServer();

	// Networking

	/**
	 * Register a packet.
	 * @param id the id of the packet.
	 * @param clazz the packet class.
	 * @param encoder the encoder for the packet.
	 * @param decoder the decoder for the packet.
	 * @param <T> the packet class.
	 */
	<T> void registerPacket(ResourceLocation id, Class<T> clazz, BiConsumer<T, FriendlyByteBuf> encoder,
									   Function<FriendlyByteBuf, T> decoder);

	/**
	 * Register a serverbound packet handler.
	 * @param id the id of the packet to register a receiver for.
	 * @param handler the function to run on the server when a packet is received.
	 * @param <T> the packet class.
	 */
	<T> void registerServerboundReceiver(ResourceLocation id, BiConsumer<T, C2SContext> handler);

	/**
	 * Send a packet to the client.
	 * @param player the player to send the packet to.
	 * @param packet the packet to send to the client.
	 * @throws IllegalArgumentException if the packet object is not a recognised dash packet.
	 */
	void sendClientboundPacket(Player player, Object packet);

	/**
	 * The adapter instance for this current platform.
	 */
	Adapter INSTANCE = new FabricAdapter();
}
