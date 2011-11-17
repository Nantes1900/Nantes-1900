package fr.nantes1900.view.display3d;

import fr.nantes1900.models.basis.Triangle;

/**
 * TODO.
 * @author Daniel
 */
public class TriangleView
{

    /**
     * TODO.
     */
    private Triangle triangle;
    /**
     * TODO.
     */
    private boolean selected;

    /**
     * TODO.
     * @param triangleIn
     *            TODO.
     */
    public TriangleView(final Triangle triangleIn)
    {
        this.triangle = triangleIn;
    }

    /**
     * TODO.
     * @return TODO.
     */
    public final Triangle getTriangle()
    {
        return this.triangle;
    }

    /**
     * TODO.
     * @return TODO.
     */
    public final boolean isSelected()
    {
        return this.selected;
    }

    /**
     * TODO.
     * @param selectedIn
     *            TODO.
     */
    public final void setSelected(final boolean selectedIn)
    {
        this.selected = selectedIn;
    }

}
