package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class FloatOption extends Option<Float> {
	public FloatOption(Collection<Option<?>> options, String name, float defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = Float.valueOf(properties.getProperty(this.name));
	}
}
