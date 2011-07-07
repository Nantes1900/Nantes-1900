package utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.activity.InvalidActivityException;
import javax.vecmath.Vector3d;

import modeles.Edge;
import modeles.Point;
import modeles.Triangle;

public class Parser {

	private static Vector3d currentVector;
	private static ArrayList<Point> currentPoints;

	private static HashSet<Triangle> readSTLB(String fileName) throws Exception {
		InputStream stream = new BufferedInputStream(new FileInputStream(fileName));

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

		byte[] fileContent = new byte[50*size];
		stream.read(fileContent);
		bBuf = ByteBuffer.wrap(fileContent);
		bBuf.order(ByteOrder.nativeOrder());

		for(int i = 0; i < size; i ++) {
			try {
				//Si le triangle existe déjà : la méthode hashCode renvoie la même valeur, et il ne l'ajoute pas ...  !
				//FIXME : attention : revoir les hashCode de Edge et de Triangle !
				mesh.add(processLineB(bBuf, pointMap, edgeMap));
			} catch (InvalidActivityException e) {
				//C'est un triangle plat, donc on ne l'ajoute pas !
			} catch (OutOfBoundsPointException e) {
				//Les coordonnées du triangle sont démesurées !
			}
		}

		System.out.println("Lecture du fichier " + fileName + " terminée !");
		System.out.println("Nombre de triangles : " + mesh.size());

		return mesh;
	}

	public static Triangle processLineB(ByteBuffer bBuf, HashMap<Point, Point> pointMap, HashMap<Edge, Edge> edgeMap) throws Exception {
		Vector3d norm = new Vector3d(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p1 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p2 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p3 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		
		double maxLimit = 1e5;
		if(p1.getX() > maxLimit || p1.getY() > maxLimit || p1.getZ() > maxLimit)
			throw new OutOfBoundsPointException();
		if(p2.getX() > maxLimit || p2.getY() > maxLimit || p2.getZ() > maxLimit)
			throw new OutOfBoundsPointException();
		if(p3.getX() > maxLimit || p3.getY() > maxLimit || p3.getZ() > maxLimit)
			throw new OutOfBoundsPointException();
		
		//FIXME : essayer de trier les points pour que le hashCode de Edge soit mieux codé.
		Point p = pointMap.get(p1);
		if(p == null)
			pointMap.put(p1, p1);
		else
			p1 = p;

		p = pointMap.get(p2);
		if(p == null)
			pointMap.put(p2, p2);
		else
			p2 = p;

		p = pointMap.get(p3);
		if(p == null)
			pointMap.put(p3, p3);
		else
			p3 = p;

		if(p1 == p2 || p2 == p3 || p1 == p3)
			//FIXME : créer une exception !
			throw new InvalidActivityException();
		
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);

		Edge e = edgeMap.get(e1);
		if(e == null)
			edgeMap.put(e1, e1);
		else
			e1 = e;

		e = edgeMap.get(e2);
		if(e == null)
			edgeMap.put(e2, e2);
		else
			e2 = e;

		e = edgeMap.get(e3);
		if(e == null)
			edgeMap.put(e3, e3);
		else
			e3 = e;

		bBuf.get();
		bBuf.get();

		return new Triangle(p1, p2, p3, e1, e2, e3, norm);
	}

	public static HashSet<Triangle> readSTL(String fileName) throws Exception {
		Scanner scanner = new Scanner(new FileReader(fileName));

		//Reading the file		
		try {
			if(scanner.hasNextLine()) {
				StringTokenizer brokenLine = new StringTokenizer(scanner.nextLine(), " ");
				String openingWord = brokenLine.nextToken();
				if(openingWord.equals("solid"))
					return readSTLA(fileName);
				else
					return readSTLB(fileName);
			}
			else
				return null;
		} finally {
			scanner.close();
		}
	}

	private static HashSet<Triangle> readSTLA(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(fileName));
		LinkedList<Triangle> facesFromSTL = new LinkedList<Triangle>();

		HashMap<Point, Point> pointMap = new HashMap<Point, Point>();
		HashMap<Edge, Edge> edgeMap = new HashMap<Edge, Edge>();

		//Reading the file
		try {
			while (scanner.hasNextLine()){
				//Reacting to the line			
//				try {
				processLineA(facesFromSTL, scanner.nextLine(), pointMap, edgeMap);
				//FIXME : gérer les triangles plats !
//				} catch (InvalidActivityException e) {
//					//C'est un triangle plat, donc on ne l'ajoute pas !
//				} catch (Point.OutOfBoundsPointException e) {
//					//Les coordonnées du triangle sont démesurées !
//				}
			}
		} finally {
			scanner.close();
		}

		System.out.println("Lecture du fichier " + fileName + " terminée !");
		System.out.println("Nombre de triangles : " + facesFromSTL.size());

		return new HashSet<Triangle>(facesFromSTL);
	}

	//To improve : no verification whatsoever, if the stl is badly formed, explosion !
	public static void processLineA(LinkedList<Triangle> facesFromSTL, String line, HashMap<Point, Point> pointMap, HashMap<Edge, Edge> edgeMap) {
		if (line.isEmpty()){
			//TODO Generate an exception
		} else {
			StringTokenizer brokenLine = new StringTokenizer(line, " ");
			String openingWord = brokenLine.nextToken();

			//Facet normal
			if (openingWord.equals("facet")) {
				if (brokenLine.nextToken().equals("normal")) {
					currentVector = new Vector3d(Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken()));
				} else {
					//TODO Exception. Should be normal
				}
			} else {
				//Vertex
				if(openingWord.equals("vertex")) {
					Point p = new Point(Double.parseDouble(brokenLine.nextToken()), Double.parseDouble(brokenLine.nextToken()), Double.parseDouble(brokenLine.nextToken()));

					Point pp = pointMap.get(p);

					if(pp == null)
						pointMap.put(p, p);
					else
						p = pp;

					currentPoints.add(p);
				} else {
					//Endfacet
					if(openingWord.equals("endfacet")) {
						Edge e1 = new Edge(currentPoints.get(0), currentPoints.get(1));
						Edge e2 = new Edge(currentPoints.get(1), currentPoints.get(2));
						Edge e3 = new Edge(currentPoints.get(2), currentPoints.get(0));

						Edge e = edgeMap.get(e1);
						if(e == null)
							edgeMap.put(e1, e1);
						else
							e1 = e;

						e = edgeMap.get(e2);
						if(e == null)
							edgeMap.put(e2, e2);
						else
							e2 = e;

						e = edgeMap.get(e3);
						if(e == null)
							edgeMap.put(e3, e3);
						else
							e3 = e;

						facesFromSTL.add(new Triangle(currentPoints.get(0), currentPoints.get(1), currentPoints.get(2), e1, e2, e3, currentVector));
					} else {
						//Outer loop
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

	private static class OutOfBoundsPointException extends Exception {
		private static final long serialVersionUID = -7976248317048367730L;
	}
}
