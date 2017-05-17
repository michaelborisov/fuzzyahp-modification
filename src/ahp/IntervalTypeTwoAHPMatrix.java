package ahp;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import fuzzy.Alternative;
import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import helper.ExcelFileParser;
import helper.IntervalArithmetic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class IntervalTypeTwoAHPMatrix {

    public ArrayList<ArrayList<IntervalTypeTwoMF>> getMatrix() {
        return matrix;
    }

    ArrayList<ArrayList<IntervalTypeTwoMF>> matrix;

    public IntervalTypeTwoAHPMatrix(ArrayList<ArrayList<IntervalTypeTwoMF>> matrix){
        this.matrix = matrix;
    }

    public IntervalTypeTwoAHPMatrix(String pathToExcel){
        ExcelFileParser parser = new ExcelFileParser(pathToExcel);
        try {
            this.matrix = parser.parseExcel();
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    public double calculateEigenValue(){
        double[][][] source = new double[][][]{new double[][]{new double[]{1,1,1}, new double[]{0.89,1.6,2.25}, new double[]{0.65,1.07,1.88}, new double[]{0.82,1.47,2.76}, new double[]{0.8,1.37,3.19}},
                         new double[][]{new double[]{0.44,0.62,1.12}, new double[]{1,1,1}, new double[]{2.02,3.08,4.64}, new double[]{0.80,1,1.47}, new double[]{1.17,2.36,4.53}},
                         new double[][]{new double[]{0.53,0.93,1.53}, new double[]{0.22,0.34,0.50}, new double[]{1,1,1}, new double[]{0.68,1.11,1.66}, new double[]{0.80,1,1.72}},
                         new double[][]{new double[]{0.36,0.68,1.21}, new double[]{0.68,1,1.26}, new double[]{0.60,0.90,1.47}, new double[]{1,1,1}, new double[]{0.76,0.93,1.25}},
                         new double[][]{new double[]{0.31,0.73,1.26}, new double[]{0.22,0.42,0.86}, new double[]{0.58,1,1.26}, new double[]{0.80,1.08,1.32}, new double[]{1,1,1}}};
        double[][] notDoubleMatrix = new double[matrix.size()][matrix.size()];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5.; j++) {
                double[] ar = source[i][j];
                //notDoubleMatrix[i][j] = new TypeOneMF(ar[0], ar[1], ar[2]).getDefuzzifiedCOS();
            }
        }
        Matrix a = new Matrix(notDoubleMatrix);
        EigenvalueDecomposition res = a.eig();
        double maxEigenValue = res.getD().get(0, 0);

        double[][] doubleMatrix = new double[matrix.size()][matrix.size()];
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                //doubleMatrix[i][j] = matrix.get(i).get(j).getCentroid(10).getLeft();
            }
        }
        a = new Matrix(doubleMatrix);
        res = a.eig();
        maxEigenValue = res.getD().get(0, 0);
        return maxEigenValue;
    }
    private IntervalTypeTwoMF calculateMatrixAggregation(){
        double lowerBoundsLower = 0.0;
        double lowerBoundsUpper = 0.0;

        double middle = 0.0;

        double upperBoundsLower = 0.0;
        double upperBoundsUpper = 0.0;

        for (int i = 0; i < this.matrix.size(); i++) {
            for (int j = 0; j < this.matrix.get(i).size(); j++) {
                lowerBoundsLower += this.matrix.get(i).get(j).getUpperMF().getUpperBound();
                lowerBoundsUpper += this.matrix.get(i).get(j).getLowerMF().getUpperBound();

                middle += this.matrix.get(i).get(j).getLowerMF().getMiddle();

                upperBoundsLower += this.matrix.get(i).get(j).getLowerMF().getLowerBound();
                upperBoundsUpper += this.matrix.get(i).get(j).getUpperMF().getLowerBound();
            }
        }

        TypeOneMF tOneLower = new TypeOneMF(lowerBoundsLower, middle, upperBoundsUpper);
        TypeOneMF tOneUpper = new TypeOneMF(lowerBoundsUpper, middle, upperBoundsLower);

        IntervalTypeTwoMF tTwo = new IntervalTypeTwoMF(tOneLower, tOneUpper);
        return tTwo;
    }

    private IntervalTypeTwoMF calculateRowAggregation(int rowIndex){
        double lowerBoundsLower = 0.0;
        double lowerBoundsUpper = 0.0;

        double middle = 0.0;

        double upperBoundsLower = 0.0;
        double upperBoundsUpper = 0.0;
        for (int i = 0; i < this.matrix.get(rowIndex).size(); i++) {
            lowerBoundsLower += this.matrix.get(rowIndex).get(i).getUpperMF().getLowerBound();
            lowerBoundsUpper += this.matrix.get(rowIndex).get(i).getLowerMF().getLowerBound();

            middle += this.matrix.get(rowIndex).get(i).getLowerMF().getMiddle();

            upperBoundsLower += this.matrix.get(rowIndex).get(i).getLowerMF().getUpperBound();
            upperBoundsUpper += this.matrix.get(rowIndex).get(i).getUpperMF().getUpperBound();
        }

        TypeOneMF tOneLower = new TypeOneMF(lowerBoundsUpper, middle, upperBoundsLower);
        TypeOneMF tOneUpper = new TypeOneMF(lowerBoundsLower, middle, upperBoundsUpper);

        IntervalTypeTwoMF tTwo = new IntervalTypeTwoMF(tOneLower, tOneUpper);
        return tTwo;
    }

    private IntervalTypeTwoMF calculateFuzzySyntheticExtent(int rowIndex, IntervalTypeTwoMF agMF){
        IntervalTypeTwoMF rowMF = calculateRowAggregation(rowIndex);

        double[] firstInterval = new double[]{rowMF.getUpperMF().getLowerBound(), rowMF.getLowerMF().getLowerBound()};
        double [] secondInterval = new double []{agMF.getUpperMF().getLowerBound(), agMF.getLowerMF().getLowerBound()};
        double[] lowerBounds = IntervalArithmetic.devide(firstInterval, secondInterval);

        double middle = rowMF.getLowerMF().getMiddle() / agMF.getLowerMF().getMiddle();

        firstInterval = new double[]{rowMF.getLowerMF().getUpperBound(), rowMF.getUpperMF().getUpperBound()};
        secondInterval = new double []{agMF.getLowerMF().getUpperBound(), agMF.getUpperMF().getUpperBound()};
        double[] upperBounds = IntervalArithmetic.devide(firstInterval, secondInterval);

        TypeOneMF tOneLower = new TypeOneMF(lowerBounds[1], middle, upperBounds[0]);
        TypeOneMF tOneUpper = new TypeOneMF(lowerBounds[0], middle, upperBounds[1]);

        IntervalTypeTwoMF tTwo = new IntervalTypeTwoMF(tOneLower, tOneUpper);
        return tTwo;
    }

    public ArrayList<IntervalTypeTwoMF> calculateFuzzySyntheticExtents(){

        IntervalTypeTwoMF agMF = calculateMatrixAggregation();
        ArrayList<IntervalTypeTwoMF> fuzzyExtents = new ArrayList<IntervalTypeTwoMF>();
        for (int i = 0; i < this.matrix.size(); i++) {
            fuzzyExtents.add(calculateFuzzySyntheticExtent(i, agMF));
        }
        return fuzzyExtents;
    }

    private Double[] calculateDegreeOfPossibility(IntervalTypeTwoMF first, IntervalTypeTwoMF second){
        double firstValue = TypeOneMF.calculateHeightOfIntersection(first.getLowerMF(), second.getLowerMF());
        double secondValue = TypeOneMF.calculateHeightOfIntersection(first.getUpperMF(), second.getUpperMF());

        return new Double[]{firstValue, secondValue};
    }

    public ArrayList<ArrayList<Double[]>> calculateComparisonsOfFuzzyExtents(){
        ArrayList<IntervalTypeTwoMF> fuzzyExtents = calculateFuzzySyntheticExtents();
        ArrayList<ArrayList<Double[]>> degrees = new ArrayList<ArrayList<Double[]>>();
        for (int i = 0; i < fuzzyExtents.size() ; i++) {
            degrees.add(new ArrayList<Double[]>());
            for (int j = 0; j < fuzzyExtents.size(); j++) {
                if (i == j){
                    continue;
                }

                Double[] degree = calculateDegreeOfPossibility(fuzzyExtents.get(i), fuzzyExtents.get(j));
                degrees.get(i).add(degree);
            }
        }

        return degrees;
    }

    public Double[] findMinFuzzyExtentInRow(ArrayList<Double[]> extents){
        Double [] min = extents.get(0);
        for (int i = 1; i < extents.size(); i++) {
            min = IntervalArithmetic.min(min, extents.get(i));
        }
        return min;
    }

    public ArrayList<Double[]> calculateIntervalVector(){
        ArrayList<ArrayList<Double[]>>fuzzyExtents = calculateComparisonsOfFuzzyExtents();
        ArrayList<Double[]> result = new ArrayList<Double[]>();

        double lower = 0.0;
        double higher = 0.0;
        for (int i = 0; i < fuzzyExtents.size(); i++) {
            Double[] minFuzzyExtent = findMinFuzzyExtentInRow(fuzzyExtents.get(i));
            result.add(minFuzzyExtent);
            lower += minFuzzyExtent[0];
            higher += minFuzzyExtent[1];
        }

        for (int i = 0; i < result.size(); i++) {
            result.set(
                    i,
                    new Double[]{
                            Math.min(result.get(i)[0]/lower,result.get(i)[1]/higher),
                            Math.max(result.get(i)[0]/lower,result.get(i)[1]/higher)
                    }
            );
        }
        return result;
    }

    public ArrayList<Double[]> normalizeVector(ArrayList<Double[]> vector){
        ArrayList<Double[]> result = new ArrayList<Double[]>();

        double lower = 0.0;
        double higher = 0.0;
        for (int i = 0; i < vector.size(); i++) {
            result.add(new Double[2]);
            lower += vector.get(i)[0];
            higher += vector.get(i)[1];
        }

        for (int i = 0; i < vector.size(); i++) {
            result.set(
                    i,
                    new Double[]{
                            Math.min(vector.get(i)[0]/lower,vector.get(i)[1]/higher),
                            Math.max(vector.get(i)[0]/lower,vector.get(i)[1]/higher)
                    }
            );
        }
        return result;
    }

    public ArrayList<Alternative> calculateResultVector (){

        ArrayList<ArrayList<Double[]>>fuzzyExtents = calculateComparisonsOfFuzzyExtents();
        ArrayList<Double[]> result = new ArrayList<Double[]>();

        double lower = 0.0;
        double higher = 0.0;
        for (int i = 0; i < fuzzyExtents.size(); i++) {
            Double[] minFuzzyExtent = findMinFuzzyExtentInRow(fuzzyExtents.get(i));
            result.add(minFuzzyExtent);

            lower += minFuzzyExtent[0];
            higher += minFuzzyExtent[1];
        }

        for (int i = 0; i < result.size(); i++) {
            result.set(i, IntervalArithmetic.constructProperInterval(
                    new Double[]{
                            result.get(i)[0]/lower,
                            result.get(i)[1]/higher
                    })
            );
        }

        ArrayList<Alternative> alternatives = new ArrayList<Alternative>();
        for (int i = 0; i <result.size(); i++) {
            alternatives.add(new Alternative(String.valueOf(i + 1), result.get(i)));
        }
        alternatives.sort(new Comparator<Alternative>() {
            @Override
            public int compare(Alternative o1, Alternative o2) {
                if(IntervalArithmetic.min(o1.getInterval(), o2.getInterval()).equals(o1.getInterval())){
                    return 1;
                }
                if(IntervalArithmetic.min(o1.getInterval(), o2.getInterval()).equals(o2.getInterval())){
                    return -1;
                }
                return 0;
            }
        });


        return alternatives;



    }

}
