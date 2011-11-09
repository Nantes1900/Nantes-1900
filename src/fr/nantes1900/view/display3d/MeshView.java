package fr.nantes1900.view.display3d;

import java.util.HashSet;

import javax.media.j3d.Shape3D;

import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.view.display3d.TriangleView;
import fr.nantes1900.models.middle.Mesh;

public class MeshView extends HashSet<TriangleView> {
	private static final long serialVersionUID = 1L;

	public Point centroid;

	public MeshView(Mesh mesh) {
		for (Triangle tri : mesh) {
			this.add(new TriangleView(tri));
		}

		this.centroid = mesh.getCentroid();
	}

	public Point getCentroid() {
		return this.centroid;
	}

	public HashSet<Shape3D> createTriangleShapes() {

		HashSet<Shape3D> shapeSet = new HashSet<Shape3D>();

		for (TriangleView triangle : this) {
			triangle.createShape3D();
		}

		return shapeSet;
	}

}
