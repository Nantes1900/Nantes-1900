/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.nantes1900.models.islets.steps;

import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Wall;
import java.util.List;

/**
 *
 * @author tagazok
 */
public interface Writable {

    public List<Building> getBuildings();

    public Ground getGrounds();
}
