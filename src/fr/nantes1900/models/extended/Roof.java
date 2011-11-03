package fr.nantes1900.models.extended;

import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Polygon;
import fr.nantes1900.models.middle.Surface;

public class Roof extends Surface {
    public Roof(Mesh m) {
	super(m);
    }

    public Roof(Polygon p) {
	super(p);
    }
}
