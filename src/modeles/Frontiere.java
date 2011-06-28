package modeles;

import java.util.HashSet;
import java.util.List;


public class Frontiere {
	
	protected HashSet<Edge> edgeSet;
	protected HashSet<Point> pointSet;
	private static final long serialVersionUID = 1L;
	
	public Frontiere(List<Edge> a) {
		edgeSet = new HashSet<Edge>(a);
		for(Edge e : a) {
			pointSet.add(e.p1);
			pointSet.add(e.p2);
		}
		pointSet = new HashSet<Point>();
	}
	
	public Frontiere() {
		edgeSet = new HashSet<Edge>();
		pointSet = new HashSet<Point>();
	}
	
	public boolean containsPoint(Point p1) {
		for(Point p : pointSet) {
			if(p.equals(p1))
				return true;
		}
		return false;
//		return(pointSet.contains(p1));
	}
	
	public void add(Edge e) {
		edgeSet.add(e);
//		if(!this.containsEqualPoint1(e))
			pointSet.add(e.p1);
//		if(!this.containsEqualPoint2(e))
			pointSet.add(e.p2);
	}
	
	public void addAll(List<Edge> l) {
		for(Edge e : l) {
			add(e);
		}
	}
	
	public boolean containsEqualPoint1(Edge e) {
		for(Point p : pointSet) {
			if(p.equals(e.p1)) {
				e.p1 = p;
				return true;
			}
		}
		return false;
	}
	
	public boolean containsEqualPoint2(Edge e) {
		for(Point p : pointSet) {
			if(p.equals(e.p2)) {
				e.p2 = p;
				return true;
			}
		}
		return false;
	}

	public int size() {
		return edgeSet.size();
	}
	
	public double distance() {
		double distance = 0;
		for(Edge e : edgeSet) {
			distance += e.distance();
		}
		return distance;
	}
	
	public Mesh returnMesh() {
		Mesh ens = new Mesh();
		for(Edge e : edgeSet) {
			ens.add(e.t1);
		}
		return ens;
	}
	
	public void suppress(Frontiere aSuppr) {
		for(Edge e : aSuppr.edgeSet) {
			this.edgeSet.remove(e);
			this.pointSet.remove(e.p1);
			this.pointSet.remove(e.p2);
		}
	}
	
	public void returnNeighbours(Frontiere f, Edge e) {
		if(!f.edgeSet.contains(e)) {
			f.add(e);
			for(Edge edg : this.edgeSet) {
				if(edg.contains(e.p1) || edg.contains(e.p2))
					this.returnNeighbours(f, edg);
			}
		}	
	}
}
