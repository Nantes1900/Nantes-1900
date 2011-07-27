package nantes1900.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.vecmath.Vector3d;

import nantes1900.models.basis.Edge;
import nantes1900.models.basis.Edge.MoreThanTwoTrianglesPerEdgeException;
import nantes1900.models.basis.Point;
import nantes1900.models.basis.Triangle;

/**
 * Parse a STL file : detect if it is a ASCII or a binary file, and parse it.
 * During the parsing, it builds the Mesh, giving to the same points the same
 * references (and to the edges), and avoiding the bad-formed triangles to be
 * used and to create errors further.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 */
public class ParserSTL {

	/**
	 * A sub-class for the exception when a triangle has two points identical.
	 * 
	 * @author Daniel Lefevre
	 * 
	 */
	private static class FlatTriangleException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * A sub-class for the exception when a point has one coordinate too high.
	 * 
	 * @author Daniel Lefevre
	 * 
	 */
	private static class OutOfBoundsPointException extends Exception {
		private static final long serialVersionUID = 1L;
		private static final double boundLimit = 1e5;
	}

	/**
	 * A sub-class for the exception when the file is bad formed.
	 * 
	 * @author Daniel Lefevre
	 * 
	 */
	public static class BadFormedFileException extends Exception {
		private static final long serialVersionUID = 1L;
	}

	private static Vector3d currentVector;

	private static ArrayList<Point> currentPoints;

	/**
	 * Read one line of the file. If a point is out of bounds, it removes it. If
	 * a triangle is flat, it removes it. It doesn't create double points for
	 * points which have the same values, but give to the two triangles the same
	 * references to the point (and same work for the edges).
	 * 
	 * @param facesFromSTL
	 *            the HashSet of triangle to complete
	 * @param line
	 *            the line as a String
	 * @param pointMap
	 *            the map of points to check if the point already exists
	 * @param edgeMapthe
	 *            map of edges to check if the edge already exists
	 * @throws FlatTriangleException
	 *             if the triangle is flat (two points equals)
	 * @throws OutOfBoundsPointException
	 *             if one point has a coordinate > 1e5
	 * @throws BadFormedFileException
	 *             if the file is not well formed
	 */
	private static void processLineA(HashSet<Triangle> facesFromSTL,
			String line, HashMap<Point, Point> pointMap,
			HashMap<Edge, Edge> edgeMap) throws FlatTriangleException,
			OutOfBoundsPointException, BadFormedFileException {

		if (line.isEmpty()) {
			throw new BadFormedFileException();
		} else {
			// Select the first word of the line.
			StringTokenizer brokenLine = new StringTokenizer(line, " ");
			String openingWord = brokenLine.nextToken();

			// If the word is facet normal, read the vetor.
			if (openingWord.equals("facet")) {
				if (brokenLine.nextToken().equals("normal")) {
					currentVector = new Vector3d(Double.parseDouble(brokenLine
							.nextToken()), Double.parseDouble(brokenLine
							.nextToken()), Double.parseDouble(brokenLine
							.nextToken()));

					currentVector.normalize();
				} else {
					throw new BadFormedFileException();
				}
			} else {
				// If the word is vertex, read one of the three points.
				if (openingWord.equals("vertex")) {
					Point p = new Point((float) Double.parseDouble(brokenLine
							.nextToken()),
							(float) Double.parseDouble(brokenLine.nextToken()),
							(float) Double.parseDouble(brokenLine.nextToken()));

					// If the point has one coordinate > 1e5, throws and
					// Exception. It can cause an error further in the program.
					double maxLimit = OutOfBoundsPointException.boundLimit;
					if (p.getX() > maxLimit || p.getY() > maxLimit
							|| p.getZ() > maxLimit)
						throw new OutOfBoundsPointException();

					Point mapP = pointMap.get(p);

					// Checks in the HashSet of points if this point doesn't
					// already exist. If it already exists, it doesn't create
					// another point, but keep the same reference.
					if (mapP == null)
						pointMap.put(p, p);
					else
						p = mapP;

					currentPoints.add(p);
				} else {
					// If the the points are read, create the triangle and add
					// it to the HashSet.
					if (openingWord.equals("endfacet")) {
						Edge e1 = new Edge(currentPoints.get(0),
								currentPoints.get(1));
						Edge e2 = new Edge(currentPoints.get(1),
								currentPoints.get(2));
						Edge e3 = new Edge(currentPoints.get(2),
								currentPoints.get(0));

						// Checks in the HashSet of edges if this edge doesn't
						// already exist. If it already exists, it doesn't
						// create
						// another edge, but keep the same reference.
						Edge e = edgeMap.get(e1);
						if (e == null)
							edgeMap.put(e1, e1);
						else
							e1 = e;

						e = edgeMap.get(e2);
						if (e == null)
							edgeMap.put(e2, e2);
						else
							e2 = e;

						e = edgeMap.get(e3);
						if (e == null)
							edgeMap.put(e3, e3);
						else
							e3 = e;

						// Check for the flat triangles.
						if (e1 == e2 || e2 == e3 || e1 == e3)
							throw new FlatTriangleException();

						try {
							facesFromSTL.add(new Triangle(currentPoints.get(0),
									currentPoints.get(1), currentPoints.get(2),
									e1, e2, e3, currentVector));
						} catch (MoreThanTwoTrianglesPerEdgeException e4) {
							// Do nothing : it will not add the Triangle to the
							// mesh.
							// TODO? Select the good triangle to remove ?
						}
					} else {
						// If the triangle is read, clear the currentPoints
						// static ArrayList for the next triangle.
						if (openingWord.equals("outer")) {
							if (brokenLine.nextToken().equals("loop")) {
								currentPoints = new ArrayList<Point>();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Read one line of the file. If a point is out of bounds, it removes it. If
	 * a triangle is flat, it removes it. It doesn't create double points for
	 * points which have the same values, but give to the two triangles the same
	 * references to the point (and same work for the edges).
	 * 
	 * @param bBuf
	 *            the bytebuffer to read in
	 * @param pointMap
	 *            the map of points to check if the point already exists
	 * @param edgeMap
	 *            the map of edges to check if the edge already exists
	 * @return the triangle read
	 * @throws FlatTriangleException
	 *             if the triangle is flat (two points equals)
	 * @throws OutOfBoundsPointException
	 *             if one point has a coordinate > 1e5
	 * @throws MoreThanTwoTrianglesPerEdgeException
	 *             if one edge of the new triangle contains already two
	 *             triangles
	 */
	private static Triangle processLineB(ByteBuffer bBuf,
			HashMap<Point, Point> pointMap, HashMap<Edge, Edge> edgeMap)
			throws FlatTriangleException, OutOfBoundsPointException,
			MoreThanTwoTrianglesPerEdgeException {

		// Read in the ByteBuffer the floats.
		Vector3d norm = new Vector3d(bBuf.getFloat(), bBuf.getFloat(),
				bBuf.getFloat());
		norm.normalize();

		Point p1 = new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
				(float) bBuf.getFloat());
		Point p2 = new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
				(float) bBuf.getFloat());
		Point p3 = new Point((float) bBuf.getFloat(), (float) bBuf.getFloat(),
				(float) bBuf.getFloat());

		// If the point is out of bounds, it's maybe an error when the file was
		// written. But it could cause error further in the program. Thus we
		// remove the triangles which contain thos points.
		double maxLimit = OutOfBoundsPointException.boundLimit;
		if (p1.getX() > maxLimit || p1.getY() > maxLimit
				|| p1.getZ() > maxLimit)
			throw new OutOfBoundsPointException();
		if (p2.getX() > maxLimit || p2.getY() > maxLimit
				|| p2.getZ() > maxLimit)
			throw new OutOfBoundsPointException();
		if (p3.getX() > maxLimit || p3.getY() > maxLimit
				|| p3.getZ() > maxLimit)
			throw new OutOfBoundsPointException();

		// Add the point to the HashSet using get : if null is returned, it
		// means that the HashSet doesn't contain any point with the same
		// values.
		// The good thing is that it search on the values, and not only on the
		// references, because we implemented the hashCode method in the class
		// Point.
		Point p = pointMap.get(p1);
		if (p == null)
			pointMap.put(p1, p1);
		else
			p1 = p;

		p = pointMap.get(p2);
		if (p == null)
			pointMap.put(p2, p2);
		else
			p2 = p;

		p = pointMap.get(p3);
		if (p == null)
			pointMap.put(p3, p3);
		else
			p3 = p;

		// If two points are the same, throw a FlatTriangleException.
		if (p1 == p2 || p2 == p3 || p1 == p3)
			throw new FlatTriangleException();

		// Same verification for the edges : using the method get of a HashSet.
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);

		Edge e = edgeMap.get(e1);
		if (e == null)
			edgeMap.put(e1, e1);
		else
			e1 = e;

		e = edgeMap.get(e2);
		if (e == null)
			edgeMap.put(e2, e2);
		else
			e2 = e;

		e = edgeMap.get(e3);
		if (e == null)
			edgeMap.put(e3, e3);
		else
			e3 = e;

		// Add two bytes to respect the binary format. Those bytes can be used
		// to put a color to the triangle. But we don't use them.
		bBuf.get();
		bBuf.get();

		return new Triangle(p1, p2, p3, e1, e2, e3, norm);
	}

	/**
	 * Read an ASCII STL file. Create a HashSet of triangle to put them in. If a
	 * point is found equals with another, only one point is created, and the
	 * references is given to the two triangles. This work is done to the edges
	 * too. Flat triangles (two points equals) are removed. Points out of bounds
	 * (containing coordinate > 1e5) are removed and their triangles too.
	 * 
	 * @param fileName
	 *            the name if the file to read
	 * @return the HashSet containing all the triangles
	 * @throws IOException
	 *             if the file is badformed
	 * @throws FileNotFoundException
	 *             if the file doesn't exist
	 */
	private static HashSet<Triangle> readSTLA(String fileName)
			throws FileNotFoundException, BadFormedFileException {

		Scanner scanner = new Scanner(new FileReader(fileName));
		HashSet<Triangle> mesh = new HashSet<Triangle>();

		HashMap<Point, Point> pointMap = new HashMap<Point, Point>();
		HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

		// Reading the file
		try {
			while (scanner.hasNextLine()) {
				// Reacting to the line
				try {
					// If a Triangle exists already, and if the Parser read
					// another Triangle with the same values,
					// only one of those Triangles will be added to the Mesh.
					processLineA(mesh, scanner.nextLine(), pointMap, edgeMap);
				} catch (FlatTriangleException e) {
					// If it is a flat Triangle : 2 identical Points, then 2
					// identical Edge, it is not added to the Mesh.
				} catch (OutOfBoundsPointException e) {
					// The coordinates of the Point are unbounded, then the
					// Triangle is not added to the Mesh.
				}
			}
		} finally {
			scanner.close();
		}

		return new HashSet<Triangle>(mesh);
	}

	/**
	 * Read a binary STL file. Create a HashSet of triangle to put them in. If a
	 * point is found equals with another, only one point is created, and the
	 * references is given to the two triangles. This work is done to the edges
	 * too. Flat triangles (two points equals) are removed. Points out of bounds
	 * (containing coordinate > 1e5) are removed and their triangles too.
	 * 
	 * @param fileName
	 *            the name if the file to read
	 * @return the HashSet containing all the triangles
	 * @throws IOException
	 *             if the file is badformed
	 */
	private static HashSet<Triangle> readSTLB(String fileName)
			throws IOException {
		InputStream stream = new BufferedInputStream(new FileInputStream(
				fileName));

		HashMap<Point, Point> pointMap = new HashMap<Point, Point>();
		HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();
		HashSet<Triangle> mesh = new HashSet<Triangle>();

		byte[] header = new byte[80];
		stream.read(header, 0, 80);
		byte[] sizeBytes = new byte[4];
		stream.read(sizeBytes, 0, 4);
		ByteBuffer bBuf = ByteBuffer.wrap(sizeBytes);
		bBuf.order(ByteOrder.nativeOrder());
		int size = bBuf.getInt();

		byte[] fileContent = new byte[50 * size];
		stream.read(fileContent);
		bBuf = ByteBuffer.wrap(fileContent);
		bBuf.order(ByteOrder.nativeOrder());

		for (int i = 0; i < size; i++) {
			try {
				// If a Triangle exists already, and if the Parser read another
				// Triangle with the same values,
				// only one of those Triangles will be added to the Mesh.
				mesh.add(processLineB(bBuf, pointMap, edgeMap));
			} catch (FlatTriangleException e) {
				// If it is a flat Triangle : 2 identical Points, then 2
				// identical Edge, it is not added to the Mesh.
			} catch (OutOfBoundsPointException e) {
				// The coordinates of the Point are unbounded, then the Triangle
				// is not added to the Mesh.
			} catch (MoreThanTwoTrianglesPerEdgeException e) {
				// If one edge of the new triangle contains already two
				// triangles, then the new triangle is removed from the mesh.
				// TODO? Try to improve that by removing the triangle which is
				// the worst.
			}
		}

		stream.close();
		return mesh;
	}

	/**
	 * Detect the format of the STL file, and read it using the good method.
	 * 
	 * @param fileName
	 *            the file to read
	 * @return a HashSet of triangle to build a mesh with
	 * @throws BadFormedFileException
	 *             if the file is not well formed
	 * @throws IOException
	 */
	public static HashSet<Triangle> readSTL(String fileName)
			throws BadFormedFileException, IOException {
		Scanner scanner = new Scanner(new FileReader(fileName));

		// Reading the file
		try {
			if (scanner.hasNextLine()) {
				StringTokenizer brokenLine = new StringTokenizer(
						scanner.nextLine(), " ");
				String openingWord = brokenLine.nextToken();

				// If the first word is solid, this means it's an ASCII file
				// If it's a binary file, it will not be found
				if (openingWord.equals("solid"))
					return readSTLA(fileName);
				else
					return readSTLB(fileName);
			} else
				return null;
		} finally {
			scanner.close();
		}

	}
}
