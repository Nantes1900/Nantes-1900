package fr.nantes1900.listener;

import javax.swing.event.EventListenerList;

public class ProgressComputer {

    private static EventListenerList progressListenerList = new EventListenerList();

    private static double progress;

    private static int buildingsNumber;

    private static int stepsNumber;

    private static int trianglesNumber;

    private static int trianglesCounter;
    private static int buildingsCounter;
    private static int stepsCounter;

    public static double getProgress() {
        return ProgressComputer.progress;
    }

    public static final void
            addProgressListener(final ProgressListener listener) {
        ProgressComputer.progressListenerList.add(ProgressListener.class,
                listener);
    }

    public static final void removeProgressListener(
            final ProgressListener listener) {
        ProgressComputer.progressListenerList.remove(ProgressListener.class,
                listener);
    }

    public static void computeProgress() {
        ProgressComputer.progress = (buildingsCounter + (stepsCounter + ((double) trianglesCounter / (double) trianglesNumber))
                / stepsNumber)
                / buildingsNumber;
        fireProgress(ProgressComputer.progress);
    }

    private static void fireProgress(double progress2) {
        for (ProgressListener listener : progressListenerList
                .getListeners(ProgressListener.class)) {
            listener.updateProgress(ProgressComputer.progress);
        }
    }

    public static void initTrianglesCounter() {
        ProgressComputer.trianglesCounter = 0;
    }

    public static void setBuildingsNumber(int buildingsNumberIn) {
        ProgressComputer.buildingsNumber = buildingsNumberIn;
    }

    public static void setStepsNumber(int stepsNumberIn) {
        ProgressComputer.stepsNumber = stepsNumberIn;
    }

    public static void setTrianglesNumber(int trianglesNumberIn) {
        ProgressComputer.trianglesNumber = trianglesNumberIn;
    }

    public static void incBuildingsCounter() {
        ProgressComputer.buildingsCounter++;
    }

    public static void incStepsCounter() {
        ProgressComputer.stepsCounter++;
    }

    public static void incTrianglesCounter(int size) {
        ProgressComputer.trianglesCounter += size;
        ProgressComputer.computeProgress();
    }

    public static void initBuildingsNumber() {
        ProgressComputer.buildingsCounter = 0;
    }

    public static void initStepsNumber() {
        ProgressComputer.stepsCounter = 0;
    }
}
