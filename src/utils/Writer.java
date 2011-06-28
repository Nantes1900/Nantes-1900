package utils;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import modeles.Mesh;
import modeles.Triangle;

/**
 * Write a STL file containing the faces, list of faces or buildings.
 *
 */

public class Writer {

	/**
	 * 
	 * @param fw The file we want to write in.
	 * @param face The face to be written.
	 */
	public static void writeASCIIFace(FileWriter fw, Triangle face) {
		try {
			fw.write("\nfacet normal");
			String s1 = new String();
			double[] t = new double[3];
			face.getNormal().get(t);
			for (int j=0;j<3;j++) {
				s1 += " "+t[j];				
			}
			fw.write(s1+"\nouter loop");
			fw.write("\nvertex"+" "+face.getP0().getX()+" "+face.getP0().getY()+" "+face.getP0().getZ());
			fw.write("\nvertex"+" "+face.getP1().getX()+" "+face.getP1().getY()+" "+face.getP1().getZ());
			fw.write("\nvertex"+" "+face.getP2().getX()+" "+face.getP2().getY()+" "+face.getP2().getZ());
			fw.write("\nendloop\nendfacet");
		}
		catch (java.io.IOException ee){
			ee.printStackTrace();
		}
	}


	/**
	 * 
	 * @param fw The file we want to write in
	 * @param surface The list of faces to be written.
	 */
	public static void writeSTLA(String fileName, Mesh surface) {
		try {
			FileWriter fw = new FileWriter(fileName);
			fw.write("solid");
			for (Triangle f : surface) {
				writeASCIIFace(fw,f);
			}
			fw.write("\nendsolid");
			fw.flush();
		}
		catch (java.io.IOException e){
			e.printStackTrace();
		}
	}

	private static void writeInGoodOrder(DataOutputStream writer, double a) throws Exception {
		ByteBuffer bBuf = ByteBuffer.allocate(Float.SIZE);
		bBuf.putFloat((float)a);
		bBuf.order(ByteOrder.LITTLE_ENDIAN);
		writer.writeFloat(bBuf.getFloat(0));
	}

	public static void writeBinaryFace(DataOutputStream writer, Triangle face) throws Exception {
		writeInGoodOrder(writer, face.getNormal().getX());
		writeInGoodOrder(writer, face.getNormal().getY());
		writeInGoodOrder(writer, face.getNormal().getZ());

		writeInGoodOrder(writer, face.getP0().getX());
		writeInGoodOrder(writer, face.getP0().getY());
		writeInGoodOrder(writer, face.getP0().getZ());

		writeInGoodOrder(writer, face.getP1().getX());
		writeInGoodOrder(writer, face.getP1().getY());
		writeInGoodOrder(writer, face.getP1().getZ());

		writeInGoodOrder(writer, face.getP2().getX());
		writeInGoodOrder(writer, face.getP2().getY());
		writeInGoodOrder(writer, face.getP2().getZ());

		writer.write(new byte[2]);
	}

	public static void writeSTLB(String fileName,  Mesh surface) {
		try {
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(fileName));

			byte[] header = new byte[80];
			stream.write(header);
			ByteBuffer bBuf = ByteBuffer.allocate(Integer.SIZE);
			bBuf.putInt(surface.size());
			bBuf.order(ByteOrder.LITTLE_ENDIAN);
			stream.writeInt(bBuf.getInt(0));

			for(Triangle t : surface) {
				writeBinaryFace(stream, t);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


	/* //Methode pour ecrire un CityGML
    public Element enteteCityGML(String name, ArrayList<NeoBatiment> quartier) {
		Namespace ns = Namespace.getNamespace("http://www.citygml.org/citygml/profiles/base/1.0");
		Namespace nsCore = Namespace.getNamespace("core", "http://www.opengis.net/citygml/1.0");
		Namespace nsGml = Namespace.getNamespace("gml", "http://www.opengis.net/gml");
		Namespace nsBldg = Namespace.getNamespace("bldg", "http://www.opengis.net/citygml/building/1.0");
		Namespace nsXal = Namespace.getNamespace("xAL", "urn:oasis:names:tc:ciq:xsdschema:xAL:2.0");
		Namespace nsTex = Namespace.getNamespace("tex", "http://www.opengis.net/citygml/texturedsurface/1.0");
		Namespace nsGrp = Namespace.getNamespace("grp", "http://www.opengis.net/citygml/cityobjectgroup/1.0");
		Namespace nsXsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

		Element racine = new Element("CityModel", nsCore);
		racine.addNamespaceDeclaration(ns);
		racine.addNamespaceDeclaration(nsGml);
		racine.addNamespaceDeclaration(nsBldg);
		racine.addNamespaceDeclaration(nsCore);
		racine.addNamespaceDeclaration(nsBldg);
		racine.addNamespaceDeclaration(nsXal);
		racine.addNamespaceDeclaration(nsTex);
		racine.addNamespaceDeclaration(nsGrp);
		racine.addNamespaceDeclaration(nsXsi);
		racine.setAttribute("schemaLocation", "http://www.opengis.net/citygml/landuse/1.0 http://schemas.opengis.net/citygml/landuse/1.0/landUse.xsd http://www.opengis.net/citygml/cityfurniture/1.0 http://schemas.opengis.net/citygml/cityfurniture/1.0/cityFurniture.xsd http://www.opengis.net/citygml/appearance/1.0 http://schemas.opengis.net/citygml/appearance/1.0/appearance.xsd http://www.opengis.net/citygml/texturedsurface/1.0 http://schemas.opengis.net/citygml/texturedsurface/1.0/texturedSurface.xsd http://www.opengis.net/citygml/transportation/1.0 http://schemas.opengis.net/citygml/transportation/1.0/transportation.xsd http://www.opengis.net/citygml/waterbody/1.0 http://schemas.opengis.net/citygml/waterbody/1.0/waterBody.xsd http://www.opengis.net/citygml/building/1.0 http://schemas.opengis.net/citygml/building/1.0/building.xsd http://www.opengis.net/citygml/relief/1.0 http://schemas.opengis.net/citygml/relief/1.0/relief.xsd http://www.opengis.net/citygml/vegetation/1.0 http://schemas.opengis.net/citygml/vegetation/1.0/vegetation.xsd http://www.opengis.net/citygml/cityobjectgroup/1.0 http://schemas.opengis.net/citygml/cityobjectgroup/1.0/cityObjectGroup.xsd http://www.opengis.net/citygml/generics/1.0 http://schemas.opengis.net/citygml/generics/1.0/generics.xsd", nsXsi);

		Element description = new Element("description", nsGml);
		Element nom = new Element("name", nsGml);
		Element cityObjectMember = new Element("cityObjectMember", nsCore);

		description.addContent("Description du fichier CityGML");
		racine.addContent(description);
		racine.addContent(nom);
		racine.addContent(cityObjectMember);
		nom.addContent(name);

		// Pour chaque batiment de quartier :
		for (int i=0;i<quartier.size();i++) {
			ecrireBatiment(racine, quartier.get(i));
		}

		return racine;
	}*/

}
