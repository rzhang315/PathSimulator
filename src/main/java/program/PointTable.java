package program;

import model.Path;
import model.Point;
import util.PointGenerator;
import util.PointParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;

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

    private final TextArea dataInput = new TextArea();

    private final TableView<Point> table = new TableView<Point>();
    private final Spinner<Integer> numPointsSpinner = new Spinner<Integer>(1, 12, 12, 1);

    private final TextField xinput = new TextField();
    private final TextField yinput = new TextField();
    private final HBox pointAddBar = new HBox();
    private final HBox pointActBar = new HBox();
    private final HBox pointGenBar = new HBox();
    private final VBox tableBox = new VBox();

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

        numPointsSpinner.setPrefWidth(150);

        // TODO Delete individual points

        final Button clearPoints = new Button("Clear");
        clearPoints.setOnAction((ActionEvent e) -> {
                graphModule.clear();
                data.clear();
            }
        );

        // TODO: Bindings to enable or disable based on number of points
        // final Button deletePoint = new Button("Delete");
        // deletePoint.setOnAction((ActionEvent e) -> {
        //         if (data.size() != 0) {
        //             data.remove(data.size() - 1);
        //         }
        //     }
        // );

        // TODO: bind the series graph to the table for automatic updates
        final Button addPoint = new Button("Add");
        addPoint.setOnAction((ActionEvent e) -> {
                data.add(new Point(Integer.parseInt(xinput.getText()), Integer.parseInt(yinput.getText()), data.size() + 1));
                graphModule.plotPoints();
                xinput.setText("");
                yinput.setText("");
            }
        );

        // Set text area properties
        dataInput.setPrefHeight(100);
        dataInput.setPrefWidth(300);

        final Button parseDataBtn = new Button("Parse");
        parseDataBtn.setOnAction((ActionEvent e) -> {
                // Clear points
                data.clear();

                // Parse data
                data.addAll(PointParser.parsePointsFromString(dataInput.getText()));

                // Plot data
                graphModule.plotPoints();

            }
        );

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
                graphModule.plotPoints();
            }
        );

        // Set table properties
        table.setEditable(true);
        table.setPrefWidth(300);
        table.setPrefHeight(600);
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
        yCol.setCellValueFactory(new PropertyValueFactory<Point, Integer>("y"));
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
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 10, 10, 10));
        tableBox.getChildren().addAll(table);

        pointAddBar.setSpacing(10);
        pointAddBar.getChildren().addAll(xinput, yinput, addPoint);
        pointAddBar.setPadding(new Insets(10, 10, 10, 10));

        pointActBar.setSpacing(10);
        pointActBar.getChildren().addAll(dataInput, parseDataBtn);
        pointActBar.setPadding(new Insets(10, 10, 10, 10));

        pointGenBar.setSpacing(10);
        pointGenBar.getChildren().addAll(numPointsSpinner, generatePoints, clearPoints);
        pointGenBar.setPadding(new Insets(10, 10, 10, 10));

        root.getChildren().addAll(table, pointAddBar, pointActBar, pointGenBar);
    }

    /**
     * Get the root view for the module
     * @return VBox root view
     */
    public Node getView() {
        return root;
    }
}
