package fr.nantes1900.models.decimation;

import java.io.IOException;

import fr.nantes1900.utils.ParserSTL;

public class MainDecimator {

    public static void main(String[] args) throws IOException {
        ParserSTL parser = new ParserSTL("files/gravity.stl");
        Decimator d = new Decimator(parser.read());
        d.launchDecimation().writeSTL("files/gravityDecim.stl");
    }
}
