package utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import modeles.Mesh;
import modeles.Triangle;

/**
 * Write a STL file containing the faces, list of faces or buildings. This
 * writer use the ASCII or the binary format, depending on the static attribute
 * MODE.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 */
public class WriterSTL {

	/**
	 * Possible value of MODE. Intend to write ASCII STL files.
	 */
	public static final int ASCII_MODE = 1;

	/**
	 * Possible value of MODE. Intend to write binary STL files.
	 */
	public static final int BINARY_MODE = 2;

	/**
	 * The mode of writing. Use the two constants : ASCII_MODE or BINARY_MODE.
	 */
	private static int MODE = ASCII_MODE;

	// /**
	// * The logger to write the informations in.
	// */
	// private static Logger log = Logger.getLogger("logger");

	/**
	 * Write a mesh, depending on the attribute MODE.
	 * 
	 * @param fileName
	 *            the name of the file to write in
	 * @param m
	 *            the mesh to write
	 */
	public static void write(String fileName, Mesh m) {
		if (MODE == ASCII_MODE)
			writeA(fileName, m);
		else if (MODE == BINARY_MODE)
			writeB(fileName, m);
	}

	/**
	 * Allows to change the writing mode attribute : MODE.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public static void setWriteMode(int mode) {
		MODE = mode;
	}

	/**
	 * Allows to know the value of the attribute MODE.
	 * 
	 * @return the attribute MODE
	 */
	public static int getWriteMode() {
		return MODE;
	}

	/**
	 * Write a mesh in an ASCII file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param m
	 *            the mesh to write
	 */
	private static void writeA(String fileName, Mesh m) {
		WriterSTL.writeSTLA(fileName, m);
		// log.info(fileName + " written in STL ASCII!");
	}

	/**
	 * Write a mesh in an binary file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param m
	 *            the mesh to write
	 */
	private static void writeB(String fileName, Mesh m) {
		WriterSTL.writeSTLB(fileName, m);
		// log.info(fileName + " written in STL binary!");
	}

	/**
	 * 
	 * @param fw
	 *            The file we want to write in.
	 * @param face
	 *            The face to be written.
	 */
	private static void writeASCIIFace(BufferedWriter fw, Triangle face) {
		try {
			// Write facet normal : to begin a triangle with writing its normal.
			fw.write("\nfacet normal");

			String s1 = new String();

			double[] t = new double[3];
			face.getNormal().get(t);
			// Write the normal.
			for (int j = 0; j < 3; j++) {
				s1 += " " + t[j];
			}

			// Write outer loop : to begin to write the three points.
			fw.write(s1 + "\nouter loop");
			// Write the three points.
			fw.write("\nvertex" + " " + face.getP1().getX() + " "
					+ face.getP1().getY() + " " + face.getP1().getZ());
			fw.write("\nvertex" + " " + face.getP2().getX() + " "
					+ face.getP2().getY() + " " + face.getP2().getZ());
			fw.write("\nvertex" + " " + face.getP3().getX() + " "
					+ face.getP3().getY() + " " + face.getP3().getZ());

			// Write the end of the facet.
			fw.write("\nendloop\nendfacet");
		} catch (java.io.IOException ee) {
			ee.printStackTrace();
		}
	}

	/**
	 * Write a mesh in an ASCII file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param surface
	 *            the mesh to write
	 */
	private static void writeSTLA(String fileName, Mesh surface) {
		try {
			// Write the header of the file : solid.
			BufferedWriter fw = new BufferedWriter(new FileWriter(fileName));
			fw.write("solid");
			for (Triangle f : surface) {
				writeASCIIFace(fw, f);
			}
			// Write the end of the file : endsolid.
			fw.write("\nendsolid");
			// Finish to write the last datas before closing the writer.
			fw.flush();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write a double in the good order (LITTLE_ENDIAN) in the writer.
	 * 
	 * @param writer
	 *            the writer which writes in the file
	 * @param a
	 *            the double to write
	 * @throws IOException
	 *             if the writer throws an error
	 */
	private static void writeInGoodOrder(OutputStream writer, double a)
			throws IOException {
		// Write the double : must order it in the LITTLE_ENDIAN format.
		ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
		bBuf.order(ByteOrder.LITTLE_ENDIAN);
		bBuf.putFloat((float) a);
		writer.write(bBuf.array(), 0, Float.SIZE / 8);
	}

	/**
	 * Write a triangle in the binary format.
	 * 
	 * @param writer
	 *            the writer which writes in the file
	 * @param face
	 *            the triangle to write
	 * @throws IOException
	 *             if the writer throws an error
	 */
	private static void writeBinaryFace(OutputStream writer, Triangle face)
			throws IOException {
		// Write first the normal.
		writeInGoodOrder(writer, face.getNormal().getX());
		writeInGoodOrder(writer, face.getNormal().getY());
		writeInGoodOrder(writer, face.getNormal().getZ());

		// And the three points after.
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

	/**
	 * Write a mesh in an binary file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param surface
	 *            the mesh to write
	 */
	private static void writeSTLB(String fileName, Mesh surface) {
		try {
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(fileName));

			// Write a 80-byte long header. Possibility to write the name of the
			// author.
			byte[] header = new byte[80];
			stream.write(header);

			// Write the number of triangles : must order the Int in the
			// LITTLE_ENDIAN format.
			ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
			bBuf.order(ByteOrder.LITTLE_ENDIAN);
			bBuf.putInt(surface.size());
			stream.write(bBuf.array(), 0, Integer.SIZE / 8);

			for (Triangle t : surface) {
				writeBinaryFace(stream, t);
			}

			// Finish to write the last datas before closing the writer.
			stream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}