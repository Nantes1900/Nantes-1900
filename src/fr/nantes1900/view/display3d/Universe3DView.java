package fr.nantes1900.view.display3d;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.TriangleArray;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.mouse.MouseTranslate;
import com.sun.j3d.utils.behaviors.mouse.MouseZoom;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import fr.nantes1900.control.display3d.NewMouseRotate;
import fr.nantes1900.control.display3d.Universe3DController;

/**
 * @author Daniel
 */

public class Universe3DView extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<MeshView> meshesList = new ArrayList<MeshView>();
    private List<PolygonView> polygonsList = new ArrayList<PolygonView>();

    /**
     * The Universe3DController attached
     */
    private Universe3DController u3DController;

    /**
     * The universe.
     */
    private SimpleUniverse simpleUniverse;

    public SimpleUniverse getSimpleUniverse() {
        return this.simpleUniverse;
    }

    public void setSimpleUniverse(SimpleUniverse simpleUniverse) {
        this.simpleUniverse = simpleUniverse;
    }

    /**
     * Creates a new universe.
     * @param Universe3DController
     */
    public Universe3DView(Universe3DController u3DController) {

        this.u3DController = u3DController;
        this.setLayout(new BorderLayout());

        Canvas3D c = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        this.add(c, BorderLayout.CENTER);

        // Setups the SimpleUniverse, attachss the Canvas3D
        this.simpleUniverse = new SimpleUniverse(c);

        // Size to show the panel while there is nothing to show
        this.setMinimumSize(new Dimension(600, 600));
        this.setPreferredSize(new Dimension(600, 600));
    }

    public List<MeshView> getMeshesList() {
        return this.meshesList;
    }

    public List<PolygonView> getPolygonsList() {
        return this.polygonsList;
    }

    @SuppressWarnings("static-method")
    private BranchGroup createSceneGraph(TransformGroup transformGroup) {

        BranchGroup objRoot = new BranchGroup();
        objRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        objRoot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

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
        // ///////////////////

        objRoot.compile();
        return objRoot;
    }

    /**
     * Removes everything displayed !
     */
    public void clearAllMeshes() {
        Canvas3D c = this.simpleUniverse.getCanvas();
        this.simpleUniverse.cleanup();
        this.simpleUniverse = new SimpleUniverse(c);
        c.getView().setBackClipDistance(1000);
    }

    /**
     * Adds a mesh to the things displayed...
     * @param meshView
     */

    public void addMesh(MeshView meshView) {

        TransformGroup transformGroup = createTransformGroup(meshView);
        this.simpleUniverse.addBranchGraph(this
                .createSceneGraph(transformGroup));
        // moving the camera so that we can see the mesh properly
        translateCamera(meshView.getCentroid().getX(), meshView.getCentroid()
                .getY(), meshView.getCentroid().getZ() + 30);
        // changing the rotation center
        this.u3DController.getMouseRotate().setCenter(
                new Point3d(meshView.getCentroid().getX(), meshView
                        .getCentroid().getY(), meshView.getCentroid().getZ()));
    }

    /**
     * Adds a mesh to the things displayed...
     * @param polygonView
     */
    public void addPolygon(PolygonView polygonView) {
        // TODO Auto-generated method stub
    }

    private TransformGroup createTransformGroup(MeshView meshView) {
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

        Shape3D shape = new Shape3D();

        for (TriangleArray ta : meshView) {
            shape.addGeometry(ta);
        }

        // FIXME
        /*
         * Appearance app = new Appearance(); Material mat = null; if (color ==
         * Color.blue) { mat = new Material(new Color3f(0f, 0, 0.2f), new
         * Color3f(0, 0, 0), new Color3f(Color.blue), new Color3f(Color.blue),
         * 64); } else if (color == Color.red) { mat = new Material(new
         * Color3f(0.2f, 0, 0), new Color3f(0, 0, 0), new Color3f(Color.red),
         * new Color3f(Color.red), 64); } mat.setColorTarget(3);
         * app.setMaterial(mat); this.setAppearance(app);
         */

        shape.setCapability(Shape3D.ALLOW_APPEARANCE_READ);
        shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);

        sceneRoot.addChild(shape);

        // FIXME too !
        // shape.setAppearance(app);
        translationGroup2.addChild(sceneRoot);

        // Links the left button of the mouse with a rotation transformation
        NewMouseRotate mouseRotate = new NewMouseRotate(translationGroup1,
                rotationGroup, translationGroup2);
        mouseRotate.setSchedulingBounds(boundingSphere);
        translationGroup2.addChild(mouseRotate);
        this.u3DController.setMouseRotate(mouseRotate);

        // Links the middle button of the mouse with a zoom transformation
        MouseZoom mouseZoom = new MouseZoom();
        mouseZoom.setTransformGroup(transformGroup);
        transformGroup.addChild(mouseZoom);
        mouseZoom.setSchedulingBounds(boundingSphere);

        // Links the right button of the mouse with a translation transformation
        MouseTranslate mouseTranslate = new MouseTranslate();
        mouseTranslate.setTransformGroup(transformGroup);
        transformGroup.addChild(mouseTranslate);
        mouseTranslate.setSchedulingBounds(boundingSphere);

        return transformGroup;
    }

    public void translateCamera(double x, double y, double z) {
        ViewingPlatform camera = this.simpleUniverse.getViewingPlatform();
        TransformGroup cameraTransformGroup = camera.getMultiTransformGroup()
                .getTransformGroup(0);
        Transform3D cameraTranslation = new Transform3D();
        cameraTransformGroup.getTransform(cameraTranslation);
        cameraTranslation.setTranslation(new Vector3d(x, y, z));
        cameraTransformGroup.setTransform(cameraTranslation);
    }
}
