package program;

import model.Point;
import model.PointReal;
import util.LocationDataParser;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
/*
 * Java FX module that allows for conversion of raw location data
 * @author Kairi Kozuma
 * @version 1.0
 */
public class DataInputConverter {

    private final TextArea dataInput = new TextArea();

    private final Button parseDataBtn = new Button("Parse");
    private final Button plotPointsBtn = new Button("Plot");
    private final VBox tableBox = new VBox();

    private final VBox root = new VBox();

    private final HBox dataActBar = new HBox();
    private final TableView<PointReal> table = new TableView<PointReal>();

    private final ObservableList<Point> dataTheor;
    private final ObservableList<PointReal> dataExper = FXCollections.observableArrayList();
    private final PathGraph graphModule;

    public DataInputConverter(ObservableList<Point> dataTheoretical, PathGraph graphModule) {
        dataTheor = dataTheoretical;
        this.graphModule = graphModule;

        parseDataBtn.setOnAction((ActionEvent e) -> {
                dataExper.clear();
                List<PointReal> mPoints = LocationDataParser
                    .parseDataFromString(dataInput.getText(), true);
                dataExper.addAll(mPoints);
            }
        );

        plotPointsBtn.setOnAction((ActionEvent e) -> {
                graphModule.plotPointsExperimental(dataExper);
            }
        );

        // Set text area properties
        dataInput.setPrefHeight(100);
        dataInput.setPrefWidth(300);

        // Set table properties
        table.setEditable(false);
        table.setPrefWidth(300);
        table.setPrefHeight(600);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn xCol = new TableColumn("X");
        xCol.setMinWidth(30);
        xCol.setCellValueFactory(new Callback<CellDataFeatures<PointReal, String>,
            ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<PointReal, String> p) {
                    return Bindings.format("%.3f", p.getValue().getX());
            }
        });


        TableColumn yCol = new TableColumn("Y");
        yCol.setMinWidth(30);
        yCol.setCellValueFactory(new Callback<CellDataFeatures<PointReal, String>,
            ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<PointReal, String> p) {
                    return Bindings.format("%.3f", p.getValue().getY());
            }
        });

        TableColumn indexCol = new TableColumn("Index");
        indexCol.setMinWidth(30);
        indexCol.setCellValueFactory(new Callback<CellDataFeatures<PointReal, String>,
            ObservableValue<String>>() {
                public ObservableValue<String> call(CellDataFeatures<PointReal, String> p) {
                    return Bindings.format("%d", p.getValue().getIndex());
            }
        });

        table.setItems(dataExper);
        table.getColumns().addAll(xCol, yCol, indexCol);

        // Organize layouts
        tableBox.setSpacing(5);
        tableBox.setPadding(new Insets(10, 10, 10, 10));
        tableBox.getChildren().addAll(table);

        dataActBar.setSpacing(10);
        dataActBar.getChildren().addAll(parseDataBtn, plotPointsBtn);
        dataActBar.setPadding(new Insets(10, 10, 10, 10));

        root.getChildren().addAll(table, dataActBar, dataInput);
    }

    public Node getView() {
        return root;
    }
}
