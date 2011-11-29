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
public class MeshView extends TriangleArray
{

    /**
     * The center of the mesh.
     */
    private Point                        centroid;

    /**
     * The mesh of the things displayed.
     */
    private Mesh                         mesh;

    private Hashtable<Triangle, Integer> selectTableTriangle = new Hashtable<>();

    private Hashtable<Integer, Triangle> selectTableIndex    = new Hashtable<>();

    /**
     * The method of constructor of the class MeshView
     * @param m
     *            The mesh of the things displayed.
     */
    public MeshView(final Mesh m)
    {
        super(m.size() * 3, GeometryArray.COORDINATES | GeometryArray.COLOR_3
                | GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

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
            this.selectTableTriangle.put(triangle, i);
            this.selectTableIndex.put(i, triangle);

            this.setCoordinate(i, new Point3d(triangle.getP1().getX(), triangle
                    .getP1().getY(), triangle.getP1().getZ()));
            this.setCoordinate(i + 1, new Point3d(triangle.getP2().getX(),
                    triangle.getP2().getY(), triangle.getP2().getZ()));
            this.setCoordinate(i + 2, new Point3d(triangle.getP3().getX(),
                    triangle.getP3().getY(), triangle.getP3().getZ()));

            this.setNormal(i, convertNormal(triangle));
            this.setNormal(i + 1, convertNormal(triangle));
            this.setNormal(i + 2, convertNormal(triangle));

            this.setTextureCoordinate(0, i, new TexCoord2f(0.0f, 1.0f));
            this.setTextureCoordinate(0, i + 1, new TexCoord2f(0.0f, 0.0f));
            this.setTextureCoordinate(0, i + 2, new TexCoord2f(1.0f, 0.0f));

            i = i + 3;
        }
    }

    public Triangle getTriangleFromArrayPosition(int position)
    {
        return this.selectTableIndex.get(position);
    }

    public Integer getArrayPositionFromTriangle(Triangle triangle)
    {
        return this.selectTableTriangle.get(triangle);
    }

    /**
     * Get a Vector3f as the normal of the triangle in parameter (The normal
     * stored in the triangle is a Vector3d)
     * @param triangle
     *            The triangle to get the normal from.
     * @return Vector3f
     */
    public static Vector3f convertNormal(final Triangle triangle)
    {
        Vector3f normalFloat = new Vector3f(
                (float) triangle.getNormal().getX(), (float) triangle
                        .getNormal().getY(), (float) triangle.getNormal()
                        .getZ());
        return normalFloat;

    }

    /**
     * Getter.
     * @return the centroid point
     */
    public final Point getCentroid()
    {
        return this.centroid;
    }

    /**
     * Select a triangle knowing its index in the TriangleArray.
     * @param i
     *            The index of the triangle to select in the TriangleArray
     */
    public final void select(int i)
    {
        // FIXME : add the triangle in the Universe3DController triangle
        // selection.
        this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
        this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(1.0f, 1.0f));
        this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
    }

    /**
     * Selects a triangle.
     * @param triangle
     *            The Triangle to select
     */
    public final void select(final Triangle triangle)
    {
        this.select(this.getArrayPositionFromTriangle(triangle));
    }

    /**
     * TODO.
     * @param i
     *            The index of the triangle which to be unselected. TODO.
     */
    public void unselect(int i)
    {
        // FIXME : remove the triangle from the Universe3DController triangle
        // selection.
        this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
        this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(0.0f, 0.0f));
        this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
    }
}
