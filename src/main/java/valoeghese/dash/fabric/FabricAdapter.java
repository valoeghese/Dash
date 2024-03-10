package valoeghese.dash.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.C2SContext;
import valoeghese.dash.adapter.Packet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class FabricAdapter implements Adapter {
	private final Map<Class<?>, Packet<?>> packets = new HashMap<>();

	@SuppressWarnings("unchecked")
	<T> Packet<T> getPacket(Class<T> clazz) {
		return (Packet<T>) this.packets.get(clazz);
	}

	@Override
	public boolean isDedicatedServer() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
	}

	@Override
	public File getConfigFolder() {
		return FabricLoader.getInstance().getConfigDir().toFile();
	}

	@Override
	public <T> Packet<T> registerPacket(Class<T> clazz, Packet<T> packet) {
		this.packets.put(clazz, packet);
		return packet;
	}

	@Override
	public <T> void registerServerboundReceiver(Packet<T> packet, BiConsumer<T, C2SContext> handler) {
		ServerPlayNetworking.registerGlobalReceiver(packet.id(), (server, player, packetListener, buf, responseSender) -> {
			try {
				C2SContext context = new C2SContext(player, packetListener, server::execute);
				T packetInstance = packet.decoder().apply(buf);

				handler.accept(packetInstance, context);
			} catch (Exception e) {
				packetListener.disconnect(Component.literal(e.toString()));
			}
		});
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void sendToPlayer(ServerPlayer player, Object packet) {
		Packet packetType = ((FabricAdapter)Adapter.INSTANCE).getPacket(packet.getClass());

		if (packetType == null) {
			throw new IllegalArgumentException("Unknown packet type for class " + packet.getClass().getSimpleName());
		}

		FriendlyByteBuf buf = PacketByteBufs.create();
		packetType.encoder().accept(packet, buf);

		ServerPlayNetworking.send(player, packetType.id(), buf);
	}
}

