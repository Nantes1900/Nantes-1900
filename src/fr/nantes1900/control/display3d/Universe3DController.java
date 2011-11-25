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
	private ArrayList<TriangleView> trianglesViewSelected;

	private ArrayList<MeshView> triangleMesh = new ArrayList<MeshView>();
	// private ArrayList<PolygonView> polygonsViewSelected;
	private int xPressed;
	private int yPressed;

	private List<Integer> trianglePicked = new ArrayList<Integer>();
	private ArrayList<Integer> selectedIndex = new ArrayList<Integer>();

	private MeshView triangleMeshView;

	private MeshView triangleMeshLastSelected;

	private Shape3D shape3DLastSelected;

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

			// Bouton gauche enfonc

			this.pickCanvas.setShapeLocation(e);
			PickResult result = this.pickCanvas.pickClosest();
			if (result == null) {
				// System.out.println("Nothing picked");
			} else {

				PickIntersection PI = result.getIntersection(0);

				int[] PointIndex = PI.getPrimitiveVertexIndices();
				int TriangleIndex = PointIndex[0] / 3;

				MeshView triangleMeshView = (MeshView) PI.getGeometryArray();
				this.triangleMeshView = triangleMeshView;

				if (this.triangleMeshView.getMeshSelectedOrNot() == false) {
					// return the shape3D selected
					Shape3D shapePicked = (Shape3D) result
							.getNode(PickResult.SHAPE3D);
					this.selectShape3D(shapePicked);
					this.triangleMeshView.selectMesh();
					if (this.shape3DLastSelected != null) {
						if (this.shape3DLastSelected != shapePicked) {
							this.unselectShape3D(this.shape3DLastSelected);
							this.triangleMeshLastSelected.unselectMesh();

						}

					}
					this.shape3DLastSelected = shapePicked;
					this.triangleMeshLastSelected = this.triangleMeshView;
				}

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

			// Bouton gauche enfonc

			this.pickCanvas.setShapeLocation(e);
			PickResult result = this.pickCanvas.pickClosest();
			if (result == null) {
				// System.out.println("Nothing picked");
			} else {

				PickIntersection PI = result.getIntersection(0);
				int[] PointIndex = PI.getPrimitiveVertexIndices();
				int TriangleIndex = PointIndex[0] / 3;
				MeshView triangleMeshView = (MeshView) PI.getGeometryArray();
				this.triangleMeshView = triangleMeshView;

				if (this.triangleMeshView.getMeshSelectedOrNot() == false) {
					// return the shape3D selected
					Shape3D shapePicked = (Shape3D) result
							.getNode(PickResult.SHAPE3D);
					this.selectShape3D(shapePicked);
					this.triangleMeshView.selectMesh();
					if (this.shape3DLastSelected != null) {
						if (this.shape3DLastSelected != shapePicked) {
							this.unselectShape3D(this.shape3DLastSelected);
							this.triangleMeshLastSelected.unselectMesh();

						}

					}
					this.shape3DLastSelected = shapePicked;
					this.triangleMeshLastSelected = this.triangleMeshView;
				} else {
					triangleMeshView.select(TriangleIndex);
					if (this.trianglesViewSelected
							.contains(this.triangleMeshView.getTriangleArray()
									.get(TriangleIndex)) == false) {
						this.trianglesViewSelected.add(this.triangleMeshView
								.getTriangleArray().get(TriangleIndex));
						// ///////////////////
						this.triangleMesh.add(triangleMeshView);
						// ////////////////////
					}

					List<Integer> triangleNewSelected = new ArrayList<Integer>();

					triangleNewSelected.add(TriangleIndex);
					// this.trianglePicked.add(TriangleIndex);
					int turn = 30;
					selectVoisin(TriangleIndex, triangleNewSelected,
							triangleMeshView, turn);
					this.mouseRotate.setCenter(triangleMeshView
							.getTriangleArray().get(TriangleIndex));
					this.selectedIndex
							.add(this.trianglesViewSelected.size() - 1);
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
			// this is the method to cancled a zone of triangles, don't delete
			// it
			// this.pickCanvas.setShapeLocation(e);
			// PickResult result = this.pickCanvas.pickClosest();
			// if (result == null) {
			// System.out.println("Nothing canceled");
			// } else {
			//
			// PickIntersection PI=result.getIntersection(0);
			// int []PointIndex=PI.getPrimitiveVertexIndices();
			// int TriangleIndex=PointIndex[0]/3;
			// TriangleMeshView
			// triangleMeshView=(TriangleMeshView)PI.getGeometryArray();
			// this.triangleMeshView=triangleMeshView;
			//
			//
			// if(this.trianglesViewSelected.contains(this.triangleMeshView.getTriangleArray().get(TriangleIndex))){
			//
			// int
			// triangleIndex=this.trianglesViewSelected.indexOf(this.triangleMeshView.getTriangleArray().get(TriangleIndex));
			// int beginIndex=0;
			// int finishIndex=0;
			// int indexOfBeginIndex=0;
			// int indexOfFinishIndex=0;
			// for(int i=0;i<this.selectedIndex.size();i++){
			// if(i==0&&this.selectedIndex.get(i)>=triangleIndex){
			// beginIndex=-1;
			// finishIndex=this.selectedIndex.get(i);
			// break;
			// }
			// else{
			// if(this.selectedIndex.get(i)<triangleIndex&&this.selectedIndex.get(i+1)>=triangleIndex){
			// beginIndex=this.selectedIndex.get(i);
			// finishIndex=this.selectedIndex.get(i+1);
			// indexOfBeginIndex=i;
			// indexOfFinishIndex=i+1;
			// break;
			// }
			// }
			// }
			//
			// for(int i=finishIndex;i>beginIndex;i--){
			// TriangleView triangleSelected=this.trianglesViewSelected.get(i);
			// int index=triangleSelected.getTriangle().getTriangleViewIndex();
			// this.triangleMeshView.unselect(index);
			// this.trianglesViewSelected.remove(i);
			//
			// }
			//
			// this.selectedIndex.remove(indexOfFinishIndex);
			// for(int i=indexOfFinishIndex;i<this.selectedIndex.size();i++){
			// int index=this.selectedIndex.get(i);
			// this.selectedIndex.set(i, index-(finishIndex-beginIndex));
			//
			// }
			//
			// }
			//
			//
			// }
		}

	}

	// slectionner des triangles autour du triangle clique, turn est le tour de
	// selection. le premier tour,
	// on selectionne trois voisins du triangle clique, le deuxieme tour, on
	// selectionne des voisins de ces trois triangles,
	// pour chaque tour, on refait comme ca
	// triangleSelected pour recuperer des triangles selectionnes
	// triangleNewSelected sauvgarder des triangels selctionnes dans chaque tour
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
							// //////////
							this.triangleMesh.add(this.triangleMeshView);
							// ///////////////////////
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
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse Pressed");
		this.xPressed = e.getX();
		this.yPressed = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// int buttonDown = e.getButton();
		// if (buttonDown == MouseEvent.BUTTON1) {
		// for (int x = this.xPressed; x < e.getX(); x = x + 2) {
		// for (int y = this.yPressed; y < e.getY(); y = y + 2) {
		// this.pickCanvas.setShapeLocation(x, y);
		// PickResult result = this.pickCanvas.pickClosest();
		// if (result == null) {
		// System.out.println("Nothing picked");
		// } else {
		// PickIntersection PI=result.getIntersection(0);
		// int []PointIndex=PI.getPrimitiveVertexIndices();
		// int TriangleIndex=PointIndex[0]/3;
		// MeshView triangleMeshView=(MeshView)PI.getGeometryArray();
		// triangleMeshView.select(TriangleIndex);
		// mouseRotate.setCenter(triangleMeshView.getTriangleArray().get(TriangleIndex));
		//
		// }
		// }
		// }
		// }
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
		app.setMaterial(fr.nantes1900.view.display3d.MeshList.matUnSelected);
		shape.setAppearance(app);

	}

	public void selectShape3D(Shape3D shape) {

		Appearance app = shape.getAppearance();
		app.setMaterial(fr.nantes1900.view.display3d.MeshList.matSelected);
		shape.setAppearance(app);

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
}
