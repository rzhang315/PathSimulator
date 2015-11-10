package program;

import model.Path;
import model.Point;
import algorithm.SortAlgorithm;
import algorithm.PermutationAlgorithm;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Spinner;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.beans.binding.Bindings;

/**
 * Java FX class that plots best path
 * @author Kairi Kozuma
 * @version 1.2
 */
public class PathSimulator extends Application {

    private static final int X_MAX = 5;
    private static final int X_MIN = -5;
    private static final int Y_MAX = 6;
    private static final int Y_MIN = -6;
    private static final int RESOLUTION = 1;

    private List<Point> mPointList;
    private SortAlgorithm mSortAlgorithm;

    private final TableView<Point> table = new TableView<Point>();
    private final ObservableList<Point> data =
        FXCollections.observableArrayList();

    private final NumberAxis xAxis = new NumberAxis(X_MIN, X_MAX, RESOLUTION);
    private final NumberAxis yAxis = new NumberAxis(Y_MIN, Y_MAX, RESOLUTION);
    private final LineChart<Number,Number> lineChart = new
        LineChart<Number,Number>(xAxis,yAxis);
    private final Label totalLengthLabel = new Label("Distance: ");
    private final Label totalAngleMaxLabel = new Label("Angle(Max): ");
    private final Label totalAngleMinLabel = new Label("Angle(Min): ");
    private final Text totalLength = new Text("           ft");
    private final Text totalAngleMax = new Text("             °");
    private final Text totalAngleMin = new Text("             °");

    final Spinner<Integer> numPointsSpinner = new Spinner<Integer>(1, 12, 12, 1);

    private final HBox root = new HBox();
    private final HBox pointActBar = new HBox();
    private final HBox pointGenBar = new HBox();
    private final HBox graphBtnBar = new HBox();
    private final HBox graphInfoBar = new HBox();
    private final VBox graphModule = new VBox();
    private final VBox pointModule = new VBox();
    private final VBox vTableBox = new VBox();

    @Override
    public void start(Stage stage) {

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        xAxis.setLabel("Feet");
        yAxis.setLabel("Feet");
        lineChart.setTitle("Points");
        lineChart.setLegendVisible(false);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        // TODO: make separate class for points and graph
        // TODO Delete individual points

        final Button clearPoints = new Button("Clear");
        clearPoints.setOnAction((ActionEvent e) -> {
                clearPath();
                data.clear();
            }
        );

        final Button generatePoints = new Button("Random");
        generatePoints.setOnAction((ActionEvent e) -> {
                mPointList = PointGenerator
                    .generate(numPointsSpinner.getValue(), X_MIN + 1, X_MAX - 1, Y_MIN + 1, Y_MAX - 1);
                clearPath();
                data.clear();
                data.addAll(mPointList);

                XYChart.Series pointSeries = new XYChart.Series();
                for (Point p : mPointList) {
                    pointSeries.getData()
                        .add(new XYChart.Data(p.getX(), p.getY()));
                }
                lineChart.getData().addAll(pointSeries);
            }
        );

        final Button calculatePath = new Button("Calculate Path");
        calculatePath.disableProperty().bind(Bindings.size(data).isEqualTo(0));
        calculatePath.setOnAction((ActionEvent e) -> {

                Path bestPath = new PermutationAlgorithm(mPointList).bestPath();
                List<Point> mBestPointList = bestPath.getPoints();

                data.clear();
                data.addAll(mBestPointList);

                totalLength.setText(String.format("%6.3fft", bestPath.length()));
                totalAngleMax.setText(String.format("%7.3f°", bestPath.angle()));
                totalAngleMin.setText(String.format("%7.3f°", bestPath.angleSmallest()));

                XYChart.Series pointSeries = new XYChart.Series();
                for (Point p : mBestPointList) {
                    pointSeries.getData()
                        .add(new XYChart.Data(p.getX(), p.getY()));
                }

                // Connect points in order that they were added
                if(lineChart.getData().size() > 1) {
                    lineChart.getData().remove(lineChart.getData().size() - 1);
                }
                lineChart.getData().addAll(pointSeries);
            }
        );

        // Set table properties
        table.setEditable(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Callback<TableColumn<String,Integer>,TableCell<String,Integer>> cellFactory =
            TextFieldTableCell.forTableColumn(new StringConverter<Integer>(){
                    @Override
                    public String toString(Integer object) {
                        return object.toString();
                    }

                    @Override
                    public Integer fromString(String string) {
                        return Integer.parseInt(string);
                }
            });


        TableColumn xCol = new TableColumn("X");
        xCol.setMinWidth(30);
        xCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("x"));
        xCol.setCellFactory(cellFactory);
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
        yCol.setCellFactory(cellFactory);
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
        indexCol.setCellFactory(cellFactory);
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

        graphBtnBar.setSpacing(10);
        graphBtnBar.getChildren().addAll(calculatePath);
        graphBtnBar.setPadding(new Insets(10, 10, 10, 50));

        graphInfoBar.setSpacing(10);
        graphInfoBar.getChildren().addAll(
            totalLengthLabel,
            totalLength,
            totalAngleMaxLabel,
            totalAngleMax,
            totalAngleMinLabel,
            totalAngleMin
        );
        graphInfoBar.setPadding(new Insets(10, 10, 10, 50));

        pointActBar.setSpacing(10);
        pointActBar.getChildren().addAll(clearPoints);
        pointActBar.setPadding(new Insets(10, 10, 10, 10));

        pointGenBar.setSpacing(10);
        pointGenBar.getChildren().addAll(numPointsSpinner, generatePoints);
        pointGenBar.setPadding(new Insets(10, 10, 10, 10));

        graphModule.getChildren().addAll(lineChart, graphInfoBar, graphBtnBar);
        pointModule.getChildren().addAll(table, pointActBar, pointGenBar);

        root.getChildren().addAll(graphModule, pointModule);

        final Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass()
            .getResource("chart.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Path");
        stage.show();
    }

    private void clearPath() {
        while(lineChart.getData().size() > 0) {
            lineChart.getData().remove(0);
        }
        totalLength.setText("           ft");
        totalAngleMax.setText("             °");
        totalAngleMin.setText("             °");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
