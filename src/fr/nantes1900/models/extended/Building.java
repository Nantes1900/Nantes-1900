package fr.nantes1900.models.extended;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.extended.steps.BuildingStep3;
import fr.nantes1900.models.extended.steps.BuildingStep4;
import fr.nantes1900.models.extended.steps.BuildingStep5;
import fr.nantes1900.models.extended.steps.BuildingStep6;
import fr.nantes1900.models.extended.steps.BuildingStep7;
import fr.nantes1900.models.extended.steps.BuildingStep8;
import fr.nantes1900.models.islets.buildings.AbstractBuildingsIslet;

/**
 * Implements a building as two lists of surfaces : walls and roofs.
 * @author Daniel Lefevre
 */
public class Building
{

    private BuildingStep3          bStep3;
    private BuildingStep4          bStep4;
    private BuildingStep5          bStep5;
    private BuildingStep6          bStep6;
    private BuildingStep7          bStep7;
    private BuildingStep8          bStep8;

    private AbstractBuildingsIslet parentIslet;

    /**
     * TODO.
     * @param mesh
     *            TODO.
     */
    public Building(final Mesh mesh)
    {
        this.bStep3 = new BuildingStep3(mesh);
    }

    public BuildingStep3 getbStep3()
    {
        return this.bStep3;
    }

    public BuildingStep4 getbStep4()
    {
        return this.bStep4;
    }

    public BuildingStep5 getbStep5()
    {
        return this.bStep5;
    }

    public BuildingStep6 getbStep6()
    {
        return this.bStep6;
    }

    public BuildingStep7 getbStep7()
    {
        return this.bStep7;
    }

    public BuildingStep8 getbStep8()
    {
        return this.bStep8;
    }

    public void launchTreatment()
    {
        switch (this.parentIslet.getProgression())
        {
            case 0:
            // TODO : error
            break;
            case 1:
            // TODO : error
            break;
            case 2:
            // TODO : error
            break;
            case 3:
                this.bStep4 = this.bStep3.launchTreatment();
            break;
            case 4:
                this.bStep5 = this.bStep4.launchTreatment();
            break;
            case 5:
                this.bStep6 = this.bStep5.launchTreatment();
            break;
            case 6:
                this.bStep7 = this.bStep6.launchTreatment();
            break;
            case 7:
                this.bStep8 = this.bStep7.launchTreatment();
            break;
            default:
            // TODO : error
            break;
        }
    }

    public DefaultMutableTreeNode returnNode()
    {
        switch (this.parentIslet.getProgression())
        {
            case 0:
                // FIXME : how to do that ?
                return null;
            case 1:
                // TODO : error
                return null;
            case 2:
                // TODO : error
                return null;
            case 3:
                return this.bStep3.returnNode();
            case 4:
                return this.bStep4.returnNode();
            case 5:
                return this.bStep5.returnNode();
            case 6:
                return this.bStep6.returnNode();
            case 7:
                return this.bStep7.returnNode();
            case 8:
                return this.bStep8.returnNode();
            default:
                return null;
        }

    }
}
