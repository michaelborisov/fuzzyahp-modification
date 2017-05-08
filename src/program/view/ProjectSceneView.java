package program.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeView;
import com.oracle.javafx.jmx.json.JSONReader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import program.controller.ProjectSceneController;
import program.model.*;

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
        this.setTitle(title);
        this.setScene(new Scene(bPane, 700, 450));
        initLeftSideTreeView();
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
        treeView.setPrefWidth(160);
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
//        // TODO correct
//        mProject.setCriteriaMatrix(new Assumption[criteria.size()][criteria.size()]);
        mController.saveProjectToFile();
    }

    private void initTreeViewClickListener(JFXTreeView<String> treeView){
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                final ContextMenu cm = new ContextMenu();
                if(event.getButton() == MouseButton.PRIMARY){

                    if (selectedItem.getValue().equals("Критерии")) {
                        ScrollPane sPane = new ScrollPane();
                        GridPane gridpane = new GridPane();
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
                                }
                                mButton.setAlignment(Pos.CENTER);
                                mButton.setPadding(new Insets(0, 0, 0, 5));
                                mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                    @Override
                                    public void handle(MouseEvent event) {
                                        AssumptionSceneView mAssumptionView = new AssumptionSceneView(
                                                GridPane.getRowIndex(mButton),
                                                GridPane.getColumnIndex(mButton)
                                        );

                                        mAssumptionView.setOnHidden(new EventHandler<WindowEvent>() {
                                            @Override
                                            public void handle(WindowEvent event) {

                                                Assumption mAssumption = mAssumptionView.getAssumption();
                                                mProject.getCriteriaMatrix()[GridPane.getRowIndex(mButton) - 1]
                                                        [GridPane.getColumnIndex(mButton) - 1] = mAssumption;
                                                mController.saveProjectToFile();

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
                        gridpane.setPadding(new Insets(5));
                        gridpane.setAlignment(Pos.TOP_CENTER);


                        TextArea tArea = new TextArea();
                        tArea.setText("Комментарий ...");
                        tArea.setPadding(new Insets(5));

                        VBox vBox = new VBox();
                        vBox.setSpacing(10);
                        vBox.getChildren().addAll(sPane, tArea);
                        bPane.setCenter(vBox);
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

    private void addMenuItemClick(TreeItem<String> selectedItem,
                                  ContextMenu cm,
                                  JFXTreeView<String> treeView,
                                  MouseEvent event){
        MenuItem cmItem1 = new MenuItem("Добавить");

        cmItem1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                if (selectedItem.getValue().equals("Критерии")) {
                    entities.add(new CriteriaHierarchyEntity("Критерий_2"));
                }
                if (selectedItem.getValue().equals("Альтернативы")) {
                    entities.add(new AlternativeHierarchyEntity("Альтернатива_2"));
                }
                initLeftSideTreeView();
            }
        });
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
                        entities.add(new AlternativeHierarchyEntity(result.get()));
                    }
                    if(toDelete != null) {
                        entities.remove(toDelete);
                    }
                    initLeftSideTreeView();
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
                    ArrayList<String> q = mProject.getCriteria();
                    String w = toDelete.getName();
                    int e = q.indexOf(w);
                    int indexToDelete = mProject.getCriteria().indexOf(toDelete.getName());
                    modifyCriteriaMatrix(indexToDelete);
                    entities.remove(toDelete);
                }
                initLeftSideTreeView();
            }
        });
        cm.getItems().addAll(cmItem2, cmItem3);
        cm.show(treeView, event.getScreenX(), event.getScreenY());
    }

    private void modifyCriteriaMatrix(int indexToDelete){
        Assumption[][] newMatrix = new Assumption[mProject.getCriteria().size()][mProject.getCriteria().size()];
        Assumption[][] givenMatrix = mProject.getCriteriaMatrix();

        int p = 0;
        for (int i = 0; i < givenMatrix.length; i++) {
            if ( i == indexToDelete)
                continue;
            int q = 0;
            for (int j = 0; j < givenMatrix[i].length; j++) {
                if ( j == indexToDelete)
                    continue;


                    newMatrix[p][q] = givenMatrix[i][j];
                    q++;
            }
            p++;
        }
        
        mProject.setCriteriaMatrix(newMatrix);
    }
    private static String matrixCellButtonStyle =
            "    -fx-background-color: #0066FF;\n" +
                    "    -fx-text-fill: white;\n" +
                    "    -fx-border-color: white;\n" +
                    "    -fx-border-width: 4px;";
}
