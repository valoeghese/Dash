package valoeghese.dash.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.Dash;
import valoeghese.dash.DashTracker;
import valoeghese.dash.client.DashInputHandler;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Shadow @Nullable public LocalPlayer player;

	@Inject(at = @At("HEAD"), method = "handleKeybinds")
	private void afterVanillaHandleKeybinds(CallbackInfo ci) {
		// TODO in the future have some sort of buffer for air
		if (Dash.canDash(this.player) && ((DashTracker) this.player).getDashCooldown() >= 0.98f) {
			DashInputHandler.FORWARD_DASH.measure();
			DashInputHandler.BACKWARDS_DASH.measure();
			DashInputHandler.LEFT_DASH.measure();
			DashInputHandler.RIGHT_DASH.measure();
		}
	}
}
