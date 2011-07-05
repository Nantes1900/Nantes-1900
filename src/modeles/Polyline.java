package modeles;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Vector3d;

import utils.Writer;

//TODO : enlever cette merde de SuppressWarnings
@SuppressWarnings("unused")

public class Polyline {
	
	private LinkedList<Point> pointList;
	private int ID;
	private static int ID_current = 0;
	
	public Polyline() {
		pointList = new LinkedList<Point>();
		this.ID = ++ID_current;
	}
	
	public Polyline(List<Point> l) {
		pointList = new LinkedList<Point>();
		this.ID = ++ID_current;
	}
	
	public void add(Point p) {
		pointList.add(p);
	}

	public void addAll(List<Point> l) {
		pointList.addAll(l);
	}
	
	//FIXME : A refaire !
	public Mesh buildMesh() {
		Mesh surface = new Mesh();
		Point downRight = pointList.get(0);
		Point downLeft = pointList.get(1);
		for(int i = 2; i < pointList.size() - 1; i ++) {
			Point p1 = pointList.get(i);
			Point p2 = pointList.get(i+1);
			Point p3, p4, p5;
			p4 = new Point(p1.getX(), p1.getY(), downRight.getZ());
			p5 = new Point(p1.getX(), p2.getY(), downRight.getZ());
			if(p1.getZ() < p2.getZ()) {
				p3 = new Point(p1.getX(), p2.getY(), p1.getZ());
				surface.add(new Triangle(p1, p4, p5, new Vector3d(1, 0, 0)));
				surface.add(new Triangle(p1, p5, p3, new Vector3d(1, 0, 0)));
			}
			else {
				p3 = new Point(p1.getX(), p1.getY(), p2.getZ());
				surface.add(new Triangle(p3, p4, p5, new Vector3d(1, 0, 0)));
				surface.add(new Triangle(p3, p5, p2, new Vector3d(1, 0, 0)));
			}
			surface.add(new Triangle(p1, p3, p2, new Vector3d(1, 0, 0)));
		}
		return surface;
	}

	public Polyline changeBase(double[][] matrix) {
		Polyline line = new Polyline();
		for(Point p : pointList) {
			line.add(p.changeBase(matrix));
		}
		return line;
	}
}
