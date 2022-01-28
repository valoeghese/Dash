package valoeghese.dash.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.Dash;
import valoeghese.dash.client.DashClient;
import valoeghese.dash.client.DoubleTapHandler;

@Mixin(LocalPlayer.class)
public abstract class MixinLocalPlayer extends AbstractClientPlayer {
	public MixinLocalPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
		super(clientLevel, gameProfile);
	}

	@Unique
	private long dash_lastClientDashTicks;

	/**
	 * @reason we want to double tap dash instead.
	 */
	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughImpulseToStartSprinting()Z", ordinal = 0))
	private boolean removeDoubleTapSprint(LocalPlayer self) {
		return true;
	}

	@Inject(at = @At("RETURN"), method = "aiStep")
	private void afterAiStep(CallbackInfo ci) {
		long ticks = this.level.getGameTime();

		if (ticks - this.dash_lastClientDashTicks >= Dash.dashCooldown) {
			if (DashClient.tryDash()) {
				this.dash_lastClientDashTicks = ticks;
			}
		}
	}
}
