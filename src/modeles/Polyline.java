package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Polyline {

	private ArrayList<Point> pointList;
	private ArrayList<Edge> edgeList;

	private int ID;
	private static int ID_current = 0;
	private static final long serialVersionUID = 1L;

	public Polyline(List<Edge> a) {

		this.edgeList = new ArrayList<Edge>(a);
		this.pointList = new ArrayList<Point>();

		for(Edge e : a) {
			this.add(e.getP1());
			this.add(e.getP2());
		}

		this.ID = ++ID_current;
	}

	public Polyline() {
		edgeList = new ArrayList<Edge>();
		pointList = new ArrayList<Point>();
		this.ID = ++ID_current;
	}

	public void add(Point p) {
		if(!this.pointList.contains(p))
			this.pointList.add(p);
	}


	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	public void setEdgeList(ArrayList<Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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
		this.edgeList.add(e);

		this.add(e.getP1());
		this.add(e.getP2());
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

	public void remove(Polyline aSuppr) {
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

	public void returnNeighbours(Polyline f, Edge e) {
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

	public Polyline yBetween(double m1, double m2) {
		double min = Math.min(m1, m2);
		double max = Math.max(m1, m2);
		Polyline b = new Polyline();
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

	//	public Border changeBase(double[][] matrix) {
	//		Border line = new Border();
	//		for(Point p : pointList) {
	//			line.getPointList().add(p.changeBase(matrix));
	//			//FIXME : ajouter les Edges
	//		}
	//		return line;
	//	}

	//FIXME : au plus tôt !
	//	public Mesh buildRoofMesh() {
	//		Mesh m = new Mesh();
	//		Point p = new Point(this.xAverage(), this.yAverage(), this.zAverage());
	//		for(int i = 0; i < pointList.size() - 2; i ++) {
	//			m.add(new Triangle(pointList.get(i), pointList.get(i + 1), p, new Vector3d(1, 0, 0)));
	//		}
	//		return m;
	//	}
	//	
	//	//FIXME : A refaire !
	//	public Mesh buildWallMesh() {
	//		Mesh surface = new Mesh();
	//		Point downRight = pointList.get(0);
	//		Point downLeft = pointList.get(1);
	//		for(int i = 2; i < pointList.size() - 1; i ++) {
	//			Point p1 = pointList.get(i);
	//			Point p2 = pointList.get(i+1);
	//			Point p3, p4, p5;
	//			p4 = new Point(p1.getX(), p1.getY(), downRight.getZ());
	//			p5 = new Point(p1.getX(), p2.getY(), downRight.getZ());
	//			if(p1.getZ() < p2.getZ()) {
	//				p3 = new Point(p1.getX(), p2.getY(), p1.getZ());
	//				surface.add(new Triangle(p1, p4, p5, new Vector3d(1, 0, 0)));
	//				surface.add(new Triangle(p1, p5, p3, new Vector3d(1, 0, 0)));
	//			}
	//			else {
	//				p3 = new Point(p1.getX(), p1.getY(), p2.getZ());
	//				surface.add(new Triangle(p3, p4, p5, new Vector3d(1, 0, 0)));
	//				surface.add(new Triangle(p3, p5, p2, new Vector3d(1, 0, 0)));
	//			}
	//			surface.add(new Triangle(p1, p3, p2, new Vector3d(1, 0, 0)));
	//		}
	//		return surface;
	//	}


	public static Polyline reduce(Polyline line, int numberOfReduction) {
		Polyline ret = new Polyline();
		ArrayList<Point> pointList = line.getPointList();
		int position = 0;

		while(position < pointList.size() - numberOfReduction) {
			//On en prend 1 sur numberOfReduction
			ret.getPointList().add(pointList.get(position));
			position += numberOfReduction;
			//Mettre à jour les Edge
		}

		//		while(position < pointList.size() - numberOfReduction) {
		//			ArrayList<Point> l = new ArrayList<Point>();
		//			for(int i = 0; i < numberOfReduction; i ++) {
		//				l.add(pointList.get(position + i));
		//			}
		//
		//			position += numberOfReduction;
		//
		//			ret.getPointList().add(Algos.average(l));
		//		}

		//		while(position < pointList.size() - 1) {
		//			ArrayList<Point> l = new ArrayList<Point>();
		//			l.add(pointList.get(position));
		//			double length = 0;
		//			
		//			while(length < pace && position < pointList.size() - 1) {
		//				l.add(pointList.get(position + 1));
		//				length += pointList.get(position + 1).distance(pointList.get(position));
		//				position ++;
		//			}
		//			
		//			ret.getPointList().add(Algos.average(l));
		//			//FIXME : ajouter l'edge aussi !
		//		}

		return ret;
	}


	public static Polyline orderBorder(Polyline bound) {
		Polyline oriented = new Polyline();

		//		if(bound.edgeSize() < 2)
		//			throw new InvalidParameterException("Border too short !");

		if(bound.edgeSize() > 1)
		{
			Edge e = bound.getEdgeList().get(0);
			Point first = e.getP1();
			oriented.getEdgeList().add(e);
			oriented.getPointList().add(e.getP1());
			oriented.getPointList().add(e.getP2());
			bound.remove(e);

			Point p = e.getP2();

			int counter = 0;

			do {
				Iterator<Edge> i = bound.getEdgeList().iterator();
				do { e = i.next(); }
				while(i.hasNext() && !e.contains(p));
				//FIXME : si on arrive à la fin de la liste sans avoir trouvé ?
				if(e.getP1() != p) {
					p = e.getP1();
				}
				else {
					p = e.getP2();
				}
				oriented.getEdgeList().add(e);
				oriented.getPointList().add(p);

				counter ++;
			}
			while(p != first && counter <= bound.getPointList().size());

			if(counter == bound.getPointList().size()) {
				System.err.println("Erreur : boucle infinie");
				return null;
			}

			return oriented;
		}
		else
			return bound;
	}

}
