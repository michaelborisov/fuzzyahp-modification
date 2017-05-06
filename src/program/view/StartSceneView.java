package program.view;

import classes.ProjectInfo;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import program.controller.StartSceneController;

import java.util.ArrayList;

public class StartSceneView extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        initStartScene(primaryStage);
    }


    private void initStartScene(Stage primaryStage) throws Exception{
        HBox root = FXMLLoader.load(getClass().getResource("layout/startSceneBottom.fxml"));
        BorderPane bPane = new BorderPane();
        bPane.setBottom(root);
        bPane.setCenter(generateTasksList());
        primaryStage.setTitle("Выбор задачи");
        primaryStage.setScene(new Scene(bPane, 300, 275));
        primaryStage.show();
    }


    private JFXListView<Label> generateTasksList(){
        JFXListView<Label> tasksList = new JFXListView<>();
        StartSceneController mController = new StartSceneController(tasksList);
        ArrayList<ProjectInfo> infos = (ArrayList<ProjectInfo>) mController.readProjectInfos();

        for (ProjectInfo task: infos) {
            tasksList.getItems().add(new Label(task.getProjectTitle()));
        }
        return tasksList;
    }

    private static String matrixCellButtonStyle =
                    "    -fx-background-color: #0066FF;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-border-color: white;\n" +
                    "    -fx-border-width: 4px;";


    private static String matrixCellChosenButtonStyle =
                    "    -fx-background-color: #00FF66;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-border-color: white;\n" +
                    "    -fx-border-width: 4px;";

    public static void main(String[] args) {
        launch(args);
    }
}