package fr.nantes1900.view.display3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Shape3D;

public class SurfaceView extends Shape3D{
	private MeshView meshView;
	private PolygonView polygonView;
	private Appearance appearance;
	
	public SurfaceView(){
		super();
	}
	
	public void setMeshView(MeshView meshView){
		this.meshView=meshView;
		this.setGeometry(this.meshView);
	}
	
	public MeshView getMeshView(){
		return this.meshView;
	}
	
	public void setPolygonView(PolygonView polygonView){
		this.polygonView=polygonView;
		this.setGeometry(this.polygonView);
	}
	
	public PolygonView getPolygonView(){
		return this.polygonView;
	}
	
	public void setAppearance(Appearance appearance){
		this.appearance=appearance;
		this.setAppearance(appearance);
	}
	
	public Appearance getAppearance(){
		return this.appearance;
	}

}
