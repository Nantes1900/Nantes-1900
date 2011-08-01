package fr.nantes1900.models.extended;

import fr.nantes1900.models.Mesh;

public class SpecialBuilding {

	private Mesh mesh = new Mesh();

	/**
	 * Constructor.
	 */
	public SpecialBuilding() {
	}

	/**
	 * Build a special building from a mesh.
	 * 
	 * @param m
	 *            the special building as a mesh
	 */
	public void buildFromMesh(Mesh m) {
		this.mesh = m;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
}
