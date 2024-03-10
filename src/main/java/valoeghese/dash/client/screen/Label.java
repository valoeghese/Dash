package valoeghese.dash.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class Label extends AbstractWidget {

	public Label(int x, int y, int width, int height, Component text) {
		super(x, y, width, height, text);
	}

	public int colour = 0xFFFFFF;

	@Override
	public void updateWidgetNarration(NarrationElementOutput narration) {
		narration.add(NarratedElementType.HINT, this.getMessage());
	}

	@Override
	public void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
		int renderY = this.getY() + this.height - Minecraft.getInstance().font.lineHeight;
		gui.drawString(Minecraft.getInstance().font, this.getMessage(), this.getY(), renderY, this.colour);
	}

	@Override
	public void playDownSound(SoundManager handler) {
		// no sound
	}
}
