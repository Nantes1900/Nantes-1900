package nantes1900.coefficients;

/**
 * Implement class to define some coefficients used in the algorithms.
 * 
 * @author Daniel Lefevre
 */
public final class SeparationTreatmentWallsRoofs {

	// TODO : doc
	// If there is a matrix exception durint the findEdges, then try to increase
	// the wallFactor.
	public static final double ANGLE_WALL_ERROR = 30;
	public static final double ANGLE_ROOF_ERROR = 20;
	public static final double LARGE_ANGLE_ERROR = 50;
	public static final double NORMALTO_ERROR = 0.2;
	public static final double WALL_BLOCK_SIZE_ERROR = 2;
	public static final double ROOF_BLOCK_SIZE_ERROR = 7;

	private SeparationTreatmentWallsRoofs() {
	}
}
