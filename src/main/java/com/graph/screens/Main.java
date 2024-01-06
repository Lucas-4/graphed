/*     
    Copyright (C) 2024  Lucas Dias Borges <diaslucas8822@gmail.com>

    Graphed is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Graphed is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.graph.screens;

import com.graph.graphview.GraphView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;

public class Main extends GridPane {
    private GraphView graphView;

    private ObservableList<Integer> vertexList;
    private ListView<Integer> vertexListView;

    private ObservableList<String> edgeList;
    private ListView<String> edgeListView;

    private VBox controlBox = new VBox();
    private Button addVertexBtn = new Button("Add Vertex");
    private Button removeVertexBtn = new Button("Remove Vertex");
    private Button addEdgeBtn = new Button("Add Edge");
    private Button removeEdgeBtn = new Button("Remove Edge");
    private HBox edgeData = new HBox();
    private HBox edgeBtns = new HBox();
    private HBox vertexBtns = new HBox();
    private ComboBox<Integer> verticesCB1 = new ComboBox<Integer>();
    private ComboBox<Integer> verticesCB2 = new ComboBox<Integer>();
    private TextField weightTF = new TextField();
    private boolean isDirected;
    private boolean isWeighted;

    public Main(boolean isDirected, boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;

        graphView = new GraphView(isDirected, isWeighted);

        vertexList = FXCollections.observableArrayList(graphView.getVertexIds());
        vertexListView = new ListView<Integer>(vertexList);

        vertexBtns.setMaxWidth(Double.MAX_VALUE);
        vertexBtns.setSpacing(10);
        vertexBtns.getChildren().addAll(addVertexBtn, removeVertexBtn);

        edgeList = FXCollections.observableArrayList(graphView.getEdgePairs());
        edgeListView = new ListView<String>(edgeList);

        verticesCB1.setItems(vertexList);

        verticesCB2.setItems(vertexList);
        edgeData.setSpacing(10);
        edgeData.getChildren().addAll(verticesCB1, verticesCB2);
        if (isWeighted) {
            edgeData.getChildren().add(weightTF);
        }
        edgeBtns.setSpacing(10);
        edgeBtns.getChildren().addAll(addEdgeBtn, removeEdgeBtn);

        controlBox.setAlignment(Pos.CENTER);
        controlBox.setSpacing(12);
        controlBox.getChildren().addAll(vertexListView, vertexBtns, edgeListView, edgeData,
                edgeBtns);
        controlBox.setPrefWidth(200);

        GridPane.setRowIndex(controlBox, 0);
        GridPane.setRowIndex(graphView.canvas, 0);

        GridPane.setColumnIndex(controlBox, 0);
        GridPane.setColumnIndex(graphView.canvas, 1);
        GridPane.setVgrow(controlBox, Priority.ALWAYS);
        GridPane.setVgrow(graphView.canvas, Priority.ALWAYS);
        setWidth(Double.MAX_VALUE);
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        cc1.setPercentWidth(15);
        cc2.setPercentWidth(70);
        cc3.setPercentWidth(15);
        getColumnConstraints().addAll(cc1, cc2, cc3);
        setMaxHeight(Double.MAX_VALUE);
        GridPane.setHalignment(graphView.canvas, HPos.CENTER);
        getChildren().addAll(controlBox, graphView.canvas);
        addVertexBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                graphView.addVertex();
                vertexList.setAll(graphView.getVertexIds());
            }
        });
        removeVertexBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vertexListView.getSelectionModel().getSelectedItem() != null) {
                    graphView.removeVertex(vertexListView.getSelectionModel().getSelectedItem());
                    vertexList.setAll(graphView.getVertexIds());
                    edgeList.setAll(graphView.getEdgePairs());
                }

            }
        });
        addEdgeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id1, id2;
                id1 = verticesCB1.getSelectionModel().getSelectedItem();
                id2 = verticesCB2.getSelectionModel().getSelectedItem();
                if (isWeighted) {
                    double edgeWeight = Double.parseDouble(weightTF.getCharacters().toString());
                    graphView.addEdge(id1, id2, edgeWeight);
                } else {
                    graphView.addEdge(id1, id2);
                }

                edgeList.setAll(graphView.getEdgePairs());
            }
        });
        removeEdgeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String s[] = edgeListView.getSelectionModel().getSelectedItem().toString().split("-");
                int id1 = Integer.parseInt(s[0]);
                int id2 = Integer.parseInt(s[1].split("w:")[0].trim());

                graphView.removeEdge(id1, id2);
                edgeList.setAll(graphView.getEdgePairs());
            }
        });

    }

}
