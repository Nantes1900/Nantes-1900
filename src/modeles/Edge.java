package modeles;

import java.util.ArrayList;

public class Edge {
	private ArrayList<Triangle> triangleList;

	private Point p1;
	private Point p2;

	public Edge(Point p1, Point p2) {
		triangleList = new ArrayList<Triangle>();
		this.p1 = p1;
		this.p2 = p2;
	}

	public Edge() {
		triangleList = new ArrayList<Triangle>();
		p1 = null;
		p2 = null;
	}

	public ArrayList<Triangle> getTriangleList() {
		return triangleList;
	}

	public void setTriangleList(ArrayList<Triangle> triangleList) {
		this.triangleList = triangleList;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}

	public double distance() {
		return this.p1.distance(this.p2);
	}

	public boolean contains(Point p) {
		return (this.p1.equals(p) || this.p2.equals(p));
	}

	public void addTriangle(Triangle t) {
		if(!triangleList.contains(t)) {
			triangleList.add(t);
			if(triangleList.size() > 2) {
				System.err.println("Error : more than 2 Triangles !");
			}
		}
	}

	public boolean isNeighboor(Edge e) {
		return(this.contains(e.getP1()) || this.contains(e.getP2()));
	}

	@Override
	public int hashCode() {
		int result = 1;
		result = result + ((p1 == null) ? 0 : p1.hashCode());
		result = result + ((p2 == null) ? 0 : p2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge e = (Edge) obj;
		return (e.contains(this.p1) && e.contains(this.p2));
	}
}
