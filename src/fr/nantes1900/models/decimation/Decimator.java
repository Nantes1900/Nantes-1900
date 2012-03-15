package fr.nantes1900.models.decimation;

import java.util.List;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.coefficients.Decimation;

/**
 * Implements a decimator, following the Quadric Error Metrics algorithm, to
 * decimate a Mesh.
 * @author Daniel Lefèvre
 */
public class Decimator {

    /**
     * The mesh to decimate.
     */
    private MeshDecimation mesh;

    /**
     * Constructor.
     * @param initialMesh
     *            the mesh to decimate.
     */
    public Decimator(final Mesh initialMesh) {
        this.mesh = new MeshDecimation(initialMesh);
    }

    /**
     * Launches the decimation.
     * @return the reference to the mesh (the same as the initial one)
     *         decimated.
     */
    public final Mesh launchDecimation() {

        int size = this.mesh.size();

        // 1. Compute the Qi matrices for each vi.
        this.mesh.computeQiMatrices();

        // 2. Select all valid pairs.
        this.mesh.selectValidPairs();

        // 3. Compute errors for all valid pairs.
        this.mesh.computeErrors();

        while (this.mesh.getEdgeNumber() != 0
                && this.mesh.size() > Decimation.getPercentDecimation() * size) {

            // 4. Sort valid pairs.
            Edge edge = this.mesh.selectMinimalErrorPair();

            // 5. Collapse the pair with the less cost.
            List<Edge> edges = this.mesh.collapseMinusCostPair(edge,
                    this.mesh.computeNewVertex(edge));

            // 6. Recomputes every errors.
            this.mesh.updateMatricesAndErrors(edges);
        }

        return this.mesh;
    }
}
