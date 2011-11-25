package fr.nantes1900.view.display3d;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TextureAttributes;

import javax.vecmath.Color3f;

import com.sun.j3d.utils.image.TextureLoader;

import fr.nantes1900.utils.ParserSTL;



public class MeshList {
	
	
	/**
	 * a list to save all the meshes to be displayed
	 */
	private ArrayList<MeshView> meshList=new ArrayList<MeshView>(); 
	
	/**
	 * a list to save all the shape3D of the meshes
	 */
	private ArrayList<Shape3D> meshShape3D=new ArrayList<Shape3D>(); 
	/**
	 * a list to save all the appearances of shape3D
	 */
	private ArrayList<Appearance> meshAppearance=new ArrayList<Appearance>();
	
	public static final Material matSelected=new Material(new Color3f(1.0f, 1.0f, 1.0f),
			new Color3f(1.0f, 1.0f, 1.0f), new Color3f(Color.white), new Color3f(
					Color.white), 64);
	
	public static final Material matUnSelected=new Material(new Color3f(0.0f, 0.0f, 1.0f),
			new Color3f(0.0f, 0.0f, 1.0f), new Color3f(Color.blue), new Color3f(
					Color.blue), 64);
	
	/**
	 * constructor of MeshList,add all the meshs to be displayed into the list
	 */
	public MeshList(){
		
		//Material initial of all the meshes
//		Material mat = new Material(new Color3f(0, 0, 0f),
//				new Color3f(0, 0, 0), new Color3f(Color.white), new Color3f(
//						Color.white), 64);
		MeshList.matUnSelected.setColorTarget(3);
		//Texutre used when triangle is selected
		TextureLoader loader = new TextureLoader("texture.jpg", null);
		ImageComponent2D image = loader.getImage();
		Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
				image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		//app.setTexture(texture);
		TextureAttributes texAtt = new TextureAttributes();
		texAtt.setTextureMode(TextureAttributes.MODULATE);
		//app.setTextureAttributes(texAtt);
		
		ParserSTL parser;
		parser=new ParserSTL("MeshRoof - 1.stl");
		
		try {
			
			this.meshList.add(new MeshView(parser.read()));
			
			Shape3D shape=new Shape3D();
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
	    	shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.addGeometry(this.meshList.get(this.meshList.size()-1));
		    this.meshShape3D.add(shape);
		    
		    Appearance app = new Appearance();
		    app.setCapability(Appearance.ALLOW_MATERIAL_READ);
			app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
			app.setMaterial(MeshList.matUnSelected);
			app.setTexture(texture);
			app.setTextureAttributes(texAtt);
		    this.meshAppearance.add(app);
		    shape.setAppearance(app);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parser=new ParserSTL("MeshRoof - 2.stl");
		try {
			
			this.meshList.add(new MeshView(parser.read()));
			
			Shape3D shape=new Shape3D();
			shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
	    	shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
			shape.addGeometry(this.meshList.get(this.meshList.size()-1));
		    this.meshShape3D.add(shape);
		    
		    Appearance app = new Appearance();
		    app.setCapability(Appearance.ALLOW_MATERIAL_READ);
			app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
			app.setMaterial(MeshList.matUnSelected);
			app.setTexture(texture);
			app.setTextureAttributes(texAtt);
		    this.meshAppearance.add(app);
		    shape.setAppearance(app);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	

	
	/**
	 * @param triangleArray triangleArray
	 */
	public void addMesh(MeshView triangleMeshView){
		this.meshList.add(triangleMeshView);
	}
	
	public ArrayList<MeshView> getMeshList(){
		return this.meshList;
	}
	public ArrayList<Shape3D> getShape3D(){
		return this.meshShape3D;
	}

}
