package fr.nantes1900.models.islets.buildings.steps;

import javax.swing.event.EventListenerList;

import fr.nantes1900.listener.ProgressListener;

/**
 * Implements a class computing the progress of a treatment from some static
 * methods.
 * @author Daniel Lefevre
 */
public final class ProgressComputer {

    /**
     * Private constructor.
     */
    private ProgressComputer() {
    }

    /**
     * List of listeners.
     */
    private static EventListenerList progressListenerList = new EventListenerList();

    /**
     * The progress of the treatment.
     */
    private static double progress;

    /**
     * The total number of buildings.
     */
    private static int buildingsNumber;

    /**
     * The total number of steps.
     */
    private static int stepsNumber;

    /**
     * The total number of triangles.
     */
    private static int trianglesNumber;

    /**
     * The number of the current triangle.
     */
    private static int trianglesCounter;

    /**
     * The number of the current building.
     */
    private static int buildingsCounter;
    /**
     * The number of the current step.
     */
    private static int stepsCounter;

    /**
     * Getter.
     * @return the progress
     */
    public static double getProgress() {
        return ProgressComputer.progress;
    }

    /**
     * Adds a listener.
     * @param listener
     *            the listener
     */
    public static void addProgressListener(final ProgressListener listener) {
        ProgressComputer.progressListenerList.add(ProgressListener.class,
                listener);
    }

    /**
     * Removes a listener.
     * @param listener
     *            the listener
     */
    public static void removeProgressListener(final ProgressListener listener) {
        ProgressComputer.progressListenerList.remove(ProgressListener.class,
                listener);
    }

    /**
     * Computes the progress corresponding to the current numbers (triangles,
     * steps, buildings).
     */
    public static void computeProgress() {
        progress = (buildingsCounter + (stepsCounter + ((double) trianglesCounter / (double) trianglesNumber))
                / stepsNumber)
                / buildingsNumber;
        ProgressComputer.fireProgress();
    }

    /**
     * Notifies the listeners that the progress have been updated.
     */
    private static void fireProgress() {
        for (ProgressListener listener : progressListenerList
                .getListeners(ProgressListener.class)) {
            listener.updateProgress(ProgressComputer.progress);
        }
    }

    /**
     * Resets the counter of triangles.
     */
    public static void initTrianglesCounter() {
        ProgressComputer.trianglesCounter = 0;
    }

    /**
     * Setter.
     * @param buildingsNumberIn
     *            the number of buildings
     */
    public static void setBuildingsNumber(final int buildingsNumberIn) {
        ProgressComputer.buildingsNumber = buildingsNumberIn;
    }

    /**
     * Setter.
     * @param stepsNumberIn
     *            the total number of steps
     */
    public static void setStepsNumber(final int stepsNumberIn) {
        ProgressComputer.stepsNumber = stepsNumberIn;
    }

    /**
     * Setter.
     * @param trianglesNumberIn
     *            the total number of triangles
     */
    public static void setTrianglesNumber(final int trianglesNumberIn) {
        ProgressComputer.trianglesNumber = trianglesNumberIn;
    }

    /**
     * Increments the counter of buildings.
     */
    public static void incBuildingsCounter() {
        ProgressComputer.buildingsCounter++;
    }

    /**
     * Increments the counter of steps.
     */
    public static void incStepsCounter() {
        ProgressComputer.stepsCounter++;
    }

    /**
     * Increases the counter of triangles of a number.
     * @param size
     *            the number added
     */
    public static void incTrianglesCounter(final int size) {
        ProgressComputer.trianglesCounter += size;
        ProgressComputer.computeProgress();
    }

    /**
     * Resets the number of buildings.
     */
    public static void initBuildingsCounter() {
        ProgressComputer.buildingsCounter = 0;
    }

    /**
     * Resets the counter of steps.
     */
    public static void initStepsCounter() {
        ProgressComputer.stepsCounter = 0;
    }
}
