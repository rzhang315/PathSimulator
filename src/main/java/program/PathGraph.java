package program;

import model.Path;
import model.Point;
import model.PointReal;
import algorithm.SortAlgorithm;
import algorithm.PermutationAlgorithm;
import algorithm.ClosestPointPermutationAlgorithm;
import util.Stopwatch;
import java.util.List;
import java.util.ArrayList;
import java.text.NumberFormat;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.Label;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;

/*
 * Java FX module that allows for visualization and sorting of points
 * @author Kairi Kozuma
 * @version 1.0
 */
public class PathGraph {
    private static final int X_MAX = 5;
    private static final int X_MIN = -5;
    private static final int Y_MAX = 6;
    private static final int Y_MIN = -6;
    private static final int RESOLUTION = 1;

    private final NumberAxis xAxis = new NumberAxis(X_MIN, X_MAX, RESOLUTION);
    private final NumberAxis yAxis = new NumberAxis(Y_MIN, Y_MAX, RESOLUTION);
    private final LineChart<Number,Number> lineChart = new
        LineChart<Number,Number>(xAxis,yAxis);
    private final Label totalLengthLabel = new Label("Distance: ");
    private final Label totalAngleMaxLabel = new Label("Angle(Max): ");
    private final Label totalAngleMinLabel = new Label("Angle(Min): ");
    private final Label totalTimeLabel = new Label("Time: ");
    private final Label totalPathCheckedLabel = new Label("Checked Paths: ");
    private final Text totalLength = new Text("           ft");
    private final Text totalAngleMax = new Text("             °");
    private final Text totalAngleMin = new Text("             °");
    private final Text totalTime = new Text("          s");
    private final Label totalPathChecked = new Label("      ");

    private final HBox graphBtnBar = new HBox();
    private final HBox graphInfoBar = new HBox();
    private final HBox algInfoBar = new HBox();
    private final VBox root = new VBox();

    // TODO: Decide on limits dynamically
    private final Label numBranchLabel = new Label("Branches");
    private final Spinner<Integer> numBranchSpinner
        = new Spinner<Integer>(1, 6, 3, 1);

    private final Label numIteartionLabel = new Label("Iterations");
    private final Spinner<Integer> numIterationSpinner
        = new Spinner<Integer>(1, 12, 4, 1);


    final Button calculatePath1 = new Button("Permutate");
    final Button calculatePath2 = new Button("ClosestPointPerm");

    private final ObservableList<Point> data;

    /**
     * Construct PathGraph module
     * @param  data ObservableList of Point
     */
    public PathGraph(ObservableList<Point> data) {
        this.data = data;

        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(true);
        lineChart.setPrefHeight(650);
        lineChart.setPrefWidth(600);
        xAxis.setLabel("Feet");
        yAxis.setLabel("Feet");
        lineChart.setTitle("Points");
        lineChart.setLegendVisible(false);
        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);

        calculatePath1.disableProperty().bind(Bindings.size(data).isEqualTo(0));
        calculatePath1.setOnAction((ActionEvent e) -> {
                startAlgorithm(new PermutationAlgorithm(data));
            }
        );

        calculatePath2.disableProperty().bind(Bindings.size(data).isEqualTo(0));
        calculatePath2.setOnAction((ActionEvent e) -> {
                startAlgorithm(new ClosestPointPermutationAlgorithm(
                    data, numBranchSpinner.getValue(), numIterationSpinner.getValue()));
            }
        );

        numBranchSpinner.setPrefWidth(70);
        numIterationSpinner.setPrefWidth(70);

        List<Button> calcPathButtons = new ArrayList<Button>();
        calcPathButtons.add(calculatePath1);
        calcPathButtons.add(calculatePath2);

        graphBtnBar.setSpacing(10);
        graphBtnBar.getChildren().addAll(calcPathButtons);
        graphBtnBar.getChildren().addAll(
            numBranchLabel,
            numBranchSpinner,
            numIteartionLabel,
            numIterationSpinner
        );
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

        algInfoBar.setSpacing(10);
        algInfoBar.getChildren().addAll(
            totalTimeLabel,
            totalTime,
            totalPathCheckedLabel,
            totalPathChecked
        );
        algInfoBar.setPadding(new Insets(10, 10, 10, 50));

        root.getChildren()
            .addAll(lineChart, graphInfoBar, algInfoBar, graphBtnBar);
    }

    /**
     * Clear the points on the graph
     */
    public void clear() {
        lineChart.getData().clear();
        totalLength.setText("           ft");
        totalAngleMax.setText("             °");
        totalAngleMin.setText("             °");
    }

    /**
     * Clear the experimental points
     */
    public void clearExperimental() {
        clear("Experimental");
    }

    /**
     * Clear best path
     */
    public void clearBest() {
        clear("Best");
    }

    private void clear(String seriesName) {
        XYChart.Series pointSeries = new XYChart.Series();
        for(XYChart.Series series : lineChart.getData()) {
            if(series.getName().equals(seriesName)) {
                lineChart.getData().remove(series);
            };
        }
    }

    /**
     * Update the points on the graph
     */
    public void plotPoints() {
        clear();
        XYChart.Series pointSeries = new XYChart.Series();
        for (Point p : data) {
            pointSeries.getData()
                .add(new XYChart.Data(p.getX(), p.getY()));
        }
        lineChart.getData().add(pointSeries);
        pointSeries.setName("Points");
        pointSeries.getNode().setStyle("-fx-stroke: transparent; -fx-background-color: green, white;");
    }

    /**
     * Update the points on the graph
     */
    public void plotPointsExperimental(List<PointReal> points) {
        XYChart.Series pointSeries = new XYChart.Series();
        for (PointReal p : points) {
            pointSeries.getData()
                .add(new XYChart.Data(p.getX(), p.getY()));
        }
        lineChart.getData().add(pointSeries);
        pointSeries.setName("Experimental");
        pointSeries.getNode().setStyle("-fx-stroke: green; -fx-background-color: green, white;");
    }

    /**
     * Plot the points given in a path
     * @param mBestPointList list of points to plot in a path
     */
    private void plotPath(List<Point> mBestPointList) {
        data.clear();
        data.addAll(mBestPointList);

        XYChart.Series pointSeries = new XYChart.Series();
        for (Point p : mBestPointList) {
            pointSeries.getData()
                .add(new XYChart.Data(p.getX(), p.getY()));
        }

        // Connect points in order that they were added
        lineChart.getData().clear();
        pointSeries.setName("Best");
        lineChart.getData().add(pointSeries);

        pointSeries.getNode().setStyle("-fx-stroke: orange; -fx-background-color: orange, white;");
    }

    /**
     * Get the root view of the module
     * @return VBox root view
     */
    public Node getView() {
        return root;
    }

    /**
     * Use the sort algorithm to sort points and update data when finished
     * @param mAlgorithm algorithm used to sort points
     */
    private void startAlgorithm(SortAlgorithm mAlgorithm) {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Path bestPath = mAlgorithm.bestPath();
        stopwatch.stop();
        totalTime.setText(String.format("%6.3fs", stopwatch.elapsedSeconds()));
        totalPathChecked.setText(
            NumberFormat.getInstance().format(mAlgorithm.getNumPath()));

        totalLength.setText(String.format("%6.3fft", bestPath.length()));
        totalAngleMax.setText(String.format("%7.3f°", bestPath.angle()));
        totalAngleMin.setText(String.format("%7.3f°", bestPath.angleSmallest()));
        List<Point> mBestPointList = bestPath.getPoints();
        plotPath(mBestPointList);
    }
}
