package fr.nantes1900.utils;

import fr.nantes1900.models.Polyline;
import fr.nantes1900.models.basis.Triangle;
import fr.nantes1900.models.extended.Ground;
import fr.nantes1900.models.extended.SpecialBuilding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.JAXBBuilder;
import org.citygml4j.factory.CityGMLFactory;
import org.citygml4j.factory.GMLFactory;
import org.citygml4j.factory.geometry.DimensionMismatchException;
import org.citygml4j.factory.geometry.GMLGeometryFactory;
import org.citygml4j.model.citygml.building.AbstractBoundarySurface;
import org.citygml4j.model.citygml.building.BoundarySurfaceProperty;
import org.citygml4j.model.citygml.building.Building;
import org.citygml4j.model.citygml.core.CityModel;
import org.citygml4j.model.gml.geometry.complexes.CompositeSurface;
import org.citygml4j.model.gml.geometry.primitives.Polygon;
import org.citygml4j.model.gml.geometry.primitives.Solid;
import org.citygml4j.model.gml.geometry.primitives.SurfaceProperty;
import org.citygml4j.model.module.citygml.CityGMLVersion;
import org.citygml4j.util.gmlid.DefaultGMLIdManager;
import org.citygml4j.util.gmlid.GMLIdManager;
import org.citygml4j.xml.io.CityGMLOutputFactory;
import org.citygml4j.xml.io.reader.CityGMLReadException;
import org.citygml4j.xml.io.writer.CityGMLWriteException;
import org.citygml4j.xml.io.writer.CityGMLWriter;

/**
 * Implements a CityGML writer of a Town.
 * 
 * @author Daniel Lefevre
 */
public class WriterCityGML {

    /**
     * The city gml factory.
     */
    private CityGMLFactory citygml = new CityGMLFactory();
    /**
     * The gml factory.
     */
    private GMLFactory gml = new GMLFactory();
    /**
     * The city model.
     */
    private CityModel cityModel = this.citygml.createCityModel();
    /**
     * The gml ID manager.
     */
    private GMLIdManager gmlIdManager = DefaultGMLIdManager.getInstance();
    /**
     * The geometry factory.
     */
    private GMLGeometryFactory geom = new GMLGeometryFactory();
    /**
     * The JAXB building.
     */
    private JAXBBuilder builder;

    /**
     * The name of the file to write in.
     */
    private String fileName;

    /**
     * Constructor.
     * 
     * @param fileNameWrite
     *            the file to write in
     */
    public WriterCityGML(final String fileNameWrite) {

        try {
            final CityGMLContext ctx = new CityGMLContext();
            this.builder = ctx.createJAXBBuilder();
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        this.fileName = fileNameWrite;
    }

    /**
     * Adds a building to the CityGMLFactory.
     * 
     * @param buildingToAdd
     *            the building to write
     */
    public final void addBuilding(
        final fr.nantes1900.models.extended.Building buildingToAdd) {
        final Building building = this.citygml.createBuilding();

        // LOD2 solid
        final List<SurfaceProperty> surfaceMember =
            new ArrayList<SurfaceProperty>();

        // Creates the surface object.
        final CompositeSurface compositeSurface =
            this.gml.createCompositeSurface();
        compositeSurface.setSurfaceMember(surfaceMember);
        final Solid solid = this.gml.createSolid();
        solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

        building.setLod2Solid(this.gml.createSolidProperty(solid));

        // Thematic boundary surfaces
        final List<BoundarySurfaceProperty> boundedBy =
            new ArrayList<BoundarySurfaceProperty>();

        try {
            for (Polyline surface : buildingToAdd.getWalls()) {
                // Creates the geometry as a suite of coordinates.
                final Polygon geometry =
                    this.geom.createLinearPolygon(surface
                        .getPointsAsCoordinates(), 3);

                // Adds an ID.
                geometry.setId(this.gmlIdManager.generateGmlId());
                surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
                    .getId()));

                // Creates a surface.
                final AbstractBoundarySurface boundarySurface =
                    this.citygml.createWallSurface();

                // Adds the polygon as a surface.
                boundarySurface.setLod2MultiSurface(this.gml
                    .createMultiSurfaceProperty(this.gml
                        .createMultiSurface(geometry)));

                boundedBy.add(this.citygml
                    .createBoundarySurfaceProperty(boundarySurface));
            }

            // Idem for the roofs.
            for (Polyline surface : buildingToAdd.getRoofs()) {
                // Creates the polygon.
                final Polygon geometry =
                    this.geom.createLinearPolygon(surface
                        .getPointsAsCoordinates(), 3);
                geometry.setId(this.gmlIdManager.generateGmlId());
                surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
                    .getId()));

                // Creates the roof.
                final AbstractBoundarySurface boundarySurface =
                    this.citygml.createRoofSurface();

                // Adds the polygon as the surface.
                boundarySurface.setLod2MultiSurface(this.gml
                    .createMultiSurfaceProperty(this.gml
                        .createMultiSurface(geometry)));

                boundedBy.add(this.citygml
                    .createBoundarySurfaceProperty(boundarySurface));
            }
        } catch (DimensionMismatchException e) {
            e.printStackTrace();
        }

        building.setBoundedBySurface(boundedBy);

        this.cityModel.setBoundedBy(building.calcBoundedBy(false));
        this.cityModel.addCityObjectMember(this.citygml
            .createCityObjectMember(building));
    }

    /**
     * Adds a list of edifices to the CityGMLFactory.
     * 
     * @param buildings
     *            the list of buildings to add
     */
    public final void addBuildings(
        final List<fr.nantes1900.models.extended.Building> buildings) {
        for (fr.nantes1900.models.extended.Building building : buildings) {
            this.addBuilding(building);
        }
    }

    /**
     * Adds a ground to the CityGMLFactory.
     * 
     * @param ground
     *            the ground to add
     */
    public final void addGround(final Ground ground) {
        final Building building = this.citygml.createBuilding();

        // LOD2 solid
        final List<SurfaceProperty> surfaceMember =
            new ArrayList<SurfaceProperty>();

        final CompositeSurface compositeSurface =
            this.gml.createCompositeSurface();
        compositeSurface.setSurfaceMember(surfaceMember);
        final Solid solid = this.gml.createSolid();
        solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

        building.setLod2Solid(this.gml.createSolidProperty(solid));

        // Thematic boundary surfaces
        final List<BoundarySurfaceProperty> boundedBy =
            new ArrayList<BoundarySurfaceProperty>();

        try {
            for (Triangle t : ground.getMesh()) {
                // Creates the polygon.
                final Polygon geometry =
                    this.geom
                        .createLinearPolygon(t.getPointsAsCoordinates(), 3);
                geometry.setId(this.gmlIdManager.generateGmlId());
                surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
                    .getId()));

                // Creates the ground surface.
                final AbstractBoundarySurface boundarySurface =
                    this.citygml.createGroundSurface();

                // Adds the polygon as the surface.
                boundarySurface.setLod2MultiSurface(this.gml
                    .createMultiSurfaceProperty(this.gml
                        .createMultiSurface(geometry)));

                boundedBy.add(this.citygml
                    .createBoundarySurfaceProperty(boundarySurface));
            }
        } catch (DimensionMismatchException e) {
            e.printStackTrace();
        }

        building.setBoundedBySurface(boundedBy);

        this.cityModel.setBoundedBy(building.calcBoundedBy(false));
        this.cityModel.addCityObjectMember(this.citygml
            .createCityObjectMember(building));
    }

    /**
     * Adds a list of grounds to the CityGMLFactory.
     * 
     * @param grounds
     *            the list of grounds to add
     */
    public final void addGrounds(final List<Ground> grounds) {
        for (Ground ground : grounds) {
            this.addGround(ground);
        }
    }

    /**
     * Creates a CityGML Building, and adds the special building as a mesh.
     * 
     * @param specialBuilding
     *            the mesh of the special building
     */
    public final void addSpecialBuilding(final SpecialBuilding specialBuilding) {
        final Building building = this.citygml.createBuilding();

        // LOD2 solid
        final List<SurfaceProperty> surfaceMember =
            new ArrayList<SurfaceProperty>();

        final CompositeSurface compositeSurface =
            this.gml.createCompositeSurface();
        compositeSurface.setSurfaceMember(surfaceMember);
        final Solid solid = this.gml.createSolid();
        solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

        building.setLod2Solid(this.gml.createSolidProperty(solid));

        // Thematic boundary surfaces
        final List<BoundarySurfaceProperty> boundedBy =
            new ArrayList<BoundarySurfaceProperty>();

        try {
            for (Triangle t : specialBuilding.getMesh()) {
                // Creates the polygon.
                final Polygon geometry =
                    this.geom
                        .createLinearPolygon(t.getPointsAsCoordinates(), 3);
                geometry.setId(this.gmlIdManager.generateGmlId());
                surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
                    .getId()));

                // Creates the building as a surface.
                final AbstractBoundarySurface boundarySurface =
                    this.citygml.createWallSurface();

                // Adds the polygon as the surface.
                boundarySurface.setLod2MultiSurface(this.gml
                    .createMultiSurfaceProperty(this.gml
                        .createMultiSurface(geometry)));

                boundedBy.add(this.citygml
                    .createBoundarySurfaceProperty(boundarySurface));
            }
        } catch (DimensionMismatchException e) {
            e.printStackTrace();
        }

        building.setBoundedBySurface(boundedBy);

        this.cityModel.setBoundedBy(building.calcBoundedBy(false));
        this.cityModel.addCityObjectMember(this.citygml
            .createCityObjectMember(building));
    }

    /**
     * Adds a list of special buildings as meshes.
     * 
     * @param specialBuildings
     *            the list of the special buildings
     */
    public final void addSpecialBuildings(
        final List<SpecialBuilding> specialBuildings) {
        for (SpecialBuilding specialBuilding : specialBuildings) {
            this.addSpecialBuilding(specialBuilding);
        }
    }

    /**
     * Writes the CityGML file with the CityGMLFactory.
     */
    public final void write() {

        CityGMLOutputFactory out;
        try {
            out =
                this.builder.createCityGMLOutputFactory(CityGMLVersion.v1_0_0);
            final CityGMLWriter writer =
                out.createCityGMLWriter(new File(this.fileName));

            writer.setPrefixes(CityGMLVersion.v1_0_0);
            writer.setSchemaLocations(CityGMLVersion.v1_0_0);
            writer.setIndentString("  ");
            writer.write(this.cityModel);
            writer.close();
        } catch (CityGMLReadException e) {
            e.printStackTrace();
        } catch (CityGMLWriteException e) {
            e.printStackTrace();
        }

    }
}
