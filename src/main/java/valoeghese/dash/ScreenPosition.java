package valoeghese.dash;

import java.util.Locale;

/**
 * Represents a position on the screen along a single axis in terms of percentage and pixels
 */
public record ScreenPosition(float percentageXX, float percentageXY, float pxX, float percentageYX, float percentageYY, float pxY) {
	public float x(float width, float height) {
		return width * this.percentageXX + height * this.percentageXY + this.pxX;
	}

	public float y(float width, float height) {
		return width * this.percentageYX + height * this.percentageYY + this.pxY;
	}

	public static ScreenPosition parse(String x, String y) {
		float[] xParts = decompose(x, false);
		float[] yParts = decompose(y, true);
		return new ScreenPosition(xParts[0], xParts[1], xParts[2], yParts[0], yParts[1], yParts[2]);
	}

	private static float[] decompose(String axisPosition, boolean isYAxis) {
		axisPosition = axisPosition.toLowerCase(Locale.ROOT) + "+"; // append + as hack so I don't have to duplicate code
		StringBuilder part = new StringBuilder();

		float resultWidthMultiplier = 0;
		float resultHeightMultiplier = 0;
		float resultPxOffset = 0;

		for (char c : axisPosition.toCharArray()) {
			if (c == '+' || c == '-') {
				String component = part.toString();
				part = new StringBuilder().append(c);

				if (component.endsWith("%")) { // width or height dependent on which way it is
					component = component.substring(0, component.length() - 1); // remove %

					if (isYAxis)
						resultHeightMultiplier += Float.parseFloat(component);
					else
						resultWidthMultiplier += Float.parseFloat(component);
				}
				else if (component.endsWith("vw")) {
					component = component.substring(0, component.length() - 2); // remove vw
					resultWidthMultiplier += Float.parseFloat(component);
				}
				else if (component.endsWith("vh")) {
					component = component.substring(0, component.length() - 2); // remove vh
					resultHeightMultiplier += Float.parseFloat(component);
				}
				else { // offsets
					float additionMultiplier = 1.0f;

					if (component.endsWith("em")) {
						component = component.substring(0, component.length() - 2); // remove em
						additionMultiplier = 16.0f; // 1em = 16px
					}
					else if (component.endsWith("px")) {
						component = component.substring(0, component.length() - 2); // remove px
					}

					resultPxOffset += additionMultiplier * Float.parseFloat(component); // add to the offset
				}
			}
			else {
				part.append(c);
			}
		}

		//System.out.println("E" + resultHeightMultiplier + " " + resultWidthMultiplier + " " + resultPxOffset);
		return new float[] {
				resultWidthMultiplier / 100.0f,
				resultHeightMultiplier / 100.0f,
				resultPxOffset
		};
	}
}
