package valoeghese.dash.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import valoeghese.dash.Dash;
import valoeghese.dash.client.DashClient;
import valoeghese.dash.client.screen.DashConfigScreen;

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

		ModContainer thisMod = ModList.get().getModContainerById("minecraft")
				.orElseThrow(() -> new RuntimeException("Double Tap Dash cannot find... double tap dash?!"));

		thisMod.registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory(
				(mc, scrn) -> new DashConfigScreen(scrn)
		));

		client.onRegisterKeyMappings(k -> {
			ClientRegistry.registerKeyBinding(k);
			return k;
		});
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
