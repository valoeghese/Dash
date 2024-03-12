package benzenestudios.sulphate;

/**
 * A simple pair of two integers.
 */
public record Vec2i(int x, int y) {
	public Vec2i add(Vec2i other) {
		return this.add(other.x, other.y);
	}

	public Vec2i add(int x, int y) {
		return new Vec2i(this.x + x, this.y + y);
	}

	public Vec2i sub(Vec2i other) {
		return this.sub(other.x, other.y);
	}

	public Vec2i sub(int x, int y) {
		return new Vec2i(this.x - x, this.y - y);
	}

	public Vec2i mul(Vec2i other) {
		return this.mul(other.x, other.y);
	}

	public Vec2i mul(int x, int y) {
		return new Vec2i(this.x * x, this.y * y);
	}

	public Vec2i scale(float by) {
		return new Vec2i((int) (this.x * by), (int) (this.y * by));
	}
}