package algos;

import java.util.ArrayList;

import modeles.Mesh;

public class Algos {

	public static ArrayList<Mesh> blockExtract(Mesh m) {
		ArrayList<Mesh> buildingList = new ArrayList<Mesh>();
		Mesh buildings = new Mesh(m);
		
		int originalSize = buildings.size();
		
		while(!buildings.isEmpty())
		{
			System.out.println("Number of triangles left : " + buildings.size() + " sur : " + originalSize);
			Mesh e = new Mesh();
			buildings.getOne().returnNeighbours(e);			
			buildings.remove(e);
			buildingList.add(e);
		}
		
		return buildingList;
	}
}
