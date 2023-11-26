package valoeghese.dash.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.client.DashClient;

@Mixin(Gui.class)
public class MixinGui {
	@Inject(at = @At("RETURN"), method = "renderCrosshair")
	private void afterRenderCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
		DashClient.renderBar(guiGraphics);
	}
}
