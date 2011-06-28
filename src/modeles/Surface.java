package modeles;

import java.util.ArrayList;

import javax.vecmath.Vector3d;

//TODO : enlever cette merde de SuppressWarnings
@SuppressWarnings("unused")

public class Surface{
	private Polyline points;
	private String topologie;
	private String type;
	private Vector3d normale;
	private int ID;
	private ArrayList<Surface> voisins;
	
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
