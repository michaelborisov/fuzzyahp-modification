package sample;

import ahp.IntervalTypeTwoAHP;
import fuzzy.IntervalTypeTwoMF;
import helper.ExcelFileParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

//        Controller.readExcel("My file");
//        IntervalTypeTwoMF myMF = new ExcelFileParser("My File")
//                                        .convertStringToIntervalTypeTwoMF(
//                                                "(0.87, 0.91); 1.6; (2.23, 2.27)"
//                                        );
//        System.out.println(myMF);

        IntervalTypeTwoAHP ahp = new IntervalTypeTwoAHP("path");
        ahp.calcualteFuzzySyntheticExtents();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
