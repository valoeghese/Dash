package valoeghese.dash.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class EditBoxPlus extends EditBox {
	public EditBoxPlus(Font font, int x, int y, int width, int height, Component component) {
		super(font, x, y, width, height, component);
	}

	// Single-Select

	private Set<EditBoxPlus> group;

	public void addToGroup(Set<EditBoxPlus> group) {
		group.add(this);
		this.group = group;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) {
			if (this.group != null) {
				for (EditBoxPlus editBox : this.group) {
					if (editBox != this) {
						editBox.setFocus(false);
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}


	// Tooltip

	public @Nullable Button.OnTooltip onTooltip; // NB button parameter will be null

	@Override
	public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		super.renderButton(poseStack, mouseX, mouseY, partialTick);

		if (this.isHoveredOrFocused()) {
			this.renderToolTip(poseStack, mouseX, mouseY);
		}
	}

	@Override
	public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
		if (this.onTooltip != null) {
			this.onTooltip.onTooltip(null, poseStack, mouseX, mouseY);
		}
	}
}
