package program.view;

import com.jfoenix.controls.JFXTreeView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import program.model.AhpProject;
import program.model.AlternativeHierarchyEntity;
import program.model.BaseHierarchyEntity;
import program.model.CriteriaHierarchyEntity;
import sample.Main;

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

    public ProjectSceneView(String title, AhpProject mProject){
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

    private void initTreeViewClickListener(JFXTreeView<String> treeView){
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                final ContextMenu cm = new ContextMenu();
                if(event.getButton() == MouseButton.SECONDARY){
                    if((selectedItem.getValue().equals("Критерии")) || selectedItem.getValue().equals("Альтернативы")){


                        MenuItem cmItem1 = new MenuItem("Добавить");

                        cmItem1.setOnAction(new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent e) {

                                if (selectedItem.getValue().equals("Критерии")) {
                                    entities.add(new CriteriaHierarchyEntity("Критерий_2"));
                                    initLeftSideTreeView();
                                }
                                if (selectedItem.getValue().equals("Альтернативы")) {
                                    entities.add(new AlternativeHierarchyEntity("Альтернатива_2"));
                                    initLeftSideTreeView();
                                }
                            }
                        });
                        cm.getItems().add(cmItem1);
                        cm.show(treeView, event.getScreenX(), event.getScreenY());
                    }else {
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
                                        }

                                    }
                                    if(selectedItem.getParent().getValue().equals("Критерии")){
                                        entities.add(new CriteriaHierarchyEntity(result.get()));
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
                        cm.getItems().add(cmItem2);
                        cm.show(treeView, event.getScreenX(), event.getScreenY());
                    }
                    return;
                }
            }
        });
    }
}
