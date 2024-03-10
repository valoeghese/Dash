package valoeghese.dash.config;

import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.Properties;

public abstract class Option<T> {
	public Option(Collection<Option<?>> options, String name, T defaultValue) {
		this.name = name;
		this.value = defaultValue;

		options.add(this);
	}

	public final String name;
	protected T value;

	public T get() {
		return this.value;
	}

	public void set(T value) {
		this.value = value;
	}

	/**
	 * Read the value from the properties into this option. You must ensure the property exists on the properties
	 * (typically by filling the default value from {@link Option#serialise(Properties)}
	 *
	 * @param properties the properties to read and set the value from.
	 * @throws IllegalArgumentException if the property value is malformed.
	 */
	public abstract void deserialise(Properties properties) throws IllegalArgumentException;

	public void serialise(Properties properties) {
		properties.setProperty(this.name, String.valueOf(this.value));
	}

	public Component getComponent(Object... parameters) {
		return Component.translatable("options.dtdash." + this.name, parameters);
	}
}
