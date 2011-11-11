package fr.nantes1900.view.display3d;

import java.util.HashSet;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

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
}
