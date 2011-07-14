package mains;

import java.io.IOException;

import javax.vecmath.Vector3d;

import modeles.Edge;
import modeles.Mesh;
import modeles.Point;
import modeles.Triangle;
import utils.ParserSTL;
import utils.ParserSTL.BadFormedFileException;
import utils.WriterCityGML;

public class MainCityGMLWriting {

	public static void main(String[] args) throws BadFormedFileException, IOException {
		Mesh mesh = new Mesh(ParserSTL.readSTL("Originals/floor.stl"));

		Point p1 = new Point(0, 0, 0);
		Point p2 = new Point(0, 1, 0);
		Point p3 = new Point(1, 1, 0);
		Edge e1 = new Edge(p1, p2);
		Edge e2 = new Edge(p2, p3);
		Edge e3 = new Edge(p3, p1);
		Vector3d norm = new Vector3d(0, 0, 1);
		Triangle t1 = new Triangle(p1, p2, p3, e1, e2, e3, norm);
		mesh.add(t1);

		try {
			WriterCityGML.write("test", mesh);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
