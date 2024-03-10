package valoeghese.dash;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valoeghese.dash.adapter.Adapter;
import valoeghese.dash.config.DashConfig;
import valoeghese.dash.config.SynchronisedConfig;
import valoeghese.dash.network.ClientboundResetTimerPacket;
import valoeghese.dash.network.ClientboundSyncConfigPacket;
import valoeghese.dash.network.ServerboundDashPacket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class Dash {
	public void setup() {
		LOGGER.info(new Random().nextDouble() < 0.001 ? "Wir flitzen in die Zukunft!" : "Dashing into the future!");
		activeConfig = localConfig = DashConfig.loadOrCreate();
	}

	public void setupNetwork() {
		// Register Packets

		Adapter.INSTANCE.registerPacket(
				ServerboundDashPacket.class,
				ServerboundDashPacket.PACKET
		);

		Adapter.INSTANCE.registerPacket(
				ClientboundSyncConfigPacket.class,
				ClientboundSyncConfigPacket.PACKET
		);

		Adapter.INSTANCE.registerPacket(
				ClientboundResetTimerPacket.class,
				ClientboundResetTimerPacket.PACKET
		);

		// Receivers

		Adapter.INSTANCE.registerServerboundReceiver(
				ServerboundDashPacket.PACKET,
				(packet, context) -> {
					ServerPlayer player = context.player();
					long time = player.level().getGameTime();
					DashTracker tracker = (DashTracker) player;

					context.workEnqueuer().accept(() -> {
						// on the server we have 20ms leniency for lag
						// yes this could theoretically be exploited for a slight advantage, but I consider the benefits worth it.
						float twentyMs = activeConfig.cooldown.get() == 0.0f ? 0 : 0.02f / activeConfig.cooldown.get();

						if (canDash(player) && tracker.getDashCooldown() >= 1.0f - twentyMs) {
							tracker.setLastDash(time);
							// TODO check dash direction allowed against dash direction
							// if diagonal try restore to a legal single if possible - May not implement cause client's problem
							// anyway. likely trying to cheat if this happens (or sync fail)

							Vec3 look = player.getLookAngle().normalize();
							Vec3m horizontalDirectionVector = new Vec3m(0, 0, 0);
							DashDirection direction = packet.direction();

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
								Adapter.INSTANCE.sendToPlayer(player, new ClientboundResetTimerPacket());
							}

							// apply exhaustion (affects hunger)
							// by default this is 0 so won't have any effect
							player.causeFoodExhaustion(activeConfig.exhaustion.get());
						}
					});
				});
	}

	/**
	 * Runs when a client connection to this server enters PLAY state.
	 */
	public void onClientJoinGame(ServerGamePacketListenerImpl handler) {
		LOGGER.info("Connection Established");

		// sync even if connecting to local server
		// This informs the client that they cannot change settings. Perhaps in the future we can reverse this
		// restriction if we implement it that the settings can be changed and synced to connected clients once in game.

		// serialise settings
		Properties properties = new Properties();
		localConfig.save(properties, false);

		try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
			properties.store(bos, "Double-Tap Dash Config");

			// send properties
			Adapter.INSTANCE.sendToPlayer(handler.player, new ClientboundSyncConfigPacket(properties));
		} catch (IOException e) {
			handler.disconnect(Component.translatable("dtdash.err.sync", e.toString()));
		}
	}

	public static final Logger LOGGER = LoggerFactory.getLogger("Double-Tap Dash");

	public static DashConfig localConfig;
	public static SynchronisedConfig activeConfig; // may be either localConfig or the server config

	public static boolean canDash(Player player) {
		if (player.isSwimming()) return activeConfig.dashWhileSwimming.get();
		if (player.isFallFlying()) return activeConfig.dashWhileGliding.get();
		if (player.isInWater()) return activeConfig.dashWhileFloating.get();

		// I've heard isOnGround() is largely controlled by the client but I'm not an anticheat.
		// I would guess anticheats modify this property server side anyway.
		return player.onGround() || activeConfig.dashMidair.get();
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
