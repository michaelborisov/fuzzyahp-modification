package program.model;

import com.google.gson.Gson;
import fuzzy.TypeOneMF;

import java.util.ArrayList;
import java.util.Arrays;
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
        if (descriptionMap == null){
            descriptionMap = new HashMap<>();
        }
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

    public ArrayList<TypeOneMF> getExpectations() {
        if (expectations == null){
            expectations = generateDefaultMFs();
        }
        return expectations;
    }

    public ArrayList<TypeOneMF> generateDefaultMFs(){
        ArrayList<TypeOneMF> mfs = new ArrayList<>();
        mfs.add(new TypeOneMF(1, 1, 3));
        mfs.add(new TypeOneMF(1, 3, 5));
        mfs.add(new TypeOneMF(3, 5, 7));
        mfs.add(new TypeOneMF(5, 7 ,9));
        mfs.add(new TypeOneMF(7, 9, 9));
        return mfs;
    }


    public ArrayList<TypeOneMF> getConfDegrees() {
        if(confDegrees == null){
            confDegrees = generateDefaultMFs();
        }
        return confDegrees;
    }

    private ArrayList<TypeOneMF> expectations;

    public void setConfDegrees(ArrayList<TypeOneMF> confDegrees) {
        this.confDegrees = confDegrees;
    }

    public void setExpectations(ArrayList<TypeOneMF> expectations) {
        this.expectations = expectations;
    }

    private ArrayList<TypeOneMF> confDegrees;
}
