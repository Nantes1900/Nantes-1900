package fr.nantes1900.models.decimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Polygon;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.exceptions.ImpossibleProjectionException;
import fr.nantes1900.models.extended.Building;
import fr.nantes1900.models.extended.Roof;
import fr.nantes1900.models.extended.Wall;
import fr.nantes1900.models.islets.AbstractBuildingsIslet;

public final class Recollage {

    private AbstractBuildingsIslet islet;
    private Mesh ground;
    private List<Building> buildings;
    private HashMap<Polygon, Building> bordersToRestick;

    /**
     * Private constructor.
     */
    public Recollage(AbstractBuildingsIslet isletIn) {
        this.islet = isletIn;
        this.ground = this.islet.getBiStep6().getGrounds().getMesh();
    }

    public void launchProcess() {

        this.buildings = this.islet.getBiStep6().getBuildings();
        try {
            rmvTrianglesInsideBuildings();

            findBordersToRestick();
        } catch (ImpossibleProjectionException e) {
            // TODO handle in final integration
            System.err.println("Buildings not well simplified");
            e.printStackTrace();
        }

        System.out.println(this.ground.size());
        projectBordersOnWalls();
        System.out.println(this.ground.size());
        System.out.println(this.ground.size());

        this.ground.refresh();
        System.out.println(this.ground.size());

        // FIXME : make a method.
        Mesh totalSurface = new Mesh();
        for (Building b : this.buildings) {
            for (Wall w : b.getbStep6().getWalls()) {
                if (w.getPolygon() != null) {
                    totalSurface.addAll(w.getPolygon().returnCentroidMesh());
                } else {
                    totalSurface.addAll(w.getMesh());
                }
            }
            for (Roof r : b.getbStep6().getRoofs()) {
                if (r.getPolygon() != null) {
                    totalSurface.addAll(r.getPolygon().returnCentroidMesh());
                } else {
                    totalSurface.addAll(r.getMesh());
                }
            }
        }
        totalSurface.writeSTL("files/resultBuildings.stl");
        totalSurface.addAll(this.ground);
        totalSurface.writeSTL("files/resultTotal.stl");

        System.out.println("Written !");
    }

    /**
     * Associates each building with a border and stores them into the borders
     * to restick map.
     * @return Mesh = border Polygon = building associated with the border
     * @throws ImpossibleProjectionException
     */
    private void findBordersToRestick() throws ImpossibleProjectionException {
        this.bordersToRestick = new HashMap<>();

        // Gets ground borders
        List<Polygon> borders = this.ground.returnSortedBorders();

        removeExternalBorder(borders);

        // Matches buildings with borders
        for (Polygon border : borders) {
            // Finds buildings contained in the border
            int counter = 0;
            Building matchingBuilding = null;
            for (Building building : this.buildings) {
                if (border.containsWithJts(Recollage
                        .getGroundProjection(building.getbStep6().getWalls()))) {
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

    private static void removeExternalBorder(List<Polygon> borders) {
        Polygon externalBorder = null;
        for (Polygon border : borders) {
            if (border.containsAllWithJts(borders)) {
                externalBorder = border;
            }
        }
        borders.remove(externalBorder);
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
                    pClose.set(wallPoint.getPointAsCoordinates());
                }
            }
        }

        System.out.println("Step 3 of resticking done (projection)");
    }

    public static Map<Edge, List<Point>> getEdgePointsMap(List<Point> points,
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

    // FIXME : put in Polygon, or in Point.
    public static Point
            getCloserProjectedPointOnEdge(Point p, List<Edge> edges) {
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

    /**
     * Gets the ground projection of a list of walls which make a building.
     * @param walls
     *            the list of a building's walls
     * @return polygon of the ground projection of walls
     * @throws ImpossibleProjectionException
     */
    private static com.vividsolutions.jts.geom.Polygon getGroundProjection(
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

        List<Edge> orderedPoly = new ArrayList<>();
        Edge e = poly.getOne();
        Mesh.returnNeighbours(poly.getEdgeList(), orderedPoly, e);
        poly = new Polygon(orderedPoly);

        // Transforms into jts structure
        return poly.convertPolygonToJts();
    }

    /**
     * Removes triangles from the ground that have a point inside the building
     * @throws ImpossibleProjectionException
     */
    private void rmvTrianglesInsideBuildings()
            throws ImpossibleProjectionException {
        Mesh toRemove = new Mesh();

        this.ground.writeSTL("files/debug_groundprojbeforeremove.stl");

        for (Building b : this.buildings) {
            com.vividsolutions.jts.geom.Polygon polygon = getGroundProjection(b
                    .getbStep6().getWalls());

            // Looks for each triangle of the ground.
            for (Triangle tri : this.ground) {
                if (polygon.intersects(new Polygon(tri.getEdges())
                        .convertPolygonToJts())) {
                    toRemove.add(tri);
                }
            }
        }

        System.out.println("nombres de triangles à supprimer : "
                + toRemove.size());
        this.ground.remove(toRemove);

        System.out.println("Step 1 of resticking done (triangles inside)");
        System.out.println(this.ground.size() + " triangles restants");
    }

}
