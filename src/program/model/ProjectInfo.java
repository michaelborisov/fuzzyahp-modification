package program.model;

import java.io.Serializable;

/**
 * Created by michaelborisov on 30.04.17.
 */
public class ProjectInfo implements Serializable {

    public ProjectInfo(){

    }

    public ProjectInfo(String projectPath, String projectTitle){
        this.projectPath = projectPath;
        this.projectTitle = projectTitle;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public void setProjectTitle(String projectTitle) {
        this.projectTitle = projectTitle;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    String projectPath;
    String projectTitle;
}
