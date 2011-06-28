package modeles;

import java.util.LinkedList;
import java.util.List;

//TODO : enlever cette merde de SuppressWarnings
@SuppressWarnings("unused")

public class Building extends LinkedList<Surface> {

	private static final long serialVersionUID = 1L;
	
	private String type;
	private int ID;
	private static int ID_current = 0;
	
	public Building(List<Surface> l) {
		super(l);
		this.ID = ++ID_current;
	}
	
	public Building() {
		super();
		this.ID = ++ID_current;
	}
}
