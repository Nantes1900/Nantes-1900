package fr.nantes1900.view.display3d;

import java.awt.Color;

import javax.media.j3d.Appearance;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;

import fr.nantes1900.models.basis.Triangle;

public class TriangleView extends TriangleArray {

    private Triangle triangle;

    public TriangleView(Triangle tri) {
        super(3, GeometryArray.COORDINATES | GeometryArray.COLOR_3
                | GeometryArray.NORMALS);
        this.triangle = tri;
        this.setCoordinate(0, new Point3d(this.triangle.getP1().getX(),
                this.triangle.getP1().getY(), this.triangle.getP1().getZ()));
        this.setCoordinate(1, new Point3d(this.triangle.getP2().getX(),
                this.triangle.getP2().getY(), this.triangle.getP2().getZ()));
        this.setCoordinate(2, new Point3d(this.triangle.getP3().getX(),
                this.triangle.getP3().getY(), this.triangle.getP3().getZ()));
        this.setNormal(0, convertNormal());
        this.setNormal(1, convertNormal());
        this.setNormal(2, convertNormal());

        // TODO : I disable the two-faces view. Maybe it could be useful to
        // reactivate it if it is not too much for the computer.
    }

    public Vector3f convertNormal() {
        return new Vector3f((float) this.triangle.getNormal().getX(),
                (float) this.triangle.getNormal().getY(), (float) this.triangle
                        .getNormal().getZ());
    }

    public Vector3f convertOppositeNormal() {
        return new Vector3f(-(float) this.triangle.getNormal().getX(),
                -(float) this.triangle.getNormal().getY(),
                -(float) this.triangle.getNormal().getZ());
    }

    public Triangle getTriangle() {
        return this.triangle;
    }

    public void setTriangle(Triangle triangleIn) {
        this.triangle = triangleIn;
    }
}
