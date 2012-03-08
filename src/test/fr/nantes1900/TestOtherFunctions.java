package test.fr.nantes1900;

public class TestOtherFunctions {
    // Test de la recherche des bâtiments avec la frontière du sol associée.
    // ParserSTL parser = new ParserSTL("test.stl");
    // Mesh gravityGround = parser.read();
    // Vector3d gravityNormal = gravityGround.averageNormal();
    // Vector3d groundNormal = gravityGround.averageNormal();
    //
    // parser = new ParserSTL("test2.stl");
    // Mesh total = parser.read();
    //
    // AbstractBuildingsIslet islet = new ResidentialIslet();
    // islet.setBiStep0(new BuildingsIsletStep0(total));
    // islet.setGravityNormal(gravityNormal);
    // islet.setGroundNormal(groundNormal);
    // try {
    // islet.launchProcess0();
    // islet.launchProcess1();
    // } catch (NullArgumentException | WeirdResultException e) {
    // e.printStackTrace();
    // }
    // islet.launchProcess2();
    // Building building = islet.getBiStep3().getBuildings().get(0);
    // Mesh ground = islet.getBiStep3().getGrounds().getMesh();
    //
    // List<Building> list = new ArrayList<>();
    // list.add(building);
    //
    // if (building == findBuilding(ground.returnUnsortedBounds(), list)) {
    // System.out.println(":)");
    // } else {
    // System.out.println(":(");
    // }

    // Tests pour savoir si isInPlanes marche.
    // Point p1 = new Point(0, 0, 0);
    // Point p2 = new Point(0, 0, 1);
    // Point p3 = new Point(0, 1, 1);
    // Point p4 = new Point(0, 1, 0);
    //
    // Point centroid = new Point(0, 0.5, 0.5);
    //
    // Edge e1 = new Edge(p1, p2);
    // Edge e2 = new Edge(p2, p3);
    // Edge e3 = new Edge(p3, p4);
    // Edge e4 = new Edge(p4, p1);
    //
    // Edge e5 = new Edge(p1, centroid);
    // Edge e6 = new Edge(p2, centroid);
    // Edge e7 = new Edge(p3, centroid);
    // Edge e8 = new Edge(p4, centroid);
    //
    // Vector3d norm = new Vector3d(1, 0, 0);
    //
    // Triangle t1 = new Triangle(p1, p2, centroid, e1, e5, e6, norm);
    // Triangle t2 = new Triangle(p2, p3, centroid, e2, e6, e7, norm);
    // Triangle t3 = new Triangle(p3, p4, centroid, e3, e7, e8, norm);
    // Triangle t4 = new Triangle(p4, p1, centroid, e4, e8, e5, norm);
    //
    // Mesh mesh = new Mesh();
    // mesh.add(t1);
    // mesh.add(t2);
    // mesh.add(t3);
    // mesh.add(t4);
    //
    // Polygon polygon = new Polygon();
    // polygon.add(e1);
    // polygon.add(e2);
    // polygon.add(e3);
    // polygon.add(e4);
    //
    // Wall w = new Wall();
    // w.setMesh(mesh);
    // w.setPolygon(polygon);
    //
    // // TESTS !
    // Point pt1 = new Point(0.5, 0.5, 0);
    //
    // System.out.println(isBetweenPlanes(pt1, w)); // TRUE
    //
    // // TESTS !
    // Point pt2 = new Point(0.5, 1.5, 0);
    //
    // System.out.println(isBetweenPlanes(pt2, w)); // FALSE
    //
    // // TESTS !
    // Point pt3 = new Point(0.5, 0.5, 1);
    //
    // System.out.println(isBetweenPlanes(pt3, w)); // TRUE

    // Test de recollage pour des murs factices.

    // // WALL 1
    // Wall w1 = new Wall();
    // {
    // Point p1 = new Point(0, 0, 0);
    // Point p2 = new Point(0, 0, 1);
    // Point p3 = new Point(0, 1, 1);
    // Point p4 = new Point(0, 1, 0);
    //
    // Point centroid = new Point(0, 0.5, 0.5);
    //
    // Edge e1 = new Edge(p1, p2);
    // Edge e2 = new Edge(p2, p3);
    // Edge e3 = new Edge(p3, p4);
    // Edge e4 = new Edge(p4, p1);
    //
    // Edge e5 = new Edge(p1, centroid);
    // Edge e6 = new Edge(p2, centroid);
    // Edge e7 = new Edge(p3, centroid);
    // Edge e8 = new Edge(p4, centroid);
    //
    // Vector3d norm = new Vector3d(-1, 0, 0);
    //
    // Triangle t1 = new Triangle(p1, p2, centroid, e1, e5, e6, norm);
    // Triangle t2 = new Triangle(p2, p3, centroid, e2, e6, e7, norm);
    // Triangle t3 = new Triangle(p3, p4, centroid, e3, e7, e8, norm);
    // Triangle t4 = new Triangle(p4, p1, centroid, e4, e8, e5, norm);
    //
    // Mesh mesh = new Mesh();
    // mesh.add(t1);
    // mesh.add(t2);
    // mesh.add(t3);
    // mesh.add(t4);
    //
    // Polygon polygon = new Polygon();
    // polygon.add(e1);
    // polygon.add(e2);
    // polygon.add(e3);
    // polygon.add(e4);
    //
    // w1.setMesh(mesh);
    // w1.setPolygon(polygon);
    // }
    //
    // // WALL 2
    // Wall w2 = new Wall();
    // {
    // Point p1 = new Point(0, 0, 0);
    // Point p2 = new Point(0, 0, 1);
    // Point p3 = new Point(1, 0, 1);
    // Point p4 = new Point(1, 0, 0);
    //
    // Point centroid = new Point(0.5, 0, 0.5);
    //
    // Edge e1 = new Edge(p1, p2);
    // Edge e2 = new Edge(p2, p3);
    // Edge e3 = new Edge(p3, p4);
    // Edge e4 = new Edge(p4, p1);
    //
    // Edge e5 = new Edge(p1, centroid);
    // Edge e6 = new Edge(p2, centroid);
    // Edge e7 = new Edge(p3, centroid);
    // Edge e8 = new Edge(p4, centroid);
    //
    // Vector3d norm = new Vector3d(0, -1, 0);
    //
    // Triangle t1 = new Triangle(p1, p2, centroid, e1, e5, e6, norm);
    // Triangle t2 = new Triangle(p2, p3, centroid, e2, e6, e7, norm);
    // Triangle t3 = new Triangle(p3, p4, centroid, e3, e7, e8, norm);
    // Triangle t4 = new Triangle(p4, p1, centroid, e4, e8, e5, norm);
    //
    // Mesh mesh = new Mesh();
    // mesh.add(t1);
    // mesh.add(t2);
    // mesh.add(t3);
    // mesh.add(t4);
    //
    // Polygon polygon = new Polygon();
    // polygon.add(e1);
    // polygon.add(e2);
    // polygon.add(e3);
    // polygon.add(e4);
    //
    // w2.setMesh(mesh);
    // w2.setPolygon(polygon);
    // }
    //
    // // WALL 3
    // Wall w3 = new Wall();
    // {
    // Point p1 = new Point(1, 0, 0);
    // Point p2 = new Point(1, 0, 1);
    // Point p3 = new Point(1, 1, 1);
    // Point p4 = new Point(1, 1, 0);
    //
    // Point centroid = new Point(1, 0.5, 0.5);
    //
    // Edge e1 = new Edge(p1, p2);
    // Edge e2 = new Edge(p2, p3);
    // Edge e3 = new Edge(p3, p4);
    // Edge e4 = new Edge(p4, p1);
    //
    // Edge e5 = new Edge(p1, centroid);
    // Edge e6 = new Edge(p2, centroid);
    // Edge e7 = new Edge(p3, centroid);
    // Edge e8 = new Edge(p4, centroid);
    //
    // Vector3d norm = new Vector3d(1, 0, 0);
    //
    // Triangle t1 = new Triangle(p1, p2, centroid, e1, e5, e6, norm);
    // Triangle t2 = new Triangle(p2, p3, centroid, e2, e6, e7, norm);
    // Triangle t3 = new Triangle(p3, p4, centroid, e3, e7, e8, norm);
    // Triangle t4 = new Triangle(p4, p1, centroid, e4, e8, e5, norm);
    //
    // Mesh mesh = new Mesh();
    // mesh.add(t1);
    // mesh.add(t2);
    // mesh.add(t3);
    // mesh.add(t4);
    //
    // Polygon polygon = new Polygon();
    // polygon.add(e1);
    // polygon.add(e2);
    // polygon.add(e3);
    // polygon.add(e4);
    //
    // w3.setMesh(mesh);
    // w3.setPolygon(polygon);
    // }
    //
    // // WALL 4
    // Wall w4 = new Wall();
    // {
    // Point p1 = new Point(0, 1, 0);
    // Point p2 = new Point(0, 1, 1);
    // Point p3 = new Point(1, 1, 1);
    // Point p4 = new Point(1, 1, 0);
    //
    // Point centroid = new Point(0.5, 1, 0.5);
    //
    // Edge e1 = new Edge(p1, p2);
    // Edge e2 = new Edge(p2, p3);
    // Edge e3 = new Edge(p3, p4);
    // Edge e4 = new Edge(p4, p1);
    //
    // Edge e5 = new Edge(p1, centroid);
    // Edge e6 = new Edge(p2, centroid);
    // Edge e7 = new Edge(p3, centroid);
    // Edge e8 = new Edge(p4, centroid);
    //
    // Vector3d norm = new Vector3d(0, 1, 0);
    //
    // Triangle t1 = new Triangle(p1, p2, centroid, e1, e5, e6, norm);
    // Triangle t2 = new Triangle(p2, p3, centroid, e2, e6, e7, norm);
    // Triangle t3 = new Triangle(p3, p4, centroid, e3, e7, e8, norm);
    // Triangle t4 = new Triangle(p4, p1, centroid, e4, e8, e5, norm);
    //
    // Mesh mesh = new Mesh();
    // mesh.add(t1);
    // mesh.add(t2);
    // mesh.add(t3);
    // mesh.add(t4);
    //
    // Polygon polygon = new Polygon();
    // polygon.add(e1);
    // polygon.add(e2);
    // polygon.add(e3);
    // polygon.add(e4);
    //
    // w4.setMesh(mesh);
    // w4.setPolygon(polygon);
    // }
    //
    // List<Wall> wallList = new ArrayList<>();
    // wallList.add(w1);
    // wallList.add(w2);
    // wallList.add(w3);
    // wallList.add(w4);
    //
    // // BOUNDS :
    // Point p1 = new Point(0.5, -0.5, 0);
    // Point p2 = new Point(0.5, 1.5, 0);
    // Point p3 = new Point(1.5, 0.5, 0);
    // Point p4 = new Point(-0.5, 0.5, 0);
    //
    // Polygon bounds = new Polygon();
    // bounds.add(p1);
    // bounds.add(p2);
    // bounds.add(p3);
    // bounds.add(p4);
    //
    // forEachBound(bounds, wallList);
    //
    // System.out.println(p1);
    // System.out.println(p2);
    // System.out.println(p3);
    // System.out.println(p4);

    // FIXME : ne pas oublier les cas particuliers.
}
