package sample;

import ahp.IntervalTypeTwoAHP;
import fuzzy.Alternative;
import fuzzy.IntervalTypeTwoMF;
import fuzzy.TypeOneMF;
import helper.ExcelFileParser;
import helper.IntervalArithmetic;
import helper.Settings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        for(double b = 0.5; b < 1; b += 0.01) {
            Settings.BOUNDARY_VALUE = b;
            IntervalTypeTwoAHP ahp = new IntervalTypeTwoAHP(
                    "/Users/michaelborisov/IdeaProjects/borisov.bachelor/src/sample/Books.xlsx"
            );
            ArrayList<Alternative> alternatives = ahp.calculateResultVector();
            System.out.print(String.format("%.2f: ", b));
            for (int i = 0; i < alternatives.size(); i++) {
                System.out.print(String.format("%s ", alternatives.get(i).getName()));
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
