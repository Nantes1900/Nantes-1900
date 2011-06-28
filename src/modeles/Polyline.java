package modeles;

import java.util.ArrayList;
import java.util.List;


public class Polyline {
	protected ArrayList<Point> points;
	
	public Polyline() {
		points = new ArrayList<Point>();
	}
	
	public Polyline(List<Point> l) {
		points = new ArrayList<Point>();
	}
	
	public void add(Point p) {
		points.add(p);
	}

	public void addAll(List<Point> l) {
		points.addAll(l);
	}
}
