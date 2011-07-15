package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import modeles.Edifice;
import modeles.Mesh;
import modeles.Polyline;
import modeles.Triangle;

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
 * Implement a town as a CityGML set of floors, buildings, etc. Allows to write
 * a CityGML file.
 * 
 * @author Daniel Lefevre
 */
public class TownCityGML {

	private CityGMLFactory citygml;
	private GMLFactory gml;
	private CityModel cityModel;
	private GMLIdManager gmlIdManager;
	private JAXBBuilder builder;
	private GMLGeometryFactory geom;

	/**
	 * Constructor
	 */
	public TownCityGML() {

		try {
			this.citygml = new CityGMLFactory();
			this.gml = new GMLFactory();

			CityGMLContext ctx = new CityGMLContext();
			this.builder = ctx.createJAXBBuilder();
			this.geom = new GMLGeometryFactory();
			this.gmlIdManager = DefaultGMLIdManager.getInstance();
			this.cityModel = citygml.createCityModel();

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Add a ground to the CityGMLFactory
	 * 
	 * @param floor
	 *            the mesh to write
	 */
	public void addFloor(Mesh floor) {
		// GroundSurface ground = citygml.createGroundSurface();
	}

	/**
	 * Add a building to the CityGMLFactory
	 * 
	 * @param buildingToAdd
	 *            the Edifice to write
	 */
	public void addBuilding(Edifice buildingToAdd) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		building.setBoundedBySurface(boundedBy);

		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));
	}

	/**
	 * Create a CityGMLBuilding, and add the special building as a mesh.
	 * 
	 * @param specialBuilding
	 *            the mesh of the special building
	 */
	public void addSpecialBuilding(Mesh specialBuilding) {
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
			for (Triangle t : specialBuilding) {
				Polygon geometry = geom.createLinearPolygon(
						t.getPointsAsCoordinates(), 3);
				geometry.setId(this.gmlIdManager.generateGmlId());
				surfaceMember.add(this.gml.createSurfaceProperty('#' + geometry
						.getId()));
				// FIXME : peut-être classer en WALL et ROOF SURFACE !

				AbstractBoundarySurface boundarySurface = citygml
						.createRoofSurface();

				boundarySurface.setLod2MultiSurface(gml
						.createMultiSurfaceProperty(gml
								.createMultiSurface(geometry)));

				boundedBy.add(citygml
						.createBoundarySurfaceProperty(boundarySurface));
			}
		} catch (DimensionMismatchException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		building.setBoundedBySurface(boundedBy);

		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));
	}

	/**
	 * Write the CityGML file with the CityGMLFactory
	 * 
	 * @param fileName
	 *            the name of the file to write in
	 */
	public void write(String fileName) {

		CityGMLOutputFactory out;
		try {
			out = builder.createCityGMLOutputFactory(CityGMLVersion.v1_0_0);
			CityGMLWriter writer = out.createCityGMLWriter(new File(fileName
					+ ".xml"));

			writer.setPrefixes(CityGMLVersion.v1_0_0);
			writer.setSchemaLocations(CityGMLVersion.v1_0_0);
			writer.setIndentString("  ");
			writer.write(cityModel);
			writer.close();
		} catch (CityGMLReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CityGMLWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}