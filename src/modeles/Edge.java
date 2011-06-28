package modeles;

public class Edge {
	private Triangle t1;
	private Triangle t2;

	private Point p1;
	private Point p2;
	
	public Edge(Triangle t1, Triangle t2) {
		this.t1 = t1;
		this.t2 = t2;
		
		if(t1.contains(t2.getP0()))
			this.p1 = t2.getP0();
		if(t1.contains(t2.getP1()))
			this.p1 = t2.getP1();		
		if(t1.contains(t2.getP2()))
			this.p1 = t2.getP2();
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
	
	public Triangle getT1() {
		return t1;
	}

	public void setT1(Triangle t1) {
		this.t1 = t1;
	}

	public Triangle getT2() {
		return t2;
	}

	public void setT2(Triangle t2) {
		this.t2 = t2;
	}

	public Point getP1() {
		return p1;
	}

	public void setP1(Point p1) {
		this.p1 = p1;
	}

	public Point getP2() {
		return p2;
	}

	public void setP2(Point p2) {
		this.p2 = p2;
	}
	
	public double distance() {
		return this.p1.distance(this.p2);
	}
	
	public boolean contains(Point p) {
		return (this.p1.equals(p) || this.p2.equals(p));
	}
}
