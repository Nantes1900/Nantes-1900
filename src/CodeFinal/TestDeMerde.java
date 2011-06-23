package CodeFinal;

import java.io.File;

public class TestDeMerde {
	public static void main(String[] args) {
		try
		{
			EnsembleFaces mesh = new EnsembleFaces(Parser.readSTLB(new File("test2.stl")));
			Writer.ecrireSurfaceB(new File("test2.stl"), mesh);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
