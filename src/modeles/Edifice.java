package modeles;

import java.util.ArrayList;
import java.util.List;

public class Edifice {

	private ArrayList<Polyline> walls = new ArrayList<Polyline>();
	private ArrayList<Polyline> roofs = new ArrayList<Polyline>();

	public Edifice() {

	}

	public void addWall(Polyline wall) {
		walls.add(wall);
	}

	public void addRoof(Polyline roof) {
		roofs.add(roof);
	}

	public void addWalls(List<Polyline> walls) {
		walls.addAll(walls);
	}

	public void addRoofs(List<Polyline> roofs) {
		roofs.addAll(roofs);
	}

	public List<Polyline> getWalls() {
		return this.walls;
	}

	public List<Polyline> getRoofs() {
		return this.roofs;
	}
}
