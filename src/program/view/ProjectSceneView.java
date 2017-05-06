package program.view;

import javafx.scene.Scene;

import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Created by michaelborisov on 06.05.17.
 */
public class ProjectSceneView extends Stage {

    public ProjectSceneView(String title){
        BorderPane bPane = new BorderPane();
        this.setTitle(title);
        this.setScene(new Scene(bPane, 700, 450));
    }
}
