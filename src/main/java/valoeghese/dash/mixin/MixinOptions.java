package valoeghese.dash.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.client.DashClient;

/**
 * @reason Fabric's system makes my "Dash" option appear at the top of the movement controls. And I'm picky so I want it at the bottom.
 */
@Mixin(Options.class)
public class MixinOptions {
	@Shadow
	@Final
	@Mutable
	public KeyMapping[] keyMappings;

	@Inject(at = @At("HEAD"), method = "load")
	private void onLoadComplete(CallbackInfo ci) {
		KeyMapping[] newMappings = new KeyMapping[this.keyMappings.length + 1];
		System.arraycopy(this.keyMappings, 0, newMappings, 0, this.keyMappings.length);
		newMappings[this.keyMappings.length] = DashClient.onOptionsLoad((Options) (Object) this);
		keyMappings = newMappings;
	}
}
