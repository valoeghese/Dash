package valoeghese.dash.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

public record ClientboundSyncConfigPacket(Properties properties) {
	public void encode(FriendlyByteBuf buf) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			properties.store(bos, "Double-Tap Dash Config");

			buf.writeByteArray(bos.toByteArray());
		} catch (IOException e) {
			// this is important; will lead to wonky issues if it fails.
			// I don't know if this will kick the client or crash the server in the forge impl but both seem appropriate
			// (Hopefully the former)
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}

	public static ClientboundSyncConfigPacket decode(FriendlyByteBuf buf) {
		byte[] bytes = buf.readByteArray();
		Properties properties = new Properties();

		try (ByteArrayInputStream stream = new ByteArrayInputStream(bytes)) {
			properties.load(stream);
			return new ClientboundSyncConfigPacket(properties);
		} catch (IOException e) {
			// this is important and should disconnect the player if it fails.
			throw new UncheckedIOException(e.getMessage(), e);
		}
	}

	public static final Packet<ClientboundSyncConfigPacket> PACKET = new Packet<>(
			new ResourceLocation("dtdash", "sync"),
			ClientboundSyncConfigPacket::encode,
			ClientboundSyncConfigPacket::decode
	);
}
