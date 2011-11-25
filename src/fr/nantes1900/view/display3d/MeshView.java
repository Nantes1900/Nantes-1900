package fr.nantes1900.view.display3d;

import java.util.ArrayList;

import javax.media.j3d.GeometryArray;
import javax.media.j3d.TriangleArray;
import javax.vecmath.Point2f;
import javax.vecmath.Point3d;
import javax.vecmath.TexCoord2f;
import javax.vecmath.Vector3f;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

/**
 * MeshView is a class extended of the class TriangleArray. It's used to display
 * all the triangles of the mesh.
 * 
 * @author Nicolas Bouillon Siju Wu
 */
public class MeshView extends TriangleArray {

	/**
	 * Generate a list to save all the TriangleView.
	 */
	private ArrayList<TriangleView> trianglesViewList;

	/**
	 * The center of the mesh.
	 */
	private Point centroid;

	/**
	 * The mesh of the things displayed.
	 */
	private Mesh mesh;

	/**
	 * To indicate if the mesh is selected.
	 */
	private boolean meshSelected;

	/**
	 * The method of constructor of the class MeshView
	 * 
	 * @param m
	 *            The mesh of the things displayed.
	 */
	public MeshView(final Mesh m) {
		super(m.size() * 3, GeometryArray.COORDINATES | GeometryArray.COLOR_3
				| GeometryArray.NORMALS | GeometryArray.TEXTURE_COORDINATE_2);

		this.mesh = m;

		this.centroid = m.getCentroid();

		this.meshSelected = false;

		this.trianglesViewList = new ArrayList<>();

		// Add all the triangles in the mesh to the list of triangles.
		int triangleCount = 0;
		for (Triangle triangle : this.mesh) {
			TriangleView triangleView = new TriangleView(triangle);
			this.trianglesViewList.add(triangleView);
			triangle.setTriangleViewIndex(triangleCount);
			triangleCount++;
		}

		this.setCapability(ALLOW_COLOR_WRITE);
		this.setCapability(ALLOW_COLOR_READ);
		this.setCapability(ALLOW_TEXCOORD_READ);
		this.setCapability(ALLOW_TEXCOORD_WRITE);

		int i = 0;

		// Create the triangles to be displayed.
		for (TriangleView triangleView : this.trianglesViewList) {

			triangleView.setSelected(false);

			this.setCoordinate(i, new Point3d(triangleView.getTriangle()
					.getP1().getX(), triangleView.getTriangle().getP1().getY(),
					triangleView.getTriangle().getP1().getZ()));
			this.setCoordinate(i + 1, new Point3d(triangleView.getTriangle()
					.getP2().getX(), triangleView.getTriangle().getP2().getY(),
					triangleView.getTriangle().getP2().getZ()));
			this.setCoordinate(i + 2, new Point3d(triangleView.getTriangle()
					.getP3().getX(), triangleView.getTriangle().getP3().getY(),
					triangleView.getTriangle().getP3().getZ()));

			this.setNormal(i, convertNormal(triangleView.getTriangle()));
			this.setNormal(i + 1, convertNormal(triangleView.getTriangle()));
			this.setNormal(i + 2, convertNormal(triangleView.getTriangle()));

			this.setTextureCoordinate(0, i, new TexCoord2f(0.0f, 1.0f));
			this.setTextureCoordinate(0, i + 1, new TexCoord2f(0.0f, 0.0f));
			this.setTextureCoordinate(0, i + 2, new TexCoord2f(1.0f, 0.0f));

			i = i + 3;
		}

	}

	/**
	 * TODO.
	 * 
	 * @param i
	 * 
	 *            TODO.
	 * @return TODO.
	 */
	public static Vector3f convertNormal(final Triangle triangle) {
		Vector3f normalFloat = new Vector3f(
				(float) triangle.getNormal().getX(), (float) triangle
						.getNormal().getY(), (float) triangle.getNormal()
						.getZ());
		return normalFloat;

	}

	/**
	 * TODO.
	 * 
	 * @param i
	 *            TODO.
	 */
	public final void changeColor(final int i) {
		if (this.trianglesViewList.get(i).isSelected()) {

			this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(1.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
		} else {

			this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(0.0f, 0.0f));
			this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
		}

	}

	/**
	 * Getter.
	 * 
	 * @return the centroid point
	 */
	public final Point getCentroid() {
		return this.centroid;
	}

	/**
	 * TODO.
	 * 
	 * @return this.trianglesViewList. The list of the TriangleView in the mesh.
	 */
	public final ArrayList<TriangleView> getTriangleArray() {
		return this.trianglesViewList;
	}

	/**
	 * Select the TriangleView whose index is i.
	 * 
	 * @param i
	 *            The index of TriangleView to be selected in the MeshView.
	 * 
	 */
	public final void select(final int i) {
		this.trianglesViewList.get(i).setSelected(true);

		this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
		this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(1.0f, 1.0f));
		this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
	}

	/**
	 * TODO.
	 * 
	 * @param i
	 *            The index of the triangle which to be unselected. TODO.
	 */
	public void unselect(int i) {
		this.trianglesViewList.get(i).setSelected(false);

		this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
		this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(0.0f, 0.0f));
		this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
	}

	/**
	 * Select or unselect the TriangleView whose index is i.
	 * 
	 * @param i
	 *            The index of TriangleView in the MeshView.
	 * 
	 */
	public final void selectOrUnselect(final int i) {
		// If the TriangleView is selected,unselect it.
		if (this.trianglesViewList.get(i).isSelected()) {
			this.trianglesViewList.get(i).setSelected(false);

			this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(0.0f, 0.0f));
			this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));
		}
		// If the TriangleView is unselected,select it.
		else {
			this.trianglesViewList.get(i).setSelected(true);

			this.setTextureCoordinate(0, i * 3, new TexCoord2f(0.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 1, new TexCoord2f(1.0f, 1.0f));
			this.setTextureCoordinate(0, i * 3 + 2, new TexCoord2f(1.0f, 0.0f));

		}
	}

	/**
	 * Select the mesh.
	 */
	public void selectMesh() {
		this.meshSelected = true;

	}

	/**
	 * Unselect the mesh.
	 */
	public void unselectMesh() {
		this.meshSelected = false;

	}

	/**
	 * Check if the mesh is selected.
	 * 
	 * @return this.meshSeleted;
	 */
	public boolean getMeshSelectedOrNot() {
		return this.meshSelected;
	}
}
