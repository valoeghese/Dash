package valoeghese.dash;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dash implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("Dash");

	@Override
	public void onInitialize() {
		LOGGER.info("*dashing noises*");
	}
}
