package fr.nantes1900.view.display3d;

import java.util.Hashtable;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

/**
 * MeshView is a class extended of the class TriangleArray. It's used to display
 * all the triangles of the mesh.
 * @author Nicolas Bouillon Siju Wu
 */
public class MeshView extends TriangleArray {

    /**
     * The center of the mesh.
     */
    private Point centroid;

    /**
     * The mesh of the things displayed.
     */
    private Mesh mesh;

    /**
     * Hash table linking the triangles with their indexes.
     */
    private Hashtable<Triangle, Integer> selectTableTriangle = new Hashtable<>();

    /**
     * Hash table linking the indexes of the triangles with the references of
     * these triangles.
     */
    private Hashtable<Integer, Triangle> selectTableIndex = new Hashtable<>();

    /**
     * The number of the points of triangle.
     */
    public static final int TRIANGLE_POINTS_COUNT = 3;

    /**
     * Constructor of the class MeshView.
     * @param m
     *            The mesh of the things displayed.
     */
    public MeshView(final Mesh m) {

        super(m.size() * TRIANGLE_POINTS_COUNT, GeometryArray.COORDINATES
                | GeometryArray.COLOR_3 | GeometryArray.NORMALS
                | GeometryArray.TEXTURE_COORDINATE_2);

        this.mesh = m;

        this.centroid = m.getCentroid();

        this.setCapability(ALLOW_COLOR_WRITE);
        this.setCapability(ALLOW_COLOR_READ);
        this.setCapability(ALLOW_TEXCOORD_READ);
        this.setCapability(ALLOW_TEXCOORD_WRITE);

        int i = 0;

        // Create the triangles to be displayed.
        for (Triangle triangle : this.mesh)
        {
            this.selectTableTriangle.put(triangle, i / TRIANGLE_POINTS_COUNT);
            this.selectTableIndex.put(i / TRIANGLE_POINTS_COUNT, triangle);

            this.setCoordinate(i, new Point3d(triangle.getP1().getX(), triangle
                    .getP1().getY(), triangle.getP1().getZ()));
            this.setCoordinate(i + 1, new Point3d(triangle.getP2().getX(),
                    triangle.getP2().getY(), triangle.getP2().getZ()));
            this.setCoordinate(i + 2, new Point3d(triangle.getP3().getX(),
                    triangle.getP3().getY(), triangle.getP3().getZ()));

            this.setNormal(i, convertNormal(triangle));
            this.setNormal(i + 1, convertNormal(triangle));
            this.setNormal(i + 2, convertNormal(triangle));

            i += TRIANGLE_POINTS_COUNT;
        }
    }

    /**
     * Converts the normal of the triangle (Vector3d) in a Vector3f.
     * @param triangle
     *            The triangle to get the normal from.
     * @return the normal as a Vector3d
     */
    public static Vector3f convertNormal(final Triangle triangle) {
        Vector3f normalFloat = new Vector3f(
                (float) triangle.getNormal().getX(), (float) triangle
                        .getNormal().getY(), (float) triangle.getNormal()
                        .getZ());
        return normalFloat;

    }

    /**
     * Gets the index in the TriangleArray of the triangle given.
     * @param triangle
     *            the triangle we want to have index of
     * @return the index of this Triangle in the TriangleArray
     */
    public final Integer getArrayPositionFromTriangle(final Triangle triangle) {
        return this.selectTableTriangle.get(triangle);
    }

    /**
     * Getter.
     * @return the centroid point
     */
    public final Point getCentroid() {
        return this.centroid;
    }

    /**
     * Getter.
     * @return the reference to the mesh contained
     */
    public final Mesh getMesh() {
        return this.mesh;
    }

    /**
     * Gets the Triangle specified in the TriangleArray at a position given.
     * @param position
     *            the index of the triangle we want to have the reference of
     * @return the Triangle associated
     */
    public final Triangle getTriangleFromArrayPosition(final int position) {
        return this.selectTableIndex.get(position);
    }

    /**
     * Select a triangle knowing its index in the TriangleArray.
     * @param i
     *            The index of the triangle to select in the TriangleArray
     */
    public final void select(final int i) {

        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT, new TexCoord2f(
                0.0f, 1.0f));
        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT + 1,
                new TexCoord2f(1.0f, 1.0f));
        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT + 2,
                new TexCoord2f(1.0f, 0.0f));
    }

    /**
     * Selects a triangle.
     * @param triangle
     *            The Triangle to select
     */
    public final void select(final Triangle triangle) {
        this.select(this.getArrayPositionFromTriangle(triangle));
    }

    /**
     * Unselects a triangle knowing its index in the TriangleArray.
     * @param i
     *            The index of the triangle which to be unselected.
     */
    public final void unSelect(final int i) {

        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT, new TexCoord2f(
                0.0f, 0.0f));
        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT + 1,
                new TexCoord2f(0.0f, 0.0f));
        this.setTextureCoordinate(0, i * TRIANGLE_POINTS_COUNT + 2,
                new TexCoord2f(0.0f, 0.0f));
    }

    /**
     * Unselects a triangle.
     * @param triangle
     *            the triangle to unselect
     */
    public void unSelect(Triangle triangle) {
        Integer arrayPosition = this.getArrayPositionFromTriangle(triangle);
        if (arrayPosition != null)
        {
            this.unSelect(this.getArrayPositionFromTriangle(triangle));
        }
    }
}
