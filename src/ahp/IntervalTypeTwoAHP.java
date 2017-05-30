package ahp;

import Jama.Matrix;
import Jama.EigenvalueDecomposition;
import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import helper.IntervalArithmetic;
import program.model.AhpProject;

import java.util.ArrayList;

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
        double[][] criteriaConsist = new double[mProject.getCriteriaMatrix().length][mProject.getCriteriaMatrix().length];
        for (int i = 0; i <mProject.getCriteriaMatrix().length; i++) {
            criteriaMatrix.add(new ArrayList<>());
            for (int j = 0; j < mProject.getCriteriaMatrix()[i].length; j++) {
                if(j < i){
                    criteriaMatrix.get(i).add(criteriaMatrix.get(j).get(i).getReciprocal());
                    criteriaConsist[i][j] = (1 / criteriaConsist[j][i]);
                }else{
                    if(mProject.getCriteriaMatrix()[i][j] == null) {
                        criteriaMatrix.get(i).add(
                                new IntervalTypeTwoMF(
                                        new TypeOneMF(1, 1, 1),
                                        new TypeOneMF(1, 1, 1)
                                )
                        );
                        criteriaConsist[i][j] = 1;
                    }else{
                        criteriaMatrix.get(i).add(mProject.getCriteriaMatrix()[i][j].convertT1MFtoIT2MF(mProject.getConfDegrees().get(2)));
                        criteriaConsist[i][j] = (mProject.getCriteriaMatrix()[i][j].getExpectation().getDefuzzifiedValue());
                    }
                }
            }
        }
        EigenvalueDecomposition dec = new Matrix(criteriaConsist).eig();
        double maxEigenValue = dec.getD().get(0, 0);


        IntervalTypeTwoAhpMatrix ahp = new IntervalTypeTwoAhpMatrix(criteriaMatrix);
        ArrayList<Double[]> criteriaVector = ahp.calculateIntervalVector();
        return criteriaVector;
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
                                    mProject.getAlternativeMatrices().get(k)[i][j].convertT1MFtoIT2MF(mProject.getConfDegrees().get(2))
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
