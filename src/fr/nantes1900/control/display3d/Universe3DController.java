package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Shape3D;
import javax.swing.event.EventListenerList;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.PolygonView;
import fr.nantes1900.view.display3d.SurfaceView;
import fr.nantes1900.view.display3d.Universe3DView;

/**
 * TODO.
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
	 * The controller to change the center of rotation.
	 */
	private NewMouseRotate mouseRotate;

	private ElementsSelectedListener parentController;

	private final EventListenerList listeners = new EventListenerList();

	public static final int DISPLAY_MESH_MODE = 1;

	public static final int DISPLAY_POLYGON_MODE = 2;

	private int displayMode;

	private List<Triangle> trianglesSelected = new ArrayList<>();

	private List<Integer> selectionIndexes = new ArrayList<>();

	private List<Surface> surfacesSelected = new ArrayList<>();

	/**
	 * @param isletSelectionController
	 */
	public Universe3DController(ElementsSelectedListener parentControllerIn) {
		this.parentController = parentControllerIn;
		this.u3DView = new Universe3DView(this);
	}

	public void addElementsSelectedListener(ElementsSelectedListener listener) {
		this.listeners.add(ElementsSelectedListener.class, listener);
	}

	private void firePolygonDeselected(Polygon polygonDeselected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.polygonDeselected(polygonDeselected);
		}
	}

	private void firePolygonSelected(Polygon polygonSelected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.polygonSelected(polygonSelected);
		}
	}

	private void fireTriangleDeselected(Triangle triangleDeselected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleSelected(triangleDeselected);
		}
	}

	private void fireTriangleSelected(Triangle triangleSelected) {
		ElementsSelectedListener[] ESListeners = this.listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleSelected(triangleSelected);
		}
	}

	public int getDisplayMode() {
		return this.displayMode;
	}

	/**
	 * Getter.
	 * 
	 * @return the mouse rotate class
	 */
	public NewMouseRotate getMouseRotate() {
		return this.mouseRotate;
	}

	public ElementsSelectedListener getParentController() {
		return this.parentController;
	}

	/**
	 * Returns the list of Triangle associated with the trianglesView contained
	 * in trianglesViewSelected.
	 * 
	 * @return the list of selected triangles
	 */
	public List<Triangle> getTrianglesSelected() {
		return this.trianglesSelected;
	}

	public Universe3DView getUniverse3DView() {
		return this.u3DView;
	}


	public void removeElementsSelectedListener(ElementsSelectedListener listener) {
		this.listeners.remove(ElementsSelectedListener.class, listener);
	}

	/**
	 * @param MeshView
	 *            The triangle
	 */
	public void selectMesh(MeshView MeshView) {

	}

	public void selectMeshFromTree(Object object) {
		// Selects the object if it is a surface.
		if (object instanceof Surface) {
			((Surface) object).select();
		} else {
			System.out.println("Weird.");
			// TODO : throw an exception.
		}

		// Gets the index of the meshView in the list of the meshes displayed.
		int meshViewIndex = meshList.indexOf(object);

		// Gets the shape3D of the meshView to be selected.
		Shape3D shapeSelect = meshShape3D.get(meshViewIndex);
		Appearance appSelect = shapeSelect.getAppearance();

		// Changes the material of the mesh to be seleted.
		appSelect.setMaterial(matSelected);
		shapeSelect.setAppearance(appSelect);

		// If the shape3D selected last time is not null.
		if (shape3DSelected != null) {

			// If the shape3D selected last time is not this one.
			if (shape3DSelected.contains(shapeSelect)) {

				// Change back the material of the mesh selected last time.
				for (Shape3D shape : shape3DSelected) {
					Appearance appUnselect = shape.getAppearance();
					appUnselect.setMaterial(matUnSelected);
					shape.setAppearance(appUnselect);
				}

				// Unselect the meshView selected last time.
				for (Object meshObject : meshSelected) {
					if (meshObject.getClass().getSimpleName()
							.equals("MeshView")) {
						((MeshView) meshObject).unselectMesh();
					} else {
						((PolygonView) meshObject).unselect();
					}
				}

			}
		}

		// Set the shape3D last selected to be the shape3D picked.
		shape3DSelected.clear();
		shape3DSelected.add(shapeSelect);

		// Set the meshView last selected to be the meshView picked.
		meshSelected.clear();
		meshSelected.add(object);
	}

	/**
	 * Select the triangles around the triangle clicked.
	 * 
	 * @param triangleIndex
	 *            The index of the triangle clicked in the list of TriangleView.
	 * @param trianglesNewSelected
	 *            The indexs of the triagles selected in each turn.
	 * @param meshView
	 *            The MeshView selected.
	 * @param turn
	 *            The time of the recurrence to find the neighbour.
	 */
	public void selectNeighbours(List<Triangle> trianglesNewSelected,
			MeshView meshView, int turn) {

		Vector3d normal = trianglesNewSelected.get(0).getNormal();

		for (int i = 0; i < turn; i++) {
			List<Triangle> tempTrianglesList = new ArrayList<>();
			for (Triangle triangle : trianglesNewSelected) {
				List<Triangle> triangleNeighbours = triangle.getNeighbours();
				for (Triangle triangleNeighbour : triangleNeighbours) {
					if (!this.trianglesSelected.contains(triangleNeighbour)
							&& triangleNeighbour.angularTolerance(normal, 0.5)) {
						meshView.select(triangleNeighbour);
						this.trianglesSelected.add(triangleNeighbour);
						tempTrianglesList.add(triangleNeighbour);
					}
				}
			}
			trianglesNewSelected.clear();
			for (Triangle tempTriangle : tempTrianglesList) {
				trianglesNewSelected.add(tempTriangle);
			}
			tempTrianglesList.clear();
		}
	}

	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * @param mouseRotate
	 */
	public final void setMouseRotate(final NewMouseRotate mouseRotate) {
		this.mouseRotate = mouseRotate;
	}

	public void setParentController(IsletSelectionController parentController) {
		this.parentController = parentController;
	}

	/**
	 * @param branchGroup
	 */
	public final void setPickCanvas(final BranchGroup branchGroup) {
		this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
				.getCanvas(), branchGroup);
		this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
		this.u3DView.getSimpleUniverse().getCanvas().addMouseListener(this);
		this.u3DView.getSimpleUniverse().getCanvas()
				.addMouseMotionListener(this);
	}

	/**
	 * Change the appearance of the surfaceView parameter Select or unselect the
	 * corresponding surface
	 * 
	 * @param surfaceView
	 *            The surfaceView containing the surface to select
	 */
	public void selectOrUnselectSurface(SurfaceView surfaceView) {
		Surface surface = surfaceView.getSurface();
		// surface not selected when clicked
		if (!this.surfacesSelected.contains(surface)) {
			this.surfacesSelected.add(surface);
			surfaceView.setMaterial(SurfaceView.MATERIAL_SELECTED);
		}
		// surface already selected when clicked
		else {
			this.surfacesSelected.remove(surface);
			surfaceView.setMaterial(SurfaceView.MATERIAL_UNSELECTED);
		}
	}

	/**
	 * Change the rotation center when clicking on a surface
	 * 
	 * @param surfaceView
	 *            The surfaceView becomming the rotation center
	 */
	public void changeRotationCenter(SurfaceView surfaceView) {
		// FIXME handle the case of mesh = null.
		Point center = new Point(
				surfaceView.getMeshView().getCentroid().getX(), surfaceView
						.getMeshView().getCentroid().getY(), surfaceView
						.getMeshView().getCentroid().getZ());
		this.mouseRotate.setCenter(center);
	}

	/**
	 * Treat the different clicking actions (left or right)
	 * 
	 * @param MouseEvent
	 *            The MouseEvent caught by the mouseListener when clicking
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		int buttonDown = e.getButton();
		if (buttonDown == MouseEvent.BUTTON1) {
			this.treatLeftClick(e);
		} else if (buttonDown == MouseEvent.BUTTON3) {
			this.treatRightClick(e);
		}
	}

	/**
	 * Treatment of a left click action Handling of a surface selection
	 * 
	 * @param mouseEvent
	 */
	private void treatLeftClick(MouseEvent e) {
		this.pickCanvas.setShapeLocation(e);
		PickResult result = this.pickCanvas.pickClosest();
		if (result != null) {
			SurfaceView surfaceViewPicked = (SurfaceView) result
					.getNode(PickResult.SHAPE3D);

			if (e.isControlDown()) {
				this.surfacesSelected.clear();
			}
			this.selectOrUnselectSurface(surfaceViewPicked);
			this.changeRotationCenter(surfaceViewPicked);
		}

	}

	/**
	 * Treatment of a right click action Handling of triangles selection
	 * 
	 * @param mouseEvent
	 */
	private void treatRightClick(MouseEvent e) {
		this.pickCanvas.setShapeLocation(e);
		PickResult result = this.pickCanvas.pickClosest();
		if (result != null) {
			PickIntersection pickIntersection = result.getIntersection(0);
			// Get the meshView picked.
			GeometryArray geometryArray = pickIntersection.getGeometryArray();
			MeshView meshView = (MeshView) geometryArray;
			// Get the index of the triangle picked
			int[] pointIndex = pickIntersection.getPrimitiveVertexIndices();
			int indexSelected = pointIndex[0] / 3;

			Triangle trianglePicked = meshView
					.getTriangleFromArrayPosition(indexSelected);

			if (!this.trianglesSelected.contains(trianglePicked)) {

				meshView.select(trianglePicked);
				this.trianglesSelected.add(trianglePicked);

				// Times to search the neighbour.
				// FIXME use a button in the tool bar to set this number
				int turn = 30;

				// Store the triangles newly selected
				List<Triangle> trianglesNewSelected = new ArrayList<>();
				trianglesNewSelected.add(trianglePicked);
				this.selectNeighbours(trianglesNewSelected, meshView, turn);

				// Change the center of rotation.
				this.mouseRotate.setCenter(trianglePicked.getP1());
				// Index to know where a new selection begins
				this.selectionIndexes.add(this.trianglesSelected.size() - 1);

			} else {
				// Get the index of the triangle picked in the list of triangles
				// selected.
				int firstIndex = 0;
				int lastIndex = 0;
				int indexOfLastIndex = 0;

				// Find the triangle picked belongs to which time of selection.
				for (int i = 0; i < this.selectionIndexes.size(); i++) {
					// If the triangle picked belongs to the first time of
					// seleciton.
					if (i == 0 && this.selectionIndexes.get(i) >= indexSelected) {
						firstIndex = -1;
						lastIndex = this.selectionIndexes.get(i);
						break;
					}
					// If the triangle picked doesn't belong to the first time
					// of seleciton.
					else {
						if (this.selectionIndexes.get(i) < indexSelected
								&& this.selectionIndexes.get(i + 1) >= indexSelected) {
							firstIndex = this.selectionIndexes.get(i);
							lastIndex = this.selectionIndexes.get(i + 1);
							indexOfLastIndex = i + 1;
							break;
						}
					}
				}

				// Unselect all the triangles selected in the time of selection
				// which the triangle picked belongs to.
				for (int i = lastIndex; i > firstIndex; i--) {
					Triangle triangleSelected = this.trianglesSelected.get(i);
					int index = meshView
							.getArrayPositionFromTriangle(triangleSelected);
					meshView.select(index);
					this.trianglesSelected.remove(triangleSelected);

				}

				// Refresh the list of last index of triangles
				// selected in each time of selection.
				this.selectionIndexes.remove(indexOfLastIndex);
				for (int i = indexOfLastIndex; i < this.selectionIndexes.size(); i++) {
					int index = this.selectionIndexes.get(i);
					this.selectionIndexes.set(i, index
							- (lastIndex - firstIndex));
				}
			}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// Not used.
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Not used.
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Not used.
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// Not used.
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Not used.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Not used.
	}
}
