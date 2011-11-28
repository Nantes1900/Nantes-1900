package fr.nantes1900.view.display3d;

import fr.nantes1900.models.basis.Triangle;

/**
 * TODO.
 * 
 * @author Daniel
 */
public class TriangleView {

	/**
	 * The triangle tobe displayed.
	 */
	private Triangle triangle;
	/**
	 * TODO.
	 */
	// FIXME : remove this attribute.
	private boolean selected;

	/**
	 * TODO.
	 * 
	 * @param triangleIn
	 *            TODO.
	 */
	public TriangleView(final Triangle triangleIn) {
		this.triangle = triangleIn;
	}

	/**
	 * Get the triangle.
	 * 
	 * @return this.triangle
	 */
	public final Triangle getTriangle() {
		return this.triangle;
	}

	/**
	 * Check if the triangle is selected.
	 * 
	 * @return this.selected 
	 */
	public final boolean isSelected() {
		return this.selected;
	}

	/**
	 * Change the condition of seleciton of this triangle.
	 * 
	 * @param selectedIn The condition of selection of this triangle.
	 *            
	 */
	public final void setSelected(final boolean selectedIn) {
		this.selected = selectedIn;
	}

}
