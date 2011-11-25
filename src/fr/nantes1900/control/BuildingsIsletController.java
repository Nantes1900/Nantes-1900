package fr.nantes1900.control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.constants.ActionTypes;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.extended.steps.BuildingStep6;
import fr.nantes1900.models.extended.steps.BuildingStep7;
import fr.nantes1900.models.extended.steps.BuildingStep8;
import fr.nantes1900.models.islets.AbstractIslet;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.ResidentialIslet;
import fr.nantes1900.models.islets.buildings.exceptions.InvalidCaseException;
import fr.nantes1900.models.islets.buildings.exceptions.NullArgumentException;
import fr.nantes1900.models.islets.buildings.exceptions.UnCompletedParametersException;
import fr.nantes1900.models.islets.buildings.steps.BuildingsIsletStep0;
import fr.nantes1900.utils.ParserSTL;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;

/**
 * Implements the controller of a building islet. Used to visualize the islets,
 * to launch the treatments.
 * 
 * @author Daniel
 */
public class BuildingsIsletController {

	/**
	 * The buildings islet containing the model.
	 */
	private AbstractBuildingsIslet islet;
	/**
	 * The islet selection controller, which is the parent of this.
	 */
	private IsletSelectionController parentController;
	/**
	 * The universe 3D controller to interact with the universe 3D.
	 */
	private Universe3DController u3DController;

	/**
	 * Constructor.
	 * 
	 * @param isletSelectionController
	 *            the controller of the islet selection
	 * @param universe3DControllerIn
	 *            the universe 3D controller
	 */
	public BuildingsIsletController(
			final IsletSelectionController isletSelectionController,
			final Universe3DController universe3DControllerIn) {
		this.parentController = isletSelectionController;
		this.u3DController = universe3DControllerIn;
		// LOOK : maybe it would be good to choose between industrial islet and
		// residential islet.
		this.islet = new ResidentialIslet();
	}

	/**
	 * Changes the type of a list of triangles. To call only during the second
	 * step.
	 * 
	 * @param trianglesSelected
	 *            the list of triangles
	 * @param type
	 *            the new type of these triangles
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public final void action2(final List<Triangle> trianglesSelected,
			final int type) throws InvalidCaseException {
		if (type == ActionTypes.TURN_TO_BUILDING) {
			// The user wants these triangles to turn building.
			this.islet.getBiStep2().getInitialGrounds().getMesh()
					.removeAll(trianglesSelected);
			this.islet.getBiStep2().getInitialBuildings()
					.addAll(trianglesSelected);
		} else if (type == ActionTypes.TURN_TO_GROUND) {
			// The user wants these triangles to turn ground.
			this.islet.getBiStep2().getInitialBuildings()
					.removeAll(trianglesSelected);
			this.islet.getBiStep2().getInitialGrounds().getMesh()
					.addAll(trianglesSelected);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Changes the type of a list of triangles (remove them or turn them to
	 * building). To call only during the third step.
	 * 
	 * @param trianglesSelected
	 *            the list of triangles
	 * @param actionType
	 *            the new type of the triangles
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public final void action3(final List<Triangle> trianglesSelected,
			final int actionType) throws InvalidCaseException {
		if (actionType == ActionTypes.REMOVE) {
			for (Building building : this.islet.getBiStep3().getBuildings()) {
				BuildingStep3 buildingStep = building.getbStep3();
				buildingStep.getInitialTotalMesh().removeAll(trianglesSelected);
			}
			this.islet.getBiStep3().getGrounds().getMesh()
					.removeAll(trianglesSelected);
		} else if (actionType == ActionTypes.TURN_TO_BUILDING) {
			this.islet.getBiStep3().getBuildings()
					.add(new Building(new Mesh(trianglesSelected)));
			this.islet.getBiStep3().getNoise().removeAll(trianglesSelected);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Changes the type of a mesh (in noise or in real building). To call only
	 * during the third step.
	 * 
	 * @param mesh
	 *            the mesh
	 * @param actionType
	 *            the new type of the mesh
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public final void action3(final Mesh mesh, final int actionType)
			throws InvalidCaseException {
		if (actionType == ActionTypes.TURN_TO_NOISE) {
			this.islet.getBiStep3().getBuildings()
					.remove(this.returnBuildingContaining3(mesh));
			this.islet.getBiStep3().getNoise().addAll(mesh);
		} else if (actionType == ActionTypes.TURN_TO_BUILDING) {
			this.islet.getBiStep3().getBuildings().add(new Building(mesh));
			this.islet.getBiStep3().getNoise().removeAll(mesh);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Builds the step tree.
	 * 
	 * @return a mutable tree node
	 * @throws InvalidCaseException
	 *             if an invalid case has been called
	 */
	public final DefaultMutableTreeNode returnNode()
			throws InvalidCaseException {
		return this.islet.returnNode();
	}

	/**
	 * Changes the type of a list of triangles. To call only during the fourth
	 * step.
	 * 
	 * @param trianglesSelected
	 *            the list of triangles
	 * @param actionType
	 *            the new type of the triangles
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public final void action4(final List<Triangle> trianglesSelected,
			final int actionType) throws InvalidCaseException {
		Building building = this
				.searchForBuildingContaining4(trianglesSelected);
		BuildingStep4 buildingStep = building.getbStep4();

		if (actionType == ActionTypes.TURN_TO_WALL) {
			buildingStep.getInitialWall().addAll(trianglesSelected);
			buildingStep.getInitialRoof().remove(trianglesSelected);
		} else if (actionType == ActionTypes.TURN_TO_ROOF) {
			buildingStep.getInitialRoof().addAll(trianglesSelected);
			buildingStep.getInitialWall().remove(trianglesSelected);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Searches in the list of buildings for the building which contains the
	 * triangles selected.
	 * 
	 * @param trianglesSelected
	 *            the triangles
	 * @return the building containing <strong>all</strong> the triangles
	 */
	private Building searchForBuildingContaining4(
			final List<Triangle> trianglesSelected) {
		for (Building building : this.islet.getBiStep4().getBuildings()) {
			BuildingStep4 buildingStep = building.getbStep4();
			if (buildingStep.getInitialWall().containsAll(trianglesSelected)
					|| buildingStep.getInitialRoof().containsAll(
							trianglesSelected)) {
				return building;
			}
		}
		return null;
	}

	/**
	 * Changes the type of the list of surfaces. To call onyl during the fifth
	 * step.
	 * 
	 * @param surfacesSelected
	 *            the list of surfaces
	 * @param actionType
	 *            the new type of the surfaces
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 * @throws UnCompletedParametersException
	 *             if the surfaces are not contained in only one building
	 */
	public final void action5(final List<Surface> surfacesSelected,
			final int actionType) throws InvalidCaseException,
			UnCompletedParametersException {
		Building building = this.searchForBuildingContaining5(surfacesSelected);
		BuildingStep5 buildingStep = building.getbStep5();
		if (actionType == ActionTypes.MERGE) {
			if (buildingStep.getWalls().contains(surfacesSelected.get(0))) {
				// It means the meshes selected belong to the walls.
				buildingStep.getWalls().removeAll(surfacesSelected);
				Wall sum = new Wall();
				for (Surface s : surfacesSelected) {
					sum.getMesh().addAll(s.getMesh());
				}
				buildingStep.getWalls().add(sum);
			} else {
				// It means the meshes selected belong to the roofs.
				buildingStep.getRoofs().removeAll(surfacesSelected);
				Roof sum = new Roof();
				for (Surface s : surfacesSelected) {
					sum.getMesh().addAll(s.getMesh());
				}
				buildingStep.getRoofs().add(sum);
			}
		} else if (actionType == ActionTypes.TURN_TO_NOISE) {
			buildingStep.getWalls().removeAll(surfacesSelected);
			buildingStep.getRoofs().removeAll(surfacesSelected);
			for (Surface s : surfacesSelected) {
				buildingStep.getNoise().addAll(s.getMesh());
			}
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Modifies characteristics of the list of surfaces (add neighbour, or
	 * remove). To call only in the sixth step.
	 * 
	 * @param surfacesSelected
	 *            the list of surfaces
	 * @param currentSurface
	 *            the current surface, where to add or remove neighbours
	 * @param actionType
	 *            the type of action to make
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public static final void action6(final List<Surface> surfacesSelected,
			final Surface currentSurface, final int actionType)
			throws InvalidCaseException {
		if (actionType == ActionTypes.ADD_NEIGHBOURS) {
			currentSurface.getNeighbours().addAll(surfacesSelected);
		} else if (actionType == ActionTypes.REMOVE_NEIGHBOURS) {
			currentSurface.getNeighbours().removeAll(surfacesSelected);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Changes the order of the list of neighbours of one surface. To call only
	 * in the seventh step.
	 * 
	 * @param surfaceToMove
	 *            the neighbour whose order has to be changed
	 * @param currentSurface
	 *            the surface we want to change neighbours order of
	 * @param actionType
	 *            the type of action
	 * @throws InvalidCaseException
	 *             if the type of action is not possible in this method
	 */
	public static final void action7(final Surface surfaceToMove,
			final Surface currentSurface, final int actionType)
			throws InvalidCaseException {
		List<Surface> neighbours = currentSurface.getNeighbours();
		if (actionType == ActionTypes.UP_NEIGHBOUR) {
			neighbours
					.set(neighbours.indexOf(surfaceToMove) - 1, surfaceToMove);
		} else if (actionType == ActionTypes.DOWN_NEIGHBOUR) {
			neighbours
					.set(neighbours.indexOf(surfaceToMove) + 1, surfaceToMove);
		} else {
			throw new InvalidCaseException();
		}
	}

	/**
	 * Computes the average normal with the triangles selected in the universe
	 * 3D controller.
	 * 
	 * @return the average normal
	 */
	public final Vector3d computeNormalWithTrianglesSelected() {
		Mesh mesh = new Mesh(this.u3DController.getTrianglesSelected());
		return mesh.averageNormal();
	}

	/**
	 * Dsisplays the set of meshes, considering the progression of the
	 * treatement.
	 */
	public final void display() {
		this.u3DController.getUniverse3DView().clearAllMeshes();

		try {
			switch (this.islet.getProgression()) {
			case AbstractBuildingsIslet.ZERO_STEP:
				this.viewStep0();
				break;
			case AbstractBuildingsIslet.FIRST_STEP:
				this.viewStep1();
				break;
			case AbstractBuildingsIslet.SECOND_STEP:
				this.viewStep2();
				break;
			case AbstractBuildingsIslet.THIRD_STEP:
				this.viewStep3();
				break;
			case AbstractBuildingsIslet.FOURTH_STEP:
				this.viewStep4();
				break;
			case AbstractBuildingsIslet.FIFTH_STEP:
				this.viewStep5();
				break;
			case AbstractBuildingsIslet.SIXTH_STEP:
				this.viewStep6();
				break;
			case AbstractBuildingsIslet.SEVENTH_STEP:
				this.viewStep7();
				break;
			case AbstractBuildingsIslet.EIGHTH_STEP:
				this.viewStep8();
				break;
			default:
				throw new InvalidCaseException();
			}
		} catch (InvalidCaseException e) {
			System.out.println("Big problem");
		}
	}

	/**
	 * Getter.
	 * 
	 * @return the buildings islet
	 */
	public final AbstractBuildingsIslet getIslet() {
		return this.islet;
	}

	/**
	 * Getter.
	 * 
	 * @return the controller of the islet selection
	 */
	public final IsletSelectionController getIsletSelectionController() {
		return this.parentController;
	}

	/**
	 * Getter.
	 * 
	 * @return the parent controller
	 */
	public final IsletSelectionController getParentController() {
		return this.parentController;
	}

	/**
	 * Getter.
	 * 
	 * @return the universe 3D controller
	 */
	public final Universe3DController getU3DController() {
		return this.u3DController;
	}

	/**
	 * Progression incrementation.
	 */
	private void incProgression() {
		this.islet.incProgression();
	}

	/**
	 * Launch the treatment, considering the progression.
	 */
	public final void launchTreatment() {
		try {
			switch (this.islet.getProgression()) {
			case AbstractBuildingsIslet.ZERO_STEP:
				this.islet.launchTreatment0();
				break;
			case AbstractBuildingsIslet.FIRST_STEP:
				this.islet.launchTreatment1();
				break;
			case AbstractBuildingsIslet.SECOND_STEP:
				this.islet.launchTreatment2();
				break;
			case AbstractBuildingsIslet.THIRD_STEP:
				this.islet.launchTreatment3();
				break;
			case AbstractBuildingsIslet.FOURTH_STEP:
				this.islet.launchTreatment4();
				break;
			case AbstractBuildingsIslet.FIFTH_STEP:
				this.islet.launchTreatment5();
				break;
			case AbstractBuildingsIslet.SIXTH_STEP:
				this.islet.launchTreatment6();
				break;
			case AbstractBuildingsIslet.SEVENTH_STEP:
				this.islet.launchTreatment7();
				break;
			default:
				throw new InvalidCaseException();
			}

			this.incProgression();
			this.display();
		} catch (InvalidCaseException e) {
			System.out.println("Invalid case exc.");
			e.printStackTrace();
		} catch (NullArgumentException e) {
			System.out.println("Null argument exc.");
			e.printStackTrace();
		}
	}

	public void abortTreatment() {
		// FIXME
	}

	public void getPreviousTreatment() {
		// FIXME
		System.out.println("LastTreatment");
	}

	/**
	 * Sets the gravity normal in the islet attribute.
	 */
	public final void putGravityNormal() {
		this.islet.setGravityNormal(this.islet.getGravityNormal());
	}

	/**
	 * Reads the file containing the gravity normal and put it in this gravity
	 * normal.
	 * 
	 * @param fileName
	 *            the way of the file of the gravity normal
	 * @throws IOException
	 *             if the file cannot be read
	 */
	public final void readGravityNormal(final String fileName)
			throws IOException {
		ParserSTL parser = new ParserSTL(fileName);
		Mesh mesh = parser.read();
		this.islet.setGravityNormal(mesh.averageNormal());
	}

	/**
	 * Returns the neighbours.
	 * 
	 * @param s
	 * @return
	 */
	public List<Surface> getCaracteristics6(Surface s) {
		return s.getNeighbours();
	}

	/**
	 * Returns the neighbours.
	 * 
	 * @param s
	 * @return
	 */
	public List<Surface> getCaracteristics7(Surface s) {
		return s.getNeighbours();
	}

	/**
	 * Returns the building containing the mesh. To call only in the third step.
	 * 
	 * @param mesh
	 *            the mesh to check
	 * @return the building containing the mesh
	 */
	public final Building returnBuildingContaining3(final Mesh mesh) {
		for (Building building : this.islet.getBiStep3().getBuildings()) {
			BuildingStep3 buildingStep = building.getbStep3();
			if (buildingStep.getInitialTotalMesh() == mesh) {
				return null;
			}
		}
		return null;
	}

	/**
	 * Searches in the list of the buildings the one which contains all the
	 * surfaces selected.
	 * 
	 * @param surfacesSelected
	 *            the list of surfaces
	 * @return the building containing <strong>all</strong> these surfaces
	 * @throws UnCompletedParametersException
	 *             if no building contains all of these surfaces
	 */
	private Building searchForBuildingContaining5(
			final List<Surface> surfacesSelected)
			throws UnCompletedParametersException {
		for (Building building : this.islet.getBiStep5().getBuildings()) {
			BuildingStep5 buildingStep = building.getbStep5();
			if (buildingStep.getWalls().containsAll(surfacesSelected)
					|| buildingStep.getRoofs().containsAll(surfacesSelected)) {
				return building;
			}
		}
		throw new UnCompletedParametersException();
	}

	/**
	 * Setter.
	 * 
	 * @param groundNormal
	 *            the ground normal
	 */
	public final void setGroundNormal(final Vector3d groundNormal) {
		this.islet.setGroundNormal(groundNormal);
	}

	/**
	 * Setter.
	 * 
	 * @param isletIn
	 *            the new buildings islet
	 */
	public final void setIslet(final AbstractBuildingsIslet isletIn) {
		this.islet = isletIn;
	}

	/**
	 * Setter.
	 * 
	 * @param isletSelectionControllerIn
	 *            the controller of the islet selection
	 */
	public final void setIsletSelectionController(
			final IsletSelectionController isletSelectionControllerIn) {
		this.parentController = isletSelectionControllerIn;
	}

	/**
	 * Setter.
	 * 
	 * @param parentControllerIn
	 *            the parent controller
	 */
	public final void setParentController(
			final IsletSelectionController parentControllerIn) {
		this.parentController = parentControllerIn;
	}

	/**
	 * Setter.
	 * 
	 * @param u3dcontrollerIn
	 *            the universe 3D controller
	 */
	public final void setUniverse3DController(
			final Universe3DController u3dcontrollerIn) {
		this.u3DController = u3dcontrollerIn;
	}

	/**
	 * Sets the islet ground normal with the gravity normal.
	 */
	public final void useGravityNormalAsGroundNormal() {
		this.islet.setGroundNormal(this.islet.getGravityNormal());
	}

	/**
	 * Displays the zero step.
	 */
	public final void viewStep0() {
		ArrayList<MeshView> mesh0 = new ArrayList<MeshView>();
		mesh0.add((new MeshView(this.islet.getBiStep0().getInitialTotalMesh())));
		this.getU3DController().getUniverse3DView().addMesh(mesh0);

	}

	/**
	 * Displays the first step.
	 */
	public final void viewStep1() {
		ArrayList<MeshView> mesh1 = new ArrayList<MeshView>();
		mesh1.add((new MeshView(this.islet.getBiStep1()
				.getInitialTotalMeshAfterBaseChange())));
		this.getU3DController().getUniverse3DView().addMesh(mesh1);

	}

	/**
	 * Displays the second step.
	 */
	public final void viewStep2() {
		ArrayList<MeshView> mesh2 = new ArrayList<MeshView>();

		if (!this.islet.getBiStep2().getInitialBuildings().isEmpty()) {
			mesh2.add(new MeshView(this.islet.getBiStep2()
					.getInitialBuildings()));

		} else {
			// TODO
			System.out.println("Initial Buildings empty : error !");
		}
		if (!this.islet.getBiStep2().getInitialGrounds().getMesh().isEmpty()) {
			mesh2.add(new MeshView(this.islet.getBiStep2().getInitialGrounds()
					.getMesh()));

		} else {
			// TODO
			System.out.println("Initial Grounds empty : error !");
		}

		this.getU3DController().getUniverse3DView().addMesh(mesh2);
	}

	/**
	 * Displays the third step.
	 */
	public final void viewStep3() {
		ArrayList<MeshView> mesh3 = new ArrayList<MeshView>();
		if (!this.islet.getBiStep2().getInitialGrounds().getMesh().isEmpty()) {
			mesh3.add(new MeshView(this.islet.getBiStep3().getGrounds()
					.getMesh()));

		} else {
			// TODO
			System.out.println("Initial Grounds empty : error !");
		}

		for (Building building : this.islet.getBiStep3().getBuildings()) {

			BuildingStep3 buildingStep = building.getbStep3();
			mesh3.add(new MeshView(buildingStep.getInitialTotalMesh()));

		}

		this.getU3DController().getUniverse3DView().addMesh(mesh3);
	}

	/**
	 * Displays the fourth step.
	 */
	public final void viewStep4() {
		ArrayList<MeshView> mesh4 = new ArrayList<MeshView>();
		for (Building building : this.islet.getBiStep4().getBuildings()) {
			BuildingStep4 buildingStep = building.getbStep4();
			mesh4.add(new MeshView(buildingStep.getInitialWall()));
			mesh4.add(new MeshView(buildingStep.getInitialRoof()));
			this.getU3DController().getUniverse3DView().addMesh(mesh4);
		}
	}

	/**
	 * Displays the fifth step.
	 */
	public final void viewStep5() {
		ArrayList<MeshView> mesh5 = new ArrayList<MeshView>();
		for (Building building : this.islet.getBiStep5().getBuildings()) {
			BuildingStep5 buildingStep = building.getbStep5();
			for (Surface wall : buildingStep.getWalls()) {
				mesh5.add(new MeshView(wall.getMesh()));

			}
			for (Surface roof : buildingStep.getRoofs()) {
				mesh5.add(new MeshView(roof.getMesh()));

			}
		}

		if (!this.islet.getBiStep5().getNoise().isEmpty()) {
			mesh5.add(new MeshView(this.islet.getBiStep5().getNoise()));

		} else {
			System.out.println("Noise empty : error !");
		}

		this.getU3DController().getUniverse3DView().addMesh(mesh5);
	}

	/**
	 * Displays the sixth step.
	 */
	public final void viewStep6() {
		ArrayList<MeshView> mesh6 = new ArrayList<MeshView>();
		for (Building building : this.islet.getBiStep6().getBuildings()) {
			BuildingStep6 buildingStep = building.getbStep6();
			for (Surface wall : buildingStep.getWalls()) {
				mesh6.add(new MeshView(wall.getMesh()));

			}
			for (Surface roof : buildingStep.getRoofs()) {
				mesh6.add(new MeshView(roof.getMesh()));

			}
		}
		this.getU3DController().getUniverse3DView().addMesh(mesh6);
	}

	/**
	 * Displays the seventh step.
	 */
	public final void viewStep7() {
		ArrayList<MeshView> mesh7 = new ArrayList<MeshView>();
		for (Building building : this.islet.getBiStep7().getBuildings()) {
			BuildingStep7 buildingStep = building.getbStep7();
			for (Surface wall : buildingStep.getWalls()) {
				mesh7.add(new MeshView(wall.getMesh()));

			}
			for (Surface roof : buildingStep.getRoofs()) {
				mesh7.add(new MeshView(roof.getMesh()));

			}
		}
		this.getU3DController().getUniverse3DView().addMesh(mesh7);
	}

	/**
	 * Displays the eighth step.
	 */
	public final void viewStep8() {
		for (Building building : this.islet.getBiStep8().getBuildings()) {
			BuildingStep8 buildingStep = building.getbStep8();
			for (Surface wall : buildingStep.getWalls()) {
				this.getU3DController().getUniverse3DView()
						.addPolygonView(new PolygonView(wall.getPolygone()));
			}
			for (Surface roof : buildingStep.getRoofs()) {
				this.getU3DController().getUniverse3DView()
						.addPolygonView(new PolygonView(roof.getPolygone()));
			}
		}
		// TODO : displays with other colors the surfaces not well computed.
	}

	/**
	 * Parses the file and builds the first step of the BuildingIslet.
	 * 
	 * @param fileName
	 *            the name of the file
	 */
	public final void readFile(final String fileName) {
		this.islet.setBiStep0(new BuildingsIsletStep0(AbstractIslet
				.parseFile(fileName)));
	}
}
