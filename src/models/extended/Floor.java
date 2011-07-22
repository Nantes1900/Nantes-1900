package models.extended;

import models.Mesh;

public class Floor extends Mesh {

	private String attribute = new String();
	private Mesh floor = new Mesh();

	private static final long serialVersionUID = 1L;

	public Floor(String type) {
		super();
		this.attribute = type;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public Mesh getFloor() {
		return floor;
	}

	public void setFloor(Mesh floor) {
		this.floor = floor;
	}

	public void buildFromMesh(Mesh m) {
		floor = new Mesh(m);

		this.reduce(floor);
	}

	public void reduce(Mesh floor) {

	}

	public void writeSTL(String fileName) {
		this.writeSTL(fileName);
	}
}
