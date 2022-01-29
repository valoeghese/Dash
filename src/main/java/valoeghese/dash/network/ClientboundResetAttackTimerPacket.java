package valoeghese.dash.network;

import net.minecraft.network.FriendlyByteBuf;

public class ClientboundResetAttackTimerPacket {
	public static final int ID = 1;

	public static void encode(ClientboundResetAttackTimerPacket msg, FriendlyByteBuf buf) {
	}

	public static ClientboundResetAttackTimerPacket decode(FriendlyByteBuf buf) {
		return new ClientboundResetAttackTimerPacket();
	}
}
