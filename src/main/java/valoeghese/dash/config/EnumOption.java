package valoeghese.dash.config;

import java.util.Collection;
import java.util.Properties;
import java.util.function.Function;

public class EnumOption<E extends Enum<E>> extends Option<E> {
	public EnumOption(Collection<Option<?>> options,
					  String name, E defaultValue, E[] values, Function<String, E> parser) {
		super(options, name, defaultValue);
		this.parser = parser;
		this.values = values;
	}

	private final E[] values;
	private final Function<String, E> parser;

	@Override
	public void deserialise(Properties properties) throws IllegalArgumentException {
		this.value = this.parser.apply(properties.getProperty(this.name));
	}

	public E[] getValues() {
		return this.values;
	}
}
