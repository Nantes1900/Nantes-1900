package modeles;

import java.util.ArrayList;
import java.util.List;

//TODO : enlever cette merde de SuppressWarnings
@SuppressWarnings("unused")

public class Polyline {
	
	private ArrayList<Point> points;
	private int ID;
	private static int ID_current = 0;
	
	public Polyline() {
		points = new ArrayList<Point>();
		this.ID = ++ID_current;
	}
	
	public Polyline(List<Point> l) {
		points = new ArrayList<Point>();
		this.ID = ++ID_current;
	}
	
	public void add(Point p) {
		points.add(p);
	}

	public void addAll(List<Point> l) {
		points.addAll(l);
	}
}
