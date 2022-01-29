package valoeghese.dash.network;

import net.minecraft.network.FriendlyByteBuf;

public record ServerboundDashPacket(byte dir) {
	public static final int ID = 0;

	public static void encode(ServerboundDashPacket msg, FriendlyByteBuf buf) {
		buf.writeByte(msg.dir);
	}

	public static ServerboundDashPacket decode(FriendlyByteBuf buf) {
		return new ServerboundDashPacket(buf.readByte());
	}
}
