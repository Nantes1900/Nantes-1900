package nantes1900.models.basis;

import nantes1900.utils.MatrixMethod;

/**
 * @author Daniel Lefèvre, Elsa Arroud-Vignod
 * 
 *         Implement a point, composed of three coordinates.
 */
public class Point {

	// LOOK : maybe truncate those doubles to reduce useless precision... :
	// maybe cast it in the parser. But it can be dangerous. And this conversion
	// is weird in JAVA... See also then the hashCode AND equals method. For
	// now, these methods truncate, but it could be useful to have a further
	// look to see if everything walk on perfectly.

	private double x;
	private double y;
	private double z;

	/**
	 * Constructor.
	 * 
	 * @param x
	 *            coordinate
	 * @param y
	 *            coordinate
	 * @param z
	 *            coordinate
	 */
	public Point(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Copy constructor.
	 * 
	 * To use very cautiously : it can create two Points with equal Values and
	 * different references in the Mesh !
	 * 
	 * @param point
	 *            the point to copy
	 */
	public Point(final Point point) {
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}

	/**
	 * Operate a change base on the point
	 * 
	 * @param matrix
	 *            of base change Be careful : the point doesn't have the same
	 *            hashCode after this operation...
	 */
	public void changeBase(final double[][] matrix) {
		final double[] coords = { this.x, this.y, this.z };
		this.set(MatrixMethod.changeBase(coords, matrix));
	}

	/**
	 * Compute the distance between two points
	 * 
	 * @param p
	 *            the other point
	 * @return the distance
	 */
	public double distance(Point p) {
		return Math.pow(
				Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2)
						+ Math.pow(p.z - z, 2), 0.5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Point other = (Point) obj;
		if (Double.doubleToLongBits((float) x) != Double
				.doubleToLongBits((float) other.x)) {
			return false;
		}
		if (Double.doubleToLongBits((float) y) != Double
				.doubleToLongBits((float) other.y)) {
			return false;
		}
		if (Double.doubleToLongBits((float) z) != Double
				.doubleToLongBits((float) other.z)) {
			return false;
		}
		return true;
	}

	/**
	 * Getter of the coordinates of the point.
	 * 
	 * @return a table of double
	 */
	public double[] getPointAsCoordinates() {
		return new double[] { this.x, this.y, this.z };
	}

	/**
	 * Getter.
	 * 
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Getter.
	 * 
	 * @return the y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Getter.
	 * 
	 * @return the z coordinate
	 */
	public double getZ() {
		return z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits((float) x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits((float) y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits((float) z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Setter
	 * 
	 * @param coords
	 *            the three coordinates
	 */
	public void set(final double[] coords) {
		this.setX(coords[0]);
		this.setY(coords[1]);
		this.setZ(coords[2]);
	}

	/**
	 * Setter.
	 * 
	 * @param x
	 *            : the x coordinate
	 */
	public void setX(final double x) {
		this.x = x;
	}

	/**
	 * Setter
	 * 
	 * @param y
	 *            coordinate
	 */
	public void setY(final double y) {
		this.y = y;
	}

	/**
	 * Setter
	 * 
	 * @param z
	 *            coordinate
	 */
	public void setZ(final double z) {
		this.z = z;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String("(" + x + ", " + y + ", " + z + ")");
	}

	public void set(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
