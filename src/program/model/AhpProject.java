package program.model;

import java.util.ArrayList;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class AhpProject {

    public String getGoal() {
        return goal;
    }

    public Assumption[][] getCriteriaMatrix() {
        return criteriaMatrix;
    }

    public ArrayList<Assumption[][]> getAlternativeMatrices() {
        return alternativeMatrices;
    }

    public ArrayList<String> getCriteria() {
        return criteria;
    }

    public ArrayList<String> getAlternatives() {
        return alternatives;
    }

    private String goal;

    public void setAlternatives(ArrayList<String> alternatives) {
        this.alternatives = alternatives;
    }

    public void setAlternativeMatrices(ArrayList<Assumption[][]> alternativeMatrices) {
        this.alternativeMatrices = alternativeMatrices;
    }

    public void setCriteria(ArrayList<String> criteria) {
        this.criteria = criteria;
    }

    public void setCriteriaMatrix(Assumption[][] criteriaMatrix) {
        this.criteriaMatrix = criteriaMatrix;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    private ArrayList<String> alternatives;
    private ArrayList<String> criteria;

    private Assumption[][] criteriaMatrix;
    private ArrayList<Assumption[][]> alternativeMatrices;


}
