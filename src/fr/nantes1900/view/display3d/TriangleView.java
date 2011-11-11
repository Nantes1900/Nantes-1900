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

public class TriangleView extends Shape3D {

	private Triangle triangle;

	public TriangleView(Triangle tri) {
		this.triangle = tri;
	}

	public Triangle getTriangle() {
		return this.triangle;
	}

	public void setTriangle(Triangle triangleIn) {
		this.triangle = triangleIn;
	}

	public void createShape3D() {

		TriangleArray triangle1 = new TriangleArray(3,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3
						| GeometryArray.NORMALS);

		triangle1.setCoordinate(0, new Point3d(this.triangle.getP1().getX(),
				this.triangle.getP1().getY(), this.triangle.getP1().getZ()));
		triangle1.setCoordinate(1, new Point3d(this.triangle.getP2().getX(),
				this.triangle.getP2().getY(), this.triangle.getP2().getZ()));
		triangle1.setCoordinate(2, new Point3d(this.triangle.getP3().getX(),
				this.triangle.getP3().getY(), this.triangle.getP3().getZ()));
		triangle1.setNormal(0, convertNormal(triangle));
		triangle1.setNormal(1, convertNormal(triangle));
		triangle1.setNormal(2, convertNormal(triangle));

		TriangleArray triangle2 = new TriangleArray(3,
				GeometryArray.COORDINATES | GeometryArray.COLOR_3
						| GeometryArray.NORMALS);
		
		triangle2.setCoordinate(0, new Point3d(this.triangle.getP2().getX(),
				this.triangle.getP2().getY(), this.triangle.getP2().getZ()));
		triangle2.setCoordinate(1, new Point3d(this.triangle.getP1().getX(),
				this.triangle.getP1().getY(), this.triangle.getP1().getZ()));
		triangle2.setCoordinate(2, new Point3d(this.triangle.getP3().getX(),
				this.triangle.getP3().getY(), this.triangle.getP3().getZ()));
		triangle2.setNormal(0, convertOppositeNormal(triangle));
		triangle2.setNormal(1, convertOppositeNormal(triangle));
		triangle2.setNormal(2, convertOppositeNormal(triangle));

		this.changeColor(Color.blue);

		this.addGeometry(triangle1);
		this.addGeometry(triangle2);

		this.setCapability(ALLOW_APPEARANCE_READ);
		this.setCapability(ALLOW_APPEARANCE_WRITE);
	}

	public Vector3f convertNormal(Triangle triangle) {
		Vector3f normalFloat = new Vector3f(
				(float) triangle.getNormal().getX(), (float) triangle
						.getNormal().getY(), (float) triangle.getNormal()
						.getZ());
		return normalFloat;
	}

	public Vector3f convertOppositeNormal(Triangle triangle) {
		Vector3f normalFloat = new Vector3f(-(float) triangle.getNormal()
				.getX(), -(float) triangle.getNormal().getY(),
				-(float) triangle.getNormal().getZ());
		return normalFloat;
	}

	public void changeColor(Color color) {
		Appearance app = new Appearance();
		Material mat = null;
		if (color==Color.blue) {
			mat = new Material(new Color3f(0f, 0, 0.2f), new Color3f(0, 0, 0),
					new Color3f(Color.blue), new Color3f(Color.blue), 64);
		} else if (color==Color.red)  {
			mat = new Material(new Color3f(0.2f, 0, 0), new Color3f(0, 0, 0),
					new Color3f(Color.red), new Color3f(Color.red), 64);
		}
		mat.setColorTarget(3);
		app.setMaterial(mat);
		this.setAppearance(app);
	}

}
