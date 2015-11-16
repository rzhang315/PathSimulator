package program;

import model.Point;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Java FX application that plots best path
 * @author Kairi Kozuma
 * @version 1.4
 */
public class PathSimulator extends Application {

    private PathGraph graphModule;
    private PointTable pointModule;
    private DataInputConverter converterModule;

    private final ObservableList<Point> data = FXCollections.observableArrayList();

    private final HBox root = new HBox();

    @Override
    public void start(Stage stage) {

        graphModule = new PathGraph(data);
        pointModule = new PointTable(data, graphModule);
        converterModule = new DataInputConverter(data, graphModule);

        root.getChildren().addAll(graphModule.getView(), pointModule.getView(), converterModule.getView());

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
