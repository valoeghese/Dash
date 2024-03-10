package valoeghese.dash.adapter;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents the required things for a packet.
 * Packets need to be registered with {@link Adapter#registerPacket(PacketDirection, Class, Packet)} during initialisation.
 * @param id the id of the packet.
 * @param encoder the function that encodes the packet to a bytebuffer.
 * @param decoder the function that decodes the packet from a bytebuffer.
 * @param <T> the class type of the packet.
 */
public record Packet<T>(
		ResourceLocation id,
		BiConsumer<T, FriendlyByteBuf> encoder,
		Function<FriendlyByteBuf, T> decoder) {
}
