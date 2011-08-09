package fr.nantes1900.utils;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Edge;
import fr.nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

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

/**
 * Parse a STL file : detect if it is a ASCII or a binary
 * file, and parse it. During the parsing, it builds the
 * Mesh, giving to the same points the same references (and
 * to the edges), and avoiding the bad-formed triangles to
 * be used and to create errors further.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 */

// TODO : refactor this class : make a non-static parser,
// and respect the style conditions.
public class ParserSTL {

    /**
     * Vector attribute used for ASCII parser method.
     */
    private static Vector3d currentVector;

    /**
     * Points attribute used for ASCII parser method.
     */
    private static List<Point> currentPoints;

    /**
     * The list of triangles to stock all the triangles read in the file.
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
    private String fileName;

    /**
     * Private constructor.
     * 
     * @param fileNameRead
     *            the name of the file to read
     */
    public ParserSTL(final String fileNameRead) {
        this.fileName = fileNameRead;
    }

    /**
     * Detect the format of the STL file, and read it using
     * the good method.
     * 
     * @return a HashSet of triangle to build a mesh with
     * @throws IOException
     *             if the file is bad formed or if there is
     *             an error during the reading
     */
    public final Mesh read() throws IOException {
        final Scanner scanner = new Scanner(new FileReader(this.fileName));

        // Reading the file
        try {
            if (scanner.hasNextLine()) {
                final StringTokenizer brokenLine =
                    new StringTokenizer(scanner.nextLine(), " ");
                final String openingWord = brokenLine.nextToken();

                Set<Triangle> triangleMap;
                this.pointMap = new HashMap<Point, Point>();
                this.edgeMap = new HashMap<Edge, Edge>();

                // If the first word is solid, this means it's an ASCII file.
                // If it's a binary file, it will not be found.
                if ("solid".equals(openingWord)) {
                    triangleMap = this.readSTLA();
                } else {
                    triangleMap = this.readSTLB();
                }
                return new Mesh(triangleMap);
            } else {
                return null;
            }
        } finally {
            scanner.close();
        }

    }

    /**
     * Reads one line of the file. If a point is out of
     * bounds, it removes it. If a triangle is flat, it
     * removes it. It doesn't create double points for
     * points which have the same values, but give to the
     * two triangles the same references to the point (and
     * same work for the edges).
     * 
     * @param line
     *            the line as a String
     * @throws FlatTriangleException
     *             if the triangle is flat (two points
     *             equals)
     * @throws OutOfBoundsPointException
     *             if one point has a coordinate > 1e5
     * @throws BadFormedFileException
     *             if the file is not well formed
     */
    private void processLineA(final String line) throws FlatTriangleException,
        OutOfBoundsPointException, BadFormedFileException {

        if (line.isEmpty()) {
            throw new BadFormedFileException();
        } else {
            // Select the first word of the line.
            final StringTokenizer brokenLine = new StringTokenizer(line, " ");
            final String openingWord = brokenLine.nextToken();

            // If the word is facet normal, read the vetor.
            if ("facet".equals(openingWord)) {
                if ("normal".equals(brokenLine.nextToken())) {
                    ParserSTL.currentVector =
                        new Vector3d(
                            Double.parseDouble(brokenLine.nextToken()), Double
                                .parseDouble(brokenLine.nextToken()), Double
                                .parseDouble(brokenLine.nextToken()));

                    ParserSTL.currentVector.normalize();
                } else {
                    throw new BadFormedFileException();
                }
            } else {
                // If the word is vertex, read one of the
                // three points.
                if ("vertex".equals(openingWord)) {
                    Point p =
                        new Point((float) Double.parseDouble(brokenLine
                            .nextToken()), (float) Double
                            .parseDouble(brokenLine.nextToken()),
                            (float) Double.parseDouble(brokenLine.nextToken()));

                    p = this.treatPoint(p);

                    ParserSTL.currentPoints.add(p);
                } else {
                    // If the the points are read, create
                    // the triangle and add
                    // it to the HashSet.
                    if ("endfacet".equals(openingWord)) {
                        Edge e1 =
                            new Edge(ParserSTL.currentPoints.get(0),
                                ParserSTL.currentPoints.get(1));
                        Edge e2 =
                            new Edge(ParserSTL.currentPoints.get(1),
                                ParserSTL.currentPoints.get(2));
                        Edge e3 =
                            new Edge(ParserSTL.currentPoints.get(2),
                                ParserSTL.currentPoints.get(0));

                        // Checks in the HashSet of edges if this edge doesn't
                        // already exist. If it already exists, it doesn't
                        // create another edge, but keep the same reference.
                        e1 = this.treatEdge(e1);
                        e2 = this.treatEdge(e2);
                        e3 = this.treatEdge(e3);

                        // Check for the flat triangles.
                        if (e1 == e2 || e2 == e3 || e1 == e3) {
                            throw new FlatTriangleException();
                        }

                        try {
                            this.triangleSet.add(new Triangle(
                                ParserSTL.currentPoints.get(0),
                                ParserSTL.currentPoints.get(1),
                                ParserSTL.currentPoints.get(2), e1, e2, e3,
                                ParserSTL.currentVector));
                        } catch (MoreThanTwoTrianglesPerEdgeException e4) {
                            // Do nothing : it will not add
                            // the Triangle to the mesh.
                            // TODO? Select the good triangle to remove ?
                        }
                        // TODO : test in the ParserSTLTest the bad points and
                        // edges, and etc...
                    } else {
                        // If the triangle is read, clear
                        // the currentPoints
                        // static ArrayList for the next
                        // triangle.
                        if ("outer".equals(openingWord)) {
                            if ("loop".equals(brokenLine.nextToken())) {
                                ParserSTL.currentPoints =
                                    new ArrayList<Point>();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Read one line of the file. If a point is out of
     * bounds, it removes it. If a triangle is flat, it
     * removes it. It doesn't create double points for
     * points which have the same values, but give to the
     * two triangles the same references to the point (and
     * same work for the edges).
     * 
     * @param bBuf
     *            the bytebuffer to read in
     * @return the triangle read
     * @throws FlatTriangleException
     *             if the triangle is flat (two points
     *             equals)
     * @throws OutOfBoundsPointException
     *             if one point has a coordinate > 1e5
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if one edge of the new triangle contains
     *             already two triangles
     */
    private Triangle processLineB(final ByteBuffer bBuf)
        throws FlatTriangleException, OutOfBoundsPointException,
        MoreThanTwoTrianglesPerEdgeException {

        // Read in the ByteBuffer the floats.
        final Vector3d norm =
            new Vector3d(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
        norm.normalize();

        Point p1 =
            new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
                (float) bBuf.getFloat());
        Point p2 =
            new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
                (float) bBuf.getFloat());
        Point p3 =
            new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
                (float) bBuf.getFloat());

        p1 = this.treatPoint(p1);
        p2 = this.treatPoint(p2);
        p3 = this.treatPoint(p3);

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

        // Add two bytes to respect the binary format. Those bytes can be used
        // to put a color to the triangle. But we don't use them.
        bBuf.get();
        bBuf.get();

        return new Triangle(p1, p2, p3, e1, e2, e3, norm);
    }

    /**
     * Read an ASCII STL file. Create a HashSet of triangle
     * to put them in. If a point is found equals with
     * another, only one point is created, and the
     * references is given to the two triangles. This work
     * is done to the edges too. Flat triangles (two points
     * equals) are removed. Points out of bounds (containing
     * coordinate > 1e5) are removed and their triangles
     * too.
     * 
     * @return the HashSet containing all the triangles
     * @throws IOException
     *             if the file is badformed or if the file doesn't exist
     */
    private Set<Triangle> readSTLA() throws IOException {

        final Scanner scanner = new Scanner(new FileReader(this.fileName));

        this.triangleSet = new HashSet<Triangle>();

        // Reading the file
        try {
            while (scanner.hasNextLine()) {
                // Reacting to the line
                try {
                    // If a Triangle exists already, and if
                    // the Parser read another Triangle with the same
                    // values, only one of those Triangles will be added to the
                    // Mesh.
                    this.processLineA(scanner.nextLine());
                } catch (FlatTriangleException e) {
                    // If it is a flat Triangle : 2
                    // identical Points, then 2 identical Edge, it is not added
                    // to the Mesh.
                } catch (OutOfBoundsPointException e) {
                    // The coordinates of the Point are
                    // unbounded, then the Triangle is not added to the Mesh.
                }
            }
        } finally {
            scanner.close();
        }

        return this.triangleSet;
    }

    /**
     * Read a binary STL file. Create a HashSet of triangle
     * to put them in. If a point is found equals with
     * another, only one point is created, and the
     * references is given to the two triangles. This work
     * is done to the edges too. Flat triangles (two points
     * equals) are removed. Points out of bounds (containing
     * coordinate > 1e5) are removed and their triangles
     * too.
     * 
     * @return the HashSet containing all the triangles
     * @throws IOException
     *             if the file is badformed
     */
    private Set<Triangle> readSTLB() throws IOException {
        final InputStream stream =
            new BufferedInputStream(new FileInputStream(this.fileName));

        this.triangleSet = new HashSet<Triangle>();

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
            } catch (FlatTriangleException e) {
                // If it is a flat Triangle : 2 identical
                // Points, then 2
                // identical Edge, it is not added to the
                // Mesh.
            } catch (OutOfBoundsPointException e) {
                // The coordinates of the Point are
                // unbounded, then the Triangle
                // is not added to the Mesh.
            } catch (MoreThanTwoTrianglesPerEdgeException e) {
                // If one edge of the new triangle contains
                // already two
                // triangles, then the new triangle is
                // removed from the mesh.
                // TODO? Try to improve that by removing the
                // triangle which is
                // the worst.
            }
        }

        stream.close();
        return this.triangleSet;
    }

    /**
     * Check if the edge doesn't already exists, and if it does, return only one
     * references for edges which have the same values.
     * 
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
        } else {
            return eNew;
        }
    }

    /**
     * Check if the point doesn't already exists, and if it does, return only
     * one
     * references for points which have the same values.
     * 
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
        } else {
            return mapP;
        }
    }

    /**
     * A sub-class for the exception when the file is bad
     * formed.
     * 
     * @author Daniel Lefevre
     */
    public static final class BadFormedFileException extends IOException {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private BadFormedFileException() {
        }
    }

    /**
     * A sub-class for the exception when a triangle has two
     * points identical.
     * 
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
        private FlatTriangleException() {
        }
    }

    /**
     * Implements an exception when a triangle, a point, or an edge is bad
     * formed.
     * 
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
        private BadMeshException() {
        }

    }

    /**
     * A sub-class for the exception when a point has one
     * coordinate too high.
     * 
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
        private OutOfBoundsPointException() {
        }
    }
}
