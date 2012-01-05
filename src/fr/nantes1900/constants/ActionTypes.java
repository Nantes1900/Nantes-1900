package fr.nantes1900.constants;

/**
 * Contains some constants describing the actions the user can do in the model.
 * @author Daniel Lefèvre
 */
public final class ActionTypes {

    /**
     * To convert the things selected to building.
     */
    public static final int TURN_TO_BUILDING = 0;
    /**
     * To convert the things selected to ground.
     */
    public static final int TURN_TO_GROUND = 1;
    /**
     * To convert the things selected to wall.
     */
    public static final int TURN_TO_WALL = 2;
    /**
     * To convert the things selected to roof.
     */
    public static final int TURN_TO_ROOF = 3;
    /**
     * To convert the things selected to noise.
     */
    public static final int TURN_TO_NOISE = 4;
    /**
     * To remove the things selected.
     */
    public static final int REMOVE = 5;
    /**
     * To merge some surfaces.
     */
    public static final int MERGE = 6;
    /**
     * To add some neighbours to a surface.
     */
    public static final int ADD_NEIGHBOURS = 7;
    /**
     * To remove some neighbours of a surface.
     */
    public static final int REMOVE_NEIGHBOURS = 8;
    /**
     * To change the order of the neighbours of a surface : to make it move UP.
     */
    public static final int UP_NEIGHBOUR = 9;
    /**
     * To change the order of the neighbours of a surface : to make it move
     * DOWN.
     */
    public static final int DOWN_NEIGHBOUR = 10;

    /**
     * Empty constructor.
     */
    private ActionTypes() {
    }
}
