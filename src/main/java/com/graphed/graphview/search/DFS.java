package com.graphed.graphview.search;

import java.util.HashMap;
import com.graphed.graphview.GraphView;
import com.graphed.graphview.Vertex;
import com.graphed.graphview.animation.GraphAnimation;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class DFS extends GraphAnimation {
    private GraphView gv;
    // maps the id of the vertex to its position in the vertexList array for faster
    // access to the vertex given the id
    private HashMap<Integer, Integer> vertexMap;
    public ComboBox<Vertex> startingVertexPicker = new ComboBox<>();

    public DFS(GraphView gv) {
        Tooltip t = new Tooltip("Starting vertex");
        Tooltip.install(startingVertexPicker, t);
        startingVertexPicker.setItems(gv.vertexList);
        this.gv = gv;
        setName("Depth-first search");
        setDelay(1000);
    }

    @Override
    public Node[] params() {
        Node n[] = new Node[1];
        n[0] = startingVertexPicker;
        return n;
    }

    @Override
    public void play() {
        try {
            init();
            visit(startingVertexPicker.getSelectionModel().getSelectedItem().getId());
            delay();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        } finally {
            finish();
        }

    }

    // visits a vertex
    private void visit(int id) throws InterruptedException {
        gv.vertexList.get(vertexMap.get(id)).isVisited = true;
        delay();
        gv.vertexList.get(vertexMap.get(id)).setColor(Color.RED);
        gv.draw();

        for (Integer w : gv.neighbors(id)) {
            if (!gv.vertexList.get(vertexMap.get(w)).isVisited) {
                visit(w);
            }
        }
        delay();
        gv.vertexList.get(vertexMap.get(id)).setColor(Color.PURPLE);
        gv.draw();

    }

    // set the animation to the initial state
    public void init() {
        vertexMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < gv.vertexList.size(); i++) {
            gv.vertexList.get(i).isVisited = false;
            vertexMap.put(gv.vertexList.get(i).getId(), i);
            gv.vertexList.get(i).setDefaultColor();
        }
        gv.draw();
    }

    // resets the animation to the default state
    public void finish() {
        for (Vertex vertex : gv.vertexList) {
            vertex.isVisited = false;
            vertex.setDefaultColor();
        }
        gv.draw();
    }
}
