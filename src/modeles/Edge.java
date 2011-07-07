package modeles;

import java.util.ArrayList;

public class Edge {
	private ArrayList<Triangle> triangleList;

	private Point p1;
	private Point p2;

	public Edge(Point p1, Point p2) {
		triangleList = new ArrayList<Triangle>(2);
		this.p1 = p1;
		this.p2 = p2;
	}

	public ArrayList<Triangle> getTriangleList() {
		return triangleList;
	}
	
	public Triangle returnOther(Triangle t) {
		if(triangleList.size() > 2)
			System.err.println("Error : more than two triangles per edge !");
		if(triangleList.size() < 2)
			return null;
		if(triangleList.get(0) == t)
			return triangleList.get(1);
		else
			return triangleList.get(1);
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public double distance() {
		return this.p1.distance(this.p2);
	}

	public boolean contains(Point p) {
		return (this.p1 == p || this.p2 == p);
	}

	public void addTriangle(Triangle t) {
		if(!triangleList.contains(t)) {
			triangleList.add(t);
			if(triangleList.size() > 2) {
				//TODO : throw Exception and treat it !
				System.err.println("Problem in the mesh : more than two triangles for one edge !");
			}
		}
	}

	public boolean isNeighboor(Edge e) {
		return(this.contains(e.p1) || this.contains(e.p2));
	}

	@Override
	//FIXME : revoir le hashCode !
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
