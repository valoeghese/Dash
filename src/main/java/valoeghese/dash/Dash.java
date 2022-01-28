package valoeghese.dash;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Dash");
	public static final ResourceLocation DASH_PACKET = new ResourceLocation("dash", "dash_action");
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

			if (time - tracker.getLastDash() >= dashCooldown) {
				tracker.setLastDash(time);
				player.resetAttackStrengthTicker();

				Vec3 look = player.getLookAngle();
				byte dir = buf.readByte();
				//System.out.println(dir + " " + look + " " + Thread.currentThread());

				switch (dir) {
				case FORWARD:
					player.push(look.x, 0.3, look.z);
					break;
				case BACKWARDS:
					player.push(-look.x, 0.3, -look.z);
					break;
				case LEFT:
					player.push(-look.z, 0.3, look.x);
					break;
				case RIGHT:
					player.push(look.z, 0.3, -look.x);
					break;
				}
			}
		});
	}
}
