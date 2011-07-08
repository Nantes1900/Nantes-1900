package utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import modeles.Mesh;
import modeles.Triangle;

/**
 * Write a STL file containing the faces, list of faces or buildings.
 *
 */

public class Writer {

	public static final int ASCII_MODE = 1;
	public static final int BINARY_MODE = 2;

	private static int MODE = ASCII_MODE;

	public static void write(String fileName, Mesh m) {
		if(MODE == ASCII_MODE)
			writeA(fileName, m);
		else if(MODE == BINARY_MODE)
			writeB(fileName, m);
		else
			System.err.println("Erreur !");
	}

	public static void setWriteMode(int mode) {
		MODE = mode;
	}

	private static void writeA(String fileName, Mesh m) {
		Writer.writeSTLA(fileName, m);
		System.out.println(fileName + " written in STL ASCII!");
	}

	private static void writeB(String fileName, Mesh m) {
		Writer.writeSTLB(fileName, m);
		System.out.println(fileName + " written in STL binary!");
	}

	/**
	 * 
	 * @param fw The file we want to write in.
	 * @param face The face to be written.
	 */
	public static void writeASCIIFace(BufferedWriter fw, Triangle face) {
		try {
			fw.write("\nfacet normal");
			String s1 = new String();
			double[] t = new double[3];
			face.getNormal().get(t);
			for (int j=0;j<3;j++) {
				s1 += " "+t[j];				
			}
			fw.write(s1+"\nouter loop");
			fw.write("\nvertex"+" "+face.getP1().getX()+" "+face.getP1().getY()+" "+face.getP1().getZ());
			fw.write("\nvertex"+" "+face.getP2().getX()+" "+face.getP2().getY()+" "+face.getP2().getZ());
			fw.write("\nvertex"+" "+face.getP3().getX()+" "+face.getP3().getY()+" "+face.getP3().getZ());
			fw.write("\nendloop\nendfacet");
		}
		catch (java.io.IOException ee){
			ee.printStackTrace();
		}
	}


	/**
	 * 
	 * @param fw The file we want to write in
	 * @param surface The list of faces to be written.
	 */
	public static void writeSTLA(String fileName, Mesh surface) {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter(fileName));
			fw.write("solid");
			for (Triangle f : surface) {
				writeASCIIFace(fw,f);
			}
			fw.write("\nendsolid");
			fw.flush();
		}
		catch (java.io.IOException e){
			e.printStackTrace();
		}
	}

	private static void writeInGoodOrder(OutputStream writer, double a) throws Exception {
		ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
		bBuf.order(ByteOrder.LITTLE_ENDIAN);
		bBuf.putFloat((float)a);
		writer.write(bBuf.array(), 0, Float.SIZE/8);
	}

	public static void writeBinaryFace(OutputStream writer, Triangle face) throws Exception {
		writeInGoodOrder(writer, face.getNormal().getX());
		writeInGoodOrder(writer, face.getNormal().getY());
		writeInGoodOrder(writer, face.getNormal().getZ());

		writeInGoodOrder(writer, face.getP1().getX());
		writeInGoodOrder(writer, face.getP1().getY());
		writeInGoodOrder(writer, face.getP1().getZ());

		writeInGoodOrder(writer, face.getP2().getX());
		writeInGoodOrder(writer, face.getP2().getY());
		writeInGoodOrder(writer, face.getP2().getZ());

		writeInGoodOrder(writer, face.getP3().getX());
		writeInGoodOrder(writer, face.getP3().getY());
		writeInGoodOrder(writer, face.getP3().getZ());

		writer.write(new byte[2]);
	}

	public static void writeSTLB(String fileName,  Mesh surface) {
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fileName));

			byte[] header = new byte[80];
			stream.write(header);

			ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
			bBuf.order(ByteOrder.LITTLE_ENDIAN);
			bBuf.putInt(surface.size());
			stream.write(bBuf.array(), 0, Integer.SIZE/8);

			for(Triangle t : surface) {
				writeBinaryFace(stream, t);
			}

			stream.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}