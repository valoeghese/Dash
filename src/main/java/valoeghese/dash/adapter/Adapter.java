package valoeghese.dash.adapter;

import net.minecraft.server.level.ServerPlayer;
import valoeghese.dash.forge.ForgeAdapter;

import java.io.File;
import java.util.function.BiConsumer;

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
	 * Get the config folder location on the current platform.
	 * @return the config folder location.
	 */
	File getConfigFolder();

	// Networking

	/**
	 * Register a packet.
	 * @param clazz the packet class.
	 * @param packet the packet class.
	 * @param <T> the packet class.
	 * @return a reference to the registered packet.
	 */
	<T> Packet<T> registerPacket(Class<T> clazz, Packet<T> packet);

	/**
	 * Register a serverbound packet handler.
	 * @param packet the packet to register a receiver for.
	 * @param handler the function to run on the server when a packet is received.
	 * @param <T> the packet class.
	 */
	<T> void registerServerboundReceiver(Packet<T> packet, BiConsumer<T, C2SContext> handler);

	/**
	 * Send a packet to a given client.
	 * @param player the player to send the packet to.
	 * @param packet the packet to send to the client.
	 * @throws IllegalArgumentException if the packet object is not a recognised dash packet.
	 */
	void sendToPlayer(ServerPlayer player, Object packet);

	/**
	 * The adapter instance for this current platform.
	 */
	Adapter INSTANCE = new ForgeAdapter();
}
