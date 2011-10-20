package fr.nantes1900.models.extended;

import fr.nantes1900.models.middle.Mesh;
import fr.nantes1900.models.middle.Polygone;
import fr.nantes1900.models.middle.Surface;

public class Roof extends Surface {
    public Roof(Mesh m) {
	super(m);
    }

    public Roof(Polygone p) {
	super(p);
    }
}
