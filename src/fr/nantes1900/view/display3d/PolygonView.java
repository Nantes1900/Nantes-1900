package fr.nantes1900.view.display3d;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

/**
 * PolygonView is a class extended of the class TriangleFanArray. It's used to
 * display the surface simplified as a polygon.
 * @author Nicolas Bouillon Siju Wu
 */
public class PolygonView extends TriangleFanArray {

	/**
	 * The polygon of the thing displayed.
	 */
	private Polygon polygon;
	/**
	 * Indicate if the polygon is selected.
	 * TODO
	 */
	private boolean selected;
	/**
	 * The center of the polygon.
	 */
	private Point centroid;
	
	public static final int POLYGON_FACES_COUNT=2;

	/**
	 * Constructor of the class PolygonView.
	 * @param poly
	 *                The polygon of the thing displayed.
	 */
	public PolygonView(final Polygon poly) {
		super(poly.getPointList().size() * POLYGON_FACES_COUNT, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3 | GeometryArray.NORMALS
				| GeometryArray.TEXTURE_COORDINATE_2, new int[] { poly
				.getPointList().size() * POLYGON_FACES_COUNT });

		this.polygon = poly;
		this.centroid = new Point(0, 0, 0);
		for (Point point : poly.getPointList()) {
			this.centroid.setX(this.centroid.getX() + point.getX());
			this.centroid.setY(this.centroid.getY() + point.getY());
			this.centroid.setZ(this.centroid.getZ() + point.getZ());
		}

		this.centroid.setX(this.centroid.getX() / (poly.getPointList().size()));
		this.centroid.setY(this.centroid.getY() / (poly.getPointList().size()));
		this.centroid.setZ(this.centroid.getZ() / (poly.getPointList().size()));

		//TODO
		this.selected = false;

		Point3d[] vertex = new Point3d[poly.getPointList().size() * POLYGON_FACES_COUNT];
		for (int i = 0; i < poly.getPointList().size(); i++) {
			vertex[i] = new Point3d(poly.getPointList().get(i).getX(), poly
					.getPointList().get(i).getY(), poly.getPointList().get(i)
					.getZ());
			vertex[poly.getPointList().size() * POLYGON_FACES_COUNT - 1 - i] = new Point3d(poly
					.getPointList().get(i).getX(), poly.getPointList().get(i)
					.getY(), poly.getPointList().get(i).getZ());
		}
		this.setCoordinates(0, vertex);

		Vector3f[] normal = new Vector3f[poly.getPointList().size() * POLYGON_FACES_COUNT];
		for (int i = 0; i < poly.getPointList().size(); i++) {
			normal[i] = convertNormal(this.polygon);
			normal[poly.getPointList().size() * POLYGON_FACES_COUNT - 1 - i] = reverseConvertNormal(this.polygon);
		}

		this.setNormals(0, normal);
	}

	/**
	 * Get the polygon displayed.
	 * @return polygon
	 *             the polygon displayed.
	 */
	public Polygon getPolygon() {
		return this.polygon;
	}

	/**
	 * Set the polygon to be displayed.
	 * @param polygonIn
	 *             the polygon to set.
	 */
	public void setPolyline(Polygon polygonIn) {
		this.polygon = polygonIn;
	}

	/**
	 * To know if the polygon is selected.
	 * @return selected
	 *             the condition of selection.
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Set the condition of seleciton of this polygon.
	 * @param selectedIn 
	 *             the condition of selection to set.
	 */
	public void setSelected(boolean selectedIn) {
		this.selected = selectedIn;
	}

	/**
	 *  Select the polygon.
	 */
	public void select() {
		this.selected = true;

	}

	/**
	 *  Unselect the polygon.
	 */
	public void unselect() {
		this.selected = false;

	}

	/**
	 * Converts the normal of the polygon (Vector3d) in a Vector3f.
	 * @param polygon 
	 *           the polygon to get the normal from.
	 * @return the normal as a Vector3d
	 */
	public static Vector3f convertNormal(Polygon polygon) {
		Vector3f nromalFloat = new Vector3f((float) polygon.getNormal().getX(),
				(float) polygon.getNormal().getY(), (float) polygon.getNormal()
						.getZ());
		return nromalFloat;
	}

	/**
	 * Converts the reverse of the normal of the polygon (Vector3d) in a Vector3f.
	 * @param polygon
	 *           the polygon to get the normal from.
	 * @return the reverse of the normal as a Vector3d
	 */
	public static Vector3f reverseConvertNormal(Polygon polygon) {
		Vector3f nromalFloat = new Vector3f(
				-(float) polygon.getNormal().getX(), -(float) polygon
						.getNormal().getY(), -(float) polygon.getNormal()
						.getZ());
		return nromalFloat;
	}

	/**
	 * @return centroid The center of the polygon.
	 */
	public Point getCentroid() {
		return this.centroid;
	}

}
