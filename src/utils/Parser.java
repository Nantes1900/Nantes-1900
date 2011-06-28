package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.vecmath.Vector3d;

import modeles.Point;
import modeles.Triangle;


public class Parser {
	
	private static Vector3d currentVector;
	private static ArrayList<Point> currentPoints;
	
	
	public static HashSet<Triangle> readSTLB(File file) throws Exception {
	    FileInputStream stream = new FileInputStream(file);
	    
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
		
		HashSet<Triangle> mesh = new HashSet<Triangle>();
		for(int i = 0; i < size; i ++) {
			mesh.add(processLineB(bBuf));
		}
		
		System.out.println("Lecture du fichier terminée !");
		System.out.println("Nombre de triangles : " + mesh.size());
		
		return mesh;
	}
	
	public static Triangle processLineB(ByteBuffer bBuf) throws Exception {
		Vector3d norm = new Vector3d(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p1 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p2 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		Point p3 = new Point(bBuf.getFloat(), bBuf.getFloat(), bBuf.getFloat());
		bBuf.get();
		bBuf.get();
		
		return new Triangle(p1, p2, p3, norm);
	}
	
	public static HashSet<Triangle> readSTLA(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(file));
		LinkedList<Triangle> facesFromSTL = new LinkedList<Triangle>();
		
		//Reading the file
	    try {
	      while (scanner.hasNextLine()){
	    	  //Reacting to the line
	        processLineA(facesFromSTL, scanner.nextLine());
	      }
	    } finally {
	        scanner.close();
	      }
	    
		System.out.println("Lecture du fichier terminée !");
		System.out.println("Nombre de triangles : " + facesFromSTL.size());
	    
	    return new HashSet<Triangle>(facesFromSTL);
	}
	
	//To improve : no verification whatsoever, if the stl is badly formed, explosion !
	public static void processLineA(LinkedList<Triangle> facesFromSTL, String line){
		if (line.isEmpty()){
			//TODO Generate an exception
		} else {
			StringTokenizer brokenLine = new StringTokenizer(line, " ");
			String openingWord = brokenLine.nextToken();
			
			//Facet normal
			if (openingWord.equals("facet")){
				if (brokenLine.nextToken().equals("normal")) {
					currentVector = new Vector3d(Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken()));
				} else {
					//TODO Exception. Should be normal
				}
			} else {
				//Vertex
				if(openingWord.equals("vertex")){
					currentPoints.add(new Point(Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken()),Double.parseDouble(brokenLine.nextToken())));
				} else {
					//Endfacet
					if(openingWord.equals("endfacet")){
						facesFromSTL.add(new Triangle(currentPoints.get(0), currentPoints.get(1), currentPoints.get(2), currentVector));
					} else {
						//Outer loop
						if (openingWord.equals("outer")){
							if (brokenLine.nextToken().equals("loop")) {
								currentPoints = new ArrayList<Point>();
							}
						}
					}
				}
			}
		}
	}

}
