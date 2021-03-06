package helper;

import static helper.Settings.BOUNDARY_VALUE;

/**
 * Created by michaelborisov on 27.03.17.
 */
public class IntervalArithmetic {

    public static double[] devide(double[]first, double[] second){
        if(first.length > 2 || second.length > 2){
            throw new IllegalArgumentException();
        }

        double lowerBound = Math.min(
                first[1] / second[1], Math.min(
                        first[1] / second[0], Math.min(
                                first[0] / second[0], first[0] / second[1]
                        )
                )
        );

        double upperBound = Math.max(
                first[1] / second[1], Math.max(
                        first[1] / second[0], Math.max(
                                first[0] / second[0], first[0] / second[1]
                        )
                )
        );

        double[] result = new double[]{lowerBound, upperBound};
        return result;
    }

    public static Double[] multiply(Double[]first, Double[] second){
        if(first.length > 2 || second.length > 2){
            throw new IllegalArgumentException();
        }

        double lowerBound = Math.min(
                first[1] * second[1], Math.min(
                        first[1] * second[0], Math.min(
                                first[0] * second[0], first[0] * second[1]
                        )
                )
        );

        double upperBound = Math.max(
                first[1] * second[1], Math.max(
                        first[1] * second[0], Math.max(
                                first[0] * second[0], first[0] * second[1]
                        )
                )
        );

        Double[] result = new Double[]{lowerBound, upperBound};
        return result;
    }


    public static Double[] sum(Double[]first, Double[] second){
        if(first.length > 2 || second.length > 2){
            throw new IllegalArgumentException();
        }
        Double[] result = new Double[]{first[0] + second[0], first[1] + second[1]};
        return result;
    }
    public static Double[] min (Double[] first, Double[] second){

        double possibility = calculatePossibilitySecondGreater(first, second);
        if (possibility > BOUNDARY_VALUE){
            return first;
        }else{
            return second;
        }
    }

    private static double calculatePossibilitySecondGreater(Double[] first, Double[] second){

        if(first[0].equals(first[1]) && second[0].equals(second[1])){
            if (second[0] > first[0]){
                return 1.0;
            }
            if (second[0] < first[0]){
                return 0.0;
            }
            return 0.5;
        }

        if(first[1] < second[0]){
            return 1.0;
        }

        if(first[0] > second[1]){
            return 0.0;
        }

        if (first[0].equals(first[1]) &&
                !second[0].equals(second[1]) &&
                first[0] > second[0] &&
                first[0] < second[1]){
            return (second[1] - first[0]) / (second[1] - second[0]);
        }

        if (!first[0].equals(first[1]) &&
                second[0].equals(second[1]) &&
                second[0] > first[0] &&
                second[0] < first[1]){
            return (second[0] - first[1]) / (first[1] - first[0]);
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



        return 0.0;

    }

    public static Double[] constructProperInterval(Double[] interval){
        if(interval[0] > interval[1]){
            double temp = interval[0];
            interval[0] = interval[1];
            interval[1] = temp;
        }
        return interval;
    }

}
