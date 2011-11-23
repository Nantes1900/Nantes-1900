package fr.nantes1900.control.display3d;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.BranchGroup;

import javax.vecmath.Vector3d;
import javax.swing.event.EventListenerList;


import com.sun.j3d.utils.picking.PickCanvas;
import com.sun.j3d.utils.picking.PickIntersection;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import fr.nantes1900.control.isletselection.IsletSelectionController;
import fr.nantes1900.listener.ElementsSelectedListener;
import fr.nantes1900.models.basis.Triangle;

import fr.nantes1900.view.display3d.MeshView;
import fr.nantes1900.view.display3d.TriangleView;
import fr.nantes1900.view.display3d.Universe3DView;

// FIXME : Javadoc
/**
 * TODO.
 */
public class Universe3DController implements MouseListener, MouseMotionListener
{


	/**
	 * The view of the 3D objets to show.
	 */
	private Universe3DView u3DView;

	private PickCanvas pickCanvas;
	private NewMouseRotate mouseRotate;

	private IsletSelectionController parentController;
	
	private final EventListenerList  listeners = new EventListenerList();

	// private ArrayList<MeshView> meshesViewSelected;
	private ArrayList<TriangleView> trianglesViewSelected;

	// private ArrayList<PolygonView> polygonsViewSelected;
	private int xPressed;
	private int yPressed;
	
	private List<Integer> trianglePicked=new ArrayList<Integer>();
	private List<Integer> selectedIndex=new ArrayList<Integer>();
	private MeshView triangleMeshView;

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
	                System.out.println("Nothing picked");
	            } else {

	            	PickIntersection PI=result.getIntersection(0);
	            	int []PointIndex=PI.getPrimitiveVertexIndices();
	            	int TriangleIndex=PointIndex[0]/3;
	            	MeshView triangleMeshView=(MeshView)PI.getGeometryArray();
	                this.triangleMeshView=triangleMeshView;

//	            	triangleMeshView.selectOrUnselect(TriangleIndex); 
	            	triangleMeshView.select(TriangleIndex);
	            	if(trianglePicked.contains(TriangleIndex)==false){
	            		this.trianglePicked.add(TriangleIndex);
	            		this.selectedIndex.add(this.trianglePicked.size());
	            		this.trianglesViewSelected.add(this.triangleMeshView.getTriangleArray().get(TriangleIndex));
	            	}          	
	                this.mouseRotate.setCenter(triangleMeshView.getTriangleArray().get(TriangleIndex));
	                

	            }
		} else if (buttonDown == MouseEvent.BUTTON3) {

			// Bouton droit enfonc

			this.pickCanvas.setShapeLocation(e);
			PickResult result = this.pickCanvas.pickClosest();
			if (result == null) {
				System.out.println("Nothing picked");
			} else {
				PickIntersection PI = result.getIntersection(0);
				int[] PointIndex = PI.getPrimitiveVertexIndices();
				int TriangleIndex = PointIndex[0] / 3;
				
				MeshView triangleMeshView = (MeshView) PI.getGeometryArray();
				this.triangleMeshView=triangleMeshView;
				
				//triangleMeshView.selectOrUnselect(TriangleIndex);
				triangleMeshView.select(TriangleIndex);
				if(trianglePicked.contains(TriangleIndex)==false){
            		this.trianglePicked.add(TriangleIndex);
            		this.trianglesViewSelected.add(this.triangleMeshView.getTriangleArray().get(TriangleIndex));
            		
            	}  
				
				
				List<Integer> triangleNewSelected = new ArrayList<Integer>();
				
				triangleNewSelected.add(TriangleIndex);
				//triangleSelected.add(TriangleIndex);
				this.trianglePicked.add(TriangleIndex);
				this.trianglesViewSelected.add(this.triangleMeshView.getTriangleArray().get(TriangleIndex));
				//commentaires
				int turn = 30;
				selectVoisin(TriangleIndex,triangleNewSelected,triangleMeshView,turn);
                this.mouseRotate.setCenter(triangleMeshView.getTriangleArray().get(TriangleIndex));
                this.selectedIndex.add(this.trianglePicked.size());

			}
		}
		
		  else if (buttonDown == MouseEvent.BUTTON2){
	        	int selectTimes=this.selectedIndex.size();
	        	if(selectTimes>1){
	        		int n1=this.selectedIndex.get(selectTimes-2);
	    			int n2=this.selectedIndex.get(selectTimes-1);
	    			for(int i=n1;i<n2;i++){
	    				this.triangleMeshView.unselect(this.trianglePicked.get(i));
	    			}
	    			for(int i=n2-1;i>n1-1;i--){
	    				this.trianglePicked.remove(i);	
	    				this.trianglesViewSelected.remove(i);
	    			}		
	    			this.selectedIndex.remove(selectTimes-1);
	        	}
	        	else{
	        		int n1=0;
	        		int n2=this.selectedIndex.get(0);
	        		for(int i=n1;i<n2;i++){
	        			int picked=this.trianglePicked.get(i);
	    				this.triangleMeshView.unselect(picked);
	    			}
	    			for(int i=n2-1;i>n1-1;i--){
	    				this.trianglePicked.remove(i);		
	    				this.trianglesViewSelected.remove(i);
	    			}	
	    			this.selectedIndex.remove(0);
	        	}
	        }
	}

	// slectionner des triangles autour du triangle clique, turn est le tour de
	// selection. le premier tour,
	// on selectionne trois voisins du triangle clique, le deuxieme tour, on
	// selectionne des voisins de ces trois triangles,
	// pour chaque tour, on refait comme ca
	// triangleSelected pour recuperer des triangles selectionnes
	// triangleNewSelected sauvgarder des triangels selctionnes dans chaque tour
public void selectVoisin(int TriangleIndex,List<Integer>triangleNewSelected,MeshView triangleMeshView,int turn){
    	
    	Vector3d normal=triangleMeshView.getTriangleArray().get(TriangleIndex).getTriangle().getNormal();
 	   for(int i=0;i<turn;i++){
 		   List<Integer>triangleCount=new ArrayList<Integer>();
 		   for(int j:triangleNewSelected){
 			   List<Triangle> triangleNeighbour=triangleMeshView.getTriangleArray().get(j).getTriangle().getNeighbours();
 			  
 			  for(int l=0;l<triangleNeighbour.size();l++){

 				 int triangleNeighbourIndex=triangleNeighbour.get(l).getTriangleViewIndex();
 				 if(trianglePicked.contains(triangleNeighbourIndex)==false){
 					  Boolean isParalle;
 					  isParalle=triangleNeighbour.get(l).isParalleTo(normal, 0.5);
 					 
 					 if(isParalle){
 						triangleMeshView.select(triangleNeighbourIndex);
 	 	 				 trianglePicked.add(triangleNeighbourIndex);
 	 	 				 this.trianglesViewSelected.add(this.triangleMeshView.getTriangleArray().get(triangleNeighbourIndex));
 	 	 				 triangleCount.add(triangleNeighbourIndex);
 					 }
 					 
 				 }
 				
 			  }
 			  
 		   }
 		   triangleNewSelected.clear();
 		   for(int m=0;m<triangleCount.size();m++){
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
		System.out.println("Mouse Pressed");
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
    public Universe3DController()
    {
        this.u3DView = new Universe3DView(this);
        this.trianglesViewSelected = new ArrayList<>();
    }

  

   
   

 

  


   
 
 

    

    /**
     * @param mouseRotate
     */
    public final void setMouseRotate(final NewMouseRotate mouseRotate)
    {
        this.mouseRotate = mouseRotate;
    }

    /**
     * @param branchGroup
     */
    public final void setPickCanvas(final BranchGroup branchGroup)
    {
        this.pickCanvas = new PickCanvas(this.u3DView.getSimpleUniverse()
                .getCanvas(), branchGroup);
        this.pickCanvas.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
        this.u3DView.getSimpleUniverse().getCanvas().addMouseListener(this);
        this.u3DView.getSimpleUniverse()
                .getCanvas()
                .addMouseMotionListener(this);

    }

    public void addElementsSelectedListener(ElementsSelectedListener listener)
    {
        listeners.add(ElementsSelectedListener.class, listener);
    }

    public void
            removeElementsSelectedListener(ElementsSelectedListener listener)
    {
        listeners.remove(ElementsSelectedListener.class, listener);
    }


}
