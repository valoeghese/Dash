package valoeghese.dash.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.Dash;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.Packet;

public record ClientboundResetTimerPacket() {
	public void encode(FriendlyByteBuf buf) {
		// There is nothing to encode.
	}

	public static ClientboundResetTimerPacket decode(FriendlyByteBuf buf) {
		return new ClientboundResetTimerPacket();
	}

	public static final Packet<ClientboundResetTimerPacket> PACKET = new Packet<>(
			new ResourceLocation("dtdash", "update_timer"),
			ClientboundResetTimerPacket::encode,
			ClientboundResetTimerPacket::decode
	);
}
