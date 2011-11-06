package fr.nantes1900.view.isletview;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;
import fr.nantes1900.models.middle.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * @author Daniel
 */
public class BuildingsIsletView
{
    protected Universe3DView         universe3D;
    protected AbstractBuildingsIslet islet;

    public BuildingsIsletView()
    {
    }

    public Universe3DView getUniverse3D()
    {
        return this.universe3D;
    }

    public void setUniverse3D(Universe3DView universe3DIn)
    {
        this.universe3D = universe3DIn;
    }

    public void refresh()
    {
        System.out.println("refresh");
        this.universe3D.clearAllMeshes();

        switch (this.islet.progression)
        {
            case 0:
                this.viewBeforeStep0();
            break;

            case 1:
                this.viewBeforeStep1();
            break;

            case 2:
                this.viewBeforeStep2();
            break;

            case 3:
                this.viewBeforeStep3();
            break;

            case 4:
                this.viewBeforeStep4();
            break;

            case 5:
                this.viewBeforeStep5();
            break;

            case 6:
                this.viewBeforeStep6();
            break;

            case 7:
                this.viewBeforeStep7();
            break;

            case 8:
                this.viewBeforeStep8();
            break;
        }
    }

    /**
     * View before every treatment.
     */
    public void viewBeforeStep0()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialTotalMesh()));
    }

    /**
     * View after the base change.
     */
    public void viewBeforeStep1()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialTotalMesh()));
    }

    /**
     * View after the separation between ground and building.
     */
    public void viewBeforeStep2()
    {
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialBuilding()));
        this.universe3D.addTriangleMesh(new MeshView(this.islet
                .getInitialGround()));
    }

    /**
     * View after the separation between buildings.
     */
    public void viewBeforeStep3()
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
    public void viewBeforeStep4()
    {
        // Not implemented for now.
    }

    /**
     * After SeparationWallRoof
     */
    public void viewBeforeStep5()
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
    public void viewBeforeStep6()
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
    public void viewBeforeStep7()
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
    public void viewBeforeStep8()
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
