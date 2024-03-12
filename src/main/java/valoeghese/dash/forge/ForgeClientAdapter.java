package valoeghese.dash.forge;

import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.adapter.Packet;
import valoeghese.dash.adapter.client.ClientAdapter;
import valoeghese.dash.adapter.client.S2CContext;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ForgeClientAdapter implements ClientAdapter {
	final Map<ResourceLocation, BiConsumer<?, S2CContext>> clientHandlers = new HashMap<>();

	@Override
	public <T> void registerClientboundReceiver(Packet<T> packet, BiConsumer<T, S2CContext> handler) {
		this.clientHandlers.put(packet.id(), handler);
	}

	@Override
	public void sendToServer(Object packet) {
		ForgeAdapter.DASH_CHANNEL.sendToServer(packet);
	}
}
