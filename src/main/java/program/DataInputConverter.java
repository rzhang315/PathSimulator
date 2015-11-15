package program;

import model.Point;
import model.PointReal;
import util.LocationDataParser;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;
import model.Path;
import model.Point;
import util.PointGenerator;
import java.text.NumberFormat;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Spinner;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.beans.binding.Bindings;

/*
 * Java FX module that allows for conversion of raw location data
 * @author Kairi Kozuma
 * @version 1.0
 */
public class DataInputConverter {

    private final TextArea dataInput = new TextArea();

    private final Button parseDataBtn = new Button("Parse");

    private final HBox root = new HBox();
    private final TableView<PointReal> table = new TableView<PointReal>();

    private final ObservableList<Point> dataTheor;
    private final ObservableList<PointReal> dataExper = FXCollections.observableArrayList();

    public DataInputConverter(ObservableList<Point> data) {
        dataTheor = data;

        parseDataBtn.setOnAction((ActionEvent e) -> {
                dataExper.clear();
                List<PointReal> mPoints = LocationDataParser
                    .parseDataFromString(dataInput.getText());
                    System.out.println(mPoints);
                dataExper.addAll(mPoints);
            }
        );

        // Set text area properties
        dataInput.setPrefHeight(500);
        dataInput.setPrefWidth(200);

        // Set table properties
        table.setEditable(false);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn xCol = new TableColumn("X");
        xCol.setMinWidth(30);
        xCol.setCellValueFactory(
                new PropertyValueFactory<PointReal, Double>("x"));

        TableColumn yCol = new TableColumn("Y");
        yCol.setMinWidth(30);
        yCol.setCellValueFactory(
                new PropertyValueFactory<PointReal, Double>("y"));

        TableColumn indexCol = new TableColumn("Index");
        indexCol.setMinWidth(30);
        indexCol.setCellValueFactory(
                new PropertyValueFactory<PointReal, Integer>("index"));

        table.setItems(dataExper);
        table.getColumns().addAll(xCol, yCol, indexCol);

        root.setSpacing(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.getChildren().addAll(table, parseDataBtn, dataInput);
    }

    public HBox getView() {
        return root;
    }
}
