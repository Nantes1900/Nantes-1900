package modeles;

public class Edge {
	protected Triangle t1;
	protected Triangle t2;
	
	protected Point p1;
	protected Point p2;
	
	public Edge(Triangle t1, Triangle t2) {
		this.t1 = t1;
		this.t2 = t2;
		
		if(t1.contains(t2.p0))
			this.p1 = t2.p0;
		if(t1.contains(t2.p1))
			this.p1 = t2.p1;		
		if(t1.contains(t2.p2))
			this.p1 = t2.p2;
	}
	
	public Edge(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	
	public Edge(Point p1, Point p2, Triangle e1) {
		this(p1, p2);
		t1 = e1;
		t2 = null;
	}
	
	public double distance() {
		return this.p1.distance3D(this.p2);
	}
	
	public boolean contains(Point p) {
		return (this.p1.equals(p) || this.p2.equals(p));
	}
}
