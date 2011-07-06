package utils;

import java.util.ArrayList;

import modeles.Edge;
import modeles.Triangle;

public class Case extends ArrayList<Triangle>{

	private static final long serialVersionUID = 1L;

	public void addFace(Triangle f) {
		if(!this.contains(f))
			this.add(f);
	}

//	//TODO : à revoir !
//	public void findNeighbours() {
//		Triangle f, a;
//		for(int k = 0; k < this.size(); k ++) 
//		{
//			f = this.get(k);
////			if(f.getNumVoisins() < 3) 
////			{
//				for(int l = k; l < this.size(); l ++)
//				{
//					a = this.get(l);
//					//TODO : changer hasTwoEqualVertices()
//					if(/*(a.getNumVoisins() < 3) && (f.getNumVoisins() < 3) &&*/ (f.hasTwoEqualVerticesWith(a)) && !(f == a))
//					{
//						f.addNeighbour(a);
//						a.addNeighbour(f);
//					}
//				}
////			}
//		}
//	}

//	//When there is two equal Points, the function put them under the same reference.
//	public void updatePoints() {
//		ArrayList<Point> pointList = new ArrayList<Point>();
//
//		for(Triangle t : this) {
//			pointList.add(t.getP0());
//			pointList.add(t.getP1());
//			pointList.add(t.getP2());
//		}
//
//		Point f, a;
//		for(int k = 0; k < pointList.size(); k ++) 
//		{
//			f = pointList.get(k);
//			for(int l = k; l < pointList.size(); l ++)
//			{
//				a = pointList.get(l);
//				if(f.equals(a))
//				{
//					f = a;
//				}
//			}
//		}
//	}

//	public void createEdges() {
//		Edge e;
//		for(Triangle t : this) {
//			e = new Edge(t.getP0(), t.getP1());
//			t.setEdge1(e);
//			e.addTriangle(t);
//
//			e = new Edge(t.getP1(), t.getP2());
//			t.setEdge2(e);
//			e.addTriangle(t);
//
//			e = new Edge(t.getP2(), t.getP0());
//			t.setEdge3(e);
//			e.addTriangle(t);
//		}
//	}
	
//	//TODO : faire findNeighbours() avant.
//	public void updateEdges() {
//		for(Triangle t : this) {
//			if(t.getNeighbour1() != null)
//				t.updateSharedEdgeWith(t.getNeighbour1());
//			if(t.getNeighbour2() != null)
//				t.updateSharedEdgeWith(t.getNeighbour2());
//			if(t.getNeighbour3() != null)
//				t.updateSharedEdgeWith(t.getNeighbour3());
//		}
//	}
}