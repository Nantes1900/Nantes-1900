package fr.nantes1900.control;

import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet.UnimplementedException;
import fr.nantes1900.models.middle.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;
import fr.nantes1900.view.display3d.Universe3DView;

public class BuildingsIsletController
{

    protected Universe3DView         universe3D;
    protected AbstractBuildingsIslet islet;
    private IsletSelectionController parentController;
    private Universe3DController     u3dcontroller;

    public BuildingsIsletController(
            IsletSelectionController isletSelectionController)
    {
        this.parentController = isletSelectionController;
    }

    public AbstractBuildingsIslet getIslet()
    {
        return this.islet;
    }

    public IsletSelectionController getIsletSelectionController()
    {
        return this.parentController;
    }

    public IsletSelectionController getParentController()
    {
        return this.parentController;
    }

    public Universe3DController getU3dcontroller()
    {
        return this.u3dcontroller;
    }

    public Universe3DView getUniverse3D()
    {
        return this.universe3D;
    }

    private void incProgression()
    {
        this.islet.progression++;
    }

    public void launchNextTreatment()
    {
        switch (this.islet.progression)
        {
            case 0:
                try
                {
                    this.islet.launchStep0();
                } catch (UnimplementedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            break;
            case 1:
                try
                {
                    this.islet.launchStep1();
                } catch (UnimplementedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            break;
            case 2:
                try
                {
                    this.islet.launchStep2();
                } catch (UnimplementedException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            break;
            case 3:
                this.islet.launchStep3();
            break;
            case 4:
                this.islet.launchStep4();
            break;
            case 5:
                this.islet.launchStep5();
            break;
            case 6:
                this.islet.launchStep6();
            break;
            case 7:
                this.islet.launchStep7();
            break;
            case 8:
                this.islet.launchStep8();
            break;
        }
        this.incProgression();
        this.refresh();
    }

    public void refresh()
    {
        this.universe3D.clearAllMeshes();

        switch (this.islet.progression)
        {
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

    public void setIslet(AbstractBuildingsIslet isletIn)
    {
        this.islet = isletIn;
    }

    public void setIsletSelectionController(
            IsletSelectionController isletSelectionControllerIn)
    {
        this.parentController = isletSelectionControllerIn;
    }

    public void
            setParentController(IsletSelectionController parentControllerIn)
    {
        this.parentController = parentControllerIn;
    }

    public void setUniverse3D(Universe3DView universe3DIn)
    {
        this.universe3D = universe3DIn;
    }

    public void setUniverse3DController(Universe3DController u3dcontrollerIn)
    {
        this.u3dcontroller = u3dcontrollerIn;
    }

    /**
     * View before every treatment.
     */
    public void viewStep0()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialTotalMesh()));
    }

    /**
     * View after the base change.
     */
    public void viewStep1()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialTotalMesh()));
    }

    /**
     * View after the separation between ground and building.
     */
    public void viewStep2()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialBuilding()));
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialGround()));
    }

    /**
     * View after the separation between buildings.
     */
    public void viewStep3()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialGround()));

        for (Building building : this.islet.getBuildings())
        {
            this.universe3D.addTriangleMesh(new MeshView(building
                    .getInitialTotalMesh()));
        }
    }

    /**
     * After carving the little walls.
     */
    public void viewStep4()
    {
        // Not implemented for now.
    }

    /**
     * After SeparationWallRoof
     */
    public void viewStep5()
    {
        for (Building building : this.islet.getBuildings())
        {
            this.universe3D.addTriangleMesh(new MeshView(building
                    .getInitialWall()));
            this.universe3D.addTriangleMesh(new MeshView(building
                    .getInitialRoof()));
        }
    }

    /**
     * After SeparationWallsAndSeparationRoofs
     */
    public void viewStep6()
    {
        for (Building building : this.islet.getBuildings())
        {
            for (Surface wall : building.getWalls())
            {
                this.universe3D.addTriangleMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs())
            {
                this.universe3D.addTriangleMesh(new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After DeterminateNeighbours
     */
    public void viewStep7()
    {
        for (Building building : this.islet.getBuildings())
        {
            for (Surface wall : building.getWalls())
            {
                this.universe3D.addTriangleMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs())
            {
                this.universe3D.addTriangleMesh(new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After SortNeighbours
     */
    public void viewStep8()
    {
        for (Building building : this.islet.getBuildings())
        {
            for (Surface wall : building.getWalls())
            {
                this.universe3D.addTriangleMesh(new MeshView(wall.getMesh()));
            }
            for (Surface roof : building.getRoofs())
            {
                this.universe3D.addTriangleMesh(new MeshView(roof.getMesh()));
            }
        }
    }

    /**
     * After step 8 : SimplificationSurfaces
     */
    public void viewBeforeStep9()
    {
        for (Building building : this.islet.getBuildings())
        {
            for (Surface wall : building.getWalls())
            {
                this.universe3D.addPolygon(new PolygonView(wall.getPolygone()));
            }
            for (Surface roof : building.getRoofs())
            {
                this.universe3D.addPolygon(new PolygonView(roof.getPolygone()));
            }
        }
    }
}
