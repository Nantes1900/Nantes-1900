package fr.nantes1900.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Triangle;


/**
 * Write a STL file containing the faces, list of faces or buildings. This
 * writer use the ASCII or the binary format, depending on the static attribute
 * MODE.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 */

// FIXME : create a not static writer.
public class WriterSTL {

	private String fileName;
	private Mesh mesh = null;

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
	private int writingMode = BINARY_MODE;

	public WriterSTL(String fileName) {
		this.fileName = fileName;
	}

	public WriterSTL(String fileName, int mode) {
		this.fileName = fileName;
		this.writingMode = mode;
	}

	/**
	 * 
	 * @param fw
	 *            The file we want to write in.
	 * @param face
	 *            The face to be written.
	 */
	private void writeASCIIFace(BufferedWriter fw, Triangle face) {
		try {
			// Write facet normal : to begin a triangle with writing its normal.
			String s1 = "\nfacet normal";

			s1 += " " + face.getNormal().x + " " + face.getNormal().y + " "
					+ face.getNormal().z;

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
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
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
	private void writeBinaryFace(OutputStream writer, Triangle face)
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
	 * Write a double in the good order (LITTLE_ENDIAN) in the writer.
	 * 
	 * @param writer
	 *            the writer which writes in the file
	 * @param a
	 *            the double to write
	 * @throws IOException
	 *             if the writer throws an error
	 */
	private void writeInGoodOrder(OutputStream writer, double a)
			throws IOException {
		// Write the double : must order it in the LITTLE_ENDIAN format.
		ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
		bBuf.order(ByteOrder.LITTLE_ENDIAN);
		bBuf.putFloat((float) a);
		writer.write(bBuf.array(), 0, Float.SIZE / 8);
	}

	/**
	 * Write a mesh in an ASCII file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param surface
	 *            the mesh to write
	 * @throws IOException
	 *             if there is a problem in the opening or the closing operation
	 */
	private void writeSTLA() throws IOException {
		BufferedWriter fw = null;
		try {
			// Write the header of the file : solid.
			fw = new BufferedWriter(new FileWriter(this.fileName));
			fw.write("solid");
			for (Triangle f : this.mesh) {
				writeASCIIFace(fw, f);
			}
			// Write the end of the file : endsolid.
			fw.write("\nendsolid");
			// Finish to write the last datas before closing the writer.
			fw.flush();
		} finally {
			if (fw != null) {
				fw.close();
			}
		}

	}

	/**
	 * Write a mesh in an binary file.
	 * 
	 * @param fileName
	 *            the name of the file to write in.
	 * @param surface
	 *            the mesh to write
	 * @throws IOException
	 *             if there is a problem in the opening of the closing operation
	 */
	private void writeSTLB() throws IOException {
		BufferedOutputStream stream = null;
		try {
			stream = new BufferedOutputStream(new FileOutputStream(
					this.fileName));

			// Write a 80-byte long header. Possibility to write the name of the
			// author.
			byte[] header = new byte[80];
			stream.write(header);

			// Write the number of triangles : must order the Int in the
			// LITTLE_ENDIAN format.
			ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
			bBuf.order(ByteOrder.LITTLE_ENDIAN);
			bBuf.putInt(this.mesh.size());
			stream.write(bBuf.array(), 0, Integer.SIZE / 8);

			for (Triangle t : this.mesh) {
				writeBinaryFace(stream, t);
			}

			// Finish to write the last datas before closing the writer.
			stream.flush();
		} finally {
			// FIXME : if the directory is not created, then throw an exception
			// or create it !
			stream.close();
		}
	}

	public Mesh getMesh() {
		return this.mesh;
	}

	/**
	 * Allows to know the value of the attribute MODE.
	 * 
	 * @return the attribute MODE
	 */
	public int getWriteMode() {
		return writingMode;
	}

	public void setMesh(Mesh m) {
		this.mesh = m;
	}

	/**
	 * Allows to change the writing mode attribute : MODE.
	 * 
	 * @param mode
	 *            the new mode
	 */
	public void setWriteMode(int mode) {
		writingMode = mode;
	}

	/**
	 * Write a mesh, depending on the attribute MODE.
	 * 
	 * @param fileName
	 *            the name of the file to write in
	 * @param m
	 *            the mesh to write
	 */
	public void write() {
		try {
			if (mesh == null) {
				throw new NoMeshException();
			}
			if (writingMode == ASCII_MODE)
				this.writeSTLA();
			else if (writingMode == BINARY_MODE)
				this.writeSTLB();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoMeshException e) {
			e.printStackTrace();
		}
	}

	public class NoMeshException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}