package program.view;

import ahp.IntervalTypeTwoAhp;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import program.controller.ProjectSceneController;
import program.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

/**
 *
 * Created by Michael Borisov on 06.05.17.
 */
public class ProjectSceneView extends Stage {

    ArrayList<BaseHierarchyEntity> entities;
    BorderPane bPane;
    ProjectSceneController mController;
    AhpProject mProject;
    ContextMenu cm = new ContextMenu();

    public ProjectSceneView(String title, AhpProject mProject, ProjectSceneController mController){
        this.mController = mController;
        this.mProject = mProject;
        entities = new ArrayList<>();
        for(String a : mProject.getAlternatives()){
            entities.add(new AlternativeHierarchyEntity(a));
        }
        for(String a:mProject.getCriteria()){
            entities.add(new CriteriaHierarchyEntity(a));
        }
        bPane = new BorderPane();
        bPane.setCenter(generateWelcomeScreen());
        generateMenuBar();
        this.setTitle(title);
        this.setScene(new Scene(bPane, 700, 450));
        bPane.setStyle("-fx-background-color:#FFF");
        initLeftSideTreeView();
    }

    private VBox generateWelcomeScreen(){
        VBox mVbox = new VBox();
        Label email = new Label("Добро пожаловать!");
        Image mImage = new Image("file:logo.jpg");
        ImageView mView = new ImageView();
        mView.setFitHeight(250);
        mView.setFitWidth(250);
        mView.setImage(mImage);
        email.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        email.setTextAlignment(TextAlignment.CENTER);
        mVbox.getChildren().addAll(email, mView);
        mVbox.setAlignment(Pos.CENTER);
        mVbox.setSpacing(10);
        return mVbox;
    }


    private void initLeftSideTreeView(){
        TreeItem<String> rootNode = new TreeItem<>("Цель");
        rootNode.setExpanded(true);

        entities.sort(new Comparator<BaseHierarchyEntity>() {
            @Override
            public int compare(BaseHierarchyEntity o1, BaseHierarchyEntity o2) {
                if(o1 instanceof CriteriaHierarchyEntity && o2 instanceof AlternativeHierarchyEntity){
                    return -1;
                }
                return 1;
            }
        });
        updateAlternativesAndCriteriaProjectLists();
        final JFXTreeView<String> treeView = new JFXTreeView<>(rootNode);
        for (BaseHierarchyEntity entity : entities) {
            TreeItem<String> empLeaf = new TreeItem<>(entity.getName());
            boolean found = false;
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(entity.getGroup())) {
                    depNode.getChildren().add(empLeaf);
                    depNode.setExpanded(true);
                    found = true;
                    break;
                }
            }

            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(entity.getGroup());
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(empLeaf);
            }
        }
        TreeItem<String> resultNode = new TreeItem<>("Результат");
        rootNode.getChildren().add(resultNode);
        treeView.setPrefWidth(160);
        treeView.setPrefHeight(750);
        initTreeViewClickListener(treeView);
        bPane.setLeft(treeView);
    }

    private void updateAlternativesAndCriteriaProjectLists(){
        ArrayList<String> alternatives = new ArrayList<>();
        ArrayList<String> criteria = new ArrayList<>();
        for (BaseHierarchyEntity entity: entities) {
            if (entity instanceof  AlternativeHierarchyEntity){
                alternatives.add(entity.getName());
            }
            if (entity instanceof  CriteriaHierarchyEntity){
                criteria.add(entity.getName());
            }
        }
        mProject.setAlternatives(alternatives);
        mProject.setCriteria(criteria);
        if(criteria.size() > mProject.getCriteriaMatrix().length) {
            Assumption[][] newAssumption = new Assumption[criteria.size()][criteria.size()];
            for (int i = 0; i < mProject.getCriteriaMatrix().length; i++) {
                for (int j = 0; j < mProject.getCriteriaMatrix().length; j++) {
                    newAssumption[i][j] = mProject.getCriteriaMatrix()[i][j];
                }
            }
            mProject.setCriteriaMatrix(newAssumption);
        }
        if(criteria.size() > mProject.getAlternativeMatrices().size()){
            mProject.getAlternativeMatrices().add(new Assumption[alternatives.size()][alternatives.size()]);
        }
        if(alternatives.size() > mProject.getAlternativeMatrices().get(0).length) {
            for(int k = 0; k < mProject.getAlternativeMatrices().size(); k ++) {
                Assumption[][] newAssumption = new Assumption[alternatives.size()][alternatives.size()];
                for (int i = 0; i < mProject.getAlternativeMatrices().get(k).length; i++) {
                    for (int j = 0; j < mProject.getAlternativeMatrices().get(k)[i].length; j++) {
                        newAssumption[i][j] = mProject.getAlternativeMatrices().get(k)[i][j];
                    }
                }
                mProject.getAlternativeMatrices().set(k, newAssumption);
            }
        }

        mController.saveProjectToFile();
    }

    private void initDescriptionScene(String key){
        TextArea tArea = new TextArea();
        String criteriaComment = mProject.getDescriptionMap().get(key);
        if (criteriaComment == null){
            tArea.setText("Комментарий ...");
        }else{
            tArea.setText(criteriaComment);
        }
        tArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    mProject.getDescriptionMap().put(key, tArea.getText());
                    mController.saveProjectToFile();
                }
            }
        });
        tArea.setPadding(new Insets(5));
        bPane.setCenter(tArea);
    }

    private void initTreeViewClickListener(JFXTreeView<String> treeView){
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

                if(event.getButton() == MouseButton.PRIMARY){

                    if(selectedItem.getParent() == null){
                        initDescriptionScene("Цель");
                        return;
                    }

                    if (selectedItem.getParent().getValue().equals("Альтернативы")){
                        initDescriptionScene(selectedItem.getValue());
                        return;
                    }

                    if (selectedItem.getValue().equals("Критерии")) {
                        initCriteriaScene();
                    }
                    if(selectedItem.getParent().getValue().equals("Критерии")){
                        int ind = mProject.getCriteria().indexOf(selectedItem.getValue());
                        if(ind >= 0){
                            initSpecificCriterionScene(ind);
                        }else {
                            initSpecificCriterionScene(0);
                        }
                    }
                    if (selectedItem.getValue().equals("Результат")){

                        IntervalTypeTwoAhp mAhp = new IntervalTypeTwoAhp(mProject);
                        if(!mAhp.isAllMatricesConsistent()){
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Обнаружена несогласованность");
                            alert.setHeaderText("Обнаружена несогласованная матрица");
                            alert.setContentText("Обнаружена несогласованность матрицы критериев. " +
                                    "Проверьте введённые оценки и повторно нажмите на элемент \"Результат\"");

                            alert.showAndWait();
                            return;

                        }

                        ArrayList<Double[]> totalResultVector = mAhp.calculateTotalResultVector();
                        ArrayList<Double> crispValues = new ArrayList<Double>();
                        for (int i = 0; i < totalResultVector.size(); i++) {
                            crispValues.add((totalResultVector.get(i)[0] + totalResultVector.get(i)[1])/2.0);
                        }
                        final CategoryAxis xAxis = new CategoryAxis();
                        final NumberAxis yAxis = new NumberAxis();
                        final BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
                        bc.setTitle("Результат");
                        xAxis.setLabel("Альтернативы");
                        yAxis.setLabel("Предпочтение в процентах");

                        XYChart.Series series1 = new XYChart.Series();
                        series1.setName("Приоритеты");
                        for (int i = 0; i < crispValues.size(); i++) {
                            series1.getData().add(new XYChart.Data(mProject.getAlternatives().get(i), crispValues.get(i)*100));
                        }
                        bc.getData().addAll(series1);
                        bPane.setCenter(bc);
                    }
                }
                if(event.getButton() == MouseButton.SECONDARY){
                    if((selectedItem.getValue().equals("Критерии")) || selectedItem.getValue().equals("Альтернативы")){
                        addMenuItemClick(selectedItem, cm, treeView, event);
                    }else {
                        modifyMenuItemClick(selectedItem, cm, treeView, event);
                    }
                    return;
                }
            }
        });
    }

    private void initCriteriaScene(){
        ScrollPane sPane = new ScrollPane();
        GridPane gridpane = new GridPane();
        //sPane.setStyle("-fx-background-color:#FFF");
        //gridpane.setStyle("-fx-background-color:#FFF");
        sPane.setContent(gridpane);
        for (int i = 0; i < mProject.getCriteria().size() + 1; i++) {
            RowConstraints row = new RowConstraints(50);
            gridpane.getRowConstraints().add(row);
        }
        for (int i = 0; i < mProject.getCriteria().size() + 1; i++) {
            for (int j = 0; j < mProject.getCriteria().size() + 1; j++) {
                if (i == 0 && j == 0) {
                    Label mButton = new Label("Цель");
                    mButton.setPadding(new Insets(5, 5, 5, 5));
                    GridPane.setHalignment(mButton, HPos.CENTER);
                    gridpane.add(mButton, i, j);
                    continue;
                }
                if (i == 0 || j == 0) {
                    Label mButton = new Label(mProject.getCriteria().get(Math.max(i - 1,j - 1)));
                    mButton.setPadding(new Insets(5, 5, 5, 5));
                    GridPane.setHalignment(mButton, HPos.CENTER);
                    gridpane.add(mButton, i, j);
                    continue;

                }
                if (i == j) {
                    continue;
                }
                JFXButton mButton = new JFXButton("Оценка ?" + "\n" + "Уверенность ?");
                mButton.setWrapText(true);
                mButton.setButtonType(JFXButton.ButtonType.RAISED);

                if(mProject.getCriteriaMatrix()[j-1][i-1] != null){
                    mButton.setStyle(matrixCellButtonStyle);
                    mButton.setText(mProject.getCriteriaMatrix()[j-1][i-1].getLabel());

                }

                if(mProject.getCriteriaMatrix()[i-1][j-1] != null){
                    mButton.setStyle(matrixCellButtonStyle);
                    mButton.setText(mProject.getCriteriaMatrix()[i-1][j-1].getLabel());

                }

                mButton.setAlignment(Pos.CENTER);
                mButton.setPadding(new Insets(0, 0, 0, 5));
                mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        AssumptionSceneView mAssumptionView = new AssumptionSceneView(
                                GridPane.getRowIndex(mButton),
                                GridPane.getColumnIndex(mButton),
                                mProject
                        );

                        mAssumptionView.setOnHidden(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {

                                Assumption mAssumption = mAssumptionView.getAssumption();
                                if (mAssumption == null){
                                    return;
                                }
                                mProject.getCriteriaMatrix()[GridPane.getRowIndex(mButton) - 1]
                                        [GridPane.getColumnIndex(mButton) - 1] = mAssumption;
                                mController.saveProjectToFile();
                                //mButton.setText(mAssumptionView.getGeneratedLabel());
                                initCriteriaScene();

                            }
                        });
                        mAssumptionView.showAndWait();
                    }
                });
                if(j>i){
                    mButton.disableProperty().setValue(Boolean.TRUE);
                }
                GridPane.setHalignment(mButton, HPos.CENTER);
                gridpane.add(mButton, i, j);
            }
        }
        gridpane.setGridLinesVisible(true);
        if(mProject.getCriteria().size() <=3 ) {
            gridpane.setPadding(new Insets(5, 5, 5, 50));
        }else{
            gridpane.setPadding(new Insets(5));
        }
        TextArea tArea = new TextArea();
        String criteriaComment = mProject.getDescriptionMap().get("Критерии");
        if (criteriaComment == null){
            tArea.setText("Комментарий ...");
        }else{
            tArea.setText(criteriaComment);
        }
        tArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    mProject.getDescriptionMap().put("Критерии", tArea.getText());
                    mController.saveProjectToFile();
                }
            }
        });
        tArea.setPadding(new Insets(5));
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(sPane, tArea);
        bPane.setCenter(vBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(gridpane.getWidth() > 0) {
                    ProjectSceneView.this.setWidth(gridpane.getWidth() + 165);
                }
            }
        });
    }

    private void initSpecificCriterionScene(int criteriaIndex){
        GridPane gridpane = new GridPane();
        ScrollPane sPane = new ScrollPane();
        //sPane.setStyle("-fx-background-color:#FFF");
        //gridpane.setStyle("-fx-background-color:#FFF");
        sPane.setHvalue(0.5);
        sPane.setVvalue(0.5);
        gridpane.setAlignment(Pos.TOP_CENTER);
        sPane.setHvalue(200);

        bPane.setStyle(("-fx-background-color:#FFF"));
        sPane.setContent(gridpane);
        for (int i = 0; i < mProject.getAlternatives().size() + 1; i++) {
            RowConstraints row = new RowConstraints(50);
            gridpane.getRowConstraints().add(row);
        }
        for (int i = 0; i < mProject.getAlternatives().size() + 1; i++) {
            for (int j = 0; j < mProject.getAlternatives().size() + 1; j++) {
                if (i == 0 && j == 0) {
                    Label mButton = new Label(mProject.getCriteria().get(criteriaIndex));
                    mButton.setPadding(new Insets(5, 5, 5, 5));
                    GridPane.setHalignment(mButton, HPos.CENTER);
                    gridpane.add(mButton, i, j);
                    continue;
                }
                if (i == 0) {
                    Label mButton = new Label(mProject.getAlternatives().get(j - 1));
                    mButton.setPadding(new Insets(5, 5, 5, 5));
                    GridPane.setHalignment(mButton, HPos.CENTER);
                    gridpane.add(mButton, i, j);
                    continue;

                }
                if (j == 0) {
                    Label mButton = new Label(mProject.getAlternatives().get(i - 1));
                    mButton.setPadding(new Insets(5, 5, 5, 5));
                    GridPane.setHalignment(mButton, HPos.CENTER);
                    gridpane.add(mButton, i, j);
                    continue;
                }
                if (i == j) {
                    continue;
                }
                JFXButton mButton = new JFXButton("Оценка ?" + "\n" + "Уверенность ?");
                mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        AssumptionSceneView mAssumptionView = new AssumptionSceneView(
                                GridPane.getRowIndex(mButton),
                                GridPane.getColumnIndex(mButton),
                                mProject
                        );

                        mAssumptionView.setOnHidden(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {

                                Assumption mAssumption = mAssumptionView.getAssumption();
                                if(mAssumption == null){
                                    return;
                                }
                                mProject.getAlternativeMatrices().get(criteriaIndex)[GridPane.getRowIndex(mButton) - 1]
                                        [GridPane.getColumnIndex(mButton) - 1] = mAssumption;

                                mController.saveProjectToFile();
                                initSpecificCriterionScene(criteriaIndex);
                            }
                        });
                        mAssumptionView.showAndWait();
                    }
                });
                try {
                    if (mProject.getAlternativeMatrices().get(criteriaIndex)[j - 1][i - 1] != null) {
                        mButton.setStyle(matrixCellButtonStyle);
                        mButton.setText(mProject.getAlternativeMatrices().get(criteriaIndex)[j - 1][i - 1].getLabel());
                    }

                    if (mProject.getAlternativeMatrices().get(criteriaIndex)[i - 1][j - 1] != null) {
                        mButton.setStyle(matrixCellButtonStyle);
                        mButton.setText(mProject.getAlternativeMatrices().get(criteriaIndex)[i - 1][j - 1].getLabel());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    updateAlternativesAndCriteriaProjectLists();
                }
                mButton.setWrapText(true);
                mButton.setButtonType(JFXButton.ButtonType.RAISED);
                //mButton.setStyle(matrixCellButtonStyle);
                mButton.setAlignment(Pos.CENTER);
                mButton.setPadding(new Insets(0, 0, 0, 5));
                if(j>i){
                    mButton.disableProperty().setValue(Boolean.TRUE);
                }
                GridPane.setHalignment(mButton, HPos.CENTER);
                gridpane.add(mButton, i, j);
            }
        }

        gridpane.setGridLinesVisible(true);
        if(mProject.getAlternatives().size() <=3 ) {
            gridpane.setPadding(new Insets(5, 5, 5, 50));
        }else{
            gridpane.setPadding(new Insets(5));
        }
        bPane.getCenter().setStyle("-fx-background-color:#FFF");

        TextArea tArea = new TextArea();
        String criteriaComment = mProject.getDescriptionMap().get(mProject.getCriteria().get(criteriaIndex));
        if (criteriaComment == null){
            tArea.setText("Комментарий ...");
        }else{
            tArea.setText(criteriaComment);
        }
        tArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue){
                    mProject.getDescriptionMap().put(mProject.getCriteria().get(criteriaIndex), tArea.getText());
                    mController.saveProjectToFile();
                }
            }
        });
        tArea.setPadding(new Insets(5));

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.getChildren().addAll(sPane, tArea);
        vBox.setStyle(("-fx-background-color:#FFF"));
        bPane.setCenter(vBox);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(gridpane.getWidth() > 0) {
                    ProjectSceneView.this.setWidth(gridpane.getWidth() + 165);
                }
            }
        });
    }

    private void addMenuItemClick(TreeItem<String> selectedItem,
                                  ContextMenu cm,
                                  JFXTreeView<String> treeView,
                                  MouseEvent event){
        MenuItem cmItem1 = new MenuItem("Добавить");

        cmItem1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
                dialog.setTitle("Добавлени");
                if(selectedItem.getValue().equals("Критерии")) {
                    dialog.setHeaderText("Добавьте критерий");
                }else{
                    dialog.setHeaderText("Добавьте альтернативу");
                }
                dialog.setContentText("Пожалуйста, введите название");
                Button mButton = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
                mButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String newName = dialog.getEditor().getText();
                        if (mProject.getAlternatives().indexOf(newName) != -1 ||
                                mProject.getCriteria().indexOf(newName) != -1 ||
                                newName.equals("Критерии") ||
                                newName.equals("Альтернативы") ||
                                newName.equals("Цель") ||
                                newName.equals("Результат")){
                            event.consume();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Неправильное название");
                            alert.setHeaderText("Невозможно добавить элемент с таким именем");
                            alert.setContentText("Элемент с таким именем уже существует. " +
                                    "Пожалуйста, введите другое название");

                            alert.showAndWait();
                        }
                    }
                });
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    if (selectedItem.getValue().equals("Критерии")) {
                        entities.add(new CriteriaHierarchyEntity(result.get()));
                    }
                    if (selectedItem.getValue().equals("Альтернативы")) {
                        entities.add(new AlternativeHierarchyEntity(result.get()));
                    }
                    initLeftSideTreeView();
                    if (selectedItem.getValue().equals("Критерии")) {
                        initCriteriaScene();
                    }
                }
            }
        });
        cm.getItems().clear();
        cm.getItems().add(cmItem1);
        cm.show(treeView, event.getScreenX(), event.getScreenY());
    }

    private void modifyMenuItemClick(TreeItem<String> selectedItem,
                                     ContextMenu cm,
                                     JFXTreeView<String> treeView,
                                     MouseEvent event){


        MenuItem cmItem2 = new MenuItem("Переименовать");
        cmItem2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextInputDialog dialog = new TextInputDialog(selectedItem.getValue());
                dialog.setTitle("Переименование");
                if(selectedItem.getParent().getValue().equals("Критерии")) {
                    dialog.setHeaderText("Переименуйте критерий");
                }else{
                    dialog.setHeaderText("Переименуйте альтернативу");
                }
                dialog.setContentText("Пожалуйста, введите название");

                Button mButton = (Button)dialog.getDialogPane().lookupButton(ButtonType.OK);
                mButton.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        String newName = dialog.getEditor().getText();
                        if (mProject.getAlternatives().indexOf(newName) != -1 ||
                                mProject.getCriteria().indexOf(newName) != -1 ||
                                newName.equals("Критерии") ||
                                newName.equals("Альтернативы") ||
                                newName.equals("Цель") ||
                                newName.equals("Результат")){
                            event.consume();
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Неправильное название");
                            alert.setHeaderText("Невозможно добавить элемент с таким именем");
                            alert.setContentText("Элемент с таким именем уже существует. " +
                                    "Пожалуйста, введите другое название");

                            alert.showAndWait();
                        }
                    }
                });
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    BaseHierarchyEntity toDelete = null;
                    for (BaseHierarchyEntity entity: entities) {
                        if (entity.getName().equals(selectedItem.getValue())){
                            toDelete = entity;
                            break;
                        }

                    }
                    if(selectedItem.getParent().getValue().equals("Критерии")){
                        entities.add(entities.indexOf(toDelete), new CriteriaHierarchyEntity(result.get()));
                    }else{
                        entities.add(entities.indexOf(toDelete), new AlternativeHierarchyEntity(result.get()));
                    }
                    if(toDelete != null) {
                        entities.remove(toDelete);
                    }
                    mProject.getDescriptionMap().put(
                            result.get(),
                            mProject.getDescriptionMap().remove(selectedItem.getValue())
                    );
                    initLeftSideTreeView();

                    if(selectedItem.getParent().getValue().equals("Критерии")) {
                        initCriteriaScene();
                    }
                }
            }
        });

        MenuItem cmItem3 = new MenuItem("Удалить");
        cmItem3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                BaseHierarchyEntity toDelete = null;
                for (BaseHierarchyEntity entity: entities) {
                    if (entity.getName().equals(selectedItem.getValue())){
                        toDelete = entity;
                        break;
                    }
                }
                if(toDelete != null) {
                    ArrayList<String> criteria = mProject.getCriteria();
                    int ind = criteria.indexOf(toDelete.getName());
                    if (ind > -1) {
                        if (selectedItem.getParent().getValue().equals("Критерии")) {
                            modifyCriteriaMatrix(ind);
                        }
                    }else{
                        ind = mProject.getAlternatives().indexOf(toDelete.getName());
                        modifyAlternativeMatrices(ind);
                    }

                    entities.remove(toDelete);
                }
                initLeftSideTreeView();
                if(selectedItem.getParent().getValue().equals("Критерии")) {
                    initCriteriaScene();
                }
                if(selectedItem.getParent().getValue().equals("Альтернативы")) {
                    initSpecificCriterionScene(0);
                }
            }
        });
        cm.getItems().clear();
        cm.getItems().addAll(cmItem2, cmItem3);
        cm.hide();
        cm.show(treeView, event.getScreenX(), event.getScreenY());
    }

    private void modifyCriteriaMatrix(int indexToDelete){
        Assumption[][] newMatrix = new Assumption[mProject.getCriteria().size()][mProject.getCriteria().size()];
        Assumption[][] givenMatrix = mProject.getCriteriaMatrix();

        int p = 0;
        for (int i = 0; i < givenMatrix.length; i++) {
            if ( i == indexToDelete){
                continue;
            }
            int q = 0;
            for (int j = 0; j < givenMatrix[i].length; j++) {
                if ( j == indexToDelete) {
                    continue;
                }
                    newMatrix[p][q] = givenMatrix[i][j];
                    q++;
            }
            p++;
        }

        mProject.setCriteriaMatrix(newMatrix);
    }

    private void modifyAlternativeMatrices(int indexToDelete){
        ArrayList<Assumption[][]> alternativeMatrices = mProject.getAlternativeMatrices();
        ArrayList<Assumption[][]> newAlternativeMatrices = new ArrayList<>();
        for (int k = 0; k < alternativeMatrices.size(); k++) {
            Assumption[][] newMatrix = new Assumption[mProject.getAlternatives().size() - 1]
                    [mProject.getAlternatives().size() - 1];
            Assumption[][] givenMatrix = alternativeMatrices.get(k);

            int p = 0;
            for (int i = 0; i < givenMatrix.length; i++) {
                if ( i == indexToDelete){
                    continue;
                }
                int q = 0;
                for (int j = 0; j < givenMatrix[i].length; j++) {
                    if ( j == indexToDelete) {
                        continue;
                    }
                    newMatrix[p][q] = givenMatrix[i][j];
                    q++;
                }
                p++;
            }

            newAlternativeMatrices.add(newMatrix);
        }
        mProject.setAlternativeMatrices(newAlternativeMatrices);
    }

    private void generateMenuBar(){
        MenuBar menuBar = new MenuBar();

        Menu menuFile = new Menu("Файл");

        Menu menuSettings = new Menu("Настройки");

        Menu menuFaq = new Menu("Справка");

        MenuItem faq = new MenuItem("О программе");
        faq.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        faq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                BorderPane bPane = new BorderPane();
                Stage stage = new Stage();

                Label info = new Label("Теоретическое обоснование работы");
                Label email = new Label("Борисов Михаил \nБПИ133 \nmyuborisov@edu.hse.ru");
                email.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
                email.setTextAlignment(TextAlignment.CENTER);
                bPane.setCenter(email);
                stage.setTitle("Справка");
                stage.setScene(new Scene(bPane, 350, 450));
                stage.show();
            }
        });
        menuFaq.getItems().addAll(faq);

        MenuItem result = new MenuItem("Открыть");
        result.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fChooser = new FileChooser();
                File chosen = fChooser.showOpenDialog(menuBar.getScene().getWindow());
                if(chosen != null) {
                    new ProjectSceneController(chosen.getAbsolutePath(), chosen.getName());
                }
            }
        });
        menuFile.getItems().add(result);

        MenuItem changeT1MFs = new MenuItem("Настроить оценки");
        changeT1MFs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                new AssumptionChangeSceneView(mProject, mController).show();
            }
        });
        menuSettings.getItems().add(changeT1MFs);
        menuBar.getMenus().addAll(menuFile, menuSettings, menuFaq);
        bPane.setTop(menuBar);
    }
    private static String matrixCellButtonStyle =
            "    -fx-background-color: #0066FF;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-border-color: white;\n" +
                    "    -fx-border-width: 4px;";
}
