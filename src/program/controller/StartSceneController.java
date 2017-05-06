package program.controller;

import classes.ProjectInfo;
import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.sun.tools.hat.internal.model.Root;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StartSceneController implements Initializable {

    @FXML
    HBox buttonBox;

    public StartSceneController(){

    }

    public StartSceneController(JFXListView <Label> tasksList){

        tasksList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    new ProjectSceneController("Project", "First Project");
                    tasksList.getScene().getWindow().hide();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXButton addButton = new JFXButton("+");
        addButton.setButtonType(JFXButton.ButtonType.RAISED);
        addButton.setStyle(addButtonStyle);
        addButton.getStyleClass().addAll("animated-option-button", "animated-option-sub1-button");
        addClickListenerToAddButton(addButton);
        buttonBox.getChildren().add(addButton);
    }

    private void addClickListenerToAddButton(JFXButton addButton){
        addButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                FileChooser fChooser = new FileChooser();
                fChooser.setInitialDirectory(new File("."));
                File savedFile = fChooser.showSaveDialog(addButton.getScene().getWindow());
                String path = savedFile.getAbsolutePath();
                saveProjectPathAndTitle(path, savedFile.getName());
                showNewStage(savedFile.getName());
                addButton.getScene().getWindow().hide();
            }
        });
    }

    private void saveProjectPathAndTitle (String projectPath, String projectTitle) {
        ProjectInfo pInfo = new ProjectInfo(projectPath, projectTitle);
        String projectInfosPath = String.format(".%s%s", File.separator, "projects.fahpm");
        List<ProjectInfo> projectInfos = readAllProjectInfos(projectInfosPath);
        projectInfos.add(pInfo);
        Gson gson = new Gson();
        try {
            new FileWriter(projectPath);
            FileWriter writer = new FileWriter(projectInfosPath);
            writer.write(gson.toJson(projectInfos));
            writer.flush();
            writer.close();
        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
    }

    private List<ProjectInfo> readAllProjectInfos (String projectInfosPath){
        ArrayList<ProjectInfo> projectInfos = new ArrayList<>();
        try {
            File fileToRead = new File(projectInfosPath);
            if(fileToRead.exists()) {
                ArrayList<com.google.gson.internal.LinkedTreeMap<String,String>> projects = new Gson().fromJson(
                        new FileReader(projectInfosPath),
                        new ArrayList<ProjectInfo>().getClass()
                );
                for (int i = 0; i < projects.size(); i++) {
                    projectInfos.add(new ProjectInfo(
                            projects.get(i).get("projectPath"),
                            projects.get(i).get("projectTitle")
                    ));
                }
                if (projectInfos == null){
                    return new ArrayList<ProjectInfo>();
                }
            }
            return projectInfos;

        }catch (IOException ioEx){
            ioEx.printStackTrace();
        }
        return projectInfos;
    }

    public List<ProjectInfo> readProjectInfos(){
        return readAllProjectInfos(
                String.format(
                        ".%s%s",
                        File.separator,
                        "projects.fahpm"
                )
        );
    }

    private void showNewStage(String title){
        BorderPane bPane = new BorderPane();
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(new Scene(bPane, 700, 450));
        stage.show();
    }
    private static String addButtonStyle =
            "    -fx-pref-width: 50px;\n" +
            "    -fx-background-color: #0066FF;\n" +
            "    -fx-background-radius: 50px;\n" +
            "    -fx-pref-height: 50px;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-border-color: white;\n" +
            "    -fx-border-radius: 50px;\n" +
            "    -fx-border-width: 4px;" +
            "    -fx-font: 20 arial";


}
