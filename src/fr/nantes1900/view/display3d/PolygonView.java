package fr.nantes1900.view.display3d;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.TriangleFanArray;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;

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
     * Indicate if the polygon is selected. TODO
     */
    private boolean selected;
    /**
     * The center of the polygon.
     */
    private Point centroid;

    /**
     * FIXME : problem : the number of faces is different for each polygons.
     */
    public static final int POLYGON_FACES_COUNT = 2;

    /**
     * Constructor of the class PolygonView.
     * @param poly
     *            The polygon of the thing displayed.
     */
    public PolygonView(final Polygon poly) {
        // FIXME : instead of POLYOGN_FACES_COUNT, put poly.getNumEdges() or I
        // don't know why to count the number of faces.
        super(poly.getPointList().size() * POLYGON_FACES_COUNT,
                GeometryArray.COORDINATES | GeometryArray.COLOR_3
                        | GeometryArray.NORMALS
                        | GeometryArray.TEXTURE_COORDINATE_2, new int[] { poly
                        .getPointList().size() * POLYGON_FACES_COUNT
                });

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

        // TODO
        this.selected = false;

        Point3d[] vertex = new Point3d[poly.getPointList().size()
                * POLYGON_FACES_COUNT];
        for (int i = 0; i < poly.getPointList().size(); i++) {
            vertex[i] = new Point3d(poly.getPointList().get(i).getX(), poly
                    .getPointList().get(i).getY(), poly.getPointList().get(i)
                    .getZ());
            vertex[poly.getPointList().size() * POLYGON_FACES_COUNT - 1 - i] = new Point3d(
                    poly.getPointList().get(i).getX(), poly.getPointList()
                            .get(i).getY(), poly.getPointList().get(i).getZ());
        }
        this.setCoordinates(0, vertex);

        Vector3f[] normal = new Vector3f[poly.getPointList().size()
                * POLYGON_FACES_COUNT];
        for (int i = 0; i < poly.getPointList().size(); i++) {
            normal[i] = convertNormal(this.polygon);
            normal[poly.getPointList().size() * POLYGON_FACES_COUNT - 1 - i] = reverseConvertNormal(this.polygon);
        }

        this.setNormals(0, normal);
    }

    /**
     * Converts the normal of the polygon (Vector3d) in a Vector3f.
     * @param polygon
     *            the polygon to get the normal from.
     * @return the normal as a Vector3d
     */
    public static Vector3f convertNormal(final Polygon polygon) {
        Vector3f normalFloat = new Vector3f((float) polygon.getNormal().getX(),
                (float) polygon.getNormal().getY(), (float) polygon.getNormal()
                        .getZ());
        return normalFloat;
    }

    /**
     * Converts the reverse of the normal of the polygon (Vector3d) in a
     * Vector3f.
     * @param polygon
     *            the polygon to get the normal from.
     * @return the reverse of the normal as a Vector3d
     */
    public static Vector3f reverseConvertNormal(final Polygon polygon) {
        Vector3f normalFloat = new Vector3f(
                -(float) polygon.getNormal().getX(), -(float) polygon
                        .getNormal().getY(), -(float) polygon.getNormal()
                        .getZ());
        return normalFloat;
    }

    /**
     * TODO.
     * @return centroid The center of the polygon.
     */
    public final Point getCentroid() {
        return this.centroid;
    }

    /**
     * Get the polygon displayed.
     * @return polygon the polygon displayed.
     */
    public final Polygon getPolygon() {
        return this.polygon;
    }

    /**
     * To know if the polygon is selected.
     * @return selected the condition of selection.
     */
    public final boolean isSelected() {
        return this.selected;
    }

    /**
     * Select the polygon.
     */
    public final void select() {
        this.selected = true;

    }

    /**
     * Set the polygon to be displayed.
     * @param polygonIn
     *            the polygon to set.
     */
    public final void setPolygon(final Polygon polygonIn) {
        this.polygon = polygonIn;
    }

    /**
     * Unselect the polygon.
     */
    public final void unselect() {
        this.selected = false;

    }

}
