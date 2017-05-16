package program.view;

import program.model.ProjectInfo;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import program.controller.ProjectSceneController;
import program.controller.StartSceneController;

import java.io.File;
import java.util.ArrayList;

public class StartSceneView extends Application {

    StartSceneController mController;
    BorderPane bPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initStartScene(primaryStage);
    }


    private void initStartScene(Stage primaryStage) throws Exception{
        HBox root = FXMLLoader.load(getClass().getResource("layout/startSceneBottom.fxml"));
        bPane = new BorderPane();
        bPane.setBottom(root);
        bPane.setCenter(generateTasksList());
        bPane.setTop(initMenu());
        primaryStage.setTitle("Выбор задачи");
        primaryStage.setScene(new Scene(bPane, 300, 275));
        primaryStage.show();
    }


    private JFXListView<Label> generateTasksList(){
        JFXListView<Label> tasksList = new JFXListView<>();
        mController = new StartSceneController(tasksList);
        ArrayList<ProjectInfo> infos = (ArrayList<ProjectInfo>) mController.readProjectInfos();

        for (ProjectInfo task: infos) {
            tasksList.getItems().add(new Label(task.getProjectTitle()));
        }
        return tasksList;
    }

    private MenuBar initMenu(){
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("Файл");

        MenuItem open = new MenuItem("Открыть");
        open.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fChooser = new FileChooser();
                File chosen = fChooser.showOpenDialog(menuBar.getScene().getWindow());
                if(chosen != null) {
                    mController.chooseProjectPathAndTitle(chosen.getAbsolutePath(), chosen.getName());
                    new ProjectSceneController(chosen.getAbsolutePath(), chosen.getName());
                    menuBar.getScene().getWindow().hide();
                }
            }
        });
        menuFile.getItems().add(open);
        menuBar.getMenus().add(menuFile);
        return menuBar;

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
