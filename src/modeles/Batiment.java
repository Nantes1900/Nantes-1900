package modeles;

import java.util.LinkedList;
import java.util.List;


public class Batiment extends LinkedList<Surface> {

	private static final long serialVersionUID = 1L;
	
	protected String type;
	protected int ID;
	
	public Batiment(List<Surface> l) {
		super(l);
	}
	
	public Batiment() {
		super();
	}
}
