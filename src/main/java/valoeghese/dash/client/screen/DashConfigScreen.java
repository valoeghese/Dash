package valoeghese.dash.client.screen;

import benzenestudios.sulphate.SulphateScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class DashConfigScreen extends SulphateScreen {
	public DashConfigScreen(Screen parent) {
		super(new TranslatableComponent("screen.dtdash.config"), parent);
	}

	@Override
	protected void addWidgets() {
		this.addButton(new TranslatableComponent("screen.dtdash.momentum"), bn -> {});
		this.addButton(new TranslatableComponent("screen.dtdash.directions"), bn -> {});
		this.addButton(new TranslatableComponent("screen.dtdash.contexts"), bn -> {});
		this.addButton(new TranslatableComponent("screen.dtdash.miscellaneous"), bn -> {});
		this.addDone();
	}
}
