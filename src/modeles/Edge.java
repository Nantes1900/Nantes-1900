package modeles;

import java.util.ArrayList;

public class Edge {
	
	private ArrayList<Triangle> triangleList = new ArrayList<Triangle>(2);

	private Point[] points = new Point[3];

	public Edge(Point p1, Point p2) {
		this.points[0] = p1;
		this.points[1] = p2;
	}

	public ArrayList<Triangle> getTriangleList() {
		return this.triangleList;
	}

	public Triangle returnOther(Triangle t) {
		if(this.triangleList.size() > 2)
			//TODO : renvoyer une exception
			System.err.println("Error : more than two triangles per edge !");
		if(this.triangleList.size() < 2)
			return null;
		if(this.triangleList.get(0) == t)
			return this.triangleList.get(1);
		else
			return this.triangleList.get(1);
	}

	public Point getP1() {
		return this.points[0];
	}

	public Point getP2() {
		return this.points[1];
	}

	public double distance() {
		return this.points[0].distance(this.points[1]);
	}

	public boolean contains(Point p) {
		return (this.points[0] == p || this.points[1] == p);
	}

	public void addTriangle(Triangle t) {
		if(!this.triangleList.contains(t)) {
			this.triangleList.add(t);
			if(this.triangleList.size() > 2) {
				//TODO : throw Exception and treat it !
				System.err.println("Problem in the mesh : more than two triangles for one edge !");
			}
		}
	}

	public boolean isNeighboor(Edge e) {
		return(this.contains(e.points[0]) || this.contains(e.points[1]));
	}

	@Override
	//FIXME : revoir le hashCode !
	public int hashCode() {
		int result = 1;
		result = result + ((this.points[0] == null) ? 0 : this.points[0].hashCode());
		result = result + ((this.points[1] == null) ? 0 : this.points[1].hashCode());
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
		return (e.contains(this.points[0]) && e.contains(this.points[1]));
	}

	//FIXME !
	public void returnNeighbours(Polyline ret, Polyline p) {
		//		ret.add(this);
		//		
		//		Edge e;
		//		for(Point p : this.) {
		//			
		//		}
	}
}
