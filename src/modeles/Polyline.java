package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

public class Polyline {

	private ArrayList<Point> pointList;
	private ArrayList<Edge> edgeList;

	private int ID;
	private static int ID_current = 0;
	private static final long serialVersionUID = 1L;

	//FIXME : :)
	public Point toDelete;

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
		this.edgeList = new ArrayList<Edge>();
		this.pointList = new ArrayList<Point>();
		this.ID = ++ID_current;
	}


	public ArrayList<Edge> getEdgeList() {
		return this.edgeList;
	}

	public ArrayList<Point> getPointList() {
		return this.pointList;
	}

	public int getID() {
		return this.ID;
	}

	public void setID(int iD) {
		this.ID = iD;
	}


	public void add(Edge e) {
		if(!this.edgeList.contains(e)) {
			this.edgeList.add(e);
		}
		this.add(e.getP1());
		this.add(e.getP2());
	}

	public void add(Point p) {
		if(!this.pointList.contains(p))
			this.pointList.add(p);
	}

	public void addAll(List<Edge> l) {
		for(Edge e : l) {
			this.add(e);
		}
	}

	public Edge getOne() {
		return this.edgeList.iterator().next();
	}

	//FIXME : it doesn't remove the points !
	public void remove(Polyline aSuppr) {
		for(Edge e : aSuppr.edgeList) {
			this.edgeList.remove(e);
		}
	}

	//FIXME : it doesn't remove the points !
	public void remove(Edge e) {
		this.edgeList.remove(e);
	}


	public boolean contains(Point p) {
		return this.pointList.contains(p);
	}

	public boolean contains(Edge e) {
		return this.edgeList.contains(e);
	}

	public int edgeSize() {
		return this.edgeList.size();
	}

	public int pointSize() {
		return this.pointList.size();
	}

	public boolean isEmpty() {
		return edgeList.isEmpty();
	}


	public double xAverage(){
		double xAverage = 0;
		for(Point p : this.pointList){
			xAverage += p.getX();
		}
		return xAverage/this.pointList.size();
	}

	public double yAverage(){
		double yAverage = 0;
		for(Point p : this.pointList){
			yAverage += p.getY();
		}
		return yAverage/this.pointList.size();
	}

	public double zAverage(){
		double zAverage = 0;
		for(Point p : this.pointList){
			zAverage += p.getZ();
		}
		return zAverage/this.pointList.size();
	}


	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getX() > xMaxi){
				xMaxi = p.getX();
			}
		}
		return xMaxi;
	}

	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getX() < xMini){
				xMini = p.getX();
			}
		}
		return xMini;
	}

	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getY() > yMaxi){
				yMaxi = p.getY();
			}
		}
		return yMaxi;
	}

	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getY() < yMini){
				yMini = p.getY();
			}
		}
		return yMini;
	}

	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getZ() > zMaxi){
				zMaxi = p.getZ();
			}
		}
		return zMaxi;
	}

	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getZ() < zMini){
				zMini = p.getZ();
			}
		}
		return zMini;
	}


	public double xLengthAverage() {
		double xLengthAve = 0;
		for(Edge e : this.edgeList) {
			xLengthAve += Math.abs(e.getP1().getX() - e.getP2().getX());
		}
		return xLengthAve/(double)this.edgeList.size();
	}

	public double yLengthAverage() {
		double yLengthAve = 0;
		for(Edge e : this.edgeList) {
			yLengthAve += Math.abs(e.getP1().getY() - e.getP2().getY());
		}
		return yLengthAve/(double)this.edgeList.size();
	}	

	public double zLengthAverage() {
		double zLengthAve = 0;
		for(Edge e : this.edgeList) {
			zLengthAve += Math.abs(e.getP1().getZ() - e.getP2().getZ());
		}
		return zLengthAve/(double)this.edgeList.size();
	}

	public double lengthAverage() {
		return Math.pow(Math.pow(this.xLengthAverage(), 2) 
				+ Math.pow(this.yLengthAverage(), 2) 
				+ Math.pow(this.zLengthAverage(), 2)
				, 0.5);
	}


	public Polyline xBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for(Edge e : this.edgeList) {
			if(			e.getP1().getX() > Math.min(m1, m2) && e.getP1().getX() < Math.max(m1, m2)
					&& 	e.getP2().getX() > Math.min(m1, m2) && e.getP2().getX() < Math.max(m1, m2))
				b.add(e);				
		}
		return b;
	}

	public Polyline yBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for(Edge e : this.edgeList) {
			if(			e.getP1().getY() > Math.min(m1, m2) && e.getP1().getY() < Math.max(m1, m2)
					&& 	e.getP2().getY() > Math.min(m1, m2) && e.getP2().getY() < Math.max(m1, m2))
				b.add(e);				
		}
		return b;
	}

	public Polyline zBetween(double m1, double m2) {
		Polyline b = new Polyline();
		for(Edge e : this.edgeList) {
			if(			e.getP1().getZ() > Math.min(m1, m2) && e.getP1().getZ() < Math.max(m1, m2)
					&& 	e.getP2().getZ() > Math.min(m1, m2) && e.getP2().getZ() < Math.max(m1, m2))
				b.add(e);				
		}
		return b;
	}


	public Point zMaxPoint() {
		Point point = null;
		if(this.pointList.isEmpty())
			throw new InvalidParameterException("Empty border !");
		double zMax = Double.NEGATIVE_INFINITY;
		for (Point p : this.pointList){
			if (p.getZ() > zMax){
				zMax = p.getZ();
				point = p;
			}
		}
		return point;
	}


	public double distance() {
		double distance = 0;
		for(Edge e : this.edgeList) {
			distance += e.distance();
		}
		return distance;
	}


	public Mesh returnMesh() {
		//FIXME : si tous les Edge n'ont pas de triangles associés, renvoyer vers l'autre méthode.
		Mesh ens = new Mesh();
		for(Edge e : this.edgeList) {
			if(!e.getTriangleList().isEmpty())
				ens.addAll(e.getTriangleList());
		}
		return ens;
	}

	//TODO : delete this shit method !
	public Mesh returnMeshPoints() {
		Mesh ens = new Mesh();

		Point centroid = new Point(this.xAverage(), this.yAverage(), this.zAverage());
		Vector3d normal = new Vector3d(0, 0, 1);

		//		Point before = this.pointList.get(this.pointSize() - 1);

		for(Point p : this.pointList) {
			Point before = p;
			ens.add(new Triangle(centroid, before, p, new Edge(centroid, before), new Edge(before, p), new Edge(p, centroid), normal));
			//			before = p;
		}

		return ens;
	}

	//FIXME : make a better method
	public Mesh returnMeshShit() {
		Mesh ens = new Mesh();

		Point centroid = new Point(this.xAverage(), this.yAverage(), this.zAverage());
		Vector3d normal = new Vector3d(0, 0, 1);

		Point before = this.pointList.get(this.pointSize() - 1);

		for(Point p : this.pointList) {
			ens.add(new Triangle(centroid, before, p, new Edge(centroid, before), new Edge(before, p), new Edge(p, centroid), normal));
			before = p;
		}

		return ens;
	}

	public void changeBase(double[][] matrix) {
		if(matrix == null) {
			throw new InvalidParameterException();
		}

		Polyline line = new Polyline(this.edgeList);
		for(Point p : line.pointList) {
			p.changeBase(matrix);
		}

		this.edgeList.clear();
		this.pointList.clear();
		this.addAll(line.edgeList);
	}

	public int getNumNeighbours(Point p) {
		int counter = 0;
		for(Edge e : this.edgeList) {
			if(e.contains(p)) {
				counter ++;
			}
		}
		return counter;
	}

	public ArrayList<Edge> getNeighbours(Point p) {
		if(p == null) {
			throw new InvalidParameterException();
		}
		ArrayList<Edge> list = new ArrayList<Edge>();
		for(Edge e : this.edgeList) {
			if(e.contains(p)) {
				list.add(e);
			}
		}
		return list;
	}

	//FIXME : @Test
	public void order() {
		//TODO : vérification que chq Edge a bien 2 voisins : ni plus, ni moins !
		Polyline ret = new Polyline();

		Edge first = this.getOne();
		Point p = first.getP1();
		Edge e = first.returnNeighbour(p, this);
		p = e.returnOther(p);

		while(e!= first) {
			ret.add(e);
			e = e.returnNeighbour(p, this);
			p = e.returnOther(p);
		}
		ret.add(e);

		this.edgeList.clear();
		this.pointList.clear();
		this.addAll(ret.getEdgeList());
	}

	//FIXME : it destroys the former Polyline and all the Points...
	public Polyline zProjection(double z) {
		Polyline line = new Polyline();
		line.addAll(this.edgeList);

		for(Point p : line.pointList) {
			p.setZ(z);
		}

		return line;
	}	

	//We still consider that we are in the plane (x,y)
	public Polyline determinateSingularPoints(double error) {
		Polyline singularPoints = new Polyline();

		//We take a point, and we follow the line until we find a segment with angle change.
		Edge first = this.getOne();
		first = followTheFramedLine(first, first.getP2(), error, first);
		singularPoints.add(first);
		Edge e = first;

		//Then we record all the points where there is a angle change.
		do {
			//We determinate the point to know the direction to take...
			e = followTheFramedLine(e, this.toDelete, error, first);
			singularPoints.add(e);
		}
		while(e != first);

		return singularPoints;
	}

	//We still consider that we are in the plane (x,y)
	//Almost static method... Maybe to put in Edge ?
	public boolean areWeInTheTwoLinesOrNot(Point p1, Point p2, Point p3, double error) {
		double a, b, c, cPlus, cMinus;

		//We calculate the equation of the segment, and of the two lines parallels to it and which frame the line
		if(p1.getY() == p2.getY()) {
			a = 0;
			b = 1;
			c = - p1.getX();
		}
		else {
			a = 1;
			b = (p1.getX() - p2.getX())/(p2.getY() - p1.getY());
			c = (- p1.getX() * p2.getY() + p2.getX() * p1.getY())/(p2.getY() - p1.getY());
		}

		cPlus = -c + error;
		cMinus = -c - error;

		return (a * p3.getX() + b * p3.getY() < cPlus 
				&& a * p3.getX() + b * p3.getY() > cMinus);
	}

	//We still consider that we are in the plane (x,y)
	//FIXME : delete the Edge stop
	public Edge followTheFramedLine(Edge first, Point p2, double error, Edge stop) {

		Point p1 = first.returnOther(p2);

		//LOOK : We can take the second segment in the list : it's supposed to be ordered !
		Edge e2 = first.returnNeighbour(p2, this);
		Point p3 = e2.returnOther(p2);

		Edge eMain = first;
		Edge eNext = e2;

		while(areWeInTheTwoLinesOrNot(p1, p2, p3, error) && eNext != stop) {
			//Then the point p3 is almost aligned with the other points...
			//We add the two segments, and we do that with a third segment...
			eMain = eMain.compose(eNext, p2);
			eNext = eNext.returnNeighbour(p3, this);
			p1 = eMain.returnOther(p3);
			p2 = p3;
			p3 = eNext.returnOther(p2);
		}
		//When we're here, it means we found some Point which was not in the frame...

		this.toDelete = p3;
		return eNext;
	}
}