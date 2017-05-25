package program.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import fuzzy.TypeOneMF;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import program.model.AhpProject;
import program.model.Assumption;

import java.util.HashMap;

/**
 * Created by michaelborisov on 08.05.17.
 */
public class AssumptionSceneView extends Stage {

    Assumption assumption;

    public String getGeneratedLabel() {
        return generatedLabel;
    }

    public void setGeneratedLabel(String generatedLabel) {
        this.generatedLabel = generatedLabel;
    }

    String generatedLabel;
    int row;
    int column;
    AhpProject mProject;
    private HashMap<JFXRadioButton, TypeOneMF> expectations = new HashMap<>();
    private HashMap<JFXRadioButton, TypeOneMF> confidence = new HashMap<>();

    public AssumptionSceneView(int row, int column, AhpProject project){
        generateRadioButtonScene();
        this.row = row;
        this.column = column;
        this.mProject = project;
    }

    private void generateRadioButtonScene(){
        final ToggleGroup approximGroup = new ToggleGroup();
        BorderPane bPane = new BorderPane();
        Stage stage = this;


        JFXRadioButton first = new JFXRadioButton("В равной степени: E1");
        first.setPadding(new Insets(10));
        first.setToggleGroup(approximGroup);
        expectations.put(first, mProject.getExpectations().get(0));

        JFXRadioButton second = new JFXRadioButton("Слабое преобладание: E2");
        second.setPadding(new Insets(10));
        second.setToggleGroup(approximGroup);
        expectations.put(second, mProject.getExpectations().get(1));

        JFXRadioButton third = new JFXRadioButton("Существенное преобладание: E3");
        third.setPadding(new Insets(10));
        third.setToggleGroup(approximGroup);
        expectations.put(third, mProject.getExpectations().get(2));

        JFXRadioButton fourth = new JFXRadioButton("Очень существенное преобладание: E4");
        fourth.setPadding(new Insets(10));
        fourth.setToggleGroup(approximGroup);
        expectations.put(fourth, mProject.getExpectations().get(3));

        JFXRadioButton fifth = new JFXRadioButton("Крайне предпочтительнее: E5");
        fifth.setPadding(new Insets(10));
        fifth.setToggleGroup(approximGroup);
        expectations.put(fifth, mProject.getExpectations().get(4));

        final ToggleGroup confGroup = new ToggleGroup();
        Label approx = new Label("Оценка");
        approx.setPadding(new Insets(10,10,10,55));
        approx.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        Label conf = new Label("Уверенность");
        conf.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        conf.setPadding(new Insets(10,10,10,45));

        JFXRadioButton confFirst = new JFXRadioButton("Совсем не уверен: C1");
        confFirst.setPadding(new Insets(10));
        confFirst.setToggleGroup(confGroup);
        confidence.put(confFirst, mProject.getConfDegrees().get(0));

        JFXRadioButton confSecond = new JFXRadioButton("Не очень уверен: C2");
        confSecond.setPadding(new Insets(10));
        confSecond.setToggleGroup(confGroup);
        confidence.put(confSecond, mProject.getConfDegrees().get(1));

        JFXRadioButton confThird = new JFXRadioButton("Уверен: C3");
        confThird.setPadding(new Insets(10));
        confThird.setToggleGroup(confGroup);
        confidence.put(confThird, mProject.getConfDegrees().get(2));

        JFXRadioButton confFourth = new JFXRadioButton("Очень уверен: C4");
        confFourth.setPadding(new Insets(10));
        confFourth.setToggleGroup(confGroup);
        confidence.put(confFourth, mProject.getConfDegrees().get(3));

        JFXRadioButton confFifth = new JFXRadioButton("Абсолютно уверен: C5");
        confFifth.setPadding(new Insets(10));
        confFifth.setToggleGroup(confGroup);
        confidence.put(confFifth, mProject.getConfDegrees().get(4));


        VBox vboxApp = new VBox();
        vboxApp.getChildren().add(approx);
        vboxApp.getChildren().add(first);
        vboxApp.getChildren().add(second);
        vboxApp.getChildren().add(third);
        vboxApp.getChildren().add(fourth);
        vboxApp.getChildren().add(fifth);

        VBox vboxConf = new VBox();
        vboxConf.getChildren().addAll(conf);
        vboxConf.getChildren().add(confFirst);
        vboxConf.getChildren().add(confSecond);
        vboxConf.getChildren().add(confThird);
        vboxConf.getChildren().add(confFourth);
        vboxConf.getChildren().add(confFifth);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(vboxApp, vboxConf);
        bPane.setCenter(hBox);

        JFXButton submitButton = new JFXButton("Подтвердить");
        submitButton.setButtonType(JFXButton.ButtonType.RAISED);
        HBox subHBox = new HBox();
        subHBox.getChildren().add(submitButton);
        subHBox.setPadding(new Insets(10));
        //submitButton.setStyle(matrixCellButtonStyle);
        submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Toggle t = approximGroup.getSelectedToggle();
                TypeOneMF m = expectations.get(t);
                setAssumption(
                        expectations.get(approximGroup.getSelectedToggle()),
                        confidence.get(confGroup.getSelectedToggle())

                );

                String exp = ((JFXRadioButton)approximGroup.getSelectedToggle()).getText();
                String con = ((JFXRadioButton)confGroup.getSelectedToggle()).getText();
                assumption.setLabel(String.format("Оценка: %s\nУверенность: %s",
                        exp.substring(exp.length() - 2),
                        con.substring(con.length() - 2)
                ));
                stage.hide();
            }
        });
        subHBox.setAlignment(Pos.CENTER);
        bPane.setBottom(subHBox);

        stage.setTitle("Экспертная оценка");
        stage.setScene(new Scene(bPane, 550, 275));
    }


    private void setAssumption(TypeOneMF expectation, TypeOneMF confidence){

        this.assumption = new Assumption(expectation, confidence);
    }

    public Assumption getAssumption(){
        return assumption;
    }

}
