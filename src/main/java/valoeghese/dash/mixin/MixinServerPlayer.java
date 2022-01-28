package valoeghese.dash.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import valoeghese.dash.DashTracker;

@Mixin(ServerPlayer.class)
public abstract class MixinServerPlayer implements DashTracker {
	@Unique
	private long dash_lastServerDashTick;

	@Override
	public long getLastDash() {
		return this.dash_lastServerDashTick;
	}

	@Override
	public void setLastDash(long time) {
		this.dash_lastServerDashTick = time;
	}
}
