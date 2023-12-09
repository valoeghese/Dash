package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;

public class TimeOption extends Option<Long> {
	public TimeOption(Collection<Option<?>> options, String name, double defaultValueSeconds) {
		super(options, name, toMillis(defaultValueSeconds));
	}

	public TimeOption(Collection<Option<?>> options, String name, long defaultValueMillis) {
		super(options, name, defaultValueMillis);
	}

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = toMillis(Double.parseDouble(properties.getProperty(this.name)));
	}

	@Override
	public void serialise(Properties properties) {
		properties.setProperty(this.name, String.valueOf(fromMillis(this.value)));
	}

	private static long toMillis(double seconds) {
		return (long) (seconds * 1000);
	}

	private static double fromMillis(long millis) {
		return (double) millis / 1000.0;
	}
}
