package valoeghese.dash.client.screen;

import benzenestudios.sulphate.SulphateScreen;
import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;
import valoeghese.dash.config.Option;

import java.util.List;

public class DashConfigSubScreen extends SulphateScreen {
	protected DashConfigSubScreen(Component title, DashConfigScreen parent, ImmutableList<Option<?>> options, boolean unlocked) {
		super(title, parent);
		this.options = options;
		this.unlocked = unlocked;
	}

	private final List<Option<?>> options;
	private final boolean unlocked;

	@Override
	protected void addWidgets() {

	}
}
