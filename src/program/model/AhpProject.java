package program.model;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

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

    public HashMap<String, String> getDescriptionMap() {
        return descriptionMap;
    }

    public void setDescriptionMap(HashMap<String, String> descriptionMap) {
        this.descriptionMap = descriptionMap;
    }

    private String goal;

    private ArrayList<String> alternatives = new ArrayList<>();
    private ArrayList<String> criteria = new ArrayList<>();

    private Assumption[][] criteriaMatrix;
    private ArrayList<Assumption[][]> alternativeMatrices;

    private HashMap<String, String> descriptionMap;

    public static String serialize(AhpProject project){
        Gson gson = new Gson();
        return gson.toJson(project, AhpProject.class);
    }

    public static AhpProject deserialize(String stringRepr){
        Gson gson = new Gson();
        return (AhpProject)gson.fromJson(stringRepr, AhpProject.class);
    }
}
