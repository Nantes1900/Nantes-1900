package fr.nantes1900.view.display3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;


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
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
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

    public static final int TRANSLATION_CAMERA_ZDIRECTION=30;
    public static final int PANEL_HEIGHT=600;
    public static final int PANEL_WIDTH=600;
    public static final int LIGHT_BOUND_RADIUS=1000;
    public static final int BOUNDING_RADIUS=1000;
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
        this.setMinimumSize(new Dimension(PANEL_HEIGHT, PANEL_WIDTH));
        this.setPreferredSize(new Dimension(PANEL_HEIGHT, PANEL_WIDTH));

       
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
        } else if (this.u3DController.getDisplayMode() == Universe3DController.DISPLAY_POLYGON_MODE)
        {
            this.displayPolygons(surfaces);
        } else
        {
            System.out.println("Problem");
            // TODO : maybe throw an exception.
        }

        TransformGroup transformGroup = createTransformGroup(this.surfaceViewList);
        this.simpleUniverse.addBranchGraph(this
                .createSceneGraph(transformGroup));

        // Computes the centroid of the first surface.
        Point centroid;
        if (this.u3DController.getDisplayMode() == Universe3DController.DISPLAY_MESH_MODE)

        {

            centroid = surfaces.get(0).getMesh().getCentroid();
        } else
        {
            Polygon polygon = surfaces.get(0).getPolygon();
            centroid = new Point(polygon.xAverage(), polygon.yAverage(),
                    polygon.zAverage());
        }

        // Translates the camera.
        this.translateCamera(centroid.getX(), centroid.getY(),
                centroid.getZ() + TRANSLATION_CAMERA_ZDIRECTION);

        // Changes the rotation center
        this.u3DController.getMouseRotate().setCenter(centroid);

    }

    /**
     * Removes everything displayed !
     */
    public final void clearAll()
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
                0.0), LIGHT_BOUND_RADIUS);
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
                0.0, 0.0), BOUNDING_RADIUS);

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
     * Translate the position of the camera.
     * @param x
     *            The x coordinate of the camera.
     * @param y
     *            The y coordinate of the camera.
     * @param z
     *            The z coordinate of the camera.
     */
    private final void translateCamera(final double x, final double y,
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

    private void displayMeshes(ArrayList<Surface> surfacesList)
    {
        for (Surface surface : surfacesList)
        {
            SurfaceView surfaceView = new SurfaceView();
            MeshView meshView = new MeshView(surface.getMesh());
            surfaceView.setMeshView(meshView);
            this.surfaceViewList.add(surfaceView);
        }
    }

    private void displayPolygons(ArrayList<Surface> surfacesList)
    {
        for (Surface surface : surfacesList)
        {
            SurfaceView surfaceView = new SurfaceView();
            PolygonView polygonView = new PolygonView(surface.getPolygon());
            surfaceView.setPolygonView(polygonView);
            this.surfaceViewList.add(surfaceView);
        }
    }
}
