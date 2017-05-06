package program.controller;

import program.model.AhpProject;
import program.view.ProjectSceneView;

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
        ProjectSceneView mView = new ProjectSceneView(projectTitle);
        mView.show();
    }
}
