package fr.nantes1900.models.extended;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Polygon;

public class Roof extends Surface
{

    public Roof(Mesh m)
    {
        super(m);
    }

    public Roof(Polygon p)
    {
        super(p);
    }
}
