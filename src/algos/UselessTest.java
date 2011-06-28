package algos;

import utils.Parser;

import modeles.Mesh;

public class UselessTest {
	public static void main(String[] args) {
		try
		{
			Mesh mesh = new Mesh(Parser.readSTLA("Files\\building - 5.stl"));
			mesh.writeB("Files\\test.stl");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
