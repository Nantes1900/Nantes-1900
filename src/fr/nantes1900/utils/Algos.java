package fr.nantes1900.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Surface;
import fr.nantes1900.models.islets.steps.ProgressComputer;

/**
 * Contains some algorithms used in the other classes.
 * @author Daniel Lefevre
 */
public final class Algos {

    /**
     * Private constructor.
     */
    private Algos() {
    }

    /**
     * Divides the mesh in block of neighbours. This method uses
     * returnNeighbours to find the neighbours of one triangle, and puts it into
     * a new mesh into the arraylist. Thus, it takes another triangle and make
     * again the same operation until there is no more triangle. This method
     * does not destroy the mesh in parameter.
     * @param m
     *            the mesh to divide
     * @return an array of the blocks-meshes
     */
    public static List<Mesh> blockExtract(final Mesh m) {
        final Set<Mesh> thingsList = new HashSet<>();
        final Mesh mesh = new Mesh(m);

        while (!mesh.isEmpty()) {

            final Mesh e = new Mesh();
            mesh.getOne().returnNeighbours(e, mesh);
            mesh.remove(e);
            thingsList.add(e);
        }

        return new ArrayList<>(thingsList);
    }

    /**
     * Divides the mesh in block of neighbours depending on their orientations.
     * This method takes one triangle and use returnNeighbours to find the
     * triangles which are oriented as the first one (with an error) finds into
     * them its neighbours, and puts it in a new mesh into the arraylist. Then
     * it takes another triangle and make again the same operation until there
     * is no more triangle. This method does not destroy the mesh in parameter.
     * @param m
     *            the mesh to divide
     * @param angleNormalErrorFactor
     *            the error on the orientation
     * @return an array of the blocks-meshs
     */
    public static List<Mesh> blockOrientedExtract(final Mesh m,
            final double angleNormalErrorFactor) {
        final List<Mesh> thingsList = new ArrayList<>();
        final List<Mesh> meshList = Algos.blockExtract(m);

        int counter = 0;
        for (final Mesh mesh : meshList) {
            counter += mesh.size();
        }

        ProgressComputer.initTrianglesCounter();
        ProgressComputer.setTrianglesNumber(counter);

        for (final Mesh mesh : meshList) {

            while (!mesh.isEmpty()) {

                final Mesh e = new Mesh();
                final Triangle tri = mesh.getOne();

                final Mesh oriented = mesh.orientedAs(tri.getNormal(),
                        angleNormalErrorFactor);

                tri.returnNeighbours(e, oriented);

                mesh.remove(e);
                thingsList.add(e);

                ProgressComputer.incTrianglesCounter(e.size());
            }
        }

        return thingsList;
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list). It thus adds it to the mesh.
     * @param list
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @return the sum of every meshes
     */
    public static Mesh blockTreatNoise(final List<Mesh> list, final Mesh noise) {
        final List<Mesh> m = new ArrayList<>();
        final Mesh result = new Mesh();

        for (final Mesh e : list) {
            final Mesh meshAndNoise = new Mesh(e);
            meshAndNoise.addAll(noise);
            final Mesh mes = new Mesh();
            e.getOne().returnNeighbours(mes, meshAndNoise);
            m.add(mes);
            noise.remove(mes);
        }

        for (Mesh mesh : m) {
            result.addAll(mesh);
        }

        return result;
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list) and which have the same orientation. It thus adds it to the
     * mesh.
     * @param surfaceList
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @param largeAngleNormalErrorFactor
     *            the error on the orientation
     */
    public static void blockTreatOrientedNoise(final List<Surface> surfaceList,
            final Mesh noise, final double largeAngleNormalErrorFactor) {

        final List<Surface> list = new ArrayList<>();

        for (final Surface e : surfaceList) {
            final Mesh meshAndNoise = new Mesh(e.getMesh());
            meshAndNoise.addAll(noise.orientedAs(e.getMesh().averageNormal(),
                    largeAngleNormalErrorFactor));
            final Mesh mes = new Mesh();
            e.getMesh().getOne().returnNeighbours(mes, meshAndNoise);
            list.add(new Surface(mes));

            noise.remove(mes);
        }

        surfaceList.clear();
        surfaceList.addAll(list);
    }

    /**
     * Treats a list of mesh to add the noise which is neighbour of the mesh.
     * This method tries to find a block of noise which completes the mesh (of
     * the list) and which is contained between two planes parallel to the mesh.
     * It thus adds it to the mesh.
     * @param list
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @param errorPlanes
     *            the distance between the two planes
     */
    public static void blockTreatPlanedNoise(final List<Mesh> list,
            final Mesh noise, final double errorPlanes) {

        final List<Mesh> m = new ArrayList<>();

        for (final Mesh e : list) {
            final Mesh meshAndNoise = new Mesh(e);
            meshAndNoise.addAll(noise.inPlanes(e.averageNormal(),
                    e.getCentroid(), errorPlanes));
            final Mesh mes = new Mesh();
            e.getOne().returnNeighbours(mes, meshAndNoise);
            m.add(mes);
            noise.remove(mes);
        }

        list.clear();
        list.addAll(m);
    }
}
