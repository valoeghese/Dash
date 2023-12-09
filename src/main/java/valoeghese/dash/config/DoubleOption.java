package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class DoubleOption extends Option<Double> {
	public DoubleOption(Collection<Option<?>> options, String name, double defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = Double.valueOf(properties.getProperty(this.name));
	}
}
