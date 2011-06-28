package modeles;

import java.util.ArrayList;

import javax.vecmath.Vector3d;


public class Surface{
	protected Polyline points;
	protected String topologie;
	protected String type;
	protected Vector3d normale;
	protected int ID;
	protected ArrayList<Surface> voisins;
	
	protected static int ID_current = 0;

	public Surface(ArrayList<Point> l, String type, Vector3d normale) {
		points = new Polyline(l);
		this.type = type;
		this.normale = normale;
		this.ID = ++ID_current;
	}
	
	public Surface(ArrayList<Point> l, String topo, String type, Vector3d normale) {
		points = new Polyline(l);
		this.topologie = topo;
		this.type = type;
		this.normale = normale;
		this.ID = ++ID_current;
	}
}
