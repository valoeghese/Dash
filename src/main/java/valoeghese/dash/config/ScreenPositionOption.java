package valoeghese.dash.config;

import valoeghese.dash.ScreenPosition;

import java.util.Collection;
import java.util.Properties;

public class ScreenPositionOption extends Option<ScreenPosition> {
	public ScreenPositionOption(Collection<Option<?>> options, String name, ScreenPosition defaultValue) {
		super(options, name, defaultValue);

		this.xName = name + "_x";
		this.yName = name + "_y";
	}

	private final String xName;
	private final String yName;

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = ScreenPosition.parse(properties.getProperty(this.xName), properties.getProperty(this.yName));
	}

	@Override
	public void serialise(Properties properties) {
		properties.setProperty(this.xName, this.value.getXRepresentation());
		properties.setProperty(this.yName, this.value.getYRepresentation());
	}
}
