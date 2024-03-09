package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class DoubleOption extends NumericalOption<Double> {
	public DoubleOption(Collection<Option<?>> options, String name, double defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = Double.valueOf(properties.getProperty(this.name));
	}

	@Override
	public void setFromDouble(double value) {
		this.set(value);
	}

	@Override
	public double getAsDouble() {
		return this.get();
	}
}
