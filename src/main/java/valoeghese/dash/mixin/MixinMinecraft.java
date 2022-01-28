package valoeghese.dash.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.client.DoubleTapHandler;

@Mixin(Minecraft.class)
public class MixinMinecraft {
	@Inject(at = @At("HEAD"), method = "handleKeybinds")
	private void afterVanillaHandleKeybinds(CallbackInfo ci) {
		DoubleTapHandler.FORWARD_DASH.measure();
		DoubleTapHandler.BACKWARDS_DASH.measure();
		DoubleTapHandler.LEFT_DASH.measure();
		DoubleTapHandler.RIGHT_DASH.measure();
	}
}
