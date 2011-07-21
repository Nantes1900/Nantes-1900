package mains;

import mains.temporary.SeparationFloorBuilding;
import mains.temporary.TreatmentFloors;


//LOOK : this class must be deleted until the end of the project

public class Main1 {

	public static void main(String[] args) {

		SeparationFloorBuilding separationFB = new SeparationFloorBuilding(
				"Originals/m02 e03 - decime.stl");
		separationFB.apply();

		TreatmentFloors treatmentF = new TreatmentFloors(
				separationFB.getFloors(), "street");
		treatmentF.apply();

		// After that, maybe create a new Algo : distinguish buildings...

		// Write everything in the same CityGML file.
	}
}