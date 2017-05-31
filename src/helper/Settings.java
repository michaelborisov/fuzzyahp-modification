package helper;

import fuzzy.TypeOneMF;
import program.model.Assumption;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michaelborisov on 29.03.17.
 */
public class Settings {

    public static double BOUNDARY_VALUE = 0.83;

    public static final double CR = 0.1;

    public static ArrayList<Double> randomIndexes = new ArrayList<>();
    static {
        randomIndexes.add(0.0);
        randomIndexes.add(0.0);
        randomIndexes.add(0.0);
        randomIndexes.add(0.58);
        randomIndexes.add(0.90);
        randomIndexes.add(1.12);
        randomIndexes.add(1.24);
        randomIndexes.add(1.32);
        randomIndexes.add(1.41);
        randomIndexes.add(1.45);
        randomIndexes.add(1.49);
        randomIndexes.add(1.51);
        randomIndexes.add(1.48);
        randomIndexes.add(1.56);
        randomIndexes.add(1.57);
        randomIndexes.add(1.59);
    }

}
