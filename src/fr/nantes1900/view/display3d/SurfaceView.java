package fr.nantes1900.view.display3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;

public class SurfaceView extends Shape3D
{

    /**
     * The mesh to be displayed.
     */
    private MeshView    meshView;
    /**
     * The polygon to be displayed.
     */
    private PolygonView polygonView;
    /**
     * The appearance of the surface.
     */
    private Appearance  appearance = new Appearance();

    /**
     * Constructor of the surfaceView.
     */
    public SurfaceView()
    {
        super();
        // Set the capability of SurfaceView.
        this.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        this.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

        // Set the capability of appearance.
        this.appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
        this.appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

        TextureAttributes texAtt = new TextureAttributes();
        texAtt.setTextureMode(TextureAttributes.MODULATE);
        this.appearance.setTextureAttributes(texAtt);

        // Set the appearance of the surfaceView.
        this.setAppearance(appearance);
    }

    /**
     * Set the mesh of triangles in the surfaceView.
     * @param meshView
     *            The mesh of triangles to be displayed.
     */
    public void setMeshView(MeshView meshView)
    {
        this.meshView = meshView;
        this.setGeometry(this.meshView);
    }

    /**
     * Get the mesh of triangle in the surfaceView.
     * @return this.meshView
     */
    public MeshView getMeshView()
    {
        return this.meshView;
    }

    /**
     * Set the polygon in the surfaceView.
     * @param polygonView
     *            The polygon to be displayed.
     */
    public void setPolygonView(PolygonView polygonView)
    {
        this.polygonView = polygonView;
        this.setGeometry(this.polygonView);
    }

    /**
     * Get the polygon in the surfaceView.
     * @return this.polygonView
     */
    public PolygonView getPolygonView()
    {
        return this.polygonView;
    }

    /**
     * Set the material of the surfaceMesh.
     * @param material
     *            The material to be used.
     */
    public void setMaterial(Material material)
    {
        this.appearance.setMaterial(material);

    }

    /**
     * Set the texture of the surfaceMesh.
     * @param texture
     *            The texture to be used.
     */
    public void setTexture(Texture texture)
    {
        this.appearance.setTexture(texture);
    }

}
