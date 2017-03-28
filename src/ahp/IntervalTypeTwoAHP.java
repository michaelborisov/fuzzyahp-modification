package ahp;

import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import helper.ExcelFileParser;
import helper.IntervalArithmetic;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by michaelborisov on 26.03.17.
 */
public class IntervalTypeTwoAHP {

    ArrayList<ArrayList<IntervalTypeTwoMF>> matrix;

    public IntervalTypeTwoAHP (ArrayList<ArrayList<IntervalTypeTwoMF>> matrix){
        this.matrix = matrix;
    }

    public IntervalTypeTwoAHP (String pathToExcel){
        pathToExcel = "/Users/michaelborisov/IdeaProjects/borisov.bachelor/src/sample/Books.xlsx";
        ExcelFileParser parser = new ExcelFileParser(pathToExcel);
        try {
            this.matrix = parser.parseExcel();
            System.out.println("success");
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
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

    private IntervalTypeTwoMF calculateFuzzySytheticExtent(int rowIndex, IntervalTypeTwoMF agMF){
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
            fuzzyExtents.add(calculateFuzzySytheticExtent(i, agMF));
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
}
