package fr.nantes1900.listener;

import java.util.EventListener;

import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;

public interface ElementsSelectedListener extends EventListener
{
    public void triangleSelected(Triangle triangleSelected);
    public void polygonSelected(Polygon trianglesSelected);
    public void triangleDeselected(Triangle triangleSelected);
    public void polygonDeselected(Polygon trianglesSelected);
}
