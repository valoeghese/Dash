package valoeghese.dash.client.screen;

import benzenestudios.sulphate.Anchor;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
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
	public void updateNarration(NarrationElementOutput narration) {
		narration.add(NarratedElementType.HINT, this.getMessage());
	}

	@Override
	public void render(PoseStack stack, int mouseX, int mouseY, float partialTick) {
		int renderY = this.y + this.height - Minecraft.getInstance().font.lineHeight;
		GuiComponent.drawString(stack, Minecraft.getInstance().font, this.getMessage(), this.x, renderY, this.colour);
	}

	@Override
	public void playDownSound(SoundManager handler) {
		// no sound
	}
}
