package helper;

/**
 * Created by michaelborisov on 27.03.17.
 */
public class IntervalArithmetic {

    public static double[] devide(double[]first, double[] second){
        if(first.length > 2 || second.length > 2){
            throw new IllegalArgumentException();
        }

        double lowerBound = Math.min(
                first[1]/ second[1], Math.min(
                        first[1]/ second[0], Math.min(
                                first[0]/second[0], first[0]/ second[1]
                        )
                )
        );

        double upperBound = Math.max(
                first[1]/ second[1], Math.max(
                        first[1]/ second[0], Math.max(
                                first[0]/second[0], first[0]/ second[1]
                        )
                )
        );

        double[] result = new double[]{lowerBound, upperBound};
        return result;
    }

}
