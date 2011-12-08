package fr.nantes1900.control.display3d;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupCriterion;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.media.j3d.WakeupOnBehaviorPost;
import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import com.sun.j3d.utils.behaviors.mouse.MouseBehaviorCallback;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;

import fr.nantes1900.models.basis.Point;

/**
 * NewMouseRotate is a class extended of the class MouseRotate. It is used to
 * make a rotation of the object by using the mouse.
 * @author Siju Wu & Nicolas Bouillon
 */
public class NewMouseRotate extends MouseRotate {

    /**
     * The abscissa projection of the rotation angle.
     */
    private double xAngle;
    /**
     * The ordinate projection of the rotation angle.
     */
    private double yAngle;
    /**
     * The amplification factor of the abscissa mouse movement.
     */
    private double xFactor = .03;
    /**
     * The amplification factor of the ordinate mouse movement.
     */
    private double yFactor = .03;
    /**
     * TODO .
     */
    private MouseBehaviorCallback callback = null;
    /**
     * The transformGroup containing the translation to the universe center.
     */
    private TransformGroup tg1;
    /**
     * The transformGroup containing the rotation.
     */
    private TransformGroup tg2;
    /**
     * The transformGroup containing the reverse translation the initial
     * position of the mesh.
     */
    private TransformGroup tg3;
    /**
     * The translation to the universe center.
     */
    private Transform3D translation1 = new Transform3D();
    /**
     * The reverse translation the initial position.
     */
    private Transform3D translation2 = new Transform3D();
    /**
     * The rotation center.
     */
    private Point3d center;

    /**
     * Constructor.
     * @param tg1In
     *            The transformGroup containing the translation to the universe
     *            center.
     * @param tg2In
     *            The transformGroup containing the rotation.
     * @param tg3In
     *            The transformGroup containing the reverse translation the
     *            initial position of the mesh.
     */
    public NewMouseRotate(final TransformGroup tg1In,
            final TransformGroup tg2In, final TransformGroup tg3In) {
        super();
        this.tg1 = tg1In;
        this.tg2 = tg2In;
        this.tg3 = tg3In;
    }

    /**
     * The override process of left click dragging.
     * @param evt
     *            the mouse event.
     */
    final void doNewProcess(final MouseEvent evt) {
        int id;
        int dx, dy;

        processMouseEvent(evt);
        if (((this.buttonPress) && ((this.flags & MANUAL_WAKEUP) == 0))
                || ((this.wakeUp) && ((this.flags & MANUAL_WAKEUP) != 0))) {
            id = evt.getID();
            if ((id == MouseEvent.MOUSE_DRAGGED) && !evt.isMetaDown()
                    && !evt.isAltDown() && !evt.isShiftDown()) {
                this.x = evt.getX();
                this.y = evt.getY();

                dx = this.x - this.x_last;
                dy = this.y - this.y_last;

                if (!this.reset) {
                    this.xAngle = dy * this.yFactor;
                    this.yAngle = dx * this.xFactor;

                    this.transformX.rotX(this.xAngle);
                    this.transformY.rotY(this.yAngle);

                    this.tg2.getTransform(this.currXform);

                    Matrix4d mat = new Matrix4d();
                    // Remember old matrix
                    this.currXform.get(mat);

                    // Translate to origin
                    this.currXform.setTranslation(new Vector3d(.0, .0, .0));
                    if (this.invert) {
                        this.currXform.mul(this.currXform, this.transformX);
                        this.currXform.mul(this.currXform, this.transformY);
                    } else {
                        this.currXform.mul(this.transformX, this.currXform);
                        this.currXform.mul(this.transformY, this.currXform);
                    }

                    // Set old translation back
                    Vector3d translation = new Vector3d(mat.m03, mat.m13,
                            mat.m23);
                    this.currXform.setTranslation(translation);

                    // Update xform
                    this.tg2.setTransform(this.currXform);

                    transformChanged(this.currXform);

                    if (this.callback != null) {
                        this.callback.transformChanged(
                                MouseBehaviorCallback.ROTATE, this.currXform);
                    }
                } else {
                    this.reset = false;
                }

                this.x_last = this.x;
                this.y_last = this.y;
            } else if (id == MouseEvent.MOUSE_PRESSED) {
                this.x_last = evt.getX();
                this.y_last = evt.getY();
            }
        }
    }

    /**
     * Getter.
     * @return the rotation center.
     */
    public final Point3d getCenter() {
        return this.center;
    }

    @Override
    public final void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] events;
        MouseEvent evt;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();
            if (wakeup instanceof WakeupOnAWTEvent) {
                events = ((WakeupOnAWTEvent) wakeup).getAWTEvent();
                if (events.length > 0) {
                    evt = (MouseEvent) events[events.length - 1];

                    Point3d centerpoint = new Point3d();
                    centerpoint.x = this.center.getX();
                    centerpoint.y = this.center.getY();
                    centerpoint.z = this.center.getZ();

                    this.translation2.transform(centerpoint);
                    this.currXform.transform(centerpoint);
                    this.translation1.transform(centerpoint);

                    Vector3d vector1 = new Vector3d();
                    Vector3d vector2 = new Vector3d();
                    vector1.set(centerpoint.x, centerpoint.y, centerpoint.z);
                    vector2.set(-this.center.getX(), -this.center.getY(),
                            -this.center.getZ());

                    this.translation1.setTranslation(vector1);
                    this.translation2.setTranslation(vector2);

                    this.tg1.setTransform(this.translation1);
                    doNewProcess(evt);
                    this.tg3.setTransform(this.translation2);
                }
            } else if (wakeup instanceof WakeupOnBehaviorPost) {
                while (true) {
                    // access to the queue must be synchronized
                    synchronized (this.mouseq) {
                        if (this.mouseq.isEmpty()) {
                            break;
                        }
                        evt = (MouseEvent) this.mouseq.remove(0);
                        // consolidate MOUSE_DRAG events
                        while ((evt.getID() == MouseEvent.MOUSE_DRAGGED)
                                && !this.mouseq.isEmpty()
                                && (((MouseEvent) this.mouseq.get(0)).getID() == MouseEvent.MOUSE_DRAGGED)) {
                            evt = (MouseEvent) this.mouseq.remove(0);
                        }
                    }
                    doNewProcess(evt);
                }
            }

        }
        wakeupOn(this.mouseCriterion);
    }

    /**
     * Setter.
     * @param point
     *            the rotation center to set.
     */
    public final void setCenter(final Point point) {
        this.center = new Point3d(point.getX(), point.getY(), point.getZ());
    }

}
