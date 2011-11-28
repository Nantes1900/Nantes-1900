package fr.nantes1900.view.display3d;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

public class PolygonView extends TriangleFanArray {

	private Polygon polygon;
	private boolean selected;
	private Point centroid;

	public PolygonView(final Polygon poly) {
		super(poly.getPointList().size() * 2, GeometryArray.COORDINATES
				| GeometryArray.COLOR_3 | GeometryArray.NORMALS
				| GeometryArray.TEXTURE_COORDINATE_2, new int[] { poly
				.getPointList().size() * 2 });

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

		this.selected = false;

		Point3d[] vertex = new Point3d[poly.getPointList().size() * 2];
		for (int i = 0; i < poly.getPointList().size(); i++) {
			vertex[i] = new Point3d(poly.getPointList().get(i).getX(), poly
					.getPointList().get(i).getY(), poly.getPointList().get(i)
					.getZ());
			vertex[poly.getPointList().size() * 2 - 1 - i] = new Point3d(poly
					.getPointList().get(i).getX(), poly.getPointList().get(i)
					.getY(), poly.getPointList().get(i).getZ());
		}
		this.setCoordinates(0, vertex);

		Vector3f[] normal = new Vector3f[poly.getPointList().size() * 2];
		for (int i = 0; i < poly.getPointList().size(); i++) {
			normal[i] = convertNormal(this.polygon);
			normal[poly.getPointList().size() * 2 - 1 - i] = reverseConvertNormal(this.polygon);
		}

		this.setNormals(0, normal);
	}

	public Polygon getPolygon() {
		return this.polygon;
	}

	public void setPolyline(Polygon polygonIn) {
		this.polygon = polygonIn;
	}

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selectedIn) {
		this.selected = selectedIn;
	}

	public void select() {
		this.selected = true;

	}

	public void unselect() {
		this.selected = false;

	}

	public static Vector3f convertNormal(Polygon polygon) {
		Vector3f nromalFloat = new Vector3f((float) polygon.getNormal().getX(),
				(float) polygon.getNormal().getY(), (float) polygon.getNormal()
						.getZ());
		return nromalFloat;
	}

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
