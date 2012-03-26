package fr.nantes1900.utils;

import java.util.List;

/**
 * Interface used in the CityGMLWriter. For now, only impmemented by
 * Polygon and Triangle.
 * 
 * @author Nicolas REY
 */
public interface IPointsAsCoordinates {

    /**
     * Return a list of coordinates following the pattern :
     * x1 y1 z1 x2 y2 z2...
     * @return The list of coordinates.
     */
    List<Double> getPointsAsCoordinates();
}
