package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class BooleanOption extends Option<Boolean> {
	public BooleanOption(Collection<Option<?>> options, String name, boolean defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = Boolean.valueOf(properties.getProperty(this.name));
	}
}
