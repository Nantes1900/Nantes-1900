package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import modeles.Mesh;
import modeles.Triangle;

import org.citygml4j.CityGMLContext;
import org.citygml4j.builder.jaxb.JAXBBuilder;
import org.citygml4j.factory.CityGMLFactory;
import org.citygml4j.factory.GMLFactory;
import org.citygml4j.factory.geometry.DimensionMismatchException;
import org.citygml4j.factory.geometry.GMLGeometryFactory;
import org.citygml4j.model.citygml.CityGMLClass;
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

public class WriterCityGML {

	private static CityGMLFactory citygml = new CityGMLFactory();
	private static GMLFactory gml = new GMLFactory();

	public static void write(String fileName, Mesh mesh) throws JAXBException,
			CityGMLReadException, CityGMLWriteException,
			DimensionMismatchException {

		CityGMLContext ctx = new CityGMLContext();
		JAXBBuilder builder = ctx.createJAXBBuilder();

		GMLGeometryFactory geom = new GMLGeometryFactory();

		GMLIdManager gmlIdManager = DefaultGMLIdManager.getInstance();

		Building building = citygml.createBuilding();
		// lod2 solid
		List<SurfaceProperty> surfaceMember = new ArrayList<SurfaceProperty>();

		CompositeSurface compositeSurface = gml.createCompositeSurface();
		compositeSurface.setSurfaceMember(surfaceMember);
		Solid solid = gml.createSolid();
		solid.setExterior(gml.createSurfaceProperty(compositeSurface));

		building.setLod2Solid(gml.createSolidProperty(solid));

		// thematic boundary surfaces
		List<BoundarySurfaceProperty> boundedBy = new ArrayList<BoundarySurfaceProperty>();

		for (Triangle t : mesh) {
			Polygon p = geom.createLinearPolygon(t.getPointsAsCoordinates(), 3);
			p.setId(gmlIdManager.generateGmlId());
			surfaceMember.add(gml.createSurfaceProperty('#' + p.getId()));
			boundedBy.add(createBoundarySurface(CityGMLClass.GROUND_SURFACE, p));
		}

		building.setBoundedBySurface(boundedBy);

		CityModel cityModel = citygml.createCityModel();
		cityModel.setBoundedBy(building.calcBoundedBy(false));
		cityModel.addCityObjectMember(citygml.createCityObjectMember(building));

		CityGMLOutputFactory out = builder
				.createCityGMLOutputFactory(CityGMLVersion.v1_0_0);
		CityGMLWriter writer = out.createCityGMLWriter(new File(fileName
				+ ".xml"));

		writer.setPrefixes(CityGMLVersion.v1_0_0);
		writer.setSchemaLocations(CityGMLVersion.v1_0_0);
		writer.setIndentString("  ");
		writer.write(cityModel);
		writer.close();
	}

	private static BoundarySurfaceProperty createBoundarySurface(
			CityGMLClass type, Polygon geometry) {
		AbstractBoundarySurface boundarySurface = null;

		switch (type) {
		case WALL_SURFACE:
			boundarySurface = citygml.createWallSurface();
			break;
		case ROOF_SURFACE:
			boundarySurface = citygml.createRoofSurface();
			break;
		case GROUND_SURFACE:
			boundarySurface = citygml.createGroundSurface();
			break;
		}

		if (boundarySurface != null) {
			boundarySurface.setLod2MultiSurface(gml
					.createMultiSurfaceProperty(gml
							.createMultiSurface(geometry)));
			return citygml.createBoundarySurfaceProperty(boundarySurface);
		}

		return null;
	}

}
