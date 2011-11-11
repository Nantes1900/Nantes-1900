package fr.nantes1900.control;

import javax.vecmath.Vector3d;

import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet.UnimplementedException;
import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;

public class BuildingsIsletController {

    private AbstractBuildingsIslet islet;
    private IsletSelectionController parentController;
    private Universe3DController u3DController;

    public BuildingsIsletController(
            IsletSelectionController isletSelectionController,
            Universe3DController universe3DControllerIn) {
        this.parentController = isletSelectionController;
        this.u3DController = universe3DControllerIn;
    }

    public AbstractBuildingsIslet getIslet() {
        return this.islet;
    }

    public IsletSelectionController getIsletSelectionController() {
        return this.parentController;
    }

    public IsletSelectionController getParentController() {
        return this.parentController;
    }

    public Universe3DController getU3dcontroller() {
        return this.u3DController;
    }

    private void incProgression() {
        this.islet.progression++;
    }

    public void launchNextTreatment() {
        switch (this.islet.progression) {
        case 0:
            try {
                this.islet.launchTreatment0();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 1:
            try {
                this.islet.launchTreatment1();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 2:
            try {
                this.islet.launchTreatment2();
            } catch (UnimplementedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            break;
        case 3:
            this.islet.launchTreatment3();
            break;
        case 4:
            this.islet.launchTreatment4();
            break;
        case 5:
            this.islet.launchTreatment6();
            break;
        case 6:
            this.islet.launchTreatment7();
            break;
        case 7:
            this.islet.launchTreatment8();
            break;
        case 8:
            this.islet.launchTreatment9();
            break;
        }
        this.incProgression();
        this.display();
    }

    public void display() {
        this.u3DController.getUniverse3DView().clearAllMeshes();

        switch (this.islet.progression) {
        case 0:
            this.viewStep0();
            break;

        case 1:
            this.viewStep1();
            break;

        case 2:
            this.viewStep2();
            break;

        case 3:
            this.viewStep3();
            break;

        case 4:
            this.viewStep4();
            break;

        case 5:
            this.viewStep5();
            break;

        case 6:
            this.viewStep6();
            break;

        case 7:
            this.viewStep7();
            break;

        case 8:
            this.viewStep8();
            break;
        }
    }

    public void setIslet(AbstractBuildingsIslet isletIn) {
        this.islet = isletIn;
    }

    public void setIsletSelectionController(
            IsletSelectionController isletSelectionControllerIn) {
        this.parentController = isletSelectionControllerIn;
    }

    public void
            setParentController(IsletSelectionController parentControllerIn) {
        this.parentController = parentControllerIn;
    }

    public void setUniverse3DController(Universe3DController u3dcontrollerIn) {
        this.u3DController = u3dcontrollerIn;
    }

    public void useGravityNormalAsGroundNormal() {
        this.islet.setGroundNormal(this.islet.getGravityNormal());
    }

    /**
     * View before every treatment.
     */
    public void viewStep0() {
        this.u3DController.getUniverse3DView().addMesh(
                new MeshView(this.islet.getInitialTotalMesh()));
    }

    /**
     * View after the base change.
     */
    public void viewStep1() {
        this.u3DController.getUniverse3DView().addMesh(
                new MeshView(this.islet.getInitialTotalMesh()));
    }

    /**
     * View after the separation between ground and building.
     */
    public void viewStep2() {
        this.u3DController.getUniverse3DView().addMesh(
                new MeshView(this.islet.getInitialBuilding()));
        this.u3DController.getUniverse3DView().addMesh(
                new MeshView(this.islet.getInitialGround()));
    }

    /**
     * View after the separation between buildings.
     */
    public void viewStep3() {
        this.u3DController.getUniverse3DView().addMesh(
                new MeshView(this.islet.getInitialGround()));

        for (Building building : this.islet.getBuildings()) {
            this.u3DController.getUniverse3DView().addMesh(
                    new MeshView(building.getInitialTotalMesh()));
        }
    }

    /**
     * After carving the little walls.
     */
    public void viewStep4() {
        // Not implemented for now.
    }

    /**
     * After SeparationWallRoof
     */
    public void viewStep5() {
        for (Building building : this.islet.getBuildings()) {
            this.u3DController.getUniverse3DView().addMesh(
                    new MeshView(building.getInitialWall()));
            this.u3DController.getUniverse3DView().addMesh(
                    new MeshView(building.getInitialRoof()));
        }
    }

    /**
     * After SeparationWallsAndSeparationRoofs
     */
    public void viewStep6() {
        for (Building building : this.islet.getBuildings()) {
            for (Surface wall : building.getWalls()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After DeterminateNeighbours
     */
    public void viewStep7() {
        for (Building building : this.islet.getBuildings()) {
            for (Surface wall : building.getWalls()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After SortNeighbours
     */
    public void viewStep8() {
        for (Building building : this.islet.getBuildings()) {
            for (Surface wall : building.getWalls()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs()) {
                this.u3DController.getUniverse3DView().addMesh(
                        new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After step 8 : SimplificationSurfaces
     */
    public void viewBeforeStep9() {
        for (Building building : this.islet.getBuildings()) {
            for (Surface wall : building.getWalls()) {
                this.u3DController.getUniverse3DView().addPolygon(
                        new PolygonView(wall.getPolygone()));
            }
            for (Surface roof : building.getRoofs()) {
                this.u3DController.getUniverse3DView().addPolygon(
                        new PolygonView(roof.getPolygone()));
            }
        }
    }

    public Vector3d computeNormalWithTrianglesSelected() {
        Mesh mesh = new Mesh(this.u3DController.getTrianglesSelected());
        return mesh.averageNormal();
    }

    public void setGroundNormal(Vector3d groundNormal) {
        this.islet.setGroundNormal(groundNormal);
    }
}
