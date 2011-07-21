package modeles.basis;

import utils.MatrixMethod;

/**
 * @author Daniel Lefèvre, Elsa Arroud-Vignod
 * 
 *         Implement a point, composed of three coordinates.
 */
public class Point {

	private double x;
	private double y;
	private double z;

	/**
	 * Copy constructor.
	 * 
	 * To use very cautiously : it can create two Points with equal Values and
	 * different references in the Mesh !
	 * 
	 * @param p
	 *            the point to copy
	 */
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
	}

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
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	/**
	 * Getter of the coordinates of the point.
	 * 
	 * @return a table of double
	 */
	public double[] getPointAsCoordinates() {
		double[] coords = { x, y, z };
		return coords;
	}

	/**
	 * Setter.
	 * 
	 * @param x
	 *            : the x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Setter
	 * 
	 * @param y
	 *            coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Setter
	 * 
	 * @param z
	 *            coordinate
	 */
	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Setter
	 * 
	 * @param coords
	 *            the three coordinates
	 */
	public void set(double[] coords) {
		this.setX(coords[0]);
		this.setY(coords[1]);
		this.setZ(coords[2]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new String("(" + x + ", " + y + ", " + z + ")");
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	/**
	 * Operate a change base on the point
	 * 
	 * @param matrix
	 *            of base change Be careful : the point doesn't have the same
	 *            hashCode after this operation...
	 */
	public void changeBase(double[][] matrix) {
		double[] coords = { this.x, this.y, this.z };
		this.set(MatrixMethod.changeBase(coords, matrix));
	}
}