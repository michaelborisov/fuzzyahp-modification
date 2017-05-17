package sample;

import ahp.IntervalTypeTwoAHPMatrix;
import com.jfoenix.controls.*;
import fuzzy.Alternative;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyledTextArea;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
//        employees.addAll(asList(
//                new TreeViewEntity("Критерий_1", CRITERIA),
//                new TreeViewEntity("Критерий_2", CRITERIA),
//                new TreeViewEntity("Критерий_3", CRITERIA),
//                new TreeViewEntity("Альтернатива_1", ALTERNATIVE),
//                new TreeViewEntity("Альтернатива_2", ALTERNATIVE),
//                new TreeViewEntity("Альтернатива_3", ALTERNATIVE)));
//        initStartScene(primaryStage);




        String[] paths = new String[]{
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_1.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_2.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_3.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_4.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_5.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_6.xlsx",
                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_7.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Symmetric_8.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Asymmetric_1.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Asymmetric_2.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Asymmetric_3.xlsx",
//                "/Users/michaelborisov/IdeaProjects/fuzzyahp-modification/src/sample/Asymmetric_4.xlsx"
        };

        for(String path: paths) {
            System.out.println(path);
            for (double b = 0.5; b < 1; b += 0.01) {
                helper.Settings.BOUNDARY_VALUE = b;
                IntervalTypeTwoAHPMatrix ahp = new IntervalTypeTwoAHPMatrix(path);
                ahp.calculateEigenValue();
                ArrayList<Alternative> alternatives = ahp.calculateResultVector();
                System.out.print(String.format("%.2f: ", b));
                for (int i = 0; i < alternatives.size(); i++) {
                    System.out.print(String.format("%s, ", alternatives.get(i).getName()));
                }
                System.out.println();
            }
        }
    }

    private void initStartScene(Stage primaryStage) throws Exception{
        HBox root = FXMLLoader.load(getClass().getResource("sample.fxml"));


        BorderPane bPane = new BorderPane();
        bPane.setBottom(root);
        //tasksList.setPrefWidth(300);
        bPane.setCenter(generateTasksList());
        primaryStage.setTitle("Выбор задачи");
        primaryStage.setScene(new Scene(bPane, 300, 275));
        primaryStage.show();
    }

    private void generateMenuBar(BorderPane bPane){
        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("Файл");

        // --- Menu Edit
        Menu menuFaq = new Menu("Справка");

        MenuItem faq = new MenuItem("О программе");
        faq.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        faq.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                BorderPane bPane = new BorderPane();
                Stage stage = new Stage();

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

        MenuItem result = new MenuItem("Результат");
        result.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        result.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = new Stage();
                stage.setTitle("Результат");
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                final BarChart<String,Number> bc =
                        new BarChart<String,Number>(xAxis,yAxis);
                bc.setTitle("Результаты ");
                xAxis.setLabel("Альтернативы");
                yAxis.setLabel("Доля");

                XYChart.Series series1 = new XYChart.Series();
                series1.setName("Приоритеты");
                series1.getData().add(new XYChart.Data("Альтернатива_1", 0.296));
                series1.getData().add(new XYChart.Data("Альтернатива_2", 0.323));
                series1.getData().add(new XYChart.Data("Альтернатива_3", 0.217));
                series1.getData().add(new XYChart.Data("Альтернатива_4", 0.164));

                Scene scene  = new Scene(bc,800,600);
                bc.getData().addAll(series1);
                stage.setScene(scene);
                stage.show();
            }
        });
        menuFile.getItems().add(result);
        menuBar.getMenus().addAll(menuFile, menuFaq);
        bPane.setTop(menuBar);
    }
    private JFXListView<Label> generateTasksList(){
        JFXListView<Label> tasksList = new JFXListView<>();
        String[] sampleTasks = new String[]{
                "Выбор менеджера",
                "Санаторий для отдыха",
                "Телефон ребёнку"
        };

        for (String task: sampleTasks) {
            tasksList.getItems().add(new Label(task));
        }
        tasksList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2){
                    Label currentItemSelected = tasksList.getSelectionModel()
                            .getSelectedItem();
                    BorderPane bPane = new BorderPane();
                    Stage stage = new Stage();
                    bPane.setLeft(generateTreeView(bPane));
                    generateMenuBar(bPane);
                    stage.setTitle(currentItemSelected.getText());
                    stage.setScene(new Scene(bPane, 700, 450));
                    stage.show();
                    //((Node)(event.getSource())).getScene().getWindow().hide();

                }
            }
        });
        return tasksList;
    }

    private static final String ALTERNATIVE = "Альтернативы";
    private static final String CRITERIA = "Критерии";

    private final List<TreeViewEntity> employees = new ArrayList<>();


    private void generateRadioButtonScene(){
        final ToggleGroup approximGroup = new ToggleGroup();
        BorderPane bPane = new BorderPane();
        Stage stage = new Stage();


        JFXRadioButton first = new JFXRadioButton("В равной степени: E1");
        first.setPadding(new Insets(10));
        first.setToggleGroup(approximGroup);

        JFXRadioButton second = new JFXRadioButton("Слабое преобладание: E2");
        second.setPadding(new Insets(10));
        second.setToggleGroup(approximGroup);

        JFXRadioButton third = new JFXRadioButton("Существенное преобладание: E3");
        third.setPadding(new Insets(10));
        third.setToggleGroup(approximGroup);

        JFXRadioButton fourth = new JFXRadioButton("Очень существенное преобладание: E4");
        fourth.setPadding(new Insets(10));
        fourth.setToggleGroup(approximGroup);

        JFXRadioButton fifth = new JFXRadioButton("Крайне предпочтительнее: E5");
        fifth.setPadding(new Insets(10));
        fifth.setToggleGroup(approximGroup);

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

        JFXRadioButton confSecond = new JFXRadioButton("Не очень уверен: C2");
        confSecond.setPadding(new Insets(10));
        confSecond.setToggleGroup(confGroup);

        JFXRadioButton confThird = new JFXRadioButton("Уверен: C3");
        confThird.setPadding(new Insets(10));
        confThird.setToggleGroup(confGroup);

        JFXRadioButton confFourth = new JFXRadioButton("Очень уверен: C4");
        confFourth.setPadding(new Insets(10));
        confFourth.setToggleGroup(confGroup);

        JFXRadioButton confFifth = new JFXRadioButton("Абсолютно уверен: C5");
        confFifth.setPadding(new Insets(10));
        confFifth.setToggleGroup(confGroup);


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
        submitButton.setStyle(matrixCellButtonStyle);
        submitButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.hide();
            }
        });
        subHBox.setAlignment(Pos.CENTER);
        bPane.setBottom(subHBox);

        stage.setTitle("Экспертная оценка");
        stage.setScene(new Scene(bPane, 550, 275));
        stage.showAndWait();

        System.out.println("The scene was closed");
    }

    private TreeItem<String> rootNode = new TreeItem<>("Цель");//, rootIcon);    // Set picture

    private JFXTreeView<String> generateTreeView(BorderPane borderPane) {
        rootNode = new TreeItem<>("Цель");
        rootNode.setExpanded(true);

        final JFXTreeView<String> treeView = new JFXTreeView<>(rootNode);
        for (TreeViewEntity employee : employees) {
            TreeItem<String> empLeaf = new TreeItem<>(employee.getName());
            boolean found = false;
            for (TreeItem<String> depNode : rootNode.getChildren()) {
                if (depNode.getValue().contentEquals(employee.getGroup())) {
                    depNode.getChildren().add(empLeaf);
                    depNode.setExpanded(true);
                    found = true;
                    break;
                }
            }

            if (!found) {
                TreeItem<String> depNode = new TreeItem<>(employee.getGroup());
                rootNode.getChildren().add(depNode);
                depNode.getChildren().add(empLeaf);
            }
        }
        treeView.setPrefWidth(155);
        treeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                MouseButton b = event.getButton();
                if(event.getButton() == MouseButton.SECONDARY){

                    final ContextMenu cm = new ContextMenu();
                    MenuItem cmItem1 = new MenuItem("Добавить");
                    cmItem1.setOnAction(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            employees.add(new TreeViewEntity("Критерий_4", CRITERIA));
                            borderPane.setLeft(generateTreeView(borderPane));
                        }
                    });

                    cm.getItems().add(cmItem1);
                    cm.show(treeView, event.getScreenX(), event.getScreenY());
                    return;
                }
                TreeItem<String> e = treeView.getSelectionModel().getSelectedItem();
                if (e.getValue().equals("Критерии")) {
                    GridPane gridpane = new GridPane();
                    for (int i = 0; i < 4; i++) {
                        RowConstraints row = new RowConstraints(50);
                        gridpane.getRowConstraints().add(row);
                    }
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (i == 0 && j == 0) {
                                Label mButton = new Label("Цель");
                                mButton.setPadding(new Insets(5, 5, 5, 5));
                                GridPane.setHalignment(mButton, HPos.CENTER);
                                gridpane.add(mButton, i, j);
                                continue;
                            }
                            if (i == 0 || j == 0) {
                                Label mButton = new Label("Критерий_" + Math.max(i, j));
                                mButton.setPadding(new Insets(5, 5, 5, 5));
                                GridPane.setHalignment(mButton, HPos.CENTER);
                                gridpane.add(mButton, i, j);
                                continue;

                            }
//                            if (j == 0) {
//                                Label mButton = new Label("Критерий_" + i);
//                                mButton.setPadding(new Insets(5, 5, 5, 5));
//                                GridPane.setHalignment(mButton, HPos.CENTER);
//                                gridpane.add(mButton, i, j);
//                                continue;
//                            }
                            if (i == j) {
                                continue;
                            }
                            JFXButton mButton = new JFXButton("Оценка ?" + "\n" + "Уверенность ?");
                            mButton.setWrapText(true);
                            mButton.setButtonType(JFXButton.ButtonType.RAISED);
                            mButton.setStyle(matrixCellButtonStyle);
                            mButton.setAlignment(Pos.CENTER);
                            mButton.setPadding(new Insets(0, 0, 0, 5));
                            mButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    generateRadioButtonScene();
                                    mButton.setStyle(matrixCellChosenButtonStyle);
                                    mButton.setText("Оценка: E1 \nУверенность: C2");
                                    int row = gridpane.getRowIndex(mButton);
                                    int column = gridpane.getColumnIndex(mButton);
                                    ObservableList<Node> children = gridpane.getChildren();
                                    Node result = new JFXButton();
                                    for (Node node : children) {
                                        if(gridpane.getRowIndex(node) == column && gridpane.getColumnIndex(node) == row) {
                                            result = node;
                                            break;
                                        }
                                    }
                                    result.setStyle(matrixCellChosenButtonStyle);
                                    ((JFXButton)result).setText("Оценка: E1 \n" +
                                            "Уверенность: C2");
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
                    vBox.getChildren().addAll(gridpane, tArea);
                    borderPane.setCenter(vBox);
                }
                if(e.getValue().contains("Критерий_")){
                    GridPane gridpane = new GridPane();
                    for (int i = 0; i < 4; i++) {
                        RowConstraints row = new RowConstraints(50);
                        gridpane.getRowConstraints().add(row);
                    }
                    for (int i = 0; i < 4; i++) {
                        for (int j = 0; j < 4; j++) {
                            if (i == 0 && j == 0) {
                                Label mButton = new Label(e.getValue());
                                mButton.setPadding(new Insets(5, 5, 5, 5));
                                gridpane.add(mButton, i, j);
                                continue;
                            }
                            if (i == 0) {
                                Label mButton = new Label("Альтернатива_" + j);
                                mButton.setPadding(new Insets(5, 5, 5, 5));
                                gridpane.add(mButton, i, j);
                                continue;

                            }
                            if (j == 0) {
                                Label mButton = new Label("Альтернатива_" + i);
                                mButton.setPadding(new Insets(5, 5, 5, 5));
                                gridpane.add(mButton, i, j);
                                continue;
                            }
                            if (i == j) {
                                continue;
                            }
                            JFXButton mButton = new JFXButton("Оценка ?" + "\n" + "Уверенность ?");

                            mButton.setWrapText(true);
                            mButton.setButtonType(JFXButton.ButtonType.RAISED);
                            mButton.setStyle(matrixCellButtonStyle);
                            mButton.setAlignment(Pos.CENTER);
                            mButton.setPadding(new Insets(0, 0, 0, 5));
                            GridPane.setHalignment(mButton, HPos.CENTER);
                            gridpane.add(mButton, i, j);
                        }
                    }
                    gridpane.setGridLinesVisible(true);
                    gridpane.setPadding(new Insets(5, 5, 5, 50));

                    TextArea tArea = new TextArea();
                    tArea.setText("Комментарий ...");
                    tArea.setPadding(new Insets(5));

                    VBox vBox = new VBox();
                    vBox.setSpacing(10);
                    vBox.getChildren().addAll(gridpane, tArea);
                    borderPane.setCenter(vBox);
                }
            }
        });
        return treeView;
    }

    public static class TreeViewEntity {

        private final SimpleStringProperty name;
        private final SimpleStringProperty group;

        private TreeViewEntity(String name, String department) {
            this.name = new SimpleStringProperty(name);
            this.group = new SimpleStringProperty(department);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String firstName) {
            name.set(firstName);
        }

        public String getGroup() {
            return group.get();
        }

        public void setGroup(String firstName) {
            group.set(firstName);
        }
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
