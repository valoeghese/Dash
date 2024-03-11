/*package valoeghese.dash.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import valoeghese.dash.Dash;

public class CommonInitialiser implements ModInitializer {
	@Override
	public void onInitialize() {
		Dash common = new Dash();

		// set up
		common.setup();
		common.setupNetwork();

		// register event
		ServerPlayConnectionEvents.INIT.register((handler, server) -> common.onClientJoinGame(handler));
	}
}
*/ // fabric code has been kept and commented out to prevent merge conflicts for future changes