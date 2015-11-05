import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Java FX class that plots best path
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PathSimulator extends Application {

    private String fileName;

    private static final int X_MAX = 5;
    private static final int X_MIN = -5;
    private static final int Y_MAX = 6;
    private static final int Y_MIN = -6;
    private static final int RESOLUTION = 1;

    private TableView<Point> table = new TableView<Point>();
    private final ObservableList<Point> data =
        FXCollections.observableArrayList(
            new Point(0,0,1),
            new Point(0,0,2),
            new Point(0,0,3),
            new Point(0,0,4),
            new Point(0,0,5),
            new Point(0,0,6),
            new Point(0,0,7),
            new Point(0,0,8)
        );

    @Override
    public void start(Stage stage) {

        stage.setTitle("Path");
        final NumberAxis xAxis = new NumberAxis(X_MIN, X_MAX, RESOLUTION);
        final NumberAxis yAxis = new NumberAxis(Y_MIN, Y_MAX, RESOLUTION);
        final LineChart<Number,Number> lineChart = new
            LineChart<Number,Number>(xAxis,yAxis);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        xAxis.setLabel("Feet");
        yAxis.setLabel("Feet");
        lineChart.setTitle("Points");

        XYChart.Series pointSeries = new XYChart.Series();
        pointSeries.setName("Points Plotted");

        // Connect points in order that they were added
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        Scene scene  = new Scene(new Group());
        final VBox vbox = new VBox();
        final HBox hCommandBox = new HBox();

        final Button generatePoints = new Button("Generate Points");
        generatePoints.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                // TODO: Update graphics to chart points scatterplot
                List<Point> mPointList = PointGenerator
                    .generate(8, X_MIN + 1, X_MAX - 1, Y_MIN + 1, Y_MAX - 1);
                PointGenerator.writeToFile(mPointList, "pointsRandom.txt");
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

                List<Point> mBestPointList = (new PointSorter(
                    PathParser.parsePathFromFile("pointsRandom.txt")
                    .getPoints())).best().getPoints();

                data.clear();
                data.addAll(mBestPointList);

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

        table.setEditable(true);

        TableColumn xCol = new TableColumn("X");
        xCol.setMinWidth(50);
        xCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("x"));

        TableColumn yCol = new TableColumn("Y");
        yCol.setMinWidth(50);
        yCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("y"));

        TableColumn indexCol = new TableColumn("Index");
        indexCol.setMinWidth(50);
        indexCol.setCellValueFactory(
                new PropertyValueFactory<Point, Integer>("index"));

        table.setItems(data);
        table.getColumns().addAll(xCol, yCol, indexCol);

        final VBox vTableBox = new VBox();
        vTableBox.setSpacing(5);
        vTableBox.setPadding(new Insets(10, 10, 10, 10));
        vTableBox.getChildren().addAll(table);

        hCommandBox.setSpacing(10);
        hCommandBox.getChildren().addAll(generatePoints, calculatePath);

        vbox.getChildren().addAll(lineChart, vTableBox, hCommandBox);
        hCommandBox.setPadding(new Insets(10, 10, 10, 50));

        ((Group)scene.getRoot()).getChildren().add(vbox);
        scene.getStylesheets().add(getClass()
            .getResource("chart.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
