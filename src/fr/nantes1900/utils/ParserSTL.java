package fr.nantes1900.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import javax.vecmath.Vector3d;

import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

/**
 * Implements a STL parser : detects if it is an ASCII or a binary file, and
 * parses it. During the parsing, it builds a mesh, giving to the same points
 * the same references (and to the edges), and avoiding to keep the bad-formed
 * triangles.
 * @author Eric Berthe, Valentin Roger, Daniel Lefevre
 */
public class ParserSTL {

    /**
     * The set of triangle read in the file.
     */
    private Set<Triangle> triangleSet;

    /**
     * The map of point to compute the references of the points.
     */
    private Map<Point, Point> pointMap;

    /**
     * The map of edge to compute the references of the edges.
     */
    private Map<Edge, Edge> edgeMap;

    /**
     * The name of the file to read.
     */
    private final String fileName;

    /**
     * Private constructor.
     * @param fileNameRead
     *            the name of the file to read
     */
    public ParserSTL(final String fileNameRead) {
        this.fileName = fileNameRead;
    }

    /**
     * Reads one line of the file, considering the beginning of the line.
     * Returns true when an entire has been read, and false otherwise.
     * @param line
     *            the line as a String
     * @param currentPoints
     *            temporary parameter to stock the read points of the current
     *            triangle
     * @param currentVector
     *            temporary parameter to stock the read vector of the current
     *            triangle
     * @return true when an entire has been read, and false otherwise.
     */
    private static boolean processLineA(final String line,
            final Vector3d currentVector, final List<Point> currentPoints) {

        // If the line is empty, the parser passes to the next line.
        if (!line.isEmpty()) {

            // Select the first word of the line.
            final StringTokenizer brokenLine = new StringTokenizer(line, " ");
            final String openingWord = brokenLine.nextToken();

            // If the word is facet normal, read the vector.
            if ("facet".equals(openingWord)
                    && "normal".equals(brokenLine.nextToken())) {
                currentVector.set(Double.parseDouble(brokenLine.nextToken()),
                        Double.parseDouble(brokenLine.nextToken()),
                        Double.parseDouble(brokenLine.nextToken()));

                currentVector.normalize();
            } else if ("vertex".equals(openingWord)) {
                // If the word is vertex, read one of the three points.
                final Point p = new Point((float) Double.parseDouble(brokenLine
                        .nextToken()), (float) Double.parseDouble(brokenLine
                        .nextToken()), (float) Double.parseDouble(brokenLine
                        .nextToken()));

                currentPoints.add(p);
            } else if ("endfacet".equals(openingWord)) {
                // If the the points are read, return true so that readSTLA
                // builds the triangle.
                return true;
            }
        }
        return false;
    }

    /**
     * Reads one line of the file. If a point is out of bounds, it removes it.
     * If a triangle is flat, it removes it. It doesn't create double points for
     * points which have the same values, but give to the two triangles the same
     * reference to the point (and same work for the edges).
     * @param bBuf
     *            the bytebuffer to read in
     * @return the triangle read
     * @throws BadMeshException
     *             if the triangle is flat (two points equals) or if one point
     *             has a coordinate > 1e5 or if one edge of the new triangle
     *             contains already two triangles
     */
    private Triangle processLineB(final ByteBuffer bBuf)
            throws BadMeshException {

        // Reading part.

        // Reads in the ByteBuffer the floats.
        final Vector3d norm = new Vector3d(bBuf.getFloat(), bBuf.getFloat(),
                bBuf.getFloat());

        Point p1 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
        Point p2 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
        Point p3 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());

        // Adds two bytes to respect the binary format. Those bytes can be used
        // to put a color to the triangle. But we don't use them.
        bBuf.get();
        bBuf.get();

        // Treatment part.

        // Treats the points.
        p1 = this.treatPoint(p1);
        p2 = this.treatPoint(p2);
        p3 = this.treatPoint(p3);

        if (norm.x == 0 && norm.y == 0 && norm.z == 0) {
            throw new BadMeshException();
        }
        norm.normalize();

        // If two points are the same, throws a FlatTriangleException.
        if (p1 == p2 || p2 == p3 || p1 == p3) {
            throw new FlatTriangleException();
        }

        // Same verification for the edges : using the method get of a HashSet.
        Edge e1 = new Edge(p1, p2);
        Edge e2 = new Edge(p2, p3);
        Edge e3 = new Edge(p3, p1);

        e1 = this.treatEdge(e1);
        e2 = this.treatEdge(e2);
        e3 = this.treatEdge(e3);

        if (e1.getNumberTriangles() == 2 || e2.getNumberTriangles() == 2
                || e3.getNumberTriangles() == 2) {
            throw new MoreThanTwoTrianglesPerEdgeException();
        }

        return new Triangle(e1, e2, e3, norm);
    }

    /**
     * Detects the format of the STL file, and reads it using the good method.
     * @return a hashset of triangles to build a mesh with
     * @throws IOException
     *             if the file is bad formed or if there is an error during the
     *             reading
     */
    public final Mesh read() throws IOException {
        final Scanner scanner = new Scanner(new FileReader(this.fileName));

        // Reading the file
        try {
            if (scanner.hasNextLine()) {
                final StringTokenizer brokenLine = new StringTokenizer(
                        scanner.nextLine(), " ");
                final String openingWord = brokenLine.nextToken();

                Set<Triangle> triangleMap;
                this.pointMap = new HashMap<>();
                this.edgeMap = new HashMap<>();

                // If the first word is solid, this means it's an ASCII file.
                // If it's a binary file, it will not be found.
                if ("solid".equals(openingWord)) {
                    triangleMap = this.readSTLA();
                } else {
                    triangleMap = this.readSTLB();
                }
                return new Mesh(triangleMap);
            }
            return null;
        } finally {
            scanner.close();
        }
    }

    /**
     * Reads an ASCII STL file. Creates a HashSet of triangle to put them in. If
     * a point is found equals with another, only one point is created, and the
     * same reference is given to the two triangles. This work is done to the
     * edges too. Flat triangles (two points equals) are removed. Points out of
     * bounds (containing coordinate > 1e5) are removed and their triangles too.
     * @return the HashSet containing all the triangles
     * @throws IOException
     *             if the file is badformed or if the file doesn't exist
     */
    private Set<Triangle> readSTLA() throws IOException {

        final Scanner scanner = new Scanner(new FileReader(this.fileName));

        this.triangleSet = new HashSet<>();

        final Vector3d currentVector = new Vector3d();
        final List<Point> currentPoints = new ArrayList<>();

        // Reading the file
        try {
            while (scanner.hasNextLine()) {

                // If the processLineA have finished to read an entire triangle,
                // it proceeds to the process.
                if (ParserSTL.processLineA(scanner.nextLine(), currentVector,
                        currentPoints)) {

                    try {
                        // From the points read, checks in the hashset if they
                        // doesn't already exist.
                        final Point p1 = this.treatPoint(currentPoints.get(0));
                        final Point p2 = this.treatPoint(currentPoints.get(1));
                        final Point p3 = this.treatPoint(currentPoints.get(2));

                        // Checks in the HashSet of edges if this edge doesn't
                        // already exist. If it already exists, it doesn't
                        // create another edge, but keep the same reference.
                        final Edge e1 = this.treatEdge(new Edge(p1, p2));
                        final Edge e2 = this.treatEdge(new Edge(p2, p3));
                        final Edge e3 = this.treatEdge(new Edge(p3, p1));

                        // Checks for the flat triangles.
                        if (e1 == e2 || e2 == e3 || e1 == e3) {
                            throw new FlatTriangleException();
                        }

                        if (e1.getNumberTriangles() == 2
                                || e2.getNumberTriangles() == 2
                                || e3.getNumberTriangles() == 2) {
                            throw new MoreThanTwoTrianglesPerEdgeException();
                        }

                        this.triangleSet.add(new Triangle(e1, e2, e3,
                                currentVector));

                    } catch (final MoreThanTwoTrianglesPerEdgeException e) {
                        // This triangle can't be add : three triangles per edge
                        // will cause problems in the program.
                    } catch (final FlatTriangleException e) {
                        // If it is a flat Triangle : 2 identical Points, then 2
                        // identical Edge, it is not added to the Mesh.
                    } catch (final OutOfBoundsPointException e) {
                        // The coordinates of the Point are unbounded, then the
                        // Triangle is not added to the Mesh.
                    } finally {
                        currentPoints.clear();
                    }
                }
            }
        } finally {
            scanner.close();
        }

        return this.triangleSet;
    }

    /**
     * Reads a binary STL file. Creates a HashSet of triangle to put them in. If
     * a point is found equals with another, only one point is created, and the
     * same reference is given to the two triangles. This work is done to the
     * edges too. Flat triangles (two points equals) are removed. Points out of
     * bounds (containing coordinate > 1e5) are removed and their triangles too.
     * @return the HashSet containing all the triangles
     * @throws IOException
     *             if the file is badformed
     */
    private Set<Triangle> readSTLB() throws IOException {
        final InputStream stream = new BufferedInputStream(new FileInputStream(
                this.fileName));

        this.triangleSet = new HashSet<>();

        final int headerSize = 80;

        final byte[] header = new byte[headerSize];
        stream.read(header, 0, headerSize);

        final byte[] sizeBytes = new byte[Byte.SIZE / 2];
        stream.read(sizeBytes, 0, Byte.SIZE / 2);

        ByteBuffer bBuf = ByteBuffer.wrap(sizeBytes);
        bBuf.order(ByteOrder.nativeOrder());
        final int meshSize = bBuf.getInt();

        final int triangleSTLSize = 50;

        final byte[] fileContent = new byte[triangleSTLSize * meshSize];
        stream.read(fileContent);
        bBuf = ByteBuffer.wrap(fileContent);
        bBuf.order(ByteOrder.nativeOrder());

        for (int i = 0; i < meshSize; i = i + 1) {
            try {
                // If a Triangle exists already, and if the
                // Parser read another
                // Triangle with the same values,
                // only one of those Triangles will be added
                // to the Mesh.
                this.triangleSet.add(this.processLineB(bBuf));
            } catch (final FlatTriangleException e) {
                // If it is a flat Triangle : 2 identical Points, then 2
                // identical Edge, it is not added to the Mesh.
            } catch (final OutOfBoundsPointException e) {
                // The coordinates of the Point are unbounded, then the Triangle
                // is not added to the Mesh.
            } catch (final MoreThanTwoTrianglesPerEdgeException e) {
                // If one edge of the new triangle contains already two
                // triangles, then the new triangle is removed from the mesh.
            } catch (BadMeshException e) {
                // This execption is supposed to be treated in the three catch
                // above.
            }
        }

        stream.close();
        return this.triangleSet;
    }

    /**
     * Checks if the edge doesn't already exists, and if it does, returns only
     * one reference for other edges which have the same values.
     * @param edge
     *            the edge to check
     * @return the edge parameter if it doesn't already exists, otherwise the
     *         edge which already exists and have the same values
     */
    private Edge treatEdge(final Edge edge) {

        final Edge eNew = this.edgeMap.get(edge);
        if (eNew == null) {
            this.edgeMap.put(edge, edge);
            return edge;
        }
        return eNew;
    }

    /**
     * Checks if the point doesn't already exists, and if it does, returns only
     * one references for points which have the same values.
     * @param point
     *            the point to check
     * @return the point parameter if it doesn't already exists, otherwise the
     *         point which already exists and have the same values
     * @exception OutOfBoundsPointException
     *                if the point have incorrect values
     */
    private Point treatPoint(final Point point)
            throws OutOfBoundsPointException {

        // If the point has one coordinate >
        // 1e5, throws an Exception. It can cause an error
        // further in the program.
        final double maxLimit = OutOfBoundsPointException.BOUND_LIMIT;

        if (point.getX() > maxLimit || point.getY() > maxLimit
                || point.getZ() > maxLimit) {
            throw new OutOfBoundsPointException();
        }

        final Point mapP = this.pointMap.get(point);

        // Checks in the HashSet of points if this point doesn't already exist.
        // If it already exists, it doesn't create another point, but keep the
        // same reference.
        if (mapP == null) {
            this.pointMap.put(point, point);
            return point;
        }
        return mapP;
    }

    /**
     * Implements an exception when a triangle, a point, or an edge is bad
     * formed.
     * @author Daniel Lefevre
     */
    public static class BadMeshException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public BadMeshException() {
        }

    }

    /**
     * Implements an exception when a triangle has two points identical.
     * @author Daniel Lefevre
     */
    private static final class FlatTriangleException extends
            ParserSTL.BadMeshException {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public FlatTriangleException() {
        }
    }

    /**
     * Implements an exception when an edge has more than two triangles which
     * contain it.
     * @author Daniel Lefevre
     */
    private static final class MoreThanTwoTrianglesPerEdgeException extends
            ParserSTL.BadMeshException {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        public MoreThanTwoTrianglesPerEdgeException() {
        }
    }

    /**
     * Implements an exception when a point has one coordinate too high.
     * @author Daniel Lefevre
     */
    private static final class OutOfBoundsPointException extends
            ParserSTL.BadMeshException {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;
        /**
         * Bound limit attribute.
         */
        private static final double BOUND_LIMIT = 1e5;

        /**
         * Private constructor.
         */
        public OutOfBoundsPointException() {
        }
    }
}
