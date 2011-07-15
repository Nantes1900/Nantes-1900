package modeles;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

import utils.MatrixMethod;
import utils.WriterSTL;

/**
 * @author Daniel Lefevre, Eric Berthe, Valentin Roger, Elsa Arroud-Vignod
 * 
 */
public class Mesh extends HashSet<Triangle> {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public Mesh() {
		super();
	}

	/**
	 * @param c
	 */
	public Mesh(Collection<? extends Triangle> c) {
		super(c);
	}

	/**
	 * Compute the average normal of all Faces composing this Ensemble
	 * 
	 * @return average The average Vector3d normal.
	 */
	public Vector3d averageNormal() {
		int n = this.size();
		Vector3d average = new Vector3d();
		for (Triangle face : this) {
			average.add(face.getNormal());
		}
		average.scale(1 / (double) n);

		return average;
	}

	/**
	 * @return
	 */
	public double xAverage() {
		double xAverage = 0;
		for (Triangle face : this) {
			xAverage += face.xAverage();
		}
		return xAverage / this.size();
	}

	/**
	 * @return
	 */
	public double yAverage() {
		double yAverage = 0;
		for (Triangle face : this) {
			yAverage += face.yAverage();
		}
		return yAverage / this.size();
	}

	/**
	 * Compute the average z-coordinate of all points of all faces from this
	 * Ensemble
	 * 
	 * @return the average z-coordinate of all points
	 */
	public double zAverage() {
		double zAverage = 0;
		for (Triangle face : this) {
			zAverage += face.zAverage();
		}
		return zAverage / this.size();
	}

	/**
	 * @return
	 */
	public double xMax() {
		double xMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.xMax() > xMaxi) {
				xMaxi = face.xMax();
			}
		}
		return xMaxi;
	}

	/**
	 * @return
	 */
	public double xMin() {
		double xMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.xMin() < xMini) {
				xMini = face.xMin();
			}
		}
		return xMini;
	}

	/**
	 * @return
	 */
	public double yMax() {
		double yMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.yMax() > yMaxi) {
				yMaxi = face.yMax();
			}
		}
		return yMaxi;
	}

	/**
	 * @return
	 */
	public double yMin() {
		double yMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.yMin() < yMini) {
				yMini = face.yMin();
			}
		}
		return yMini;
	}

	/**
	 * @return
	 */
	public double zMax() {
		double zMaxi = Double.NEGATIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMax() > zMaxi) {
				zMaxi = face.zMax();
			}
		}
		return zMaxi;
	}

	/**
	 * @return
	 */
	public double zMin() {
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMin() < zMini) {
				zMini = face.zMin();
			}
		}
		return zMini;
	}

	/**
	 * @return
	 */
	public double xLengthAverage() {
		double xLengthAve = 0;
		for (Triangle t : this) {
			xLengthAve += (t.xMax() - t.xMin());
		}
		return xLengthAve / (double) this.size();
	}

	/**
	 * @return
	 */
	public double yLengthAverage() {
		double yLengthAve = 0;
		for (Triangle t : this) {
			yLengthAve += (t.yMax() - t.yMin());
		}
		return yLengthAve / (double) this.size();
	}

	/**
	 * @return
	 */
	public double zLengthAverage() {
		double zLengthAve = 0;
		for (Triangle t : this) {
			zLengthAve += (t.zMax() - t.zMin());
		}
		return zLengthAve / (double) this.size();
	}

	/**
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Mesh xBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.xMax() < Math.max(m1, m2) && t.xMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	/**
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Mesh yBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.yMax() < Math.max(m1, m2) && t.yMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	/**
	 * @param m1
	 * @param m2
	 * @return
	 */
	public Mesh zBetween(double m1, double m2) {
		Mesh ens = new Mesh();
		for (Triangle t : this) {
			if (t.zMax() < Math.max(m1, m2) && t.zMin() > Math.min(m1, m2))
				ens.add(t);
		}
		return ens;
	}

	// TODO : take care of the fact that new Triangle without order, and without
	// neighbours are created !
	// Caution !
	// FIXME : the actual key table is destroyed :)
	/**
	 * @param x
	 * @return
	 */
	public Mesh xProjection(double x) {
		Mesh e = new Mesh();
		for (Triangle t : this) {
			e.add(t.xProjection(x));
		}
		return e;
	}

	/**
	 * @param y
	 * @return
	 */
	public Mesh yProjection(double y) {
		Mesh e = new Mesh();
		for (Triangle t : this) {
			e.add(t.yProjection(y));
		}
		return e;
	}

	/**
	 * @param z
	 * @return
	 */
	public Mesh zProjection(double z) {
		Mesh e = new Mesh();
		for (Triangle t : this) {
			e.add(t.zProjection(z));
		}
		return e;
	}

	/**
	 * @return
	 */
	public Triangle zMinFace() {
		Triangle t = null;
		if (this.isEmpty())
			throw new InvalidParameterException("Empty mesh !");
		double zMini = Double.POSITIVE_INFINITY;
		for (Triangle face : this) {
			if (face.zMin() < zMini) {
				t = face;
				zMini = face.zMin();
			}
		}
		return t;
	}

	// TODO : improve the velocity of this method !

	/**
	 * @param zMax
	 * @return
	 */
	public Triangle faceUnderZ(double zMax) {
		for (Triangle t : this) {
			if (t.zMax() < zMax) {
				return t;
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Triangle getOne() {
		return this.iterator().next();
	}

	/**
	 * @param m
	 */
	public void remove(Mesh m) {
		this.removeAll(m);
	}

	/**
	 * @param fileName
	 */
	public void write(String fileName) {
		WriterSTL.write(fileName, this);
	}

	/**
	 * @param normal
	 * @param error
	 * @return
	 */
	public Mesh orientedAs(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for (Triangle f : this) {
			if (f.angularTolerance(normal, error))
				ret.add(f);
		}
		return ret;
	}

	// Caution : this is not in degrees
	// The factor is between 0 and 1.
	/**
	 * @param normal
	 * @param error
	 * @return
	 */
	public Mesh orientedNormalTo(Vector3d normal, double error) {
		Mesh ret = new Mesh();
		for (Triangle f : this) {
			if (f.isNormalTo(normal, error))
				ret.add(f);
		}
		return ret;
	}

	/**
	 * @param matrix
	 */
	public void changeBase(double[][] matrix) {
		if (matrix == null)
			throw new InvalidParameterException();

		HashSet<Point> set = new HashSet<Point>();
		HashSet<Triangle> mesh = new HashSet<Triangle>();

		for (Triangle f : this) {
			set.addAll(f.getPoints());
			MatrixMethod.changeBase(f.getNormal(), matrix);
			mesh.add(f);
		}

		for (Point p : set)
			p.changeBase(matrix);

		this.clear();
		this.addAll(mesh);
	}

	// FIXME : à améliorer !
	/**
	 * @return
	 */
	public Polyline returnUnsortedBounds() {
		Polyline bounds = new Polyline();
		for (Triangle tri : this) {
			ArrayList<Edge> edges = new ArrayList<Edge>(tri.getEdges());
			for (Edge e : edges) {
				int counter = 0;
				for (Triangle t : e.getTriangleList()) {
					if (this.contains(t))
						counter++;
				}
				// Add the edges which belong to only one triangle in this.
				if (counter == 1)
					bounds.add(e);
			}
		}
		return bounds;
	}

	/**
	 * @return
	 */
	public ArrayList<Polyline> returnBounds(Vector3d normalFloor) {
		Polyline bounds = this.returnUnsortedBounds();

		ArrayList<Polyline> boundList = new ArrayList<Polyline>();

		while (!bounds.isEmpty()) {
			Edge arete = bounds.getOne();
			Polyline ret = new Polyline();

			Point point, p31 = null, p32 = null;
			if (arete.getNumberTriangles() != 2) {
				// LOOK
				System.err.println("Encore une erreur !");
			}
			for (Point p : arete.getTriangleList().get(0).getPoints()) {
				if (!arete.contains(p)) {
					p31 = p;
				}
			}
			for (Point p : arete.getTriangleList().get(1).getPoints()) {
				if (!arete.contains(p)) {
					p32 = p;
				}
			}
			Vector3d vect = new Vector3d();
			Vector3d v31 = new Vector3d();
			Vector3d v32 = new Vector3d();
			vect.x = arete.getP2().getX() - arete.getP1().getX();
			vect.y = arete.getP2().getY() - arete.getP1().getY();
			vect.z = arete.getP2().getZ() - arete.getP1().getZ();

			v31.x = p31.getX() - arete.getP1().getX();
			v31.y = p31.getY() - arete.getP1().getY();
			v31.z = p31.getZ() - arete.getP1().getZ();

			v32.x = p32.getX() - arete.getP1().getX();
			v32.y = p32.getY() - arete.getP1().getY();
			v32.z = p32.getZ() - arete.getP1().getZ();

			Vector3d cross = new Vector3d();
			cross.cross(normalFloor, vect);

			if (!this.contains(arete.getTriangleList().get(0))) {
				if (cross.dot(v31) > 0) {
					point = arete.getP2();
				} else {
					point = arete.getP1();
				}
			} else {
				if (cross.dot(v32) > 0) {
					point = arete.getP2();
				} else {
					point = arete.getP1();
				}
			}

			try {
				arete.returnOneBound(this, ret, bounds, point, normalFloor);

				boundList.add(ret);

				// FIXME : create new Exception !
			} catch (InvalidActivityException e) {
				// Don't do anything.
			}

			// FIXME : la méthode remove ne supprime pas les Points,
			// seulement les Edges.
			bounds.remove(ret);
		}

		return boundList;
	}

	/**
	 * @return
	 */
	public Polyline returnLongestBound(Vector3d normalFloor) {
		//FIXME
		ArrayList<Polyline> boundList = this.returnBounds(normalFloor);
		Polyline ret = null;

		double max = Double.MIN_VALUE;
		for (Polyline p : boundList) {
			if (p.length() > max) {
				max = p.length();
				ret = p;
			}
		}

		return ret;
	}
}