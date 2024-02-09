package com.graphed.graphview.search;

import java.util.HashMap;
import com.graphed.graphview.GraphView;
import javafx.scene.paint.Color;

public class DFS extends Thread {
    private GraphView gv;
    // maps the id of the vertex to its position in the vertexList array for faster
    // access to the vertex given the id
    HashMap<Integer, Integer> vertexMap;

    public DFS(GraphView gv) {
        this.gv = gv;
        vertexMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < gv.vertexList.size(); i++) {
            gv.vertexList.get(i).isVisited = false;
            vertexMap.put(gv.vertexList.get(i).getId(), i);
        }

    }

    private void wait(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        visit(gv.vertexList.get(0).getId());
        wait(1000);
        for (int i = 0; i < gv.vertexList.size(); i++) {
            gv.vertexList.get(i).setDefaultColor();
        }
        gv.draw();
    }

    // visits a vertex
    private void visit(int id) {
        gv.vertexList.get(vertexMap.get(id)).isVisited = true;
        wait(1000);
        gv.vertexList.get(vertexMap.get(id)).setColor(Color.RED);
        gv.draw();

        for (Integer w : gv.neighbors(id)) {
            if (!gv.vertexList.get(vertexMap.get(w)).isVisited) {
                visit(w);
            }
        }

        wait(1000);
        gv.vertexList.get(vertexMap.get(id)).setColor(Color.PURPLE);
        gv.draw();

    }
}
