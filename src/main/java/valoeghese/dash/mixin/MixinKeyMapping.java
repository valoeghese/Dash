package valoeghese.dash.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KeyMapping.class)
public class MixinKeyMapping {
	@Redirect(
			method = "compareTo(Lnet/minecraft/client/KeyMapping;)I",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/language/I18n;get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;")
	)
	private String e(String translateKey, Object[] parameters) {
		if (translateKey.startsWith("key.dtdash")) { // move dash to the end of movement
			return "zzzzzzzz";
		}
		else {
			return I18n.get(translateKey, parameters);
		}
	}
}
