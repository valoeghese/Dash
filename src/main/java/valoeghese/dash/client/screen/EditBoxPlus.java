package valoeghese.dash.client.screen;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

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
						editBox.setFocused(false);
					}
				}
			}

			return true;
		} else {
			return false;
		}
	}
}
