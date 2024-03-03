package valoeghese.dash.fabric;

import net.fabricmc.api.ModInitializer;
import valoeghese.dash.Dash;

public class CommonInitialiser implements ModInitializer {
	@Override
	public void onInitialize() {
		new Dash().setup();
	}
}
