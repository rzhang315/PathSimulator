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

/**
 * Java FX class that plots best path
 * @author Kairi Kozuma
 * @version 1.1
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


    @Override
    public void start(Stage stage) {

        final NumberAxis xAxis = new NumberAxis(X_MIN, X_MAX, RESOLUTION);
        final NumberAxis yAxis = new NumberAxis(Y_MIN, Y_MAX, RESOLUTION);
        final LineChart<Number,Number> lineChart = new
            LineChart<Number,Number>(xAxis,yAxis);
        final Label totalLengthLabel = new Label("Distance: ");
        final Label totalAngleMaxLabel = new Label("Angle(Max): ");
        final Label totalAngleMinLabel = new Label("Angle(Min): ");
        final Text totalLength = new Text();
        final Text totalAngleMax = new Text();
        final Text totalAngleMin = new Text();

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        xAxis.setLabel("Feet");
        yAxis.setLabel("Feet");
        lineChart.setTitle("Points");
        lineChart.setLegendVisible(false);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        final XYChart.Series pointSeries = new XYChart.Series();
        pointSeries.setName("Points Plotted");

        // Create Spinner
        final Spinner<Integer> numPointsSpinner = new Spinner<Integer>(1, 12, 12, 1);

        final HBox root = new HBox();
        final HBox pointBtnBar = new HBox();
        final HBox graphBtnBar = new HBox();
        final HBox graphInfoBar = new HBox();
        final VBox graphModule = new VBox();
        final VBox pointModule = new VBox();

        final Button generatePoints = new Button("Random");
        generatePoints.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                mPointList = PointGenerator
                    .generate(numPointsSpinner.getValue(), X_MIN + 1, X_MAX - 1, Y_MIN + 1, Y_MAX - 1);
                // PointGenerator.writeToFile(mPointList, "pointsRandom.txt");
                data.clear();
                data.addAll(mPointList);

                XYChart.Series pointSeries = new XYChart.Series();
                for (Point p : mPointList) {
                    pointSeries.getData()
                        .add(new XYChart.Data(p.getX(), p.getY()));
                }

                // Clear chart
                while(lineChart.getData().size() > 0) {
                    lineChart.getData().remove(0);
                }
                lineChart.getData().addAll(pointSeries);
            }
        });

        final Button calculatePath = new Button("Calculate Path");
        calculatePath.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

                Path bestPath = new PermutationAlgorithm(mPointList).best();
                List<Point> mBestPointList = bestPath.getPoints();

                data.clear();
                data.addAll(mBestPointList);

                totalLength.setText(String.format("%.3f ft", bestPath.length()));
                totalAngleMax.setText(String.format("%.3f°", bestPath.angle()));
                totalAngleMin.setText(String.format("%.3f°", bestPath.angleSmallest()));

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
        });

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
        final VBox vTableBox = new VBox();
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

        pointBtnBar.setSpacing(10);
        pointBtnBar.getChildren().addAll(numPointsSpinner, generatePoints);
        pointBtnBar.setPadding(new Insets(10, 10, 10, 10));

        graphModule.getChildren().addAll(lineChart, graphInfoBar, graphBtnBar);
        pointModule.getChildren().addAll(table, pointBtnBar);

        root.getChildren().addAll(graphModule, pointModule);

        final Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass()
            .getResource("chart.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("Path");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
