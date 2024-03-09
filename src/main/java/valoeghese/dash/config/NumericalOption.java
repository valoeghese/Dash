package valoeghese.dash.config;

import java.util.Collection;

/**
 * Base class for a numerical option. Allows a common means of modifying and reading the various numerical options.
 * @param <T> the type of number this option holds.
 */
public abstract class NumericalOption<T extends Number> extends Option<T> {
	public NumericalOption(Collection<Option<?>> options, String name, T defaultValue) {
		super(options, name, defaultValue);
	}

	/**
	 * Set the numerical option's value from a double. This should mirror how {@link NumericalOption#getAsDouble()} works.
	 * @param value the double value to set this option to.
	 */
	public abstract void setFromDouble(double value);

	/**
	 * Get this numerical option's value as a double.
	 * @return the value as a double.
	 */
	public abstract double getAsDouble();
}
