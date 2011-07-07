package modeles;

import utils.MatrixMethod;

public class Point {
	
	private double x;
	private double y;
	private double z;
	
//	private ArrayList<Edge> edgeList;
	
	public Point() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
//		edgeList = new ArrayList<Edge>();
	}
	
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
		this.z = p.z;
//		edgeList = new ArrayList<Edge>(p.edgeList);
	}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
//		edgeList = new ArrayList<Edge>();
	}
	
	public double getX() {return x;}
	
	public double getY() {return y;}
	
	public double getZ() {return z;}
		
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

//	public void addEdge(Edge e) {
//		if(!edgeList.contains(e))
//			edgeList.add(e);
//	}
	
	public String toString() {
		return new String("(" + x + ", " + y + ", " + z + ")");
	}
	public double distance(Point p) {
		return Math.pow(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2) + Math.pow(p.z - z, 2), 0.5);
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Point other = (Point) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	public void changeBase(double[][] matrix){
		double[] coords = {this.x, this.y, this.z};
		double[] goodCoords = MatrixMethod.changeBase(coords, matrix);
		this.x = goodCoords[0];
		this.y = goodCoords[1];
		this.z = goodCoords[2];
	}
}
