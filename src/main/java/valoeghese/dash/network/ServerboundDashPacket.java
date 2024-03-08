package valoeghese.dash.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.Dash;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.Packet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public record ServerboundDashPacket(Dash.DashDirection direction) {
	public void encode(FriendlyByteBuf buf) {
		buf.writeByte(direction.ordinal());
	}

	public static ServerboundDashPacket decode(FriendlyByteBuf buf) {
		byte dirByte = buf.readByte();
		Dash.DashDirection direction = Dash.DashDirection.FORWARD; // default if invalid value sent

		if (dirByte < Dash.DashDirection.values().length) {
			direction = Dash.DashDirection.values()[dirByte];
		}

		return new ServerboundDashPacket(direction);
	}

	public static final Packet<ServerboundDashPacket> PACKET = new Packet<>(
			new ResourceLocation("dtdash", "dash_action"),
			ServerboundDashPacket::encode,
			ServerboundDashPacket::decode
	);
}
