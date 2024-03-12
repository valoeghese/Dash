package valoeghese.dash.mixin.forge;

import net.minecraft.client.gui.components.Button;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.client.gui.widget.ModListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import valoeghese.dash.forge.DashMod;

/**
 * Make the config button on the mod list go to our config menu.
 */
@Mixin(value = ModListScreen.class, remap = false)
public class MixinForgeModsScreen {
	@Shadow private ModListWidget.ModEntry selected;

	@Shadow private Button configButton;

	@Inject(at = @At("RETURN"), method = "updateCache")
	private void onUpdateCache(CallbackInfo info) {
		if (selected != null && selected.getInfo().getModId().equals(DashMod.MOD_ID)) {
			configButton.active = true;
		}
	}
}
