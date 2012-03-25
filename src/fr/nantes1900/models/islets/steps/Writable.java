/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package fr.nantes1900.models.islets.steps;

import java.util.List;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;

/**
 * TODO : Javadoc by Nicolas Rey.
 * @author Nicolas Rey
 */
public interface Writable {

    /**
     * TODO : Javadoc by Nicolas Rey.
     * @return TODO : Javadoc by Nicolas Rey.
     */
    List<Building> getBuildings();

    /**
     * TODO : Javadoc by Nicolas Rey.
     * @return TODO : Javadoc by Nicolas Rey.
     */
    Ground getGrounds();
}
