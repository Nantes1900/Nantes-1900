package fr.nantes1900.utils;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Write a STL file containing the faces, list of faces or buildings. This
 * writer use the ASCII or the binary format, depending on the static attribute
 * MODE.
 * 
 * @author Eric Berthe, Valentin Roger, Daniel Lefèvre
 */

// FIXME : create a not static writer.
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
     * The name of the file to write in.
     */
    private String fileName;

    /**
     * The mesh to write.
     */
    private Mesh mesh;

    /**
     * The mode of writing. Use the two constants : ASCII_MODE or BINARY_MODE.
     */
    private int writingMode = WriterSTL.BINARY_MODE;

    /**
     * Constructor.
     * 
     * @param fileNameWrite
     *            the name of the file to write in
     */
    public WriterSTL(
        final String fileNameWrite) {
        this.fileName = fileNameWrite;
    }

    /**
     * Constructor.
     * 
     * @param fileNameWrite
     *            the name of the file to write in
     * @param mode
     *            the mode of the writer
     */
    public WriterSTL(
        final String fileNameWrite, final int mode) {
        this.fileName = fileNameWrite;
        this.writingMode = mode;
    }

    /**
     * @param fw
     *            The file we want to write in.
     * @param face
     *            The face to be written.
     */
    private void writeASCIIFace(final BufferedWriter fw, final Triangle face) {
        try {
            // Write facet normal : to begin a triangle with writing its normal.
            String s1 = "\nfacet normal";

            s1 +=
                " " + face.getNormal().x + " " + face.getNormal().y + " "
                    + face.getNormal().z;

            // Write outer loop : to begin to write the three points.
            fw.write(s1 + "\nouter loop");
            // Write the three points.
            for (Point p : face.getPoints()) {

                fw.write("\nvertex" + " " + p.getX() + " " + p.getY() + " "
                    + p.getZ());
            }

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
    // TODO : rename face in triangle here and above.
    private void
        writeBinaryFace(final OutputStream writer, final Triangle face)
            throws IOException {
        // Write first the normal.
        this.writeInGoodOrder(writer, face.getNormal().getX());
        this.writeInGoodOrder(writer, face.getNormal().getY());
        this.writeInGoodOrder(writer, face.getNormal().getZ());

        // And the three points after.
        this.writeInGoodOrder(writer, face.getP1().getX());
        this.writeInGoodOrder(writer, face.getP1().getY());
        this.writeInGoodOrder(writer, face.getP1().getZ());

        this.writeInGoodOrder(writer, face.getP2().getX());
        this.writeInGoodOrder(writer, face.getP2().getY());
        this.writeInGoodOrder(writer, face.getP2().getZ());

        this.writeInGoodOrder(writer, face.getP3().getX());
        this.writeInGoodOrder(writer, face.getP3().getY());
        this.writeInGoodOrder(writer, face.getP3().getZ());

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
    private void writeInGoodOrder(final OutputStream writer, final double a)
        throws IOException {
        // Write the double : must order it in the LITTLE_ENDIAN format.
        final ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
        bBuf.order(ByteOrder.LITTLE_ENDIAN);
        bBuf.putFloat((float) a);
        writer.write(bBuf.array(), 0, Float.SIZE / Byte.SIZE);
    }

    /**
     * Write a mesh in an ASCII file.
     * 
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
                this.writeASCIIFace(fw, f);
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
     * @throws IOException
     *             if there is a problem in the opening of the closing operation
     */
    private void writeSTLB() throws IOException {
        BufferedOutputStream stream = null;
        try {
            stream =
                new BufferedOutputStream(new FileOutputStream(this.fileName));

            // Write a 80-byte long header. Possibility to write the name of the
            // author.
            final int headerSize = 80;
            final byte[] header = new byte[headerSize];
            stream.write(header);

            // Write the number of triangles : must order the Int in the
            // LITTLE_ENDIAN format.
            final ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
            bBuf.order(ByteOrder.LITTLE_ENDIAN);
            bBuf.putInt(this.mesh.size());
            stream.write(bBuf.array(), 0, Integer.SIZE / Byte.SIZE);

            for (Triangle t : this.mesh) {
                this.writeBinaryFace(stream, t);
            }

            // Finish to write the last datas before closing the writer.
            stream.flush();
        } finally {
            // FIXME : if the directory is not created, then throw an exception
            // or create it !
            stream.close();
        }
    }

    /**
     * Allows to know the value of the attribute MODE.
     * 
     * @return the attribute MODE
     */
    public final int getWriteMode() {
        return this.writingMode;
    }

    /**
     * Setter.
     * 
     * @param m
     *            the mesh to write
     */
    public final void setMesh(final Mesh m) {
        this.mesh = m;
    }

    /**
     * Allows to change the writing mode attribute : MODE.
     * 
     * @param mode
     *            the new mode
     */
    public final void setWriteMode(final int mode) {
        this.writingMode = mode;
    }

    /**
     * Write a mesh, depending on the attribute MODE.
     */
    public final void write() {
        try {
            if (this.mesh == null) {
                throw new NoMeshException();
            }
            if (this.writingMode == WriterSTL.ASCII_MODE) {
                this.writeSTLA();
            } else if (this.writingMode == WriterSTL.BINARY_MODE) {
                this.writeSTLB();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoMeshException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implements an exception when no mesh is found on the writer.
     * 
     * @author Daniel Lefevre
     */
    public final class NoMeshException extends Exception {

        /**
         * Version attribute.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Private constructor.
         */
        private NoMeshException() {
        }
    }
}
