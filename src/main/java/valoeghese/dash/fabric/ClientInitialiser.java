package valoeghese.dash.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import valoeghese.dash.client.DashClient;

public class ClientInitialiser implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DashClient client = new DashClient();

		// set up events
		ClientPlayConnectionEvents.DISCONNECT.register((handler, minecraft) -> client.onDisconnect());

		// run setup
		client.setupNetwork();

		// register stuff
		client.onRegisterKeyMappings(KeyBindingHelper::registerKeyBinding);
	}
}
