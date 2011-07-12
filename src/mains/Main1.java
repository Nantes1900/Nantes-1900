package mains;

import algos.SeparationFloorBuilding;


public class Main1 {

	public static void main(String[] args) {

		SeparationFloorBuilding algo = new SeparationFloorBuilding("Originals/m02 e03.stl");
		algo.setDebugMode();
		algo.apply();

	}
}