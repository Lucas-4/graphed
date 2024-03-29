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

package com.graphed.screens;

import java.io.File;

import com.graphed.graphview.GraphView;
import com.graphed.graphview.Vertex;
import com.graphed.graphview.Edge;
import com.graphed.graphview.animation.AnimationManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class Main extends GridPane {
    // GraphView
    public GraphView graphView;
    private boolean isDirected;
    private boolean isWeighted;

    // Vertices
    private ListView<Vertex> vertexListView;

    // Edges
    private ListView<Edge> edgeListView;

    // Left Pane
    private VBox leftBox = new VBox();
    private Button addVertexBtn = new Button("Add Vertex");
    private Button removeVertexBtn = new Button("Remove Vertex");
    private Button addEdgeBtn = new Button("Add Edge");
    private Button removeEdgeBtn = new Button("Remove Edge");
    private HBox edgeData = new HBox();
    private HBox edgeBtns = new HBox();
    private HBox vertexBtns = new HBox();
    private ComboBox<Vertex> verticesCB1 = new ComboBox<>();
    private ComboBox<Vertex> verticesCB2 = new ComboBox<>();
    private TextField weightTF = new TextField();

    // Middle Pane
    private VBox canvasContainer = new VBox();
    private Button layout = new Button("CL");
    private Button displayId = new Button("D");
    private HBox graphToolBar = new HBox();

    private ComboBox<String> animations = new ComboBox<String>();

    private HBox graphToolBar2 = new HBox();

    // Right Pane
    private VBox rightBox = new VBox();
    private ScrollPane graphDataSP = new ScrollPane();
    private Label numVerticesL = new Label();
    private Label numEdgesL = new Label();
    private VBox graphData = new VBox();

    public Main(
            boolean isDirected,
            boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        graphView = new GraphView(isDirected, isWeighted);
        build();
    }

    public Main(GraphView gv) {
        graphView = gv;
        this.isDirected = graphView.isDirected;
        this.isWeighted = graphView.isWeighted;
        gv.draw();
        build();
    }

    private void build() {
        updateVertexNumLabel();
        updateEdgeNumLabel();
        vertexListView = new ListView<>(graphView.vertexList);

        vertexBtns.setMaxWidth(Double.MAX_VALUE);
        vertexBtns.setSpacing(10);
        vertexBtns.getChildren().addAll(addVertexBtn, removeVertexBtn);

        edgeListView = new ListView<>(graphView.edgeList);

        verticesCB1.setItems(graphView.vertexList);

        verticesCB2.setItems(graphView.vertexList);
        edgeData.setSpacing(10);
        edgeData.getChildren().addAll(verticesCB1, verticesCB2);
        if (isWeighted) {
            edgeData.getChildren().add(weightTF);
        }
        edgeBtns.setSpacing(10);
        edgeBtns.getChildren().addAll(addEdgeBtn, removeEdgeBtn);

        leftBox.setAlignment(Pos.CENTER);
        leftBox.setSpacing(12);
        leftBox.getChildren().addAll(vertexListView, vertexBtns, edgeListView, edgeData,
                edgeBtns);
        leftBox.setPrefWidth(200);

        graphDataSP.setMinHeight(200);
        rightBox.getChildren().add(graphDataSP);
        rightBox.setAlignment(Pos.CENTER);
        graphData.getStyleClass().add("graph-data");
        graphData.getChildren().addAll(numVerticesL, numEdgesL);
        graphDataSP.setContent(graphData);
        graphDataSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        graphToolBar.getChildren().addAll(layout, displayId);
        graphToolBar.setSpacing(10);
        graphToolBar.setStyle("-fx-background-color:rgb(40, 40, 40)");
        graphToolBar.setMaxWidth(graphView.canvas.getWidth());

        AnimationManager am = new AnimationManager(graphView);
        graphToolBar2.getChildren().addAll(am.getControls());
        graphToolBar2.setSpacing(10);
        graphToolBar2.setStyle("-fx-background-color:rgb(40, 40, 40)");
        graphToolBar2.setMaxWidth(graphView.canvas.getWidth());

        canvasContainer.setAlignment(Pos.CENTER);
        canvasContainer.setStyle("-fx-background-color:49, 49, 49");
        canvasContainer.getChildren().setAll(graphToolBar, graphView.canvas, graphToolBar2);

        GridPane.setRowIndex(leftBox, 0);
        GridPane.setRowIndex(canvasContainer, 0);
        GridPane.setRowIndex(rightBox, 0);
        GridPane.setColumnIndex(leftBox, 0);
        GridPane.setColumnIndex(canvasContainer, 1);
        GridPane.setColumnIndex(rightBox, 2);
        GridPane.setVgrow(leftBox, Priority.ALWAYS);
        setWidth(Double.MAX_VALUE);
        ColumnConstraints cc1 = new ColumnConstraints();
        ColumnConstraints cc2 = new ColumnConstraints();
        ColumnConstraints cc3 = new ColumnConstraints();
        cc1.setPercentWidth(15);
        cc2.setPercentWidth(70);
        cc3.setPercentWidth(15);
        getColumnConstraints().addAll(cc1, cc2, cc3);
        setMaxHeight(Double.MAX_VALUE);
        getChildren().addAll(leftBox, canvasContainer, rightBox);

        addVertexBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                graphView.addVertex();
                updateVertexNumLabel();
            }
        });
        removeVertexBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (vertexListView.getSelectionModel().getSelectedItem() != null) {
                    graphView.removeVertex(vertexListView.getSelectionModel().getSelectedItem().getId());
                }
                updateVertexNumLabel();
                updateEdgeNumLabel();
            }
        });
        addEdgeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int id1, id2;
                id1 = verticesCB1.getSelectionModel().getSelectedItem().getId();
                id2 = verticesCB2.getSelectionModel().getSelectedItem().getId();
                if (isWeighted) {
                    double edgeWeight = Double.parseDouble(weightTF.getCharacters().toString());
                    graphView.addEdge(id1, id2, edgeWeight);
                } else {
                    graphView.addEdge(id1, id2);
                }

                updateEdgeNumLabel();
            }
        });
        removeEdgeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String s[] = edgeListView.getSelectionModel().getSelectedItem().toString().split("-");
                int id1 = Integer.parseInt(s[0]);
                int id2 = Integer.parseInt(s[1].split("w:")[0].trim());

                graphView.removeEdge(id1, id2);
                updateEdgeNumLabel();
            }
        });
        layout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                graphView.generateLayout();
            }
        });
        displayId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                graphView.toggleDisplayId();
            }
        });

    }

    public void updateVertexNumLabel() {
        numVerticesL.setText("Number of vertices: " + graphView.vertexList.size());
    }

    public void updateEdgeNumLabel() {
        numEdgesL.setText("Number of edges: " + graphView.edgeList.size());
    }

    public void save(File file) {
        graphView.save(file);
    }
}
