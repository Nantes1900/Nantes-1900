package nantes1900.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import nantes1900.models.Polyline;
import nantes1900.models.basis.Triangle;
import nantes1900.models.extended.Floor;
import nantes1900.models.extended.SpecialBuilding;

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
 * Implement a writer of a town to a CityGML file.
 * 
 * @author Daniel Lefevre
 */
public class WriterCityGML {

	private CityGMLFactory citygml = new CityGMLFactory();
	private GMLFactory gml = new GMLFactory();
	private CityModel cityModel = citygml.createCityModel();
	private GMLIdManager gmlIdManager = DefaultGMLIdManager.getInstance();
	private GMLGeometryFactory geom = new GMLGeometryFactory();
	private JAXBBuilder builder;

	private String fileName = new String();

	/**
	 * Constructor
	 */
	public WriterCityGML(String fileName) {

		try {
			CityGMLContext ctx = new CityGMLContext();
			this.builder = ctx.createJAXBBuilder();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		this.fileName = fileName;
	}

	/**
	 * Add a building to the CityGMLFactory
	 * 
	 * @param buildingToAdd
	 *            the Edifice to write
	 */
	public void addBuilding(nantes1900.models.extended.Building buildingToAdd) {
		Building building = citygml.createBuilding();

		// LOD2 solid
		List<SurfaceProperty> surfaceMember = new ArrayList<SurfaceProperty>();

		CompositeSurface compositeSurface = this.gml.createCompositeSurface();
		compositeSurface.setSurfaceMember(surfaceMember);
		Solid solid = this.gml.createSolid();
		solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

		building.setLod2Solid(this.gml.createSolidProperty(solid));

		// Thematic boundary surfaces
		List<BoundarySurfaceProperty> boundedBy = new ArrayList<BoundarySurfaceProperty>();

		try {
			for (Polyline surface : buildingToAdd.getWalls()) {
				Polygon geometry = geom.createLinearPolygon(
						surface.getPointsAsCoordinates(), 3);
				geometry.setId(this.gmlIdManager.generateGmlId());
				surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
						.getId()));

				AbstractBoundarySurface boundarySurface = citygml
						.createWallSurface();

				boundarySurface.setLod2MultiSurface(gml
						.createMultiSurfaceProperty(gml
								.createMultiSurface(geometry)));

				boundedBy.add(citygml
						.createBoundarySurfaceProperty(boundarySurface));
			}

			for (Polyline surface : buildingToAdd.getRoofs()) {
				Polygon geometry = geom.createLinearPolygon(
						surface.getPointsAsCoordinates(), 3);
				geometry.setId(this.gmlIdManager.generateGmlId());
				surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
						.getId()));

				AbstractBoundarySurface boundarySurface = citygml
						.createRoofSurface();

				boundarySurface.setLod2MultiSurface(gml
						.createMultiSurfaceProperty(gml
								.createMultiSurface(geometry)));

				boundedBy.add(citygml
						.createBoundarySurfaceProperty(boundarySurface));
			}
		} catch (DimensionMismatchException e) {
			e.printStackTrace();
		}

		building.setBoundedBySurface(boundedBy);

		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));
	}

	/**
	 * Add a list of edifices to the CityGMLFactory
	 * 
	 * @param buildingToAdd
	 *            the list of edifices to write
	 */
	public void addBuildings(List<nantes1900.models.extended.Building> buildings) {
		for (nantes1900.models.extended.Building building : buildings) {
			this.addBuilding(building);
		}
	}

	/**
	 * Add a ground to the CityGMLFactory
	 * 
	 * @param floor
	 *            the mesh to write
	 */
	public void addFloor(Floor floor) {
		// FIXME : make a real ground...
		Building building = citygml.createBuilding();

		// LOD2 solid
		List<SurfaceProperty> surfaceMember = new ArrayList<SurfaceProperty>();

		CompositeSurface compositeSurface = this.gml.createCompositeSurface();
		compositeSurface.setSurfaceMember(surfaceMember);
		Solid solid = this.gml.createSolid();
		solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

		building.setLod2Solid(this.gml.createSolidProperty(solid));

		// Thematic boundary surfaces
		List<BoundarySurfaceProperty> boundedBy = new ArrayList<BoundarySurfaceProperty>();

		try {
			for (Triangle t : floor.getMesh()) {
				Polygon geometry = geom.createLinearPolygon(
						t.getPointsAsCoordinates(), 3);
				geometry.setId(this.gmlIdManager.generateGmlId());
				surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
						.getId()));
				// TODO : peut-être classer en WALL et ROOF SURFACE !

				AbstractBoundarySurface boundarySurface = citygml
						.createRoofSurface();

				boundarySurface.setLod2MultiSurface(gml
						.createMultiSurfaceProperty(gml
								.createMultiSurface(geometry)));

				boundedBy.add(citygml
						.createBoundarySurfaceProperty(boundarySurface));
			}
		} catch (DimensionMismatchException e) {
			e.printStackTrace();
		}

		building.setBoundedBySurface(boundedBy);

		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));
	}

	/**
	 * Add a list of grounds to the CityGMLFactory
	 * 
	 * @param floor
	 *            the list of meshes to write
	 */
	public void addFloors(List<Floor> floors) {
		for (Floor floor : floors) {
			this.addFloor(floor);
		}
	}

	/**
	 * Create a CityGMLBuilding, and add the special building as a mesh.
	 * 
	 * @param specialBuilding
	 *            the mesh of the special building
	 */
	public void addSpecialBuilding(SpecialBuilding specialBuilding) {
		Building building = citygml.createBuilding();

		// LOD2 solid
		List<SurfaceProperty> surfaceMember = new ArrayList<SurfaceProperty>();

		CompositeSurface compositeSurface = this.gml.createCompositeSurface();
		compositeSurface.setSurfaceMember(surfaceMember);
		Solid solid = this.gml.createSolid();
		solid.setExterior(this.gml.createSurfaceProperty(compositeSurface));

		building.setLod2Solid(this.gml.createSolidProperty(solid));

		// Thematic boundary surfaces
		List<BoundarySurfaceProperty> boundedBy = new ArrayList<BoundarySurfaceProperty>();

		try {
			for (Triangle t : specialBuilding.getMesh()) {
				Polygon geometry = geom.createLinearPolygon(
						t.getPointsAsCoordinates(), 3);
				geometry.setId(this.gmlIdManager.generateGmlId());
				surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
						.getId()));
				// TODO : peut-être classer en WALL et ROOF SURFACE !

				AbstractBoundarySurface boundarySurface = citygml
						.createRoofSurface();

				boundarySurface.setLod2MultiSurface(gml
						.createMultiSurfaceProperty(gml
								.createMultiSurface(geometry)));

				boundedBy.add(citygml
						.createBoundarySurfaceProperty(boundarySurface));
			}
		} catch (DimensionMismatchException e) {
			e.printStackTrace();
		}

		building.setBoundedBySurface(boundedBy);

		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));
	}

	/**
	 * Add a list of special Buildings as meshes.
	 * 
	 * @param specialBuilding
	 *            the list of the special buildings
	 */
	public void addSpecialBuildings(List<SpecialBuilding> specialBuildings) {
		for (SpecialBuilding specialBuilding : specialBuildings) {
			this.addSpecialBuilding(specialBuilding);
		}
	}

	/**
	 * Write the CityGML file with the CityGMLFactory
	 * 
	 * @param fileName
	 *            the name of the file to write in
	 */
	public void write() {

		CityGMLOutputFactory out;
		try {
			out = builder.createCityGMLOutputFactory(CityGMLVersion.v1_0_0);
			CityGMLWriter writer = out.createCityGMLWriter(new File(
					this.fileName));

			writer.setPrefixes(CityGMLVersion.v1_0_0);
			writer.setSchemaLocations(CityGMLVersion.v1_0_0);
			writer.setIndentString("  ");
			writer.write(cityModel);
			writer.close();
		} catch (CityGMLReadException e) {
			e.printStackTrace();
		} catch (CityGMLWriteException e) {
			e.printStackTrace();
		}

	}
}