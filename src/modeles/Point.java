package modeles;

import utils.MatrixMethod;

public class Point {
	
	private double _x;
	private double _y;
	private double _z;
	
	public Point() {
		this._x = 0;
		this._y = 0;
		this._z = 0;
	}
	
	public Point(Point p) {
		this._x = p._x;
		this._y = p._y;
		this._z = p._z;
	}
	
	public Point(double x, double y, double z) {
		this._x = x;
		this._y = y;
		this._z = z;
	}
	
	public double getX() {return _x;}
	
	public double getY() {return _y;}
	
	public double getZ() {return _z;}
	
	
	public void setX(double x) {_x = x;}
	
	public void setY(double y) {_y = y;}
	
	public void setZ(double z) {_z = z;}
	
	public String toString() {
		return new String("(" + _x + ", " + _y + ", " + _z + ")");
	}
	public double distance(Point p) {
		return Math.pow(Math.pow(p._x - _x, 2) + Math.pow(p._y - _y, 2) + Math.pow(p._z - _z, 2), 0.5);
	}
	
	public boolean equals(Point p) {
			return ((p._x == _x) && (p._y == _y) && (p._z == _z));
	}

	public Point changeBase(double[][] matrix){
		double[] coords = new double[3];
		coords[0] = this._x; coords[1] = this._y; coords[2] = this._z;
		double[] goodCoords = MatrixMethod.changeBase(coords, matrix);
		return new Point(goodCoords[0], goodCoords[1], goodCoords[2]);
	}
}
