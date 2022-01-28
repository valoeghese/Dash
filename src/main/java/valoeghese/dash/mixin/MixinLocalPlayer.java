package valoeghese.dash.mixin;

import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
	/**
	 * @reason we want to double tap dash instead.
	 */
	@Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;hasEnoughImpulseToStartSprinting()Z", ordinal = 0))
	private boolean noMoreDoubleTapSprintGottaHateIt(LocalPlayer imNotRunningFastNorTryingToMakeSomethingOfIt) {
		return false;
	}
}
