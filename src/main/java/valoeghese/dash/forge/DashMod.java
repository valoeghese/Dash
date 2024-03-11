package valoeghese.dash.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import valoeghese.dash.Dash;
import valoeghese.dash.client.DashClient;

@Mod(DashMod.MOD_ID)
public class DashMod {
	public DashMod() {
		// create common (and client)
		this.common = new Dash();

		if (FMLEnvironment.dist == Dist.CLIENT) {
			this.client = new DashClient();
		}

		// set current instance which has access to common and client
		instance = this;

		// set up
		common.setup();

		// set up mod events
		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();
		eventHandler.addListener(this::setupCommon);
		eventHandler.addListener(this::setupClient);
	}

	final Dash common;
	DashClient client;

	private void setupClient(FMLClientSetupEvent t) {
		client.setupNetwork();
		// todo on 1.19+ forge uses RegisterKeyMappingsEvent. This event doesn't exist on 1.18 -- is it necessary?
		client.onRegisterKeyMappings(k -> k);
	}

	private void setupCommon(FMLCommonSetupEvent t) {
		common.setupNetwork();
	}

	private static DashMod instance;
	public static final String MOD_ID = "dtdash";

	public static DashMod getInstance() {
		return instance;
	}
}
