package fr.nantes1900.models.errorcalculator;

import java.io.IOException;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.ParserSTL;

public class MainErrorCalculator {

    public static void main(String[] args) throws IOException {
        //FIXME : change it to : args[0] et args[1].
        ParserSTL parser = new ParserSTL("files/adecimer1.stl");
        Mesh mesh = parser.read();

        parser = new ParserSTL("files/adecimer1Decim.stl");
        Mesh meshDecim = parser.read();

        ErrorCalculator eC = new ErrorCalculator(mesh, meshDecim);

        System.out.println(eC.computeError());
    }
}
