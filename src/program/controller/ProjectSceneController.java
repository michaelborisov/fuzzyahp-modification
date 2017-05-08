package program.controller;

import com.google.gson.Gson;
import fuzzy.TypeOneMF;
import program.model.*;
import program.view.ProjectSceneView;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class ProjectSceneController {

    AhpProject mProject;
    String projectFilePath;
    Gson gson = new Gson();

    public ProjectSceneController(String projectFilePath, String projectTitle){
        this.projectFilePath = projectFilePath;
        loadProjectFromFile(projectTitle);
    }

    private void loadProjectFromFile(String projectTitle){
        File fileToRead = new File(projectFilePath);
        if(fileToRead.exists()) {
            try {
                mProject = gson.fromJson(new FileReader(fileToRead), AhpProject.class);
                if (mProject == null){
                    mProject = new AhpProject();
                    initTestProject();
                }
            }catch (Exception ex){
                ex.printStackTrace();
                mProject = new AhpProject();
                initTestProject();
            }
        }else{
            mProject = new AhpProject();
            initTestProject();
        }
        ProjectSceneView mView = new ProjectSceneView(projectTitle, mProject, this);
        mView.show();

    }

    public void saveProjectToFile(){
        try {
            FileWriter writer = new FileWriter(projectFilePath);
            writer.write(AhpProject.serialize(mProject));
            writer.flush();
            writer.close();
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    private void initTestProject(){
        mProject.setGoal("Цель");

        ArrayList<String> alternatives = new ArrayList<>();
        alternatives.add("Альтернатива_1");
        alternatives.add("Альтернатива_2");

        ArrayList<String> criteriaList = new ArrayList<>();
        criteriaList.add("Критерий_1");
        criteriaList.add("Критерий_2");
        mProject.setAlternatives(alternatives);
        mProject.setCriteria(criteriaList);

        Assumption[][] criteria = new Assumption[3][3];
        criteria[0][0] = new Assumption(new TypeOneMF(1.0, 3.0, 5.0), new TypeOneMF(1.0, 3.0, 5.0));

        ArrayList<Assumption[][]> alternativeMatrices = new ArrayList<>();
        alternativeMatrices.add(criteria);

        mProject.setCriteriaMatrix(criteria);
        mProject.setAlternativeMatrices(alternativeMatrices);
    }
}
