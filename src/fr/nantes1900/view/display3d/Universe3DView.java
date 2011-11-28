package fr.nantes1900.view.display3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Texture2D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import fr.nantes1900.control.display3d.NewMouseRotate;
import fr.nantes1900.control.display3d.Universe3DController;
import fr.nantes1900.models.extended.Surface;

/**
 * TODO.
 * @author TODO.
 */
public class Universe3DView extends JPanel
{

    /**
     * Version ID.
     */
    private static final long      serialVersionUID    = 1L;

    /**
     * The list to save all the surfaceView.
     */
    private ArrayList<SurfaceView> surfaceViewList     = new ArrayList<>();

    /**
     * The Universe3DController attached.
     */
    private Universe3DController   u3DController;

    /**
     * The universe.
     */
    private SimpleUniverse         simpleUniverse;

    /**
     * The material of the mesh non-selected.
     */
    public static final Material   MATERIAL_SELECTED   = new Material(
                                                               new Color3f(
                                                                       1.0f,
                                                                       1.0f,
                                                                       1.0f),
                                                               new Color3f(
                                                                       1.0f,
                                                                       1.0f,
                                                                       1.0f),
                                                               new Color3f(
                                                                       Color.white),
                                                               new Color3f(
                                                                       Color.white),
                                                               64);

    /**
     * The material of the mesh selected.
     */
    public static final Material   MATERIAL_UNSELECTED = new Material(
                                                               new Color3f(
                                                                       0.2f,
                                                                       0.0f,
                                                                       0.2f),
                                                               new Color3f(
                                                                       0.0f,
                                                                       0.0f,
                                                                       0.0f),
                                                               new Color3f(
                                                                       Color.blue),
                                                               new Color3f(
                                                                       Color.blue),
                                                               64);

    /**
     * Creates a new universe.
     * @param u3DControllerIn
     *            TODO.
     */
    public Universe3DView(final Universe3DController u3DControllerIn)
    {

        this.u3DController = u3DControllerIn;
        this.setLayout(new BorderLayout());

        Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        this.add(c, BorderLayout.CENTER);

        // Setups the SimpleUniverse, attachss the Canvas3D
        this.simpleUniverse = new SimpleUniverse(c);

        // Size to show the panel while there is nothing to show
        this.setMinimumSize(new Dimension(600, 600));
        this.setPreferredSize(new Dimension(600, 600));

        // this.transformGroup = createTransformGroup();
    }

    /**
     * Adds a mesh to the things displayed...
     * @param meshView
     *            TODO.
     */

    public final void addSurface(final ArrayList<Surface> surfaces)
    {
        if (this.u3DController.getDisplayMode() == Universe3DController.DISPLAY_MESH_MODE)
        {
            this.displayMeshes(surfaces);
        } else
        {
            this.displayPolygons(surfaces);
        }

        TransformGroup transformGroup = createTransformGroup(this.surfaceViewList);
        this.simpleUniverse.addBranchGraph(this
                .createSceneGraph(transformGroup));

        if (this.u3DController.getDisplayMode() == Universe3DController.DISPLAY_MESH_MODE)
        {
            translateCamera(surfaces.get(0).getMesh().xAverage(),
                    surfaces.get(0).getMesh().yAverage(), surfaces.get(0)
                            .getMesh().zAverage() + 30);
            // changing the rotation center
            this.u3DController.getMouseRotate().setCenter(
                    new Point3d(surfaces.get(0).getMesh().xAverage(), surfaces
                            .get(0).getMesh().yAverage(), surfaces.get(0)
                            .getMesh().zAverage()));
        } else
        {
            translateCamera(surfaces.get(0).getPolygone().xAverage(), surfaces
                    .get(0).getPolygone().yAverage(), surfaces.get(0)
                    .getPolygone().zAverage() + 30);
            // changing the rotation center
            this.u3DController.getMouseRotate().setCenter(
                    new Point3d(surfaces.get(0).getPolygone().xAverage(),
                            surfaces.get(0).getPolygone().yAverage(), surfaces
                                    .get(0).getPolygone().zAverage()));
        }
    }

    /**
     * Removes everything displayed !
     */
    public final void clearAllMeshes()
    {
        Canvas3D c = this.simpleUniverse.getCanvas();
        this.simpleUniverse.cleanup();
        this.simpleUniverse = new SimpleUniverse(c);
        c.getView().setBackClipDistance(1000);
    }

    /**
     * TODO.
     * @param transformGroup
     *            TODO.
     * @return TODO.
     */
    private BranchGroup createSceneGraph(final TransformGroup transformGroup)
    {
        BranchGroup objRoot = new BranchGroup();
        objRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        objRoot.setCapability(Group.ALLOW_CHILDREN_EXTEND);
        objRoot.setCapability(Group.ALLOW_CHILDREN_WRITE);

        this.u3DController.setPickCanvas(objRoot);

        // //////////////// Lights
        // Light bound
        BoundingSphere lightBounds = new BoundingSphere(new Point3d(0.0, 0.0,
                0.0), 100000.0);
        // Ambient light
        AmbientLight ambLight = new AmbientLight(true, new Color3f(1.0f, 1.0f,
                1.0f));
        ambLight.setInfluencingBounds(lightBounds);
        // Directional light
        DirectionalLight headLight = new DirectionalLight(new Color3f(
                Color.white), new Vector3f(1.0f, -1.0f, -1.0f));
        headLight.setInfluencingBounds(lightBounds);

        objRoot.addChild(ambLight);
        objRoot.addChild(headLight);

        objRoot.addChild(transformGroup);

        objRoot.compile();
        return objRoot;
    }

    /**
     * TODO.
     * @param meshView
     *            TODO.
     * @return TODO.
     */

    private TransformGroup createTransformGroup(
            final ArrayList<SurfaceView> surfaceView)
    {
        BoundingSphere boundingSphere = new BoundingSphere(new Point3d(0.0,
                0.0, 0.0), 100000);

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        TransformGroup translationGroup1 = new TransformGroup();
        translationGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        translationGroup1.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        transformGroup.addChild(translationGroup1);

        TransformGroup rotationGroup = new TransformGroup();
        rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        translationGroup1.addChild(rotationGroup);

        TransformGroup translationGroup2 = new TransformGroup();
        translationGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        translationGroup2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotationGroup.addChild(translationGroup2);

        BranchGroup sceneRoot = new BranchGroup();

        // Read the texture.
        TextureLoader loader = new TextureLoader("texture.jpg", null);
        ImageComponent2D image = loader.getImage();
        Texture2D texture = new Texture2D(Texture2D.BASE_LEVEL, Texture2D.RGB,
                image.getWidth(), image.getHeight());
        texture.setImage(0, image);

        // Create the appearance.
        Appearance app = new Appearance();

        app.setCapability(Appearance.ALLOW_MATERIAL_READ);
        app.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

        // for (MeshView mesh : meshView) {
        // SurfaceView surfaceView=new SurfaceView();
        // surfaceView.setMeshView(mesh);
        // surfaceView.setTexture(texture);
        // surfaceView.setMaterial()
        // this.surfaceView.add(surfaceView);
        //
        // }

        for (SurfaceView surface : this.surfaceViewList)
        {
            sceneRoot.addChild(surface);
        }

        translationGroup2.addChild(sceneRoot);

        // Links the left button of the mouse with a rotation transformation
        NewMouseRotate mouseRotate = new NewMouseRotate(translationGroup1,
                rotationGroup, translationGroup2);
        mouseRotate.setSchedulingBounds(boundingSphere);
        translationGroup2.addChild(mouseRotate);
        this.u3DController.setMouseRotate(mouseRotate);

        // Links the middle button of the mouse with a zoom transformation
        MouseZoom mouseZoom = new MouseZoom();
        mouseZoom.setFactor(2);
        mouseZoom.setTransformGroup(transformGroup);
        transformGroup.addChild(mouseZoom);
        mouseZoom.setSchedulingBounds(boundingSphere);

        // Links the right button of the mouse with a translation transformation
        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setFactor(1.5);
        mouseTranslate.setTransformGroup(transformGroup);
        transformGroup.addChild(mouseTranslate);
        mouseTranslate.setSchedulingBounds(boundingSphere);

        return transformGroup;
    }

    /**
     * Getter.
     * @return the simple universe
     */
    public final SimpleUniverse getSimpleUniverse()
    {
        return this.simpleUniverse;
    }

    /**
     * Setter.
     * @param simpleUniverseIn
     *            the new simple universe
     */
    public final void setSimpleUniverse(final SimpleUniverse simpleUniverseIn)
    {
        this.simpleUniverse = simpleUniverseIn;
    }

    /**
     * Translate the position of the camera.
     * @param x
     *            The x coordinate of the camera.
     * @param y
     *            The y coordinate of the camera.
     * @param z
     *            The z coordinate of the camera.
     */
    public final void translateCamera(final double x, final double y,
            final double z)
    {
        // Get the camera.
        ViewingPlatform camera = this.simpleUniverse.getViewingPlatform();
        TransformGroup cameraTransformGroup = camera.getMultiTransformGroup()
                .getTransformGroup(0);

        Transform3D cameraTranslation = new Transform3D();
        cameraTransformGroup.getTransform(cameraTranslation);
        // Set the position of the camera.
        cameraTranslation.setTranslation(new Vector3d(x, y, z));
        cameraTransformGroup.setTransform(cameraTranslation);
    }

    public void displayMeshes(ArrayList<Surface> surfacesList)
    {
        for (Surface surface : surfacesList)
        {
            SurfaceView surfaceView = new SurfaceView();
            MeshView meshView = new MeshView(surface.getMesh());
            surfaceView.setMeshView(meshView);
            this.surfaceViewList.add(surfaceView);
        }
    }

    public void displayPolygons(ArrayList<Surface> surfacesList)
    {
        for (Surface surface : surfacesList)
        {
            SurfaceView surfaceView = new SurfaceView();
            PolygonView polygonView = new PolygonView(surface.getPolygone());
            surfaceView.setPolygonView(polygonView);
            this.surfaceViewList.add(surfaceView);
        }
    }
}
