package fr.nantes1900.constants;

/**
 * Contains some constants describing the actions the user can do in the model.
 * @author Daniel Lefèvre
 */
public final class ActionTypes
{

    /**
     * To convert the things selected to building.
     */
    public static final int TURN_TO_BUILDING = 0;
    /**
     * To convert the things selected to ground.
     */
    public static final int TURN_TO_GROUND   = 1;
    /**
     * To convert the things selected to wall.
     */
    public static final int TURN_TO_WALL     = 2;
    /**
     * To convert the things selected to roof.
     */
    public static final int TURN_TO_ROOF     = 3;
    /**
     * To convert the things selected to noise.
     */
    public static final int TURN_TO_NOISE    = 4;
    /**
     * To remove the things selected.
     */
    public static final int REMOVE           = 4;

    /**
     * Empty constructor.
     */
    private ActionTypes()
    {
    }
}
