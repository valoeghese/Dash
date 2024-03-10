package valoeghese.dash.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.Dash;
import valoeghese.dash.DashTracker;
import valoeghese.dash.client.DashClient;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer implements DashTracker {
	public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
		super(clientLevel, gameProfile);
	}

	@Shadow protected abstract boolean hasEnoughImpulseToStartSprinting();

	@Unique
	private long dash_lastClientDashTicks;

	/**
	 * Remove double tap dash if we have a forward double-tap dash.
	 * @reason we want to double tap dash instead, if it is enabled.
	 */
	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughImpulseToStartSprinting()Z", ordinal = 0))
	private boolean removeDoubleTapSprint(LocalPlayer self) {
		if (Dash.localConfig.doubleTapDash.get() && Dash.activeConfig.forwardDash.get()) {
			return true;
		} else {
			// default behaviour
			return this.hasEnoughImpulseToStartSprinting();
		}
	}

	@Inject(at = @At("RETURN"), method = "aiStep")
	private void afterAiStep(CallbackInfo ci) {
		long ticks = this.level().getGameTime();
		boolean dashKeyPressed = DashClient.consumeDash();
		boolean[] directionKeysPressed = DashClient.consumeDirections();

		if (this.getDashCooldown() >= 1.0f) {
			if (DashClient.tryDash(dashKeyPressed, directionKeysPressed)) {
				this.dash_lastClientDashTicks = ticks;
			}
		}
	}

	@Override
	public void setLastDash(long time) {
		this.dash_lastClientDashTicks = time;
	}

	@Override
	public float getDashCooldown() {
		long dTicks = this.level().getGameTime() - this.dash_lastClientDashTicks;
		return (float) (dTicks) / (20*Dash.activeConfig.cooldown.get());
	}
}
