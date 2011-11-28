package fr.nantes1900.view.display3d;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Texture;
import javax.media.j3d.TextureAttributes;

public class SurfaceView extends Shape3D
{
    private MeshView    meshView;
    private PolygonView polygonView;
    private Appearance  appearance = new Appearance();

    public SurfaceView()
    {
        super();

        this.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        this.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

        this.appearance.setCapability(Appearance.ALLOW_MATERIAL_READ);
        this.appearance.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

        TextureAttributes texAtt = new TextureAttributes();
        texAtt.setTextureMode(TextureAttributes.MODULATE);
        this.appearance.setTextureAttributes(texAtt);

        this.setAppearance(appearance);
    }

    public void setMeshView(MeshView meshView)
    {
        this.meshView = meshView;
        this.setGeometry(this.meshView);
    }

    public MeshView getMeshView()
    {
        return this.meshView;
    }

    public void setPolygonView(PolygonView polygonView)
    {
        this.polygonView = polygonView;
        this.setGeometry(this.polygonView);
    }

    public PolygonView getPolygonView()
    {
        return this.polygonView;
    }

    public void setMaterial(Material material)
    {
        this.appearance.setMaterial(material);

    }

    public void setTexture(Texture texture)
    {
        this.appearance.setTexture(texture);
    }

}
