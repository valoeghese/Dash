package valoeghese.dash.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import valoeghese.dash.client.screen.DashConfigScreen;

public class ModMenuDashConfig implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return parent -> new DashConfigScreen(parent);
	}
}
