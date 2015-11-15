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
        converterModule = new DataInputConverter(data);

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
