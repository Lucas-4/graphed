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

package com.graph.graphview;

import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class GraphView {
    public ArrayList<Edge> edgeList = new ArrayList<>();
    public ArrayList<Vertex> vertexList = new ArrayList<>();
    public Canvas canvas = new Canvas(1000, 800);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private Vertex selectedVertex;
    private Color backgroundColor = Color.rgb(20, 20, 20);
    private boolean isDirected;
    private boolean isWeighted;
    private int vertexIDCounter = 0;
    public Double lastX;
    public Double lastY;

    public GraphView(boolean isDirected, boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;

        canvas.setOnMousePressed(this::handlePressed);
        canvas.setOnMouseDragged(this::handleDrag);
        canvas.setOnMouseReleased(this::handleRelease);
        canvas.setOnMouseMoved(this::handleMove);
        canvas.setOnScroll(this::handleScroll);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    // returns the ids of all the vertices in the graph view
    public ArrayList<Integer> getVertexIds() {
        ArrayList<Integer> vertexIds = new ArrayList<Integer>();
        for (int i = 0; i < vertexList.size(); i++) {
            vertexIds.add(vertexList.get(i).getId());
        }
        return vertexIds;
    }

    public ArrayList<String> getEdgePairs() {
        ArrayList<String> edgePairs = new ArrayList<String>();
        for (int i = 0; i < edgeList.size(); i++) {
            String s = edgeList.get(i).toString();
            edgePairs.add(s);
        }
        return edgePairs;
    }

    public void addVertex() {

        Vertex v = new Vertex(vertexIDCounter);
        vertexIDCounter++;
        vertexList.add(v);
        v.setPos(v.getId() * 50, v.getId() * 50);
        draw();
    }

    public void removeVertex(int id) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).getId() == id) {
                vertexList.remove(i);
            }
        }
        for (int i = edgeList.size() - 1; i >= 0; i--) {
            if (edgeList.get(i).v1.getId() == id || edgeList.get(i).v2.getId() == id) {
                edgeList.remove(i);
            }
        }
        draw();
    }

    public void addEdge(int id1, int id2) {
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();

        if (isDirected) {
            for (int i = 0; i < edgeList.size(); i++) {
                // checks if edge already exists
                if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)) {
                    return;
                }
            }
            // gets the vertices with the specified ids
            for (int i = 0; i < vertexList.size(); i++) {
                if (vertexList.get(i).getId() == id1) {
                    v1 = vertexList.get(i);
                }
                if (vertexList.get(i).getId() == id2) {
                    v2 = vertexList.get(i);
                }
            }
            edgeList.add(new DirectedEdge(v1, v2));
            draw();
            return;
        }

        for (int i = 0; i < edgeList.size(); i++) {
            if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)
                    || (edgeList.get(i).v1.getId() == id2 && edgeList.get(i).v2.getId() == id1)) {
                return;
            }
        }
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).getId() == id1) {
                v1 = vertexList.get(i);
            }
            if (vertexList.get(i).getId() == id2) {
                v2 = vertexList.get(i);
            }
        }

        edgeList.add(new Edge(v1, v2));
        draw();
    }

    public void addEdge(int id1, int id2, double weight) {
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();

        if (isDirected) {
            for (int i = 0; i < edgeList.size(); i++) {
                // checks if edge already exists
                if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)) {
                    return;
                }
            }
            // get the vertices with the specified ids
            for (int i = 0; i < vertexList.size(); i++) {
                if (vertexList.get(i).getId() == id1) {
                    v1 = vertexList.get(i);
                }
                if (vertexList.get(i).getId() == id2) {
                    v2 = vertexList.get(i);
                }
            }
            edgeList.add(new DirectedEdge(v1, v2, weight));
            draw();
            return;
        }

        for (int i = 0; i < edgeList.size(); i++) {
            if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)
                    || (edgeList.get(i).v1.getId() == id2 && edgeList.get(i).v2.getId() == id1)) {
                return;
            }
        }
        for (int i = 0; i < vertexList.size(); i++) {
            if (vertexList.get(i).getId() == id1) {
                v1 = vertexList.get(i);
            }
            if (vertexList.get(i).getId() == id2) {
                v2 = vertexList.get(i);
            }
        }

        edgeList.add(new Edge(v1, v2, weight));
        draw();
    }

    public void removeEdge(int id1, int id2) {
        if (isDirected) {
            for (int i = 0; i < edgeList.size(); i++) {
                Edge e = edgeList.get(i);
                if (e.v1.getId() == id1 && e.v2.getId() == id2) {
                    edgeList.remove(i);
                }
            }
            draw();
            return;
        }
        for (int i = 0; i < edgeList.size(); i++) {
            Edge e = edgeList.get(i);
            if ((e.v1.getId() == id1 && e.v2.getId() == id2) || (e.v1.getId() == id2 && e.v2.getId() == id1)) {
                edgeList.remove(i);
            }
        }
        draw();
    }

    // draws the vertices and edges on the canvas
    public void draw() {
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = 0; i < edgeList.size(); i++) {
            Edge e1 = edgeList.get(i);
            e1.draw(gc);
        }

        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v0 = vertexList.get(i);
            v0.draw(gc);
        }
    }

    public void handlePressed(MouseEvent arg0) {
        if (arg0.getButton().equals(MouseButton.SECONDARY)) {
            lastX = arg0.getX();
            lastY = arg0.getY();
            return;
        }
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            if (arg0.getX() >= v.posX - 20
                    && arg0.getX() <= v.posX + 20 && arg0.getY() >= v.posY - 20 && arg0.getY() <= v.posY + 20) {
                selectedVertex = vertexList.get(i);
            }
        }

    }

    public void handleDrag(MouseEvent arg0) {
        double shiftX, shiftY;
        if (arg0.getButton().equals(MouseButton.SECONDARY)) {
            shiftX = arg0.getX() - lastX;
            shiftY = arg0.getY() - lastY;
            for (int i = 0; i < vertexList.size(); i++) {
                Vertex v = vertexList.get(i);
                v.setPos(v.posX + shiftX, v.posY + shiftY);
            }
            lastX = arg0.getX();
            lastY = arg0.getY();
            draw();
            return;
        }
        if (selectedVertex == null)
            return;
        selectedVertex.posX = (int) arg0.getX();
        selectedVertex.posY = (int) arg0.getY();
        draw();
    }

    public void handleRelease(MouseEvent arg0) {
        selectedVertex = null;
        lastX = null;
        lastY = null;
    }

    public void handleMove(MouseEvent arg0) {

        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            if (arg0.getX() >= v.posX - Vertex.radius
                    && arg0.getX() <= v.posX + Vertex.radius && arg0.getY() >= v.posY - Vertex.radius
                    && arg0.getY() <= v.posY + Vertex.radius) {
                canvas.setCursor(Cursor.HAND);
                return;
            }
            canvas.setCursor(Cursor.DEFAULT);

        }
    }

    public void handleScroll(ScrollEvent e) {
        double scroll = e.getDeltaY();
        if (scroll < 0) {
            Vertex.radius /= 1.1;
        } else {
            Vertex.radius *= 1.1;
        }
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            // zoom out
            if (scroll < 0) {
                v.posX /= 1.1;
                v.posY /= 1.1;
            } else {// zoom in
                v.posX *= 1.1;
                v.posY *= 1.1;
            }
        }
        draw();
    }

}
