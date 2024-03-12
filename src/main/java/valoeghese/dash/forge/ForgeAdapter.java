package valoeghese.dash.forge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.adapter.C2SContext;
import valoeghese.dash.adapter.Packet;
import valoeghese.dash.adapter.client.ClientAdapter;
import valoeghese.dash.adapter.client.S2CContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;


public class ForgeAdapter implements Adapter {
	private final Map<ResourceLocation, BiConsumer<?, C2SContext>> serverHandlers = new HashMap<>();


	@Override
	public boolean isDedicatedServer() {
		return FMLEnvironment.dist == Dist.DEDICATED_SERVER;
	}

	@Override
	public File getConfigFolder() {
		return FMLPaths.CONFIGDIR.get().toFile();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Packet<T> registerPacket(Class<T> clazz, Packet<T> packet) {
		final ResourceLocation packetId = packet.id();

		DASH_CHANNEL.registerMessage(
				id++,
				clazz,
				packet.encoder(),
				packet.decoder(),
				(pkt, contextSupplier) -> {
					NetworkEvent.Context context = contextSupplier.get();

					var handler = serverHandlers.get(packetId);

					if (handler != null) {
						// Client to Server, Processing on Server
						C2SContext c2SContext = new C2SContext(
							context.getSender(),
							context.getNetworkManager(),
							context::enqueueWork
						);
						((BiConsumer<T, C2SContext>) handler).accept(pkt, c2SContext);

						context.setPacketHandled(true);
					} else {
						var clientHandler = ((ForgeClientAdapter) ClientAdapter.INSTANCE).clientHandlers.get(packetId);

						if (clientHandler != null) {
							// Server to Client, Processing on Client
							DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientEvents.receivePacket(
									(BiConsumer<T, S2CContext>) clientHandler,
									pkt,
									context
							));
							context.setPacketHandled(true);
						}
					}
				}
		);
		return packet;
	}

	@Override
	public <T> void registerServerboundReceiver(Packet<T> packet, BiConsumer<T, C2SContext> handler) {
		serverHandlers.put(packet.id(), handler);
	}

	@Override
	public void sendToPlayer(ServerPlayer player, Object packet) {
		DASH_CHANNEL.sendTo(packet, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

	private static final String PROTOCOL_VERSION = "2";
	private static int id = 0; // increment ids. Packets are registered in the same order, so for a given mod version
	// this is fine

	public static final SimpleChannel DASH_CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation("dash", "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
}
