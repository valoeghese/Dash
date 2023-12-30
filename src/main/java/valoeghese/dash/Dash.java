package valoeghese.dash;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valoeghese.dash.config.DashConfig;
import valoeghese.dash.config.SynchronisedConfig;

import java.util.Random;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Double-Tap Dash");

	// C2S
	public static final ResourceLocation DASH_PACKET = new ResourceLocation("dtdash", "dash_action");

	// S2C
	public static final ResourceLocation RESET_TIMER_PACKET = new ResourceLocation("dtdash", "update_timer");

	public static DashConfig clientConfig;
	public static SynchronisedConfig activeConfig; // may be either clientConfig or the server config

	public static boolean canDash(Player player) {
		if (player.isSwimming()) return activeConfig.dashWhileSwimming.get();
		if (player.isFallFlying()) return activeConfig.dashWhileGliding.get();
		if (player.isInWater()) return activeConfig.dashWhileFloating.get();

		return player.isOnGround() || activeConfig.dashMidair.get();
	}

	@Override
	public void onInitialize() {
		LOGGER.info(new Random().nextDouble() < 0.001 ? "Wir flitzen in die Zukunft!" : "Dashing into the future!");
		activeConfig = clientConfig = DashConfig.loadOrCreate();

		ServerPlayConnectionEvents.INIT.register(new ServerPlayConnectionEvents.Init() {
			@Override
			public void onPlayInit(ServerGamePacketListenerImpl handler, MinecraftServer server) {
				LOGGER.info("Connection Established");

				// don't sync if connecting to local server
				// local player (Minecraft#player) doesn't exist yet.
				// this doesn't work in cracked (like dev). need to find a better way.
				if (!server.isDedicatedServer() &&
						Minecraft.getInstance().getUser().getUuid().equals(handler.player.getUUID().toString().replace("-", ""))) {
					LOGGER.info("Connecting to Local Integrated Server. No sync necessary.");
				}
			}
		});

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

					Vec3 look = player.getLookAngle().normalize();
					Vec3m horizontalDirectionVector = new Vec3m(0, 0, 0);
					DashDirection direction = DashDirection.values()[dir];

					// add directions
					if (direction.isForward()) {
						horizontalDirectionVector.add(look.x, 0, look.z);
					}

					if (direction.isBackwards()) {
						horizontalDirectionVector.add(-look.x, 0, -look.z);
					}

					if (direction.isLeft()) {
						horizontalDirectionVector.add(look.z, 0, -look.x);
					}

					if (direction.isRight()) {
						horizontalDirectionVector.add(-look.z, 0, look.x);
					}

					// normalise and apply strength, then add y velocity
					Vec3 move = horizontalDirectionVector.ofLength(activeConfig.strength.get())
							.add(0, activeConfig.yVelocity.get(), 0);

					// move the player in that direction
					if (activeConfig.momentumMode.get() == MomentumMode.SET) {
						player.setDeltaMovement(move.x, move.y, move.z);
					} else {
						player.push(move.x, move.y, move.z);
					}

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
