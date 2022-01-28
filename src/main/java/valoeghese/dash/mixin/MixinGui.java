package valoeghese.dash.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.client.DashClient;

@Mixin(Gui.class)
public class MixinGui {
	@Inject(at = @At("RETURN"), method = "renderCrosshair")
	private void afterRenderCrosshair(PoseStack poseStack, CallbackInfo ci) {
		DashClient.renderBar(poseStack, (Gui) (Object) this);
	}
}
