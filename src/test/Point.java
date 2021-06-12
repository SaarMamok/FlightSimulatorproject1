package test;

public class Point {
	public final float x,y;
	public Point(float x, float y) {
		this.x=x;
		this.y=y;
	}
	public Point subtract(Point p) {
		return new Point(x - p.x, y - p.y);
	}


	public double distance(Point p) {
		return Math.hypot(x - p.x, y - p.y);
	}


	// Signed area / determinant thing
	public double cross(Point p) {
		return x * p.y - y * p.x;
	}


	public String toString() {
		return String.format("Point(%g, %g)", x, y);
	}
	/*public float distanceSquaredTo(final Point p) {
		final float DX = x - p.x;
		final float DY = y - p.y;

		return  DX * DX + DY * DY;
	}

	public double distanceTo(final Point p) {
		return Math.sqrt(distanceSquaredTo(p));
	}

	public static boolean areColinear(final Point p1, final Point p2, final Point p3) {
		return Math.abs(p1.x * (p2.y - p3.y) + p2.x * (p3.y - p1.y) + p3.x * (p1.y - p2.y)) == 0.0;
	}
*/
}
