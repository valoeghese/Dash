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
import valoeghese.dash.config.DashConfig;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Double-Tap Dash");
	public static final ResourceLocation DASH_PACKET = new ResourceLocation("dtdash", "dash_action");
	public static final ResourceLocation RESET_TIMER_PACKET = new ResourceLocation("dtdash", "update_timer");

	public static DashConfig clientConfig;
	public static DashConfig activeConfig; // may be either clientConfig or the server config

	public static boolean canDash(Player player) {
		if (player.isSwimming()) return activeConfig.dashWhileSwimming.get();
		if (player.isFallFlying()) return activeConfig.dashWhileGliding.get();
		if (player.isInWater()) return activeConfig.dashWhileFloating.get();

		return player.isOnGround() || activeConfig.dashMidair.get();
	}

	@Override
	public void onInitialize() {
		LOGGER.info("*dashing noises*");
		activeConfig = clientConfig = DashConfig.loadOrCreate();

		ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET, (server, player, handler, buf, responseSender) -> {
			long time = player.level.getGameTime();
			DashTracker tracker = (DashTracker) player;
			byte dir = buf.readByte();

			server.execute(() -> {
				if (canDash(player) && tracker.getDashCooldown() >= 1.0f) { // I've heard isOnGround() is largely controlled by the client but I'm not an anticheat. I would guess anticheats modify this property server side anyway.
					tracker.setLastDash(time);
					// TODO check dash direction allowed against dash direction
					// if diagonal try restore to a legal single if possible - May not implement cause client's problem
					// anyway. likely trying to cheat if this happens (or sync fail)

					double str = activeConfig.strength.get();
					double yV = activeConfig.yVelocity.get();

					Vec3 look = player.getLookAngle().normalize();
					Vec3m move = new Vec3m(0, 0, 0);
					DashDirection direction = DashDirection.values()[dir];

					if (direction.isForward()) {

					}

					if (direction.isBackwards()) {

					}

					if (direction.isLeft()) {

					}

					if (direction.isRight()) {

					}
//					switch (dir) {
//					case FORWARD:
//						player.push(look.x, yV, look.z);
//						break;
//					case BACKWARDS:
//						player.push(-look.x, yV, -look.z);
//						break;
//					case LEFT:
//						player.push(look.z, yV, -look.x);
//						break;
//					case RIGHT:
//						player.push(-look.z, yV, look.x);
//						break;
//					}

					player.push(move.x, move.y, move.z);

					player.connection.send(new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));

					if (activeConfig.resetAttack.get()) {
						player.resetAttackStrengthTicker();
						ServerPlayNetworking.send(player, RESET_TIMER_PACKET, PacketByteBufs.create());
					}

					// apply exhaustion (affects hunger)
					// by default this is 0 so won't have any effect
					player.causeFoodExhaustion(activeConfig.exhaustion.get());
				}
			});
		});
	}

	public enum DashDirection {
		FORWARD (true,  false,  false, false),
		BACKWARD(false, true,   false, false),
		LEFT    (false, false,  true,  false),
		RIGHT   (false, false,  false, true),

		FORWARD_RIGHT (true,  false, false, true),
		BACKWARD_RIGHT(false, true,  false, true),
		BACKWARD_LEFT (false, true,  true, false),
		FORWARD_LEFT  (true,  false, true, false);

		DashDirection(boolean forward, boolean backwards, boolean left, boolean right) {
			this.forward = forward;
			this.backwards = backwards;
			this.left = left;
			this.right = right;
		}

		private final boolean forward;
		private final boolean backwards;
		private final boolean left;
		private final boolean right;

		public boolean isForward() {
			return this.forward;
		}

		public boolean isBackwards() {
			return this.backwards;
		}

		public boolean isLeft() {
			return this.left;
		}

		public boolean isRight() {
			return this.right;
		}
	}
}
