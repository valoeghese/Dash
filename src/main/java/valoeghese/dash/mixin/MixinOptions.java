package valoeghese.dash.mixin;

import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.client.DashClient;

@Mixin(value = Options.class, priority = 420)
public class MixinOptions {
	@Inject(at = @At("HEAD"), method = "load")
	private void onLoadComplete(CallbackInfo ci) {
		DashClient.onOptionsLoad((Options) (Object) this);
	}
}
