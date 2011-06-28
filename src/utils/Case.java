package utils;

import java.util.ArrayList;

import modeles.Triangle;

public class Case extends ArrayList<Triangle>{

	private static final long serialVersionUID = 1L;
	
	public void addFace(Triangle f) {
		if(!this.contains(f))
			this.add(f);
		f.addBelongedCase(this);
	}
	
	//TODO : à revoir !
	public void findNeighbours() {
		Triangle f, a;
		for(int k = 0; k < this.size(); k ++) 
		{
			f = this.get(k);
			if(f.getNumVoisins() < 3) 
			{
				for(int l = 0; l < this.size(); l ++)
				{
					a = this.get(l);
					if((a.getNumVoisins() < 3) && (f.getNumVoisins() < 3) && (f.hasTwoEqualVertices(a)) && !(f == a))
					{
						f.addNeighbour(a);
						a.addNeighbour(f);
					}
				}
			}
		}
	}
}
