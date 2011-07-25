package nantes1900.models.extended;

import nantes1900.models.Mesh;

public class Floor extends Mesh {

	private String attribute = new String();
	private Mesh floor = new Mesh();

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor. Build the Floor with an attribute as a String.
	 * 
	 * @param type
	 *            the attribute
	 */
	public Floor(String type) {
		super();
		this.attribute = type;
	}

	/**
	 * Getter.
	 * 
	 * @return the attribute
	 */
	public String getAttribute() {
		return attribute;
	}

	/**
	 * Setter
	 * 
	 * @param attribute
	 *            the attribute of the Floor
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	/**
	 * Getter.
	 * 
	 * @return the floor as a mesh
	 */
	public Mesh getFloor() {
		return floor;
	}

	/**
	 * Setter.
	 * 
	 * @param floor
	 *            the floor as a mesh
	 */
	public void setFloor(Mesh floor) {
		this.floor = floor;
	}

	/**
	 * Build a floor from a mesh, by computing the algorithms.
	 * 
	 * @param m
	 *            the mesh to convert
	 */
	public void buildFromMesh(Mesh m) {
		floor = new Mesh(m);

		this.decimate(floor);
	}

	/**
	 * Decimate the floor.
	 * 
	 * @param floor
	 *            the floor to decimate as a mesh
	 */
	public void decimate(Mesh floor) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nantes1900.models.Mesh#writeSTL(java.lang.String)
	 */
	public void writeSTL(String fileName) {
		this.writeSTL(fileName);
	}
}
