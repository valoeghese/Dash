package valoeghese.dash;

import java.util.Locale;

public enum MomentumMode {
	SET("set"),
	ADD("add");

	MomentumMode(String name) {
		this.name = name;
	}

	private final String name;

	@Override
	public String toString() {
		return this.name;
	}

	public static MomentumMode parse(String name) {
		switch (name.toUpperCase(Locale.ROOT)) {
		case "SET":
			return SET;
		case "ADD":
			return ADD;
		default:
			Dash.LOGGER.warn("Unknown Momentum Mode \"" + name + "\". Using \"add\".");
			return ADD;
		}
	}
}
