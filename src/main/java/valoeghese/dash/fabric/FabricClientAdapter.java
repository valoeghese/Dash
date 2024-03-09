package valoeghese.dash.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.Packet;
import valoeghese.dash.adapter.client.ClientAdapter;
import valoeghese.dash.adapter.client.S2CContext;

import java.util.function.BiConsumer;

public class FabricClientAdapter implements ClientAdapter {
	@Override
	public <T> void registerClientboundReceiver(Packet<T> packet, BiConsumer<T, S2CContext> handler) {
		ClientPlayNetworking.registerGlobalReceiver(packet.id(), (client, ntwkHandler, buf, responseSender) -> {
			try {
				S2CContext context = new S2CContext(client, ntwkHandler.getConnection(), client::tell);
				T packetInstance = packet.decoder().apply(buf);

				handler.accept(packetInstance, context);
			} catch (Exception e) {
				ntwkHandler.getConnection().disconnect(new TextComponent(e.toString()));
			}
		});
	}

	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public void sendToServer(Object packet) {
		Packet packetType = ((FabricAdapter)Adapter.INSTANCE).getPacket(packet.getClass());

		if (packetType == null) {
			throw new IllegalArgumentException("Unknown packet type for class " + packet.getClass().getSimpleName());
		}

		FriendlyByteBuf buf = PacketByteBufs.create();
		packetType.encoder().accept(packet, buf);

		ClientPlayNetworking.send(packetType.id(), buf);
	}
}
