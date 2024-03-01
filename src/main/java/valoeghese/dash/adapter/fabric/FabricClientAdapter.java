package valoeghese.dash.adapter.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.C2SContext;
import valoeghese.dash.adapter.client.ClientAdapter;
import valoeghese.dash.adapter.client.S2CContext;

import java.util.function.BiConsumer;

public class FabricClientAdapter implements ClientAdapter {
	@Override
	public <T> void registerClientboundReceiver(ResourceLocation id, Class<T> clazz, BiConsumer<T, S2CContext> handler) {
		ClientPlayNetworking.registerGlobalReceiver(id, (client, ntwkHandler, buf, responseSender) -> {
			try {
				Packet<T> packetType = ((FabricAdapter) Adapter.INSTANCE).getPacket(clazz);
				Object packet = packetType.decoder().apply(buf);
				S2CContext context = new S2CContext(client, ntwkHandler.getConnection());

				handler.accept((T)packet, context);
			} catch (Exception e) {
				 // TODO generic error
				ntwkHandler.getConnection().disconnect(new TranslatableComponent("dtdash.err.sync_parse", e.toString()));
			}
		});
	}

	@Override
	public void sendServerboundPacket(Object packet) {

	}
}
