package fr.nantes1900.models.errorcalculator;

import java.io.IOException;

import fr.nantes1900.models.basis.Mesh;
import fr.nantes1900.utils.ParserSTL;

public class MainErrorCalculator {

    public static void main(String[] args) throws IOException {
        ParserSTL parser = new ParserSTL(args[0]);
        Mesh mesh = parser.read();

        parser = new ParserSTL(args[1]);
        Mesh meshDecim = parser.read();

        ErrorCalculator eC = new ErrorCalculator(mesh, meshDecim);

        System.out.println(eC.computeError());
    }
}
