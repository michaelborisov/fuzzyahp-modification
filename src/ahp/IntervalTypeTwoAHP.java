package ahp;

import Jama.Matrix;
import Jama.EigenvalueDecomposition;
import com.sun.tools.javac.util.ArrayUtils;
import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import helper.IntervalArithmetic;
import helper.Settings;
import org.apache.poi.util.ArrayUtil;
import program.model.AhpProject;

import java.util.ArrayList;

import static helper.Settings.randomIndexes;

/**
 * Created by michaelborisov on 30.05.17.
 */
public class IntervalTypeTwoAhp {

    ArrayList<Double[]> totalResultVector;
    AhpProject mProject;
    public IntervalTypeTwoAhp(AhpProject mProject){
        this.mProject = mProject;

    }

    public ArrayList<Double[]> calculateTotalResultVector(){
        ArrayList<Double[]> criteriaVector = calculateCriteriaVector();

        ArrayList<ArrayList<Double[]>> alternativeVectors = calculateAlternativeVectors();


        totalResultVector = new ArrayList<Double[]>();
        for (int i = 0; i < alternativeVectors.get(0).size(); i++) {
            Double[] rowSum = new Double[]{0.0, 0.0};
            for (int j = 0; j < alternativeVectors.size(); j++) {
                rowSum = IntervalArithmetic.sum(rowSum,
                        IntervalArithmetic.multiply(
                                alternativeVectors.get(j).get(i),
                                criteriaVector.get(j)
                        )
                );
            }
            totalResultVector.add(rowSum);
        }
        totalResultVector = IntervalTypeTwoAhpMatrix.normalizeVector(totalResultVector);
        return totalResultVector;
    }

    private ArrayList<Double[]> calculateCriteriaVector(){

        ArrayList<ArrayList<IntervalTypeTwoMF>> criteriaMatrix = new ArrayList<ArrayList<IntervalTypeTwoMF>>();

        for (int i = 0; i <mProject.getCriteriaMatrix().length; i++) {
            criteriaMatrix.add(new ArrayList<>());
            for (int j = 0; j < mProject.getCriteriaMatrix()[i].length; j++) {
                if(j < i){
                    criteriaMatrix.get(i).add(criteriaMatrix.get(j).get(i).getReciprocal());
                }else{
                    if(mProject.getCriteriaMatrix()[i][j] == null) {
                        criteriaMatrix.get(i).add(
                                new IntervalTypeTwoMF(
                                        new TypeOneMF(1, 1, 1),
                                        new TypeOneMF(1, 1, 1)
                                )
                        );
                    }else{
                        criteriaMatrix.get(i).add(mProject.getCriteriaMatrix()[i][j].convertT1MFtoIT2MF(mProject.getConfDegrees().get(2)));
                    }
                }
            }
        }

        IntervalTypeTwoAhpMatrix ahp = new IntervalTypeTwoAhpMatrix(criteriaMatrix);
        ArrayList<Double[]> criteriaVector = ahp.calculateIntervalVector();
        return criteriaVector;
    }

    public boolean isAllMatricesConsistent(){

        return isCriteriaMatrixConsistent() && isAlternativeMatricesConsistent();
    }

    private boolean isMatrixConsistent(double[][] matrix){
        EigenvalueDecomposition dec = new Matrix(matrix).eig();
        double maxEigenValue = dec.getD().get(0, 0);
        double ci = (maxEigenValue - matrix.length) / (matrix.length - 1);
        double cr = ci / randomIndexes.get(matrix.length);
        if(cr < Settings.CR) {
            return true;
        }else{
            return false;
        }
    }

    private boolean isCriteriaMatrixConsistent(){

        double[][] criteriaConsist = new double[mProject.getCriteriaMatrix().length][mProject.getCriteriaMatrix().length];
        for (int i = 0; i <mProject.getCriteriaMatrix().length; i++) {
            for (int j = 0; j < mProject.getCriteriaMatrix()[i].length; j++) {
                if(j < i){
                    criteriaConsist[i][j] = (1 / criteriaConsist[j][i]);
                }else{
                    if(mProject.getCriteriaMatrix()[i][j] == null) {
                        criteriaConsist[i][j] = 1;
                    }else{
                        criteriaConsist[i][j] = (mProject.getCriteriaMatrix()[i][j].
                                getExpectation().getDefuzzifiedValue());
                    }
                }
            }
        }
        return isMatrixConsistent(criteriaConsist);
    }

    private double[][] convertDoubleArray(Double[][] src){
        double [][] result = new double[src.length][src.length];
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                result[i][j] = src[i][j];
            }
        }
        return result;
    }

    private boolean isAlternativeMatricesConsistent(){

        ArrayList<Double[][]> alternativeConsistencyList = new ArrayList<>();
        for (int k = 0; k < mProject.getAlternativeMatrices().size(); k++) {
            alternativeConsistencyList.add(
                    new Double[mProject.getAlternativeMatrices().get(k).length]
                              [mProject.getAlternativeMatrices().get(k).length]
            );
            for (int i = 0; i < mProject.getAlternativeMatrices().get(k).length; i++) {
                for (int j = 0; j < mProject.getAlternativeMatrices().get(k)[i].length; j++) {
                    if(j < i){
                        alternativeConsistencyList.get(k)[i][j] = (1 / alternativeConsistencyList.get(k)[j][i]);
                    }else{
                        if(mProject.getAlternativeMatrices().get(k)[i][j] == null) {
                            alternativeConsistencyList.get(k)[i][j] = 1.0;
                        }else{
                            alternativeConsistencyList.get(k)[i][j] = (
                                    mProject.getAlternativeMatrices().
                                            get(k)[i][j].getExpectation().
                                            getDefuzzifiedValue()
                            );
                        }
                    }
                }
            }
        }

        for (int i = 0; i < alternativeConsistencyList.size(); i++) {
            if(!isMatrixConsistent(convertDoubleArray(alternativeConsistencyList.get(i)))){
                return false;
            }
        }
        return true;
    }


    private ArrayList<ArrayList<Double[]>> calculateAlternativeVectors(){
        ArrayList<ArrayList<ArrayList<IntervalTypeTwoMF>>> alternativeMatricesList = new ArrayList<ArrayList<ArrayList<IntervalTypeTwoMF>>>();
        for (int k = 0; k < mProject.getAlternativeMatrices().size(); k++) {
            alternativeMatricesList.add(new ArrayList<>());
            for (int i = 0; i < mProject.getAlternativeMatrices().get(k).length; i++) {
                alternativeMatricesList.get(k).add(new ArrayList<>());
                for (int j = 0; j < mProject.getAlternativeMatrices().get(k)[i].length; j++) {
                    if(j < i){
                        alternativeMatricesList.get(k).get(i).add(
                                alternativeMatricesList.get(k).get(j).get(i).getReciprocal()
                        );
                    }else{
                        if(mProject.getAlternativeMatrices().get(k)[i][j] == null) {
                            alternativeMatricesList.get(k).get(i).add(
                                    new IntervalTypeTwoMF(
                                            new TypeOneMF(1, 1, 1),
                                            new TypeOneMF(1, 1, 1)
                                    )
                            );
                        }else{
                            alternativeMatricesList.get(k).get(i).add(
                                    mProject.getAlternativeMatrices().get(k)[i][j].
                                            convertT1MFtoIT2MF(mProject.getConfDegrees().get(2))
                            );
                        }
                    }
                }
            }
        }
        ArrayList<ArrayList<Double[]>> alternativeVectors = new ArrayList<ArrayList<Double[]>>();
        for (ArrayList<ArrayList<IntervalTypeTwoMF>> alternativeMatrix: alternativeMatricesList) {
            IntervalTypeTwoAhpMatrix ahp = new IntervalTypeTwoAhpMatrix(alternativeMatrix);
            alternativeVectors.add(ahp.calculateIntervalVector());
        }

        return alternativeVectors;
    }
}
