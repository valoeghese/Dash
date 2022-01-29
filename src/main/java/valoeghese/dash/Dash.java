package valoeghese.dash;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import valoeghese.dash.network.DashNetworking;

@Mod("dtdash")
public class Dash {
	public static final Logger LOGGER = LoggerFactory.getLogger("Double-Tap Dash");

	public static DashConfig config;

	public Dash() {
		LOGGER.info("*dashing noises*");
		config = DashConfig.loadOrCreate();

		final IEventBus eventHandler = FMLJavaModLoadingContext.get().getModEventBus();
		eventHandler.addListener(this::setupCommon);
	}

	private void setupCommon(FMLCommonSetupEvent t) {
		DashNetworking.onSetup();
	}
}
