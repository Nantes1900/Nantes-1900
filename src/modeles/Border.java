package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import utils.Writer;

//TODO : suppress @SuppressWarnings
@SuppressWarnings("unused")

public class Border {

	private ArrayList<Edge> edgeList;
	private ArrayList<Point> pointList;
	private int ID;
	private static int ID_current = 0;
	private static final long serialVersionUID = 1L;

	public Border(List<Edge> a) {
		edgeList = new ArrayList<Edge>(a);
		for(Edge e : a) {
			pointList.add(e.getP1());
			pointList.add(e.getP2());
		}
		pointList = new ArrayList<Point>();
		this.ID = ++ID_current;
	}

	public Border() {
		edgeList = new ArrayList<Edge>();
		pointList = new ArrayList<Point>();
		this.ID = ++ID_current;
	}

	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public ArrayList<Point> getPointList() {
		return pointList;
	}

	public void setPointList(ArrayList<Point> pointList) {
		this.pointList = pointList;
	}

	public boolean containsPoint(Point p) {
		for(Point point : pointList) {
			if(point.equals(p))
				return true;
		}
		return false;
	}

	//FIXME : A refaire sans doublons...
	public void add(Edge e) {
		edgeList.add(e);

		if(returnIfExists(e.getP1()) == null)
			pointList.add(e.getP1());
		else
			e.setP1(returnIfExists(e.getP1()));		

		if(returnIfExists(e.getP2()) == null)
			pointList.add(e.getP2());
		else
			e.setP2(returnIfExists(e.getP2()));
	}

	public Point returnIfExists(Point p) {
		for(Point point : pointList) {
			if(point.equals(p))
				return point;
		}
		return null;
	}

	public void addAll(List<Edge> l) {
		for(Edge e : l) {
			add(e);
		}
	}

	public int edgeSize() {
		return edgeList.size();
	}

	public double distance() {
		double distance = 0;
		for(Edge e : edgeList) {
			distance += e.distance();
		}
		return distance;
	}

	public Mesh returnMesh() {
		Mesh ens = new Mesh();
		for(Edge e : edgeList) {
			ens.add(e.getTriangleList().get(0));
		}
		return ens;
	}

	public void remove(Border aSuppr) {
		for(Edge e : aSuppr.edgeList) {
			this.edgeList.remove(e);
			this.pointList.remove(e.getP1());
			this.pointList.remove(e.getP2());
		}
	}

	public void remove(Edge e) {
		this.edgeList.remove(e);
		this.pointList.remove(e.getP1());
		this.pointList.remove(e.getP2());
	}

	public void returnNeighbours(Border f, Edge e) {
		if(!f.edgeList.contains(e)) {
			f.add(e);
			
			for(Edge edg : this.edgeList) {
				if(edg.isNeighboor(e)) {
					this.returnNeighbours(f, edg);
				}
			}
		}	
	}

	public double zAverage(){
		double zAverage = 0;
		for(Point p : pointList){
			zAverage += p.getZ();
		}
		return zAverage/pointList.size();
	}

	public double xAverage(){
		double xAverage = 0;
		for(Point p : pointList){
			xAverage += p.getX();
		}
		return xAverage/pointList.size();
	}

	public double yAverage(){
		double yAverage = 0;
		for(Point p : pointList){
			yAverage += p.getY();
		}
		return yAverage/pointList.size();
	}

	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointList){
			if (p.getX() > xMaxi){
				xMaxi = p.getX();
			}
		}
		return xMaxi;
	}

	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Point p : pointList){
			if (p.getX() < xMini){
				xMini = p.getX();
			}
		}
		return xMini;
	}

	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointList){
			if (p.getY() > yMaxi){
				yMaxi = p.getY();
			}
		}
		return yMaxi;
	}

	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Point p : pointList){
			if (p.getY() < yMini){
				yMini = p.getY();
			}
		}
		return yMini;
	}

	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : pointList){
			if (p.getZ() > zMaxi){
				zMaxi = p.getZ();
			}
		}
		return zMaxi;
	}

	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Point p : pointList){
			if (p.getZ() < zMini){
				zMini = p.getZ();
			}
		}
		return zMini;
	}

	public double xLengthAverage() {
		double xLengthAve = 0;
		for(Edge e : edgeList) {
			xLengthAve += Math.abs(e.getP1().getX() - e.getP2().getX());
		}
		return xLengthAve/((double)edgeList.size()*2);
	}

	public double yLengthAverage() {
		double yLengthAve = 0;
		for(Edge e : edgeList) {
			yLengthAve += Math.abs(e.getP1().getY() - e.getP2().getY());
		}
		return yLengthAve/((double)edgeList.size()*2);
	}	

	public double zLengthAverage() {
		double zLengthAve = 0;
		for(Edge e : edgeList) {
			zLengthAve += Math.abs(e.getP1().getZ() - e.getP2().getZ());
		}
		return zLengthAve/((double)edgeList.size()*2);
	}

	public double lengthAverage() {
		return Math.pow(Math.pow(xLengthAverage(), 2) 
				+ Math.pow(yLengthAverage(), 2) 
				+ Math.pow(zLengthAverage(), 2)
				, 0.5);
	}

	public Border yBetween(double m1, double m2) {
		double min = Math.min(m1, m2);
		double max = Math.max(m1, m2);
		Border b = new Border();
		for(Edge e : edgeList) {
			if(			e.getP1().getY() < max && e.getP1().getY() < max
					&& 	e.getP1().getY() > min && e.getP1().getY() > min)
				b.add(e);				
		}
		return b;
	}

	public Point zMaxPoint() {
		Point point = null;
		if(this.pointList.size() == 0)
			throw new InvalidParameterException("Empty border !");
		double zMax = Double.NEGATIVE_INFINITY;
		for (Point p : pointList){
			if (p.getZ() > zMax){
				zMax = p.getZ();
				point = p;
			}
		}
		return point;
	}

	public boolean isEmpty() {
		return pointList.isEmpty();
	}

	public Border changeBase(double[][] matrix) {
		Border line = new Border();
		for(Point p : pointList) {
			line.getPointList().add(p.changeBase(matrix));
			//FIXME : ajouter les Edges
		}
		return line;
	}

	//FIXME : au plus tôt !
	public Mesh buildRoofMesh() {
		Mesh m = new Mesh();
		Point p = new Point(this.xAverage(), this.yAverage(), this.zAverage());
		for(int i = 0; i < pointList.size() - 2; i ++) {
			m.add(new Triangle(pointList.get(i), pointList.get(i + 1), p, new Vector3d(1, 0, 0)));
		}
		return m;
	}
}
