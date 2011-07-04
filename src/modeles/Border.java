package modeles;

import java.util.HashSet;
import java.util.List;

//TODO : suppress @SuppressWarnings
@SuppressWarnings("unused")

public class Border {
	
	private HashSet<Edge> edgeSet;
	private HashSet<Point> pointSet;
	private int ID;
	private static int ID_current = 0;
	private static final long serialVersionUID = 1L;
	
	public Border(List<Edge> a) {
		edgeSet = new HashSet<Edge>(a);
		for(Edge e : a) {
			pointSet.add(e.getP1());
			pointSet.add(e.getP2());
		}
		pointSet = new HashSet<Point>();
		this.ID = ++ID_current;
	}
	
	public Border() {
		edgeSet = new HashSet<Edge>();
		pointSet = new HashSet<Point>();
		this.ID = ++ID_current;
	}
	

	public HashSet<Edge> getEdgeSet() {
		return edgeSet;
	}

	public void setEdgeSet(HashSet<Edge> edgeSet) {
		this.edgeSet = edgeSet;
	}

	public HashSet<Point> getPointSet() {
		return pointSet;
	}

	public void setPointSet(HashSet<Point> pointSet) {
		this.pointSet = pointSet;
	}
	
	public boolean containsPoint(Point p) {
		for(Point point : pointSet) {
			if(point.equals(p))
				return true;
		}
		return false;
	}
	
	public void add(Edge e) {
		edgeSet.add(e);
			pointSet.add(e.getP1());
			pointSet.add(e.getP2());
	}
	
	public void addAll(List<Edge> l) {
		for(Edge e : l) {
			add(e);
		}
	}
	
	public boolean containsEqualPoint1(Edge e) {
		for(Point p : pointSet) {
			if(p.equals(e.getP1())) {
				e.setP1(p);
				return true;
			}
		}
		return false;
	}
	
	public boolean containsEqualPoint2(Edge e) {
		for(Point p : pointSet) {
			if(p.equals(e.getP2())) {
				e.setP2(p);
				return true;
			}
		}
		return false;
	}

	public int edgeSize() {
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
			ens.add(e.getT1());
		}
		return ens;
	}
	
	public void suppress(Border aSuppr) {
		for(Edge e : aSuppr.edgeSet) {
			this.edgeSet.remove(e);
			this.pointSet.remove(e.getP1());
			this.pointSet.remove(e.getP2());
		}
	}
	
	public void returnNeighbours(Border f, Edge e) {
		if(!f.edgeSet.contains(e)) {
			f.add(e);
			for(Edge edg : this.edgeSet) {
				if(edg.contains(e.getP1()) || edg.contains(e.getP2()))
					this.returnNeighbours(f, edg);
			}
		}	
	}
	
	public double zAverage(){
		double zAverage = 0;
		for(Point p : pointSet){
			zAverage += p.getZ();
		}
		return zAverage/pointSet.size();
	}

	public double xAverage(){
		double xAverage = 0;
		for(Point p : pointSet){
			xAverage += p.getX();
		}
		return xAverage/pointSet.size();
	}

	public double yAverage(){
		double yAverage = 0;
		for(Point p : pointSet){
			yAverage += p.getY();
		}
		return yAverage/pointSet.size();
	}

	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getX() > xMaxi){
				xMaxi = p.getX();
			}
		}
		return xMaxi;
	}

	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getX() < xMini){
				xMini = p.getX();
			}
		}
		return xMini;
	}
	
	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getY() > yMaxi){
				yMaxi = p.getY();
			}
		}
		return yMaxi;
	}

	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getY() < yMini){
				yMini = p.getY();
			}
		}
		return yMini;
	}
	
	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getZ() > zMaxi){
				zMaxi = p.getZ();
			}
		}
		return zMaxi;
	}

	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Point p : pointSet){
			if (p.getZ() < zMini){
				zMini = p.getZ();
			}
		}
		return zMini;
	}
}
