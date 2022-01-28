package valoeghese.dash;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Dash");
	public static final ResourceLocation DASH_PACKET = new ResourceLocation("dash", "dash_action");
	public static final ResourceLocation RESET_TIMER_PACKET = new ResourceLocation("dash", "update_timer");
	public static long dashCooldown = 20L; // 1 second (20 ticks)

	public static final int FORWARD = 0;
	public static final int BACKWARDS = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;

	@Override
	public void onInitialize() {
		LOGGER.info("*dashing noises*");

		ServerPlayNetworking.registerGlobalReceiver(DASH_PACKET, (server, player, handler, buf, responseSender) -> {
			long time = player.level.getGameTime();
			DashTracker tracker = (DashTracker) player;
			byte dir = buf.readByte();

			server.execute(() -> {
				if (time - tracker.getLastDash() >= dashCooldown) {
					tracker.setLastDash(time);

					Vec3 look = player.getLookAngle().multiply(1.3, 0, 1.3).normalize();
					//System.out.println(dir + " " + look + " " + Thread.currentThread());

					switch (dir) {
					case FORWARD:
						player.push(look.x, 0.3, look.z);
						break;
					case BACKWARDS:
						player.push(-look.x, 0.3, -look.z);
						break;
					case LEFT:
						player.push(look.z, 0.3, -look.x);
						break;
					case RIGHT:
						player.push(-look.z, 0.3, look.x);
						break;
					}

					player.connection.send(new ClientboundSetEntityMotionPacket(player.getId(), player.getDeltaMovement()));

					player.resetAttackStrengthTicker();
					ServerPlayNetworking.send(player, RESET_TIMER_PACKET, PacketByteBufs.create());
				}
			});
		});
	}
}
