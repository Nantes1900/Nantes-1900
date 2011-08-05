package fr.nantes1900.models.basis;

import fr.nantes1900.utils.MatrixMethod;

/**
 * @author Daniel Lefèvre, Elsa Arroud-Vignod Implement a point, composed of
 *         three coordinates.
 */
public class Point {

    // LOOK : maybe truncate those doubles to reduce useless precision... :
    // maybe cast it in the parser. But it can be dangerous. And this conversion
    // is weird in JAVA... See also then the hashCode AND equals method. For
    // now, these methods truncate, but it could be useful to have a further
    // look to see if everything walk on perfectly.

    /**
     * x coordinate.
     */
    private double x;
    /**
     * y coordinate.
     */
    private double y;
    /**
     * z coordinate.
     */
    private double z;

    /**
     * Constructor.
     * @param xNew
     *            coordinate
     * @param yNew
     *            coordinate
     * @param zNew
     *            coordinate
     */
    public Point(final double xNew, final double yNew,
        final double zNew) {
        this.x = xNew;
        this.y = yNew;
        this.z = zNew;
    }

    /**
     * Copy constructor. To use very cautiously : it can create two Points with
     * equal Values and different references in the Mesh !
     * @param point
     *            the point to copy
     */
    public Point(final Point point) {
        this.x = point.x;
        this.y = point.y;
        this.z = point.z;
    }

    /**
     * Operate a change base on the point.
     * @param matrix
     *            of base change Be careful : the point doesn't have the same
     *            hashCode after this operation...
     */
    public final void changeBase(final double[][] matrix) {
        final double[] coords = {this.x, this.y, this.z
        };
        this.set(MatrixMethod.changeBase(coords, matrix));
    }

    /**
     * Compute the distance between two points.
     * @param p
     *            the other point
     * @return the distance
     */
    public final double distance(final Point p) {
        return Math.sqrt(Math.pow(p.x - x, 2)
            + Math.pow(p.y - y, 2) + Math.pow(p.z - z, 2));
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public final boolean equals(final Object obj) {
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
     * @return a table of double
     */
    public final double[] getPointAsCoordinates() {
        return new double[] {this.x, this.y, this.z
        };
    }

    /**
     * Getter.
     * @return the x coordinate
     */
    public final double getX() {
        return x;
    }

    /**
     * Getter.
     * @return the y coordinate
     */
    public final double getY() {
        return y;
    }

    /**
     * Getter.
     * @return the z coordinate
     */
    public final double getZ() {
        return z;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public final int hashCode() {
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
     * Setter method.
     * @param xNew
     *            the new x coordinate
     * @param yNew
     *            the new y coordinate
     * @param zNew
     *            the new z coordinate
     */
    public final void set(final double xNew, final double yNew,
        final double zNew) {
        this.x = xNew;
        this.y = yNew;
        this.z = zNew;
    }

    /**
     * Setter.
     * @param coords
     *            the three coordinates
     */
    public final void set(final double[] coords) {
        this.setX(coords[0]);
        this.setY(coords[1]);
        this.setZ(coords[2]);
    }

    /**
     * Setter.
     * @param xNew
     *            : the x coordinate
     */
    public final void setX(final double xNew) {
        this.x = xNew;
    }

    /**
     * Setter.
     * @param yNew
     *            coordinate
     */
    public final void setY(final double yNew) {
        this.y = yNew;
    }

    /**
     * Setter.
     * @param zNew
     *            coordinate
     */
    public final void setZ(final double zNew) {
        this.z = zNew;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {
        return new String("(" + x + ", " + y + ", " + z + ")");
    }
}
