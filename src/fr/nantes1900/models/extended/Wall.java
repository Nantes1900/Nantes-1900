package fr.nantes1900.models.extended;

import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Polygon;
import fr.nantes1900.models.middle.Surface;

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
