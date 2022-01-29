package valoeghese.dash.network;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import valoeghese.dash.Dash;
import valoeghese.dash.DashTracker;
import valoeghese.dash.client.DashClient;

public class DashNetworking {
	private static final String PROTOCOL_VERSION = "1";

	// magic numbers for networking
	public static final byte FORWARD = 0;
	public static final byte BACKWARDS = 1;
	public static final byte LEFT = 2;
	public static final byte RIGHT = 3;

	public static final SimpleChannel DASH_CHANNEL = NetworkRegistry.newSimpleChannel(
		new ResourceLocation("dash", "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	public static void onSetup() {
		// C2S
		DashNetworking.DASH_CHANNEL.registerMessage(
				ServerboundDashPacket.ID,
				ServerboundDashPacket.class,
				ServerboundDashPacket::encode,
				ServerboundDashPacket::decode,
				(pkt, contextSupplier) -> {
					NetworkEvent.Context context = contextSupplier.get();
					ServerPlayer player = context.getSender();

					long time = player.level.getGameTime();
					DashTracker tracker = (DashTracker) player;
					byte dir = pkt.dir();

					context.enqueueWork(() -> {
						if (player.isOnGround() && tracker.getDashCooldown() >= 1.0f) { // I've heard isOnGround() is largely controlled by the client but I'm not an anticheat. I would guess anticheats modify this property server side anyway.
							tracker.setLastDash(time);

							double str = Dash.config.strength();
							double yV = Dash.config.yVelocity();

							Vec3 look = player.getLookAngle().multiply(str, 0, str).normalize();

							switch (dir) {
							case FORWARD:
								player.push(look.x, yV, look.z);
								break;
							case BACKWARDS:
								player.push(-look.x, yV, -look.z);
								break;
							case LEFT:
								player.push(look.z, yV, -look.x);
								break;
							case RIGHT:
								player.push(-look.z, yV, look.x);
								break;
							}

							player.connection.send(new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));

							if (Dash.config.resetAttack()) {
								player.resetAttackStrengthTicker();
								DashNetworking.DASH_CHANNEL.sendTo(new ClientboundResetAttackTimerPacket(), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
							}
						}
					});

					context.setPacketHandled(true);
				});

		// S2C
		DashNetworking.DASH_CHANNEL.registerMessage(
				ClientboundResetAttackTimerPacket.ID,
				ClientboundResetAttackTimerPacket.class,
				ClientboundResetAttackTimerPacket::encode,
				ClientboundResetAttackTimerPacket::decode,
				(pkt, ctx) -> DashClient.resetAttackTimer(ctx));
	}
}
