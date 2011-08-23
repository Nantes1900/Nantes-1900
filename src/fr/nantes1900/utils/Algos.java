package fr.nantes1900.utils;

import fr.nantes1900.models.Mesh;
import fr.nantes1900.models.Surface;
import fr.nantes1900.models.basis.Triangle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Contains some algorithms used in the other classes.
 * 
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
     * 
     * @param m
     *            the mesh to divide
     * @return an array of the blocks-meshs
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static List<Mesh> blockExtract(final Mesh m) {
        final Set<Mesh> thingsList = new HashSet<Mesh>();
        final Mesh mesh = new Mesh(m);

        while (!mesh.isEmpty()) {

            final Mesh e = new Mesh();
            mesh.getOne().returnNeighbours(e, mesh);
            mesh.remove(e);
            thingsList.add(e);

        }

        return new ArrayList<Mesh>(thingsList);
    }

    /**
     * Divides the mesh in block of neighbours depending on their orientations.
     * This method takes one triangle and use returnNeighbours to find the
     * triangles which are oriented as the first one (with an error) finds into
     * them its neighbours, and puts it in a new mesh into the arraylist. Then
     * it takes another triangle and make again the same operation until there
     * is no more triangle. This method does not destroy the mesh in parameter.
     * 
     * @param m
     *            the mesh to divide
     * @param angleNormalErrorFactor
     *            the error on the orientation
     * @return an array of the blocks-meshs
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static List<Mesh> blockOrientedAndPlaneExtract(final Mesh m,
        final double angleNormalErrorFactor,
        final double lengthPlanesErrorFactor) {

        final List<Mesh> thingsList = new ArrayList<Mesh>();
        List<Mesh> meshList = Algos.blockExtract(m);

        for (Mesh mesh : meshList) {
            while (!mesh.isEmpty()) {
                Mesh e = new Mesh();
                final Triangle tri = mesh.getOne();

                final Mesh oriented =
                    mesh.orientedAs(tri.getNormal(), angleNormalErrorFactor);

                tri.returnNeighbours(e, oriented);

                // e =
                // e.inPlanes(e.averageNormal(), e.getCentroid(),
                // lengthPlanesErrorFactor);

                mesh.remove(e);
                thingsList.add(e);
            }
        }

        return thingsList;
    }

    /**
     * Treats a list of mesh to add the noise which is part of the mesh. This
     * method tries to find a block of noise which complete the mesh (of the
     * list). It thus adds it to the mesh.
     * 
     * @param list
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static void blockTreatNoise(final List<Mesh> list, final Mesh noise) {

        final List<Mesh> m = new ArrayList<Mesh>();

        for (Mesh e : list) {
            final Mesh meshAndNoise = new Mesh(e);
            meshAndNoise.addAll(noise);
            final Mesh mes = new Mesh();
            e.getOne().returnNeighbours(mes, meshAndNoise);
            m.add(mes);
            noise.remove(mes);
        }

        list.clear();
        list.addAll(m);
    }

    /**
     * Treats a list of mesh to add the noise which is part of the mesh. This
     * method tries to find a block of noise which complete the mesh (of the
     * list) and which have the same orientation. It thus adds it to the mesh.
     * 
     * @param wallList
     *            the list of meshes to complete with noise
     * @param noise
     *            the whole noise
     * @param largeAngleNormalErrorFactor
     *            the error on the orientation
     * @throws MoreThanTwoTrianglesPerEdgeException
     *             if the edge is bad formed
     */
    public static void blockTreatOrientedNoise(final List<Surface> wallList,
        final Mesh noise, final double largeAngleNormalErrorFactor) {

        final List<Surface> list = new ArrayList<Surface>();

        for (Mesh e : wallList) {
            final Mesh meshAndNoise = new Mesh(e);
            final Mesh noiseOriented =
                noise
                    .orientedAs(e.averageNormal(), largeAngleNormalErrorFactor);
            meshAndNoise.addAll(noiseOriented);
            final Surface mes = new Surface();
            e.getOne().returnNeighbours(mes, meshAndNoise);
            list.add(mes);

            noise.remove(mes);
        }

        wallList.clear();
        wallList.addAll(list);
    }
}
