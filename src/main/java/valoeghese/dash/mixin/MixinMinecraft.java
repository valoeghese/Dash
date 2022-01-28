package valoeghese.dash.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.DashTracker;
import valoeghese.dash.client.DoubleTapHandler;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow @Nullable public LocalPlayer player;

	@Inject(at = @At("HEAD"), method = "handleKeybinds")
	private void afterVanillaHandleKeybinds(CallbackInfo ci) {
		if (this.player.isOnGround() && ((DashTracker) this.player).getDashCooldown() >= 0.98f) {
			DoubleTapHandler.FORWARD_DASH.measure();
			DoubleTapHandler.BACKWARDS_DASH.measure();
			DoubleTapHandler.LEFT_DASH.measure();
			DoubleTapHandler.RIGHT_DASH.measure();
		}
	}
}
