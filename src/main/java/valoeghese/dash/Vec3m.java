package valoeghese.dash;

import net.minecraft.world.phys.Vec3;

public class Vec3m {
	public Vec3m(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vec3m(Vec3 vec) {
		this(vec.x, vec.y, vec.z);
	}

	public double x;
	public double y;
	public double z;

	public void add(Vec3m vec) {
		this.x += vec.x;
		this.y += vec.y;
		this.z += vec.z;
	}

	public void add(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}
}
