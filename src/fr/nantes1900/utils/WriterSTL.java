package fr.nantes1900.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Point;
import fr.nantes1900.models.basis.Triangle;

/**
 * Implements a STL writer. This writer uses the ASCII or the binary format,
 * depending on the static attribute MODE.
 * @author Eric Berthe, Valentin Roger, Daniel Lefevre
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
     * The name of the file to write in.
     */
    private final String fileName;

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
     * @param fileNameWrite
     *            the name of the file to write in
     */
    public WriterSTL(final String fileNameWrite) {
        this.fileName = fileNameWrite;
    }

    /**
     * Constructor.
     * @param fileNameWrite
     *            the name of the file to write in
     * @param mode
     *            the mode of the writer
     */
    public WriterSTL(final String fileNameWrite, final int mode) {
        this.fileName = fileNameWrite;
        this.writingMode = mode;
    }

    /**
     * Writes a triangle in ASCII.
     * @param writer
     *            The file we want to write in.
     * @param triangle
     *            The triangle to write.
     */
    private static void writeASCIITriangle(final BufferedWriter writer,
            final Triangle triangle) {
        try {
            // Write facet normal : to begin a triangle with writing its normal.
            String s1 = "\nfacet normal";

            s1 += " " + triangle.getNormal().x + " " + triangle.getNormal().y
                    + " " + triangle.getNormal().z;

            // Write outer loop : to begin to write the three points.
            writer.write(s1 + "\nouter loop");
            // Write the three points.
            for (final Point p : triangle.getPoints()) {
                writer.write("\nvertex" + " " + p.getX() + " " + p.getY() + " "
                        + p.getZ());
            }

            // Write the end of the facet.
            writer.write("\nendloop\nendfacet");
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a triangle in the binary format.
     * @param writer
     *            the writer which writes in the file
     * @param triangle
     *            the triangle to write
     * @throws IOException
     *             if the writer throws an error
     */
    private static void writeBinaryTriangle(final OutputStream writer,
            final Triangle triangle) throws IOException {

        // Write first the normal.
        WriterSTL.writeInGoodOrder(writer, triangle.getNormal().getX());
        WriterSTL.writeInGoodOrder(writer, triangle.getNormal().getY());
        WriterSTL.writeInGoodOrder(writer, triangle.getNormal().getZ());

        // And the three points after.
        WriterSTL.writeInGoodOrder(writer, triangle.getP1().getX());
        WriterSTL.writeInGoodOrder(writer, triangle.getP1().getY());
        WriterSTL.writeInGoodOrder(writer, triangle.getP1().getZ());

        WriterSTL.writeInGoodOrder(writer, triangle.getP2().getX());
        WriterSTL.writeInGoodOrder(writer, triangle.getP2().getY());
        WriterSTL.writeInGoodOrder(writer, triangle.getP2().getZ());

        WriterSTL.writeInGoodOrder(writer, triangle.getP3().getX());
        WriterSTL.writeInGoodOrder(writer, triangle.getP3().getY());
        WriterSTL.writeInGoodOrder(writer, triangle.getP3().getZ());

        writer.write(new byte[2]);
    }

    /**
     * Writes a double in the good byte order (LITTLE_ENDIAN) in the writer.
     * @param writer
     *            the writer which writes in the file
     * @param a
     *            the double to write
     * @throws IOException
     *             if the writer throws an error
     */
    private static void writeInGoodOrder(final OutputStream writer,
            final double a) throws IOException {

        // Write the double, but must before order it in the LITTLE_ENDIAN
        // format.
        final ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
        bBuf.order(ByteOrder.LITTLE_ENDIAN);
        bBuf.putFloat((float) a);

        writer.write(bBuf.array(), 0, Float.SIZE / Byte.SIZE);
    }

    /**
     * Returns the value of the attribute MODE.
     * @return the attribute MODE
     */
    public final int getWriteMode() {
        return this.writingMode;
    }

    /**
     * Setter.
     * @param m
     *            the mesh to write
     */
    public final void setMesh(final Mesh m) {
        this.mesh = m;
    }

    /**
     * Changes the writing mode attribute : MODE.
     * @param mode
     *            the new mode
     */
    public final void setWriteMode(final int mode) {
        this.writingMode = mode;
    }

    /**
     * Writes a mesh, the format depending on the attribute MODE.
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

        } catch (final IOException e) {
            e.printStackTrace();
        } catch (final NoMeshException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes a mesh in an ASCII file.
     * @throws IOException
     *             if there is a problem in the opening or the closing operation
     */
    private void writeSTLA() throws IOException {
        BufferedWriter writer = null;
        try {

            // Writes the header of the file : solid.
            writer = new BufferedWriter(new FileWriter(this.fileName));
            writer.write("solid");
            for (final Triangle f : this.mesh) {
                WriterSTL.writeASCIITriangle(writer, f);
            }

            // Writes the end of the file : endsolid.
            writer.write("\nendsolid");

            // Finishes to write the last datas before closing the writer.
            writer.flush();

        } finally {
            if (writer != null) {
                writer.close();
            }
        }

    }

    /**
     * Writes a mesh in an binary file.
     * @throws IOException
     *             if there is a problem in the opening or the closing operation
     */
    private void writeSTLB() throws IOException {
        BufferedOutputStream stream = null;
        try {
            stream = new BufferedOutputStream(new FileOutputStream(
                    this.fileName));

            // Writes a 80-byte long header. Possibility to write the name of
            // the author.
            final int headerSize = 80;
            final byte[] header = new byte[headerSize];
            stream.write(header);

            // Writes the number of triangles : must order the Int in the
            // LITTLE_ENDIAN format.
            final ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
            bBuf.order(ByteOrder.LITTLE_ENDIAN);
            bBuf.putInt(this.mesh.size());
            stream.write(bBuf.array(), 0, Integer.SIZE / Byte.SIZE);

            // Writes every triangle.
            for (final Triangle t : this.mesh) {
                WriterSTL.writeBinaryTriangle(stream, t);
            }

            // Finishes to write the last datas before closing the writer.
            stream.flush();
            stream.close();
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Implements an exception when no mesh is found on the writer.
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
        public NoMeshException() {
        }
    }
}
