package valoeghese.dash;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Double-Tap Dash");
	public static final ResourceLocation DASH_PACKET = new ResourceLocation("dtdash", "dash_action");
	public static final ResourceLocation RESET_TIMER_PACKET = new ResourceLocation("dtdash", "update_timer");

	// magic numbers for networking
	public static final int FORWARD = 0;
	public static final int BACKWARDS = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	public static DashConfig config;

	public static boolean canDash(Player player) {
		if (player.isSwimming()) return config.dashWhileSwimming();
		if (player.isFallFlying()) return config.dashWhileGliding();

		return player.onGround() || config.dashMidair();
	}

	@Override
	public void onInitialize() {
		LOGGER.info("*dashing noises*");
		config = DashConfig.loadOrCreate();

		ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET, (server, player, handler, buf, responseSender) -> {
			long time = player.level().getGameTime();
			DashTracker tracker = (DashTracker) player;
			byte dir = buf.readByte();

			server.execute(() -> {
				if (canDash(player) && tracker.getDashCooldown() >= 1.0f) { // I've heard isOnGround() is largely controlled by the client but I'm not an anticheat. I would guess anticheats modify this property server side anyway.
					tracker.setLastDash(time);

					double str = config.strength();
					double yV = config.yVelocity();

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

					if (config.resetAttack()) {
						player.resetAttackStrengthTicker();
						ServerPlayNetworking.send(player, RESET_TIMER_PACKET, PacketByteBufs.create());
					}

					// apply exhaustion (affects hunger)
					// by default this is 0 so won't have any effect
					player.causeFoodExhaustion(config.exhaustion());
				}
			});
		});
	}
}
