package de.eblelife.test.se7;

public class Point2D {
	private int x, y;

	public Point2D(int x, int y) {
		x = x;
	}

	public String toString() {
		return "[" + x + ", " + y + "]";
	}

	public static void main(String[] args) {
		Point2D point = new Point2D(10, 20);
		System.out.println(point);
	}
}