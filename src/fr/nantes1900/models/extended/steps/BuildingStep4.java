package fr.nantes1900.models.extended.steps;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.coefficients.SeparationWallsSeparationRoofs;
import fr.nantes1900.models.exceptions.NullArgumentException;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.models.islets.steps.ProgressComputer;
import fr.nantes1900.utils.Algos;

/**
 * Implements a building step : a state of the building. This step is after the
 * separation between walls and before the separation of each walls and roofs.
 * roofs.
 * @author Daniel Lefèvre
 */
public class BuildingStep4 extends AbstractBuildingStep {

    /**
     * The mesh containg all the walls.
     */
    private Surface initialWallSurface;
    /**
     * The mesh containing all the roofs.
     */
    private Surface initialRoofSurface;
    /**
     * The mesh containing the noise.
     */
    private Surface noise;
    /**
     * The normal to the ground.
     */
    private Vector3d groundNormal;
    /**
     * The grounds.
     */
    private Ground grounds;

    /**
     * The list of walls.
     */
    private List<Wall> walls = new ArrayList<>();

    /**
     * The list of roofs.
     */
    private List<Roof> roofs = new ArrayList<>();

    /**
     * Constructor.
     * @param initialWallIn
     *            the entire walls
     * @param initialRoofIn
     *            the entire roofs
     */
    public BuildingStep4(final Surface initialWallIn,
            final Surface initialRoofIn) {
        this.initialWallSurface = initialWallIn;
        this.initialRoofSurface = initialRoofIn;
    }

    /**
     * Separate the entire roof in many roofs.
     */
    public final void cutRoofs() {
        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList = Algos.blockOrientedExtract(
                this.initialRoofSurface.getMesh(),
                SeparationWallsSeparationRoofs.getRoofAngleError());

        // Considering their size and their orientation, sort the blocks in
        // roofs or noise. If a wall is oriented in direction of the ground,
        // it is not keeped.
        for (final Mesh e : thingsList) {
            if ((e.size() >= SeparationWallsSeparationRoofs.getRoofSizeError())
                    && (e.averageNormal().dot(this.groundNormal) > 0)) {
                this.roofs.add(new Roof(e));
            } else {
                this.noise.getMesh().addAll(e);
            }
        }
    }

    /**
     * Separate the entire wall in many walls.
     */
    public final void cutWalls() {
        // Cut the mesh in parts, considering their orientation.
        final List<Mesh> thingsList = Algos.blockOrientedExtract(
                this.initialWallSurface.getMesh(),
                SeparationWallsSeparationRoofs.getWallAngleError());

        // Considering their size, sort the blocks in walls or noise.
        for (final Mesh e : thingsList) {
            if (e.size() >= SeparationWallsSeparationRoofs.getWallSizeError()) {
                this.walls.add(new Wall(e));
            } else {
                this.noise.getMesh().addAll(e);
            }
        }
    }

    /**
     * Getter.
     * @return the initial roof
     */
    public final Surface getInitialRoofSurface() {
        return this.initialRoofSurface;
    }

    /**
     * Getter.
     * @return the initial wall
     */
    public final Surface getInitialWallSurface() {
        return this.initialWallSurface;
    }

    /*
     * (non-Javadoc)
     * @see
     * fr.nantes1900.models.extended.steps.AbstractBuildingStep#launchProcess ()
     */
    @Override
    public final BuildingStep5 launchProcess() throws NullArgumentException {
        if (this.groundNormal == null || this.grounds == null
                || this.noise == null) {
            throw new NullArgumentException();
        }

        // Inits a computer of progress. Implemented, but not able to display
        // for now.
        ProgressComputer.initStepsCounter();
        ProgressComputer.setStepsNumber(2);

        this.cutWalls();

        ProgressComputer.incStepsCounter();

        this.cutRoofs();

        ProgressComputer.incStepsCounter();

        this.treatNoise();

        this.treatNewNeighbours();

        List<Wall> wallsCopy = new ArrayList<>();
        for (Wall w : this.walls) {
            wallsCopy.add(new Wall(w));
        }
        List<Roof> roofsCopy = new ArrayList<>();
        for (Roof r : this.roofs) {
            roofsCopy.add(new Roof(r));
        }
        return new BuildingStep5(wallsCopy, roofsCopy);
    }

    @Override
    public final DefaultMutableTreeNode returnNode(final int counter) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Bâtiment "
                + counter);

        this.initialWallSurface.setNodeString("Murs");
        root.add(new DefaultMutableTreeNode(this.initialWallSurface));
        this.initialRoofSurface.setNodeString("Toits");
        root.add(new DefaultMutableTreeNode(this.initialRoofSurface));

        return root;
    }

    /**
     * Searches for every surfaces to check if it shares an edge with another
     * surface : then they are neighbours.
     */
    private void searchForNeighbours() {
        final Polygon groundsBounds = this.grounds.getMesh()
                .returnUnsortedBounds();

        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        // First we clear the neighbours.
        for (final Surface m : wholeList) {
            m.getNeighbours().clear();
        }
        // And we clear the neighbours of the grounds.
        this.grounds.getNeighbours().clear();

        final List<Polygon> wholeBoundsList = new ArrayList<>();

        // We compute the bounds to check if they share a common edge.
        for (final Surface m : wholeList) {
            wholeBoundsList.add(m.getMesh().returnUnsortedBounds());
        }

        // Then we check every edge of the bounds to see if some are shared
        // by two meshes. If they do, they are neighbours.
        for (int i = 0; i < wholeBoundsList.size(); i = i + 1) {
            final Polygon polygone1 = wholeBoundsList.get(i);

            for (int j = i + 1; j < wholeBoundsList.size(); j = j + 1) {
                final Polygon polygone2 = wholeBoundsList.get(j);
                if (polygone1.isNeighbour(polygone2)) {
                    wholeList.get(i).addNeighbour(wholeList.get(j));
                }
            }

            if (polygone1.isNeighbour(groundsBounds)) {
                wholeList.get(i).addNeighbour(this.grounds);
            }
        }
    }

    /**
     * Setter.
     * @param groundNormalIn
     *            the normal the ground
     * @param groundsIn
     *            the grounds
     * @param noiseIn
     *            the noise
     */
    public final void setArguments(final Vector3d groundNormalIn,
            final Ground groundsIn, final Surface noiseIn) {
        this.groundNormal = groundNormalIn;
        this.grounds = groundsIn;
        this.noise = noiseIn;
    }

    /**
     * After the noise addition, if some of the walls or some of the roofs are
     * now neighbours (they share an edge) and have the same orientation, then
     * they are added to form only one wall or roof.
     */
    private void treatNewNeighbours() {
        this.searchForNeighbours();

        // After the noise addition, if some of the walls or some of the
        // roofs are now neighbours (they share an edge) and have the same
        // orientation, then they are added to form only one wall or roof.

        // Wall is prioritary : it means that if a roof touch a wall, this
        // roof is added to the wall, and not the inverse.

        final List<Surface> wholeList = new ArrayList<>();
        wholeList.addAll(this.walls);
        wholeList.addAll(this.roofs);

        for (int i = 0; i < wholeList.size(); i = i + 1) {
            final Surface surface = wholeList.get(i);

            final List<Surface> oriented = new ArrayList<>();
            final List<Surface> ret = new ArrayList<>();

            for (final Surface m : wholeList) {
                if (m.getMesh().isOrientedAs(surface.getMesh(),
                        SeparationWallsSeparationRoofs.getMiddleAngleError())) {
                    oriented.add(m);
                }
            }

            surface.returnNeighbours(ret, oriented);

            for (final Surface m : ret) {
                if (m != surface) {
                    surface.getMesh().addAll(m.getMesh());
                    wholeList.remove(m);
                    this.walls.remove(m);
                    this.roofs.remove(m);
                }
            }
        }

        // Clears the list of neighbours to be coherent with the future display.
        for (Surface s : wholeList) {
            s.getNeighbours().clear();
        }
    }

    /**
     * Treats the current noise to add it as much as possible in the roofs or
     * the walls.
     */
    private void treatNoise() {

        List<Surface> wallsOut = new ArrayList<>();
        for (Wall w : this.walls) {
            wallsOut.add(w);
        }

        List<Surface> roofsOut = new ArrayList<>();
        for (Roof r : this.roofs) {
            roofsOut.add(r);
        }

        // Adds the oriented and neighbour noise to the walls.
        Algos.blockTreatOrientedNoise(wallsOut, this.noise.getMesh(),
                SeparationWallsSeparationRoofs.getLargeAngleError());

        // Adds the oriented and neighbour noise to the roofs.
        Algos.blockTreatOrientedNoise(roofsOut, this.noise.getMesh(),
                SeparationWallsSeparationRoofs.getLargeAngleError());

        this.walls.clear();
        for (Surface s : wallsOut) {
            this.walls.add(new Wall(s));
        }

        this.roofs.clear();
        for (Surface s : roofsOut) {
            this.roofs.add(new Roof(s));
        }
    }
}
