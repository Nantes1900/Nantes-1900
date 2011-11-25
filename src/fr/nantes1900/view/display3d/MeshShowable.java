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

public class MeshShowable {

	/**
	 * A list to save all the meshes to be displayed.
	 */
	private ArrayList<MeshView> meshList = new ArrayList<MeshView>();

	/**
	 * A list to save all the shape3D of the meshes.
	 */
	private ArrayList<Shape3D> meshShape3D = new ArrayList<Shape3D>();
	/**
	 * A list to save all the appearances of shape3D.
	 */
	private ArrayList<Appearance> meshAppearance = new ArrayList<Appearance>();

	/**
	 * The texutre of the mesh.
	 */
	private Texture2D texture;
	
	/**
	 * The material of the mesh non-selected.
	 */
	public static final Material matSelected = new Material(new Color3f(1.0f,
			1.0f, 1.0f), new Color3f(1.0f, 1.0f, 1.0f),
			new Color3f(Color.white), new Color3f(Color.white), 64);

	/**
	 * The material of the mesh selected.
	 */
	public static final Material matUnSelected = new Material(new Color3f(0.0f,
			0.0f, 1.0f), new Color3f(0.0f, 0.0f, 1.0f),
			new Color3f(Color.blue), new Color3f(Color.blue), 64);

	/**
	 * constructor of MeshShowable.
	 */
	public MeshShowable() {

		// Read the texture.
		TextureLoader loader = new TextureLoader("texture.jpg", null);
		ImageComponent2D image = loader.getImage();
		this.texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
				image.getWidth(), image.getHeight());
		this.texture.setImage(0, image);

	}



	/**
	 * @return The list of the meshes to be displayed.
	 */
	public ArrayList<MeshView> getMeshList() {
		return this.meshList;
	}

	/**
	 * @return The list of shape3D,each mesh has a shape3D.
	 */
	public ArrayList<Shape3D> getShape3D() {
		return this.meshShape3D;
	}

	/**
	 * Add the meshView to the list of meshes, create a shape3D for the mesh.
	 * @param meshView The MeshView to be displayed.
	 */
	public void addMeshView(MeshView meshView) {

		//Add the meshView to the list.
		this.meshList.add(meshView);
        //Create the shape3D.
		Shape3D shape = new Shape3D();

		shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
		shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
        //Add the mesh to the shape3D.
		shape.addGeometry(this.meshList.get(this.meshList.size() - 1));
		
		this.meshShape3D.add(shape);

		//Create the appearance.
		Appearance app = new Appearance();

		app.setCapability(Appearance.ALLOW_MATERIAL_READ);
		app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

		app.setMaterial(MeshShowable.matUnSelected);

		//Set the texture of the appearance.
		app.setTexture(this.texture);
		
		TextureAttributes texAtt = new TextureAttributes();
		texAtt.setTextureMode(TextureAttributes.MODULATE);
		app.setTextureAttributes(texAtt);

		this.meshAppearance.add(app);
		//Set the appearance of the Shape3D.
		shape.setAppearance(app);
	}

}
