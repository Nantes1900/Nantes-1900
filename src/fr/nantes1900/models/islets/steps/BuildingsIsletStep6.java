package fr.nantes1900.models.islets.steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.tree.DefaultMutableTreeNode;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.decimation.Decimator;
import fr.nantes1900.models.exceptions.ImpossibleProjectionException;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;

/**
 * Implements a step of the process. This step is after the determination of the
 * neighbours and before the sort of the neighbours.
 * 
 * @author Daniel Lefèvre
 */
public class BuildingsIsletStep6 extends AbstractBuildingsIsletStep {

	/**
	 * The list of buildings.
	 */
	private List<Building> buildings;
	/**
	 * The grounds.
	 */
	private Ground grounds;

	/**
	 * Association of borders and building they sould be resticked with.
	 */
	private HashMap<Polygon, Building> bordersToRestick;

	/**
	 * Constructor.
	 * 
	 * @param buildingsIn
	 *            the list of buildings
	 * @param groundsIn
	 *            te grounds
	 */
	public BuildingsIsletStep6(final List<Building> buildingsIn,
			final Ground groundsIn) {
		this.buildings = buildingsIn;
		this.grounds = groundsIn;
	}

	/**
	 * Getter.
	 * 
	 * @return the list of buildings
	 */
	public final List<Building> getBuildings() {
		return this.buildings;
	}

	/**
	 * Getter.
	 * 
	 * @return the grounds
	 */
	public final Ground getGrounds() {
		return this.grounds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
	 * #launchProcess()
	 */
	@Override
	public final BuildingsIsletStep7 launchProcess() {
		// decimation
		Decimator decim = new Decimator(grounds.getMesh());
		decim.launchDecimation();
		System.out.println("Decimation finished.");

		try {
			rmvTrianglesInsideBuildings();

			findBordersToRestick();
		} catch (ImpossibleProjectionException e) {
			// TODO handle in final integration
			System.err.println("Buildings not well simplified");
			e.printStackTrace();
		}

		projectBordersOnWalls();

		this.grounds.getMesh().refresh();

		return new BuildingsIsletStep7(this.buildings, this.grounds);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
	 * #returnNode()
	 */
	@Override
	public final DefaultMutableTreeNode returnNode() {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(this);

		int counter = 0;
		for (Building b : this.buildings) {
			root.add(b.returnNode6(counter));
			counter++;
		}

		if (!this.getGrounds().getMesh().isEmpty()) {
			this.grounds.setNodeString("Sols");
			root.add(new DefaultMutableTreeNode(this.grounds));
		}

		return root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.nantes1900.models.islets.steps.AbstractBuildingsIsletStep
	 * #toString()
	 */
	@Override
	public final String toString() {
		return super.toString() + AbstractBuildingsIslet.SIXTH_STEP;
	}

	/**
	 * Removes triangles from the ground that have a point inside the building
	 * 
	 * @throws ImpossibleProjectionException
	 */
	private void rmvTrianglesInsideBuildings()
			throws ImpossibleProjectionException {
		Mesh toRemove = new Mesh();

		for (Building b : this.buildings) {
            com.vividsolutions.jts.geom.Polygon polygon = getGroundProjection(b
                    .getbStep6().getWalls());

            System.out.println(polygon);

            // Looks for each triangle of the ground.
            for (Triangle tri : this.grounds.getMesh()) {
                if (polygon.intersects(new Polygon(tri.getEdges())
                        .convertPolygonToJts())) {
                    toRemove.add(tri);
                }
            }
        }

        System.out.println("nombres de triangles à supprimer : "
                + toRemove.size());
        this.grounds.getMesh().remove(toRemove);
        toRemove.writeSTL("lol.stl");

        System.out.println("Step 1 of resticking done (triangles inside)");
        System.out.println(this.grounds.getMesh().size() + " triangles restants");
	}

	/**
	 * Associates each building with a border and stores them into the borders
	 * to restick map.
	 * 
	 * @return Mesh = border Polygon = building associated with the border
	 * @throws ImpossibleProjectionException
	 */
	private void findBordersToRestick() throws ImpossibleProjectionException {
		this.bordersToRestick = new HashMap<>();

        // Gets ground borders
        List<Polygon> borders = this.grounds.getMesh().returnSortedBorders();

        removeExternalBorder(borders);

        // Matches buildings with borders
        for (Polygon border : borders) {
            // Finds buildings contained in the border
            int counter = 0;
            Building matchingBuilding = null;
            for (Building building : this.buildings) {
                if (border.containsWithJts(getGroundProjection(building.getbStep6().getWalls()))) {
                    counter++;
                    matchingBuilding = building;
                }
            }

            // If the border contains only one building, we can associate them
            if (counter == 1) {
                this.bordersToRestick.put(border, matchingBuilding);
                System.out.println("one more border found");
            }
        }
        System.out.println(this.bordersToRestick.size()
                + " frontières à recoller trouvées");
	}

	/**
	 * Projects every edge of a border to the lowest edge of the closest wall.
	 * Needs as previous step that borders and building have been associated.
	 */
	private void projectBordersOnWalls() {
		Set<Polygon> borders = this.bordersToRestick.keySet();
        for (Polygon border : borders) {
            List<Wall> walls = this.bordersToRestick.get(border).getbStep6()
                    .getWalls();

            // Gets the basis of the building
            // FIXME : why don't you use getGroundProjection ?
            List<Edge> downEdges = new ArrayList<>();
            for (Wall wall : walls) {
                downEdges.add(wall.getPolygon().getDownEdge());
            }

            for (Point p : border.getPointList()) {
                Point pProj = getCloserProjectedPointOnEdge(p, downEdges);
                p.set(pProj.getPointAsCoordinates());
            }

            // FIXME : et les triangles qui vont être inversés ??? Peut être
            // supprimer le triangle qui va être inversé, car l'autre va le
            // recouvrir, et ça sera bon quand même, non ?

            Map<Edge, List<Point>> map = getEdgePointsMap(
                    border.getPointList(), downEdges);

            // Fixes angles issues.
            for (Edge edge : downEdges) {
                Point wallPoint = edge.getP1();
                // If it is empty, it means that this edge is probably no
                // touching the ground.
                if (!map.get(edge).isEmpty()) {
                    Point pClose = wallPoint.getCloser(map.get(edge));
                    System.out.println(pClose + " set in " + wallPoint);
                    List<Triangle> list = pClose.getTriangles();

                    if (!testAlreadyContainingSamePoint(list, wallPoint)) {
                        pClose.set(wallPoint.getPointAsCoordinates());
                    }
                }
            }
        }

        System.out.println("Step 3 of resticking done (projection)");
	}

	public static boolean testAlreadyContainingSamePoint(List<Triangle> list,
            Point point) {
        for (Triangle t : list) {
            if (t.contains(point)) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * Gets the ground projection of a list of walls which make a building.
	 * 
	 * @param walls
	 *            the list of a building's walls
	 * @return polygon of the ground projection of walls
	 * @throws ImpossibleProjectionException
	 */
	private com.vividsolutions.jts.geom.Polygon getGroundProjection(
			List<Wall> walls) throws ImpossibleProjectionException {
		Polygon poly = new Polygon();
        for (Wall w : walls) {
            // Checks if the wall is well simplified
            if (w.getPolygon() == null
                    || w.getPolygon().getPointList().size() <= 2) {
                throw new ImpossibleProjectionException();
            }

            // Gets the lowest edge of the wall
            Edge wallEdge = w.getPolygon().getDownEdge();
            if (wallEdge == null) {
                throw new ImpossibleProjectionException();
            }

            poly.add(wallEdge);
        }

        // FIXME : si tu tombes sur un mur qui est tout seul (un mur en hauteur
        // par exemple) ?
        List<Edge> orderedPoly = new ArrayList<>();
        Edge e = poly.getOne();
        e.returnNeighbours(poly.getEdgeList(), orderedPoly);
        poly = new Polygon(orderedPoly);

        // Transforms into jts structure
        return poly.convertPolygonToJts();
	}

	private void removeExternalBorder(List<Polygon> borders) {
		Polygon externalBorder = null;
		for (Polygon border : borders) {
			if (border.containsAllWithJts(borders)) {
				externalBorder = border;
			}
		}
		borders.remove(externalBorder);
	}

	// FIXME : put in Polygon, or in Point.
	public static Point getCloserProjectedPointOnEdge(Point p, List<Edge> edges) {
		Point pProj = edges.get(0).getP1();
		Point pProjCurrent;
		double distance = edges.get(0).getP1().distance(p);

		for (Edge downEdge : edges) {
			pProjCurrent = downEdge.project(p);
			if (pProjCurrent.distance(p) < distance) {
				pProj = pProjCurrent;
				distance = pProjCurrent.distance(p);
			}
		}

		return pProj;
	}

	private static Map<Edge, List<Point>> getEdgePointsMap(List<Point> points,
			List<Edge> edges) {

		Map<Edge, List<Point>> map = new HashMap<>();
		for (Edge edge : edges) {
			map.put(edge, new ArrayList<Point>());
		}
		Point proj;

		for (Point point : points) {
			for (Edge edge : edges) {
				proj = edge.project(point);
				if (!proj.equals(edge.getP1()) && !proj.equals(edge.getP2())) {
					map.get(edge).add(point);
				}
			}
		}

		return map;
	}
}
