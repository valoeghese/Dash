package valoeghese.dash.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class NumberEditBox extends EditBoxPlus {
	public NumberEditBox(int x, int y, int width, int height, Component component) {
		super(Minecraft.getInstance().font, x, y, width, height, component);
	}

	public double getDoubleValue() throws NumberFormatException {
		return Double.parseDouble(this.getValue());
	}
}
