package benzenestudios.sulphate;

/**
 * Indicates how to anchor an element in a section. Depending on whether this is used on horizonal, vertical, or both axes, some values may not be fully implemented.
 */
public enum Anchor {
	/**
	 * Anchor on the top left of the object.
	 */
	TOP_LEFT(-1, -1),
	/**
	 * Anchor at the top of the object.
	 */
	TOP(0, -1),
	/**
	 * Anchor on the top right of the object.
	 */
	TOP_RIGHT(1, -1),
	/**
	 * Anchor on the left of the object.
	 */
	LEFT(-1, 0),
	/**
	 * Anchor in the centre of the object.
	 */
	CENTRE(0, 0),
	/**
	 * Anchor on the right of the object.
	 */
	RIGHT(1, 0),
	/**
	 * Anchor on the bottom left of the object.
	 */
	BOTTOM_LEFT(-1, 1),
	/**
	 * Anchor at the bottom of the object.
	 */
	BOTTOM(0, 1),
	/**
	 * Anchor on the bottom right of the object.
	 */
	BOTTOM_RIGHT(1, 1);

	Anchor(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public final int x, y;

	public Anchor withX(int x) {
		return VALUES[x + 1][this.y + 1];
	}

	public Anchor withY(int y) {
		return VALUES[this.x + 1][y + 1];
	}

	private static final Anchor[][] VALUES = new Anchor[][] {
			{TOP_LEFT, LEFT, BOTTOM_LEFT},
			{TOP, CENTRE, BOTTOM},
			{TOP_RIGHT, RIGHT, BOTTOM_RIGHT}
	};
}
