
package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;

import javax.vecmath.Vector3d;
import javax.swing.event.EventListenerList;

import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;

import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.TriangleView;
import fr.nantes1900.view.display3d.Universe3DView;

// FIXME : Javadoc
/**
 * TODO.
 */
public class Universe3DController implements MouseListener, MouseMotionListener {

	/**
	 * The view of the 3D objets to show.
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

	private IsletSelectionController parentController;

	private final EventListenerList listeners = new EventListenerList();

	// private ArrayList<MeshView> meshesViewSelected;
	/**
	 * A list of all the triangles selected.
	 */
	private ArrayList<TriangleView> trianglesViewSelected;

	/**
	 * A list of all the meshes selected.
	 */
	private ArrayList<MeshView> triangleMesh = new ArrayList<MeshView>();
	// private ArrayList<PolygonView> polygonsViewSelected;
	/**
	 * The x coordinate of the mouse on the screen.
	 */
	private int xPressed;
	/**
	 * The y coordinate of the mouse on the screen.
	 */
	private int yPressed;

	/**
	 * 
	 */
	private List<Integer> trianglePicked = new ArrayList<Integer>();
	/**
	 * 
	 */
	private ArrayList<Integer> selectedIndex = new ArrayList<Integer>();

	/**
	 * The MeshView selected each time.
	 */
	private MeshView triangleMeshView;

	/**
	 * The MeshView selected in the last time.
	 */
	public static MeshView triangleMeshLastSelected;

	/**
	 * The Shape3D selected in the last time.
	 */
	public static Shape3D shape3DLastSelected;

	/**
	 * @param isletSelectionController
	 */
	public Universe3DController(
			IsletSelectionController isletSelectionController) {
		this.parentController = isletSelectionController;
		this.u3DView = new Universe3DView(this);
		this.trianglesViewSelected = new ArrayList<TriangleView>();
	}

	/**
	 * Getter.
	 * 
	 * @return the mouse rotate class
	 */
	public NewMouseRotate getMouseRotate() {
		return this.mouseRotate;
	}

	/**
	 * Returns the list of Triangle associated with the trianglesView contained
	 * in trianglesViewSelected.
	 * 
	 * @return the list of selected triangles
	 */
	public List<Triangle> getTrianglesSelected() {
		List<Triangle> trianglesList = new ArrayList<>();
		for (TriangleView triangleView : this.trianglesViewSelected) {
			trianglesList.add(triangleView.getTriangle());
		}
		return trianglesList;
	}

	public Universe3DView getUniverse3DView() {
		return this.u3DView;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int buttonDown = e.getButton();

		if (buttonDown == MouseEvent.BUTTON1) {

			// Left click of the mouse.

			this.pickCanvas.setShapeLocation(e);
			PickResult result = this.pickCanvas.pickClosest();
			if (result == null) {

			} else {

				PickIntersection pickIntersection = result.getIntersection(0);

				int[] PointIndex = pickIntersection.getPrimitiveVertexIndices();
				int TriangleIndex = PointIndex[0] / 3;

				MeshView triangleMeshView = (MeshView) pickIntersection
						.getGeometryArray();
				this.triangleMeshView = triangleMeshView;

				// If the mesh clicked is not selected, select the mesh, change
				// the color of the mesh.
				if (this.triangleMeshView.getMeshSelectedOrNot() == false) {
					// return the shape3D selected
					Shape3D shapePicked = (Shape3D) result
							.getNode(PickResult.SHAPE3D);
					this.selectShape3D(shapePicked);
					this.triangleMeshView.selectMesh();
					if (Universe3DController.shape3DLastSelected != null) {
						if (Universe3DController.shape3DLastSelected != shapePicked) {
							this.unselectShape3D(Universe3DController.shape3DLastSelected);
							Universe3DController.triangleMeshLastSelected
									.unselectMesh();

						}

					}
					Universe3DController.shape3DLastSelected = shapePicked;
					Universe3DController.triangleMeshLastSelected = this.triangleMeshView;
				}

				// If the mesh is selected, select the triangle where the mouse
				// is.
				else {
					triangleMeshView.select(TriangleIndex);
					if (trianglePicked.contains(TriangleIndex) == false) {
						// this.trianglePicked.add(TriangleIndex);
						this.trianglesViewSelected.add(this.triangleMeshView
								.getTriangleArray().get(TriangleIndex));
						// /////////
						this.triangleMesh.add(triangleMeshView);
						// //////////
						this.selectedIndex.add(this.trianglesViewSelected
								.size() - 1);
					}
					this.mouseRotate.setCenter(triangleMeshView
							.getTriangleArray().get(TriangleIndex));
				}

			}
		} else if (buttonDown == MouseEvent.BUTTON3) {

			// Right click of the mouse.

			this.pickCanvas.setShapeLocation(e);
			PickResult result = this.pickCanvas.pickClosest();
			if (result == null) {

			} else {

				PickIntersection pickIntersection = result.getIntersection(0);
				int[] PointIndex = pickIntersection.getPrimitiveVertexIndices();
				int TriangleIndex = PointIndex[0] / 3;
				MeshView triangleMeshView = (MeshView) pickIntersection
						.getGeometryArray();
				this.triangleMeshView = triangleMeshView;

				// If the mesh is not selected, select the mesh and change the
				// color.
				if (this.triangleMeshView.getMeshSelectedOrNot() == false) {
					// return the shape3D selected
					Shape3D shapePicked = (Shape3D) result
							.getNode(PickResult.SHAPE3D);
					this.selectShape3D(shapePicked);
					this.triangleMeshView.selectMesh();
					if (Universe3DController.shape3DLastSelected != null) {
						if (Universe3DController.shape3DLastSelected != shapePicked) {
							this.unselectShape3D(Universe3DController.shape3DLastSelected);
							Universe3DController.triangleMeshLastSelected
									.unselectMesh();

						}

					}
					Universe3DController.shape3DLastSelected = shapePicked;
					Universe3DController.triangleMeshLastSelected = this.triangleMeshView;
				}
				// If the mesh is selcted, select the triangle where the mouse
				// is and its neighbours.
				else {
					if(triangleMeshView.getTriangleArray().get(TriangleIndex).isSelected()==false){
						triangleMeshView.select(TriangleIndex);
						if (this.trianglesViewSelected
								.contains(this.triangleMeshView.getTriangleArray()
										.get(TriangleIndex)) == false) {
							this.trianglesViewSelected.add(this.triangleMeshView
									.getTriangleArray().get(TriangleIndex));

							this.triangleMesh.add(triangleMeshView);

						}

						List<Integer> triangleNewSelected = new ArrayList<Integer>();

						triangleNewSelected.add(TriangleIndex);

						int turn = 30;
						selectVoisin(TriangleIndex, triangleNewSelected,
								triangleMeshView, turn);
						this.mouseRotate.setCenter(triangleMeshView
								.getTriangleArray().get(TriangleIndex));
						this.selectedIndex
								.add(this.trianglesViewSelected.size() - 1);
					}
					else{
						
						
						
						 if(this.trianglesViewSelected.contains(this.triangleMeshView.getTriangleArray().get(TriangleIndex))){
						
						 int
						 triangleIndex=this.trianglesViewSelected.indexOf(this.triangleMeshView.getTriangleArray().get(TriangleIndex));
						 int beginIndex=0;
						 int finishIndex=0;
						 int indexOfBeginIndex=0;
						 int indexOfFinishIndex=0;
						 for(int i=0;i<this.selectedIndex.size();i++){
						 if(i==0&&this.selectedIndex.get(i)>=triangleIndex){
						 beginIndex=-1;
						 finishIndex=this.selectedIndex.get(i);
						 break;
						 }
						 else{
						 if(this.selectedIndex.get(i)<triangleIndex&&this.selectedIndex.get(i+1)>=triangleIndex){
						 beginIndex=this.selectedIndex.get(i);
						 finishIndex=this.selectedIndex.get(i+1);
						 indexOfBeginIndex=i;
						 indexOfFinishIndex=i+1;
						 break;
						 }
						 }
						 }
						
						 for(int i=finishIndex;i>beginIndex;i--){
						 TriangleView triangleSelected=this.trianglesViewSelected.get(i);
						 int index=triangleSelected.getTriangle().getTriangleViewIndex();
						 this.triangleMeshView.unselect(index);
						 this.trianglesViewSelected.remove(i);
						
						 }
						
						 this.selectedIndex.remove(indexOfFinishIndex);
						 for(int i=indexOfFinishIndex;i<this.selectedIndex.size();i++){
						 int index=this.selectedIndex.get(i);
						 this.selectedIndex.set(i, index-(finishIndex-beginIndex));
						
						 }
						
						 }
						
						
						 
					}
					}
					

			}
		}
		// click the wheel all the triangles selected will be canceled
		else if (buttonDown == MouseEvent.BUTTON2) {
			for (int i = 0; i < this.trianglesViewSelected.size(); i++) {
				int index = this.trianglesViewSelected.get(i).getTriangle()
						.getTriangleViewIndex();
				this.triangleMesh.get(i).unselect(index);
			}
			this.trianglesViewSelected.clear();
			this.selectedIndex.clear();
			this.triangleMesh.clear();
			
		}

	}

	/**
	 * Select the triangles around the triangle clicked.
	 * 
	 * @param TriangleIndex
	 *            The index of the triangle clicked in the list of TriangleView.
	 * @param triangleNewSelected
	 *            The indexs of the triagles selected in each turn.
	 * @param triangleMeshView
	 *            The MeshView selected.
	 * @param turn
	 *            The time of the recurrence to find the neighbour.
	 */
	public void selectVoisin(int TriangleIndex,
			List<Integer> triangleNewSelected, MeshView triangleMeshView,
			int turn) {

		Vector3d normal = triangleMeshView.getTriangleArray()
				.get(TriangleIndex).getTriangle().getNormal();
		for (int i = 0; i < turn; i++) {
			List<Integer> triangleCount = new ArrayList<Integer>();
			for (int j : triangleNewSelected) {
				List<Triangle> triangleNeighbour = triangleMeshView
						.getTriangleArray().get(j).getTriangle()
						.getNeighbours();

				for (int l = 0; l < triangleNeighbour.size(); l++) {

					int triangleNeighbourIndex = triangleNeighbour.get(l)
							.getTriangleViewIndex();
					if (this.trianglesViewSelected
							.contains(this.triangleMeshView.getTriangleArray()
									.get(triangleNeighbourIndex)) == false) {
						Boolean isParalle;
						isParalle = triangleNeighbour.get(l).isParalleTo(
								normal, 0.5);

						if (isParalle) {
							triangleMeshView.select(triangleNeighbourIndex);
							this.trianglesViewSelected
									.add(this.triangleMeshView
											.getTriangleArray().get(
													triangleNeighbourIndex));

							this.triangleMesh.add(this.triangleMeshView);

							triangleCount.add(triangleNeighbourIndex);
						}

					}

				}

			}
			triangleNewSelected.clear();
			for (int m = 0; m < triangleCount.size(); m++) {
				triangleNewSelected.add(triangleCount.get(m));
			}

			triangleCount.clear();
		}
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// Empty.
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// Empty.
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Empty.
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// Empty.
	}

	@Override
	public void mousePressed(MouseEvent e) {

		this.xPressed = e.getX();
		this.yPressed = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	/**
	 * TODO.
	 */
	public Universe3DController() {
		this.u3DView = new Universe3DView(this);
		this.trianglesViewSelected = new ArrayList<>();
	}

	/**
	 * @param mouseRotate
	 */
	public final void setMouseRotate(final NewMouseRotate mouseRotate) {
		this.mouseRotate = mouseRotate;
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

	public void addElementsSelectedListener(ElementsSelectedListener listener) {
		listeners.add(ElementsSelectedListener.class, listener);
	}

	public void removeElementsSelectedListener(ElementsSelectedListener listener) {
		listeners.remove(ElementsSelectedListener.class, listener);
	}

	public void unselectShape3D(Shape3D shape) {

		Appearance app = shape.getAppearance();
		app.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matUnSelected);
		shape.setAppearance(app);

	}

	/**
	 * Change the material of the Shape3D
	 * 
	 * @param shape
	 *            The Shape3D of the mesh selected.
	 */
	public void selectShape3D(Shape3D shape) {

		Appearance app = shape.getAppearance();
		app.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matSelected);
		shape.setAppearance(app);

	}

	/**
	 * @param triangleMeshView
	 *            The triangle
	 */
	public void selectMesh(MeshView triangleMeshView) {

	}

	public void meshSelect(MeshView triangleMeshView) {
		triangleMeshView.selectMesh();

	}

	private void fireTriangleSelected(Triangle triangleSelected) {
		ElementsSelectedListener[] ESListeners = listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleSelected(triangleSelected);
		}
	}

	private void firePolygonSelected(Polygon polygonSelected) {
		ElementsSelectedListener[] ESListeners = listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.polygonSelected(polygonSelected);
		}
	}

	private void fireTriangleDeselected(Triangle triangleDeselected) {
		ElementsSelectedListener[] ESListeners = listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.triangleSelected(triangleDeselected);
		}
	}

	private void firePolygonDeselected(Polygon polygonDeselected) {
		ElementsSelectedListener[] ESListeners = listeners
				.getListeners(ElementsSelectedListener.class);
		for (ElementsSelectedListener listener : ESListeners) {
			listener.polygonDeselected(polygonDeselected);
		}
	}

	
	
	public void SelectMeshFromTree(MeshView mesh){
		// Select the meshView.
				mesh.selectMesh();

				// Get the index of the meshView in the list of the meshes displayed.
				int meshViewIndex = fr.nantes1900.view.display3d.MeshShowable.meshList
						.indexOf(mesh);

				// Get the shape3D of the meshView to be selected.
				Shape3D shapeSelect = fr.nantes1900.view.display3d.MeshShowable.meshShape3D
						.get(meshViewIndex);
				Appearance appSelect = shapeSelect.getAppearance();

				// Change the material of the mesh to be seleted.
				appSelect
						.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matSelected);
				shapeSelect.setAppearance(appSelect);

				// If the shape3D selected last time is not null.
				if (Universe3DController.shape3DLastSelected != null) {

					// If the shape3D selected last time is not this one.
					if (Universe3DController.shape3DLastSelected != shapeSelect) {

						// Change back the material of the mesh selected last time.
						Appearance appUnselect = Universe3DController.shape3DLastSelected
								.getAppearance();
						appUnselect
								.setMaterial(fr.nantes1900.view.display3d.MeshShowable.matUnSelected);
						Universe3DController.shape3DLastSelected
								.setAppearance(appUnselect);

						// Unselect the meshView selected last time.
						Universe3DController.triangleMeshLastSelected.unselectMesh();
					}
				}

				// Set the shape3D last selected to be the shape3D picked.
				Universe3DController.shape3DLastSelected = shapeSelect;

				// Set the meshView last selected to be the meshView picked.
				Universe3DController.triangleMeshLastSelected = mesh;
	}
}

