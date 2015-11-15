package program;

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
 * Java FX module that allows for user input and generation of points
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PointTable {

    private static final int X_MAX = 5;
    private static final int X_MIN = -5;
    private static final int Y_MAX = 6;
    private static final int Y_MIN = -6;
    private static final int RESOLUTION = 1;

    private final TableView<Point> table = new TableView<Point>();
    private final Spinner<Integer> numPointsSpinner = new Spinner<Integer>(1, 12, 12, 1);

    private final TextField xinput = new TextField();
    private final TextField yinput = new TextField();
    private final HBox pointAddBar = new HBox();
    private final HBox pointActBar = new HBox();
    private final HBox pointGenBar = new HBox();
    private final VBox vTableBox = new VBox();

    private final VBox root = new VBox();

    private final ObservableList<Point> data;
    private final PathGraph graphModule;

    /**
     * Construct new PointTable module
     * @param   data of Points
     * @param   graphModule to plot the points
     */
    public PointTable(ObservableList<Point> data, PathGraph graphModule) {

        this.data = data;
        this.graphModule = graphModule;

        // TODO: make separate class for points and graph
        // TODO Delete individual points

        final Button clearPoints = new Button("Clear");
        clearPoints.setOnAction((ActionEvent e) -> {
                graphModule.clear();
                data.clear();
            }
        );

        // final Button deletePoint = new Button("Delete");
        // deletePoint.setOnAction((ActionEvent e) -> {
        //
        //         // Remove on element
        //     }
        // );

        // TODO: Automatically bind x and y values in plot to chart
        final Button plotPoints = new Button("Plot");
        plotPoints.setOnAction((ActionEvent e) -> {
                graphModule.update();
            }
        );

        final Button addPoint = new Button("Add");
        addPoint.setOnAction((ActionEvent e) -> {
                data.add(new Point(Integer.parseInt(xinput.getText()), Integer.parseInt(yinput.getText()), data.size() + 1));
                graphModule.update();
                xinput.setText("");
                yinput.setText("");
            }
        );

        TextField textField = new TextField();

        TextFormatter<String> xformatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^-][^0-9]", ""));
            return change;
        });
        TextFormatter<String> yformatter = new TextFormatter<String>( change -> {
            change.setText(change.getText().replaceAll("[^-][^0-9]", ""));
            return change;
        });
        xinput.setTextFormatter(xformatter);
        xinput.setPrefWidth(80);
        xinput.setPromptText("x value");
        yinput.setTextFormatter(yformatter);
        yinput.setPrefWidth(80);
        yinput.setPromptText("y value");

        final Button generatePoints = new Button("Random");
        generatePoints.setOnAction((ActionEvent e) -> {
                data.clear();
                data.addAll(PointGenerator.generate(numPointsSpinner.getValue()
                    , X_MIN + 1, X_MAX - 1, Y_MIN + 1, Y_MAX - 1));
                graphModule.update();
            }
        );


        // Set table properties
        table.setEditable(true);
        table.setPrefHeight(500);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn xCol = new TableColumn("X");
        xCol.setMinWidth(30);
        xCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("x"));
        xCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Point, Integer>>() {
                @Override
                public void handle(CellEditEvent<Point, Integer> t) {
                    ((Point) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setX(t.getNewValue());
                }
             }
        );


        TableColumn yCol = new TableColumn("Y");
        yCol.setMinWidth(30);
        yCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("y"));
        yCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Point, Integer>>() {
                @Override
                public void handle(CellEditEvent<Point, Integer> t) {
                    ((Point) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setY(t.getNewValue());
                }
             }
        );


        TableColumn indexCol = new TableColumn("Index");
        indexCol.setMinWidth(30);
        indexCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("index"));
        indexCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Point, Integer>>() {
                @Override
                public void handle(CellEditEvent<Point, Integer> t) {
                    ((Point) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setIndex(t.getNewValue());
                }
             }
        );

        table.setItems(data);
        table.getColumns().addAll(xCol, yCol, indexCol);

        // Organize layouts
        vTableBox.setSpacing(5);
        vTableBox.setPadding(new Insets(10, 10, 10, 10));
        vTableBox.getChildren().addAll(table);

        pointAddBar.setSpacing(10);
        pointAddBar.getChildren().addAll(xinput, yinput, addPoint);
        pointAddBar.setPadding(new Insets(10, 10, 10, 10));

        pointActBar.setSpacing(10);
        pointActBar.getChildren().addAll(clearPoints, plotPoints);
        pointActBar.setPadding(new Insets(10, 10, 10, 10));

        pointGenBar.setSpacing(10);
        pointGenBar.getChildren().addAll(numPointsSpinner, generatePoints);
        pointGenBar.setPadding(new Insets(10, 10, 10, 10));

        root.getChildren().addAll(table, pointAddBar, pointActBar, pointGenBar);
    }

    /**
     * Get the root view for the module
     * @return VBox root view
     */
    public VBox getView() {
        return root;
    }
}
