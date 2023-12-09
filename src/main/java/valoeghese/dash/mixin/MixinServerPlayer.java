package valoeghese.dash.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import valoeghese.dash.Dash;
import valoeghese.dash.DashTracker;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer extends Player implements DashTracker {
	public MixinServerPlayer(Level level, BlockPos blockPos, float f, GameProfile gameProfile) {
		super(level, blockPos, f, gameProfile); // duck constructor
	}

	@Unique
	private long dash_lastServerDashTicks;

	@Override
	public float getDashCooldown() {
		long dTicks = this.level.getGameTime() - this.dash_lastServerDashTicks;
		return (float) (dTicks) / Dash.activeConfig.cooldown.get();
	}

	@Override
	public void setLastDash(long time) {
		this.dash_lastServerDashTicks = time;
	}
}
