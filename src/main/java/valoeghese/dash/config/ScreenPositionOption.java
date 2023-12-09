package valoeghese.dash.config;

import valoeghese.dash.ScreenPosition;

import java.util.Collection;
import java.util.Properties;

public class ScreenPositionOption extends Option<ScreenPosition> {
	public ScreenPositionOption(Collection<Option<?>> options, String name, ScreenPosition defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = ScreenPosition.parse(properties.getProperty("icon_x"), properties.getProperty("icon_y"));
	}

	@Override
	public void serialise(Properties properties) {
		properties.setProperty("icon_x", this.value.getXRepresentation());
		properties.setProperty("icon_y", this.value.getYRepresentation());
	}
}
