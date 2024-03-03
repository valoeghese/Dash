package valoeghese.dash.fabric;

import net.fabricmc.api.ClientModInitializer;
import valoeghese.dash.client.DashClient;

public class ClientInitialiser implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		new DashClient().setup();
	}
}
