package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;

public class Edge {

	private ArrayList<Triangle> triangleList = new ArrayList<Triangle>(2);

	private Point[] points = new Point[2];

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
		if(!this.triangleList.contains(t))		
			throw new InvalidParameterException("t is not part of the Edge !");
		if(this.triangleList.size() < 2)
			return null;
		if(this.triangleList.get(0) == t)
			return this.triangleList.get(1);
		else if(this.triangleList.get(1) == t)
			return this.triangleList.get(0);
		else return null;
	}

	public Point getP1() {
		return this.points[0];
	}

	public Point getP2() {
		return this.points[1];
	}

	public ArrayList<Point> getPoints() {
		return new ArrayList<Point>(Arrays.asList(this.points));
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

	public int getNumberTriangles() {
		return this.triangleList.size();
	}

	public boolean isNeighboor(Edge e) {
		if(this == e)
			return false;
		else
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

	//LOOK : fix this...
	public void returnNeighbours(Polyline ret, Polyline p) {
		if(!ret.contains(this)) {
			ret.add(this);
			for(Edge e : p.getEdgeList()) {
				if(this.isNeighboor(e)) {
					e.returnNeighbours(ret, p);
				}
			}
		}
	}

	//TODO : maybe for this method and returnNeighbours, it should be interesting to make an index and to search first.
	//Or to create references in the class Point...
	public int getNumNeighbours(Polyline p) {
		if(!p.contains(this)) {
			throw new InvalidParameterException();
		}
		int counter = 0;
		for(Edge e : p.getEdgeList()) {
			if(this.isNeighboor(e)) {
				counter ++;
			}
		}
		return counter;
	}

	public Point returnOther(Point p) {
		if(this.getP1() == p)
			return this.getP2();
		else if(this.getP2() == p)
			return this.getP1();
		else {
			return null;
			//TODO : throw Ex !
		}
	}

	//TODO : test
	//Returns the neighbours of this containing p in b
	public Edge returnNeighbour(Point p, Polyline b) {
		if(!b.contains(this)) {
			throw new InvalidParameterException();
		}
		ArrayList<Edge> list = b.getNeighbours(p);
		if(list.size() != 2) {
			System.err.println("Error !");
			//TODO : throw new Exception !
		}
		list.remove(this);
		return list.get(0);
	}

	public Edge compose(Edge eAdd, Point p) {
		return new Edge(this.returnOther(p), eAdd.returnOther(p));
	}
}
