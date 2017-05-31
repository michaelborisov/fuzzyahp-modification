package program.view;

import com.jfoenix.controls.JFXButton;
import fuzzy.TypeOneMF;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import program.controller.ProjectSceneController;
import program.model.AhpProject;

import java.util.ArrayList;

/**
 * Created by michaelborisov on 17.05.17.
 */
public class AssumptionChangeSceneView extends Stage {

    AhpProject mProject;
    ProjectSceneController mController;
    public AssumptionChangeSceneView(AhpProject mProject, ProjectSceneController controller){
        this.mProject = mProject;
        this.mController = controller;
        generateButtonScene();
    }

    private void generateButtonScene(){
        BorderPane bPane = new BorderPane();
        Stage stage = this;
        HBox hBox = new HBox();
        hBox.getChildren().addAll(generateExpectationButtons(), generateConfidenceButtons());
        hBox.setSpacing(15);
        bPane.setCenter(hBox);
        stage.setTitle("Настройка оценок");
        stage.setScene(new Scene(bPane, 510, 200));
    }


    private VBox generateExpectationButtons(){
        Label approx = new Label("Оценка");
        approx.setPadding(new Insets(10,10,10,55));
        approx.setFont(Font.font("Verdana", FontWeight.BOLD, 15));

        ArrayList<JFXButton> expButtons = new ArrayList<>();

        JFXButton expFirst = new JFXButton("В равной степени: E1");
        JFXButton expSecond = new JFXButton("Слабое преобладание: E2");
        JFXButton expThird = new JFXButton("Существенное преобладание: E3");
        JFXButton expFourth = new JFXButton("Очень существенное преобладание: E4");
        JFXButton expFifth = new JFXButton("Крайне предпочтительнее: E5");

        expButtons.add(expFirst);
        expButtons.add(expSecond);
        expButtons.add(expThird);
        expButtons.add(expFourth);
        expButtons.add(expFifth);

        for (int i = 0; i < expButtons.size(); i++) {
            setOnClickExpectation(i, expButtons.get(i), true);
        }

        VBox vboxExp = new VBox();
        vboxExp.getChildren().add(approx);
        vboxExp.getChildren().add(expFirst);
        vboxExp.getChildren().add(expSecond);
        vboxExp.getChildren().add(expThird);
        vboxExp.getChildren().add(expFourth);
        vboxExp.getChildren().add(expFifth);
        vboxExp.setPadding(new Insets(10));

        return vboxExp;
    }

    private void setOnClickExpectation(int i, JFXButton mButton, boolean isExpectation){
        mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(isExpectation) {
                    ValueChangeScene mChangeScene = new ValueChangeScene(mProject.getExpectations().get(i), mButton.getText());
                    mChangeScene.showAndWait();
                    TypeOneMF changedValue = mChangeScene.getNewValue();
                    if (changedValue != null) {
                        updateAssumptionsInProject(isExpectation, mProject.getExpectations().get(i), changedValue);
                        mProject.getExpectations().set(i, changedValue);

                    }
                }else{
                    ValueChangeScene mChangeScene = new ValueChangeScene(mProject.getConfDegrees().get(i), mButton.getText());
                    mChangeScene.showAndWait();
                    TypeOneMF changedValue = mChangeScene.getNewValue();
                    if (changedValue != null) {
                        updateAssumptionsInProject(isExpectation, mProject.getConfDegrees().get(i), changedValue);
                        mProject.getConfDegrees().set(i, changedValue);
                    }
                }
                mController.saveProjectToFile();
            }
        });
    }

    private void updateAssumptionsInProject(boolean isExpectation, TypeOneMF oldValue, TypeOneMF newValue){

        for (int i = 0; i < mProject.getCriteriaMatrix().length; i++) {
            for (int j = 0; j < mProject.getCriteriaMatrix()[i].length; j++) {
                if(mProject.getCriteriaMatrix()[i][j] == null){
                    continue;
                }
                if(isExpectation) {
                    if (mProject.getCriteriaMatrix()[i][j].getExpectation().equals(oldValue)) {
                        mProject.getCriteriaMatrix()[i][j].setExpectation(newValue);
                    }
                }else{
                    if (mProject.getCriteriaMatrix()[i][j].getConfidence().equals(oldValue)) {
                        mProject.getCriteriaMatrix()[i][j].setConfidence(newValue);
                    }
                }
            }
        }
    }

    private VBox generateConfidenceButtons(){
        Label conf = new Label("Уверенность");
        conf.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        conf.setPadding(new Insets(10, 0, 10, 35));

        JFXButton confFirst = new JFXButton("Совсем не уверен: C1");
        JFXButton confSecond = new JFXButton("Не очень уверен: C2");
        JFXButton confThird = new JFXButton("Уверен: C3");
        JFXButton confFourth = new JFXButton("Очень уверен: C4");
        JFXButton confFifth = new JFXButton("Абсолютно уверен: C5");

        ArrayList <JFXButton> confDegreeButtons = new ArrayList<>();
        confDegreeButtons.add(confFirst);
        confDegreeButtons.add(confSecond);
        confDegreeButtons.add(confThird);
        confDegreeButtons.add(confFourth);
        confDegreeButtons.add(confFifth);

        for (int i = 0; i < confDegreeButtons.size(); i++) {
            setOnClickExpectation(i, confDegreeButtons.get(i), false);
        }

        VBox vboxConf = new VBox();
        vboxConf.getChildren().add(conf);
        vboxConf.getChildren().add(confFirst);
        vboxConf.getChildren().add(confSecond);
        vboxConf.getChildren().add(confThird);
        vboxConf.getChildren().add(confFourth);
        vboxConf.getChildren().add(confFifth);
        vboxConf.setPadding(new Insets(10));

        return vboxConf;
    }

}

class ValueChangeScene extends Stage{

    public TypeOneMF getNewValue() {
        return newValue;
    }

    TypeOneMF newValue;
    public ValueChangeScene(TypeOneMF mf, String buttonText){
        this.setTitle(buttonText);
        generateTextInputScene(mf);
    }

    private void generateTextInputScene(TypeOneMF mf){
        Label labelLower = new Label("Нижняя граница");
        TextField textLower = new TextField(String.valueOf(mf.getLowerBound()));
        HBox hbLower = new HBox();
        hbLower.getChildren().addAll(labelLower, textLower);
        hbLower.setSpacing(10);
        hbLower.setPadding(new Insets(10));

        Label labelMiddle = new Label("Среднее значение");
        TextField textMiddle = new TextField(String.valueOf(mf.getMiddle()));
        HBox hbMiddle = new HBox();
        hbMiddle.getChildren().addAll(labelMiddle, textMiddle);
        hbMiddle.setSpacing(10);
        hbMiddle.setPadding(new Insets(10));

        Label labelUpper = new Label("Верхняя граница");
        TextField textUpper = new TextField(String.valueOf(mf.getUpperBound()));
        HBox hbUpper = new HBox();
        hbUpper.getChildren().addAll(labelUpper, textUpper);
        hbUpper.setSpacing(10);
        hbUpper.setPadding(new Insets(10));


        JFXButton submitButton = new JFXButton("Подтвердить");
        HBox mBox = new HBox();
        mBox.getChildren().add(submitButton);
        mBox.setAlignment(Pos.BOTTOM_CENTER);
        Stage mStage = this;
        submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try{
                    Double.valueOf(textLower.getText());
                    Double.valueOf(textMiddle.getText());
                    Double.valueOf(textUpper.getText());
                }catch (NumberFormatException ex){
                    event.consume();
                    return;
                }
                newValue = new TypeOneMF(
                        Double.valueOf(textLower.getText()),
                        Double.valueOf(textMiddle.getText()),
                        Double.valueOf(textUpper.getText())
                );
                mStage.hide();
            }
        });

        VBox resBox = new VBox();
        resBox.getChildren().addAll(hbLower, hbMiddle, hbUpper);
        resBox.setSpacing(10);

        BorderPane bPane = new BorderPane();
        Stage stage = this;
        bPane.setCenter(resBox);
        bPane.setBottom(mBox);
        stage.setScene(new Scene(bPane));
    }

}

