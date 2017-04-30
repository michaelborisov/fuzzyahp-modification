package sample;

import com.jfoenix.controls.JFXButton;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    HBox buttonBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXButton addButton = new JFXButton("+");
        addButton.setButtonType(JFXButton.ButtonType.RAISED);
        addButton.setStyle(addButtonStyle);
        addButton.getStyleClass().addAll("animated-option-button", "animated-option-sub1-button");

        buttonBox.getChildren().add(addButton);
    }

    private static String addButtonStyle =
            "    -fx-pref-width: 50px;\n" +
            "    -fx-background-color: #0066FF;\n" +
            "    -fx-background-radius: 50px;\n" +
            "    -fx-pref-height: 50px;\n" +
            "    -fx-text-fill: white;\n" +
            "    -fx-border-color: white;\n" +
            "    -fx-border-radius: 50px;\n" +
            "    -fx-border-width: 4px;" +
            "    -fx-font: 20 arial";
}
