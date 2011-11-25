package fr.nantes1900.constants;

/**
 * Constants used for the characteristics panel.
 * @author Camille, Luc
 */
public abstract class Characteristics
{
    /**
     * Selection mode triangle.
     */
    public static final int    SELECTION_TYPE_TRIANGLE = 0;

    /**
     * Selection mode element.
     */
    public static final int    SELECTION_TYPE_ELEMENT  = 1;
    /**
     * Type building.
     */
    public static final String TYPE_BUILDING           = "Bâtiment";

    /**
     * Type ground.
     */
    public static final String TYPE_GROUND             = "Sol";

    /**
     * Type noise.
     */
    public static final String TYPE_NOISE              = "Bruit";

    /**
     * Type wall.
     */
    public static final String TYPE_WALL               = "Mur";

    /**
     * Type roof.
     */
    public static final String TYPE_ROOF               = "Toit";

    /**
     * Constructor.
     */
    public Characteristics()
    {
    }
}
