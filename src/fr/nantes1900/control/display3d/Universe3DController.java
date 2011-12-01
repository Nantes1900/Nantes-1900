package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;
import javax.swing.event.EventListenerList;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.SurfaceView;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * The Universe3DController generates the Universe3DView and contains the pickCanvas.
 * It implements MouseListener, MouseMotionListener to handle users clicks on the 3D view.
 */
/**
 * @author Daniel, Siju, Nicolas
 */
public class Universe3DController implements MouseListener, MouseMotionListener {

	/**
	 * The Universe3DView linked to this controller.
	 */
	private Universe3DView u3DView;
	/**
	 * The canvas used to pick.
	 */
	private PickCanvas pickCanvas;
	/**
	 * The class overwriting MouseRotate to change the roation center.
	 */
	private NewMouseRotate mouseRotate;
	/**
	 * The parent controller.
	 */
	private ElementsSelectedListener parentController;

	private final EventListenerList listeners = new EventListenerList();
	/**
	 * The attribute to know if the U3DView has to display the mesh or the
	 * polygon (a surface is compounded of a mesh and a polygon).
	 */
	private int displayMode;
	public static final int DISPLAY_MESH_MODE = 1;
	public static final int DISPLAY_POLYGON_MODE = 2;
	/**
	 * The attribute to know about the selection mode: triangles or surfaces.
	 */
	private int selectionMode;
	public static final int SELECTION_TRIANGLE_MODE = 1;
	public static final int SELECTION_SURFACE_MODE = 2;
	/**
	 * A constant defining the orientation tolerance (in degrees) when getting
	 * all the triangles oriented as a triangle input.
	 */
	public static final int ORIENTATION_TOLERANCE = 10;
	/**
	 * The list of the triangles currently selected.
	 */
	private List<Triangle> trianglesSelected = new ArrayList<>();
	/**
	 * The list of the meshes selected. This list is used when deselecting a set
	 * of triangles.
	 */
	private List<Mesh> meshesSelected = new ArrayList<>();
	/**
	 * The list of the surfaces currently selected.
	 */
	private List<Surface> surfacesSelected = new ArrayList<>();

	/**
	 * Generates the U3DView and set the display mode to mesh.
	 * 
	 * @param isletSelectionController
	 */
	public Universe3DController(ElementsSelectedListener parentControllerIn) {
		this.parentController = parentControllerIn;
		this.u3DView = new Universe3DView(this);
		this.displayMode = DISPLAY_MESH_MODE;
		this.selectionMode = SELECTION_TRIANGLE_MODE;
	}

	public void addElementsSelectedListener(ElementsSelectedListener listener) {
		this.listeners.add(ElementsSelectedListener.class, listener);
	}

	/**
	 * Changes the rotation center when clicking on a surface.
	 * 
	 * @param surfaceView
	 *            The surfaceView becomming the rotation center
	 */
	public final void changeRotationCenter(final SurfaceView surfaceView) {
		// FIXME handle the case of mesh = null.
		Point center = new Point(
				surfaceView.getMeshView().getCentroid().getX(), surfaceView
						.getMeshView().getCentroid().getY(), surfaceView
						.getMeshView().getCentroid().getZ());
		this.mouseRotate.setCenter(center);
	}

	private void fireTriangleDeselected(Triangle triangleDeselected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleDeselected(triangleDeselected);
		}
	}

	private void fireTriangleSelected(Triangle triangleSelected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleSelected(triangleSelected);
		}
	}

	private void fireSurfaceDeselected(Surface surfaceDeselected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.surfaceDeselected(surfaceDeselected);
		}
	}

	private void fireSurfaceSelected(Surface triangleSelected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.surfaceSelected(triangleSelected);
		}
	}

	/**
	 * Getter.
	 * 
	 * @return the display mode
	 */
	public final int getDisplayMode() {
		return this.displayMode;
	}

	/**
	 * Getter.
	 * 
	 * @return the selection mode
	 */
	public final int getSelectionMode() {
		return this.selectionMode;
	}

	/**
	 * Change the selection mode.
	 * 
	 */
	public final void changeSelectionMode() {
		if (this.selectionMode == SELECTION_TRIANGLE_MODE) {
			this.selectionMode = SELECTION_SURFACE_MODE;
		} else if (this.selectionMode == SELECTION_SURFACE_MODE) {
			this.selectionMode = SELECTION_TRIANGLE_MODE;
		}
	}

	/**
	 * Getter.
	 * 
	 * @return the mouse rotate class
	 */
	public final NewMouseRotate getMouseRotate() {
		return this.mouseRotate;
	}

	/**
	 * Getter.
	 * 
	 * @return the parent controller
	 */
	public final ElementsSelectedListener getParentController() {
		return this.parentController;
	}

	/**
	 * Returns the SurfaceView associated to the surface.
	 * 
	 * @param surface
	 *            the surface to search
	 * @return the surfaceView associated
	 */
	public final SurfaceView getSurfaceViewFromSurface(final Surface surface) {
		for (SurfaceView sView : this.u3DView.getSurfaceViewList()) {
			if (sView.getSurface() == surface) {
				return sView;
			}
		}
		return null;
	}

	/**
	 * Returns the list of Triangle associated with the trianglesView contained
	 * in trianglesViewSelected.
	 * 
	 * @return the list of selected triangles
	 */
	public final List<Triangle> getTrianglesSelected() {
		return this.trianglesSelected;
	}

	/**
	 * Getter.
	 * 
	 * @return the universe 3D view
	 */
	public final Universe3DView getUniverse3DView() {
		return this.u3DView;
	}

	/**
	 * Treats the different clicking actions (left or right).
	 * 
	 * @param e
	 *            The MouseEvent caught by the mouseListener when clicking
	 */
	@Override
	public final void mouseClicked(final MouseEvent e) {
		int buttonDown = e.getButton();
		this.pickCanvas.setShapeLocation(e);
		PickResult result = this.pickCanvas.pickClosest();

		if (buttonDown == MouseEvent.BUTTON1 && result != null) {
			if (this.selectionMode == SELECTION_TRIANGLE_MODE) {
				this.treatTriangleSelection(e, result);
			} else if (this.selectionMode == SELECTION_SURFACE_MODE) {
				this.treatSurfaceSelection(e, result);
			}
		}
	}

	@Override
	public void mouseDragged(final MouseEvent arg0) {
		// Not used.
	}

	@Override
	public void mouseEntered(final MouseEvent e) {
		// Not used.
	}

	@Override
	public void mouseExited(final MouseEvent e) {
		// Not used.
	}

	@Override
	public void mouseMoved(final MouseEvent e) {
		// Not used.
	}

	@Override
	public void mousePressed(final MouseEvent e) {
		// Not used.
	}

	@Override
	public void mouseReleased(final MouseEvent e) {
		// Not used.
	}

	/**
	 * TODO .
	 * 
	 * @param listener
	 *            TODO
	 */
	public final void removeElementsSelectedListener(
			final ElementsSelectedListener listener) {
		this.listeners.remove(ElementsSelectedListener.class, listener);
	}

	/**
	 * TODO .
	 * 
	 * @param surface
	 *            TODO
	 */
	public final void selectOrUnselectSurfaceFromTree(final Surface surface) {
		SurfaceView surfaceView = this.getSurfaceViewFromSurface(surface);

		this.selectOrUnselectSurface(surfaceView);
	}

	/**
	 * Change the appearance of the surfaceView parameter : select or unselect
	 * the corresponding surface.
	 * 
	 * @param surfaceView
	 *            The surfaceView containing the surface to select
	 */
	public final void selectOrUnselectSurface(final SurfaceView surfaceView) {
		Surface surface = surfaceView.getSurface();
		// surface not selected when clicked
		if (!this.surfacesSelected.contains(surface)) {
			this.surfacesSelected.add(surface);
			surfaceView.setMaterial(SurfaceView.MATERIAL_SELECTED);
			fireSurfaceSelected(surface);
		} else {
			// surface already selected when clicked
			this.surfacesSelected.remove(surface);
			surfaceView.setMaterial(SurfaceView.MATERIAL_UNSELECTED);
			fireSurfaceDeselected(surface);
		}
	}

	/**
	 * Setter.
	 * 
	 * @param displayModeIn
	 *            TODO
	 */
	public final void setDisplayMode(final int displayModeIn) {
		this.displayMode = displayModeIn;
	}

	/**
	 * Setter.
	 * 
	 * @param mouseRotateIn
	 *            TODO
	 */
	public final void setMouseRotate(final NewMouseRotate mouseRotateIn) {
		this.mouseRotate = mouseRotateIn;
	}

	/**
	 * Setter.
	 * 
	 * @param parentControllerIn
	 *            TODO
	 */
	public final void setParentController(
			final IsletSelectionController parentControllerIn) {
		this.parentController = parentControllerIn;
	}

	/**
	 * Generate a pickCanves linked to the branchGroup in the U3DView
	 * 
	 * @param branchGroup
	 *            TODO
	 */
	public final void setPickCanvas(final BranchGroup branchGroup) {
		this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
				.getCanvas(), branchGroup);
		this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
	}

	/**
	 * Treats the surface selection.
	 * 
	 * @param MouseEvent
	 *            The mouse event get from the MouseListener.
	 * @param PickResult
	 *            the PickResult get from the MouseListener.
	 */
	private void treatSurfaceSelection(final MouseEvent e,
			final PickResult result) {

		SurfaceView surfaceViewPicked = (SurfaceView) result
				.getNode(PickResult.SHAPE3D);
		Surface surfacePicked = surfaceViewPicked.getSurface();

		if (e.isControlDown()) {
			// Control down
			// -> Add or remove the surface to the surfacesSelected list.
			this.selectOrUnselectSurface(surfaceViewPicked);
		} else {
			// Control up
			// -> Unselect all the selected surfaces except the picked one.
			if (this.surfacesSelected.contains(surfacePicked)
					&& this.surfacesSelected.size() == 1) {
				this.selectOrUnselectSurface(surfaceViewPicked);
			} else {
				this.deselectEverySurfaces();
				this.selectOrUnselectSurface(surfaceViewPicked);
			}
		}
	}

	/**
	 * Treats the triangle selection when the user clicks on the mesh and if the
	 * selection mode is set to triangle.
	 * 
	 * @param MouseEvent
	 *            The mouse event get from the MouseListener.
	 * @param PickResult
	 *            the PickResult get from the MouseListener.
	 */
	private void treatTriangleSelection(final MouseEvent e,
			final PickResult result) {

		PickIntersection pickIntersection = result.getIntersection(0);

		// Gets the meshView picked.
		MeshView meshView = (MeshView) pickIntersection.getGeometryArray();
		// Gets the the triangle picked.
		int[] pointIndex = pickIntersection.getPrimitiveVertexIndices();
		Triangle trianglePicked = meshView
				.getTriangleFromArrayPosition(pointIndex[0] / 3);

		// Computes the neighbours of the triangle picked.
		Mesh oriented = meshView.getMesh().orientedAs(
				trianglePicked.getNormal(), ORIENTATION_TOLERANCE);
		Mesh neighbours = new Mesh();
		trianglePicked.returnNeighbours(neighbours, oriented);

		if (e.isControlDown()) {
			if (this.trianglesSelected.contains(trianglePicked)) {
				// Control down and triangle picked selected before click
				// -> Unselect the area picked

				Mesh meshToRemove = getMeshSelected(trianglePicked);
				this.unSelectTriangles(meshView, meshToRemove);
			} else {
				// Control down and triangle picked not selected before click
				// -> select the area picked

				this.selectTriangles(meshView, neighbours);
			}
		} else if (!e.isControlDown()) {
			if (this.trianglesSelected.contains(trianglePicked)) {
				// Control up and triangle picked selected before click
				// -> unselect the other areas

				if (this.meshesSelected.size() == 1) {
					this.unSelectTriangles(meshView, this.meshesSelected.get(0));
				} else {
					List<Mesh> meshesToRemove = getComplementaryMeshesSelected(trianglePicked);
					this.unSelectTriangles(meshView, meshesToRemove);
				}
			} else {
				// Control up and triangle picked not selected before click
				// -> unselect the other areas and select the area picked

				List<Mesh> meshesToRemove = getComplementaryMeshesSelected(trianglePicked);
				this.unSelectTriangles(meshView, meshesToRemove);
				this.selectTriangles(meshView, neighbours);
			}

		}

	}

	/**
	 * Get the mesh from meshesSelected list containing the input triangle.
	 * 
	 * @param triangle
	 *            The triangle to check.
	 * @return Mesh The mesh containing the triangle.
	 */
	public Mesh getMeshSelected(Triangle triangle) {
		Mesh mesh = null;
		for (Mesh m : this.meshesSelected) {
			if (m.contains(triangle)) {
				mesh = m;
			}
		}
		return mesh;
	}

	/**
	 * Get the meshes from meshesSelected list not containing the input
	 * triangle.
	 * 
	 * @param triangle
	 *            The triangle to check.
	 * @return List<Mesh> The meshes not containing the triangle.
	 */
	public List<Mesh> getComplementaryMeshesSelected(Triangle triangle) {
		List<Mesh> meshes = new ArrayList<>();
		for (Mesh m : this.meshesSelected) {
			if (!m.contains(triangle)) {
				meshes.add(m);
			}
		}
		return meshes;
	}

	public void deselectEverySurfaces() {
		// Copies the list to avoid ConcurrentModificationException of the list
		// surfacesSelected.
		List<Surface> surfaces = new ArrayList<>(this.surfacesSelected);

		for (Surface surface : surfaces) {
			this.selectOrUnselectSurface(this
					.getSurfaceViewFromSurface(surface));
		}
	}

	/**
	 * Selects a set of triangles
	 * 
	 * @param MeshView
	 *            The meshiew giving the possibility to modify the triangles
	 *            appearance (change the texture).
	 * @param Mesh
	 *            The mesh containing the list of triangle to picked.
	 */
	public void selectTriangles(MeshView meshViewPicked, Mesh meshToSelect) {
		for (Triangle t : meshToSelect) {
			meshViewPicked.select(t);
			fireTriangleSelected(t);
		}
		this.trianglesSelected.addAll(meshToSelect);
		this.meshesSelected.add(meshToSelect);
	}

	public void selectTriangles(MeshView meshViewPicked,
			List<Mesh> meshesToSelect) {
		for (Mesh m : meshesToSelect) {
			this.selectTriangles(meshViewPicked, m);
		}
	}

	public void unSelectTriangles(MeshView meshViewPicked, Mesh meshToUnselect) {
		for (Triangle t : meshToUnselect) {
			meshViewPicked.unSelect(t);
			fireTriangleDeselected(t);
		}
		this.trianglesSelected.removeAll(meshToUnselect);
		this.meshesSelected.remove(meshToUnselect);
	}

	public void unSelectTriangles(MeshView meshViewPicked,
			List<Mesh> meshesToSelect) {
		for (Mesh m : meshesToSelect) {
			this.unSelectTriangles(meshViewPicked, m);
		}
	}

	public void clearAll() {
		this.surfacesSelected.clear();
		this.u3DView.clearAll();
	}
}
