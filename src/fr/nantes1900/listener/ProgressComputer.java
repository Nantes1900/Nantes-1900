package fr.nantes1900.listener;

public class ProgressComputer extends Thread {

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

    public static void computeProgress() {
        ProgressComputer.progress = (buildingsCounter / buildingsNumber)
                + (stepsCounter / stepsNumber)
                + (trianglesCounter / trianglesNumber);
        System.out.print(progress);
    }

    public static void init() {
        progress = 0;
    }

    public static void setBuildingNumber(int buildingsNumberIn) {
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
    }
}
