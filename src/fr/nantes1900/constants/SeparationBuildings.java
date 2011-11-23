package fr.nantes1900.constants;

/**
 * Contains some coefficients used in the algorithms.
 * @author Daniel Lefevre
 */
public final class SeparationBuildings
{

    /**
     * The default value of the coefficient.
     */
    private static final double BLOCK_BUILDING_SIZE_ERROR_DEFAULT = 500;

    /**
     * Coefficient used after the extraction of the building, to determine if a
     * building has enough triangles to be considered as a real building.
     */
    private static double       blockBuildingSize                 = BLOCK_BUILDING_SIZE_ERROR_DEFAULT;

    /**
     * Private constructor.
     */
    private SeparationBuildings()
    {
    }

    /**
     * Getter.
     * @return the block building size
     */
    public static double getBlockBuildingSize()
    {
        return blockBuildingSize;
    }

    /**
     * Setter.
     * @param blockBuildingSizeIn
     *            the new block building size
     */
    public static void setBlockBuildingSize(final double blockBuildingSizeIn)
    {
        SeparationBuildings.blockBuildingSize = blockBuildingSizeIn;
    }
}
