package program.controller;

import fuzzy.TypeOneMF;
import program.model.*;
import program.view.ProjectSceneView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class ProjectSceneController {

    AhpProject mProject;

    public ProjectSceneController(String projectFilePath, String projectTitle){
        loadProjectFromFile(projectFilePath, projectTitle);
    }

    private void loadProjectFromFile(String projectFilePath, String projectTitle){
        //TODO load serialized data about project from file
        mProject = new AhpProject();
        initTestProject();
        String result = AhpProject.serialize(mProject);
        AhpProject mProject = AhpProject.deserialize(result);

        ArrayList<BaseHierarchyEntity> entites = new ArrayList<>();
        entites.add(new CriteriaHierarchyEntity("Критерий_1"));
        entites.add(new AlternativeHierarchyEntity("Альтернатива_1"));
        ProjectSceneView mView = new ProjectSceneView(projectTitle, mProject);
        mView.show();

    }

    private void initTestProject(){
        mProject.setGoal("Цель");
        ArrayList<String> alternatives = new ArrayList<>();
        alternatives.add("Альтернатива_1");
        alternatives.add("Альтернатива_2");
        mProject.setAlternatives(alternatives);
        mProject.setCriteria(alternatives);

        Assumption[][] criteria = new Assumption[3][3];
        criteria[0][0] = new Assumption(new TypeOneMF(1.0, 3.0, 5.0), new TypeOneMF(1.0, 3.0, 5.0));

        ArrayList<Assumption[][]> alternativeMatrices = new ArrayList<>();
        alternativeMatrices.add(criteria);

        mProject.setCriteriaMatrix(criteria);
        mProject.setAlternativeMatrices(alternativeMatrices);
    }
}
