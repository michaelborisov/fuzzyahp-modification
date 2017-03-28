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


    public static double[] min (double[] first, double[] second){

        double possibility = calculatePossibilitySecondGreater(first, second);
        if (possibility > 0.55){
            return first;
        }else{
            return second;
        }
    }

    private static double calculatePossibilitySecondGreater(double[] first, double[] second){

        if(first[1] < second[0]){
            return 1.0;
        }

        if(first[0] > second[1]){
            return 0.0;
        }

        if (first[0] >= second[0] && first[1] >= second[1] && first[0] <= second[1]){
            return 0.5 * (Math.pow(second[1] - first[0], 2)/((first[1] - first[0]) * (second[1] - second[0])));
        }

        if (second[0] >= first[0] && second[1] >= first[1] && second[0] <= first[1]){
            return 1 - 0.5 * (Math.pow(first[1] - second[0], 2)/((first[1] - first[0]) * (second[1] - second[0])));
        }

        if (first[0] >= second[0] && first[1] <= second[1]){
            return (second[1] - first[1]) / (second[1] - second[0]) +
                    0.5 * (first[1] - first[0])/ (second[1] - second[0]);
        }

        if (second[0] >= first[0] && second[1] <= first[1]){
            return (second[0] - first[0]) / (first[1] - first[0]) +
                    0.5 * (second[1] - second[0])/ (first[1] - first[0]);
        }

        if (first[0] == first[1] &&
                second[0] != second[1] &&
                first[0] > second[0] &&
                first[0] < second[1]){
            return (second[1] - first[0]) / (second[1] - second[0]);
        }

        if (first[0] != first[1] &&
                second[0] == second[1] &&
                second[0] > first[0] &&
                second[0] < first[1]){
            return (second[0] - first[1]) / (first[1] - first[0]);
        }

        return 0.0;

    }
}
