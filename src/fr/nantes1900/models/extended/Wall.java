package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;

public class Wall extends Surface
{
    public Wall(Mesh m)
    {
        super(m);
    }

    public Wall(Polygon p)
    {
        super(p);
    }
}
