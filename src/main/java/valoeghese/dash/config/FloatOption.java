package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class FloatOption extends NumericalOption<Float> {
	public FloatOption(Collection<Option<?>> options, String name, float defaultValue) {
		super(options, name, defaultValue);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = Float.valueOf(properties.getProperty(this.name));
	}

	@Override
	public void setFromDouble(double value) {
		this.set((float) value);
	}

	@Override
	public double getAsDouble() {
		return (double) this.get();
	}
}
