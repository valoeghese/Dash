package valoeghese.dash.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class NumberEditBox extends EditBox {
	public NumberEditBox(int x, int y, int width, int height, Component component) {
		super(Minecraft.getInstance().font, x, y, width, height, component);
	}

	public @Nullable Button.OnTooltip onTooltip; // NB button parameter will be null

	public double getDoubleValue() throws NumberFormatException {
		return Double.parseDouble(this.getValue());
	}

	@Override
	public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
		if (this.onTooltip != null) {
			this.onTooltip.onTooltip(null, poseStack, mouseX, mouseY);
		}
	}
}
