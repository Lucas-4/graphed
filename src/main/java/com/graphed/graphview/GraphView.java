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

package com.graphed.graphview;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

public class GraphView {
    public ObservableList<Edge> edgeList = FXCollections.observableArrayList();
    public ObservableList<Vertex> vertexList = FXCollections.observableArrayList();
    public Canvas canvas = new Canvas(1000, 800);
    private GraphicsContext gc = canvas.getGraphicsContext2D();
    private Vertex selectedVertex;
    // scale contant
    private double scaleK = 1.1;
    private int scale = 1;
    private Color backgroundColor = Color.rgb(20, 20, 20);
    public boolean isDirected;
    public boolean isWeighted;
    private int vertexIDCounter = 0;
    private Double lastX;
    private Double lastY;

    public GraphView(boolean isDirected, boolean isWeighted) {
        this.isDirected = isDirected;
        this.isWeighted = isWeighted;
        Vertex.radius = 20;
        Vertex.displayId = true;
        canvas.setOnMousePressed(this::handlePressed);
        canvas.setOnMouseDragged(this::handleDrag);
        canvas.setOnMouseReleased(this::handleRelease);
        canvas.setOnMouseMoved(this::handleMove);
        canvas.setOnScroll(this::handleScroll);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public GraphView(File file) {
        createFromFile(file);
        canvas.setOnMousePressed(this::handlePressed);
        canvas.setOnMouseDragged(this::handleDrag);
        canvas.setOnMouseReleased(this::handleRelease);
        canvas.setOnMouseMoved(this::handleMove);
        canvas.setOnScroll(this::handleScroll);
        gc.setFill(backgroundColor);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    public void addVertex() {
        Vertex v = new Vertex(vertexIDCounter);
        vertexIDCounter++;
        vertexList.add(v);
        setRandomPos(v);
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
            v1 = getVertexById(id1);
            v2 = getVertexById(id2);
            edgeList.add(new DirectedEdge(v1, v2));
            draw();
        } else {
            // checks if edge already exists
            for (int i = 0; i < edgeList.size(); i++) {
                if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)
                        || (edgeList.get(i).v1.getId() == id2 && edgeList.get(i).v2.getId() == id1)) {
                    return;
                }
            }
            v1 = getVertexById(id1);
            v2 = getVertexById(id2);
            edgeList.add(new Edge(v1, v2));
            draw();
        }

    }

    // add weighted edge
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
            v1 = getVertexById(id1);
            v2 = getVertexById(id2);
            edgeList.add(new DirectedEdge(v1, v2, weight));
            draw();
        } else {
            // checks if edge already exists
            for (int i = 0; i < edgeList.size(); i++) {
                if ((edgeList.get(i).v1.getId() == id1 && edgeList.get(i).v2.getId() == id2)
                        || (edgeList.get(i).v1.getId() == id2 && edgeList.get(i).v2.getId() == id1)) {
                    return;
                }
            }
            v1 = getVertexById(id1);
            v2 = getVertexById(id2);
            edgeList.add(new Edge(v1, v2, weight));
            draw();
        }

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
        } else {
            for (int i = 0; i < edgeList.size(); i++) {
                Edge e = edgeList.get(i);
                if ((e.v1.getId() == id1 && e.v2.getId() == id2) || (e.v1.getId() == id2 && e.v2.getId() == id1)) {
                    edgeList.remove(i);
                }
            }
            draw();
        }

    }

    // returns the ids of all the vertices in the graph view
    public ArrayList<Integer> getVertexIds() {
        ArrayList<Integer> vertexIds = new ArrayList<Integer>();
        for (int i = 0; i < vertexList.size(); i++) {
            vertexIds.add(vertexList.get(i).getId());
        }
        return vertexIds;
    }

    // returns a list of string representation of the edges
    public ArrayList<String> getEdgePairs() {
        ArrayList<String> edgePairs = new ArrayList<String>();
        for (int i = 0; i < edgeList.size(); i++) {
            String s = edgeList.get(i).toString();
            edgePairs.add(s);
        }
        return edgePairs;
    }

    public void setRandomPos(Vertex v) {
        Random r = new Random();
        double x = Vertex.radius + r.nextDouble() * (canvas.getWidth() - Vertex.radius * 2);
        double y = Vertex.radius + r.nextDouble() * (canvas.getHeight() - Vertex.radius * 2);
        v.setPos(x, y);
    }

    public void generateLayout() {
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            setRandomPos(v);
        }
        draw();
    }

    public Vertex getVertexById(int id) {
        for (Vertex v : vertexList) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
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
            Vertex v = vertexList.get(i);
            v.draw(gc);
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
            if (arg0.getX() >= v.posX - Vertex.radius
                    && arg0.getX() <= v.posX + Vertex.radius && arg0.getY() >= v.posY - Vertex.radius
                    && arg0.getY() <= v.posY + Vertex.radius) {
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
            scale -= 1;
            Vertex.radius /= scaleK;
        } else {
            scale += 1;
            Vertex.radius *= scaleK;
        }
        System.out.println(scale);
        for (int i = 0; i < vertexList.size(); i++) {
            Vertex v = vertexList.get(i);
            // zoom out
            if (scroll < 0) {
                v.posX /= scaleK;
                v.posY /= scaleK;
            } else {// zoom in
                v.posX *= scaleK;
                v.posY *= scaleK;
            }
        }
        draw();
    }

    public void toggleDisplayId() {
        Vertex.displayId = !Vertex.displayId;
        draw();
    }

    // returns the ids of the neighbors of the vertex
    public ArrayList<Integer> neighbors(int id) {
        ArrayList<Integer> neighbors = new ArrayList<>();
        for (Edge e : edgeList) {
            Vertex v = getVertexById(id);
            Integer adjacent = e.getAdjacentIfExists(v);
            if (adjacent != null) {
                neighbors.add(adjacent);
            }

        }
        return neighbors;
    }

    // save graph to file
    public void save(File file) {
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeBoolean(isDirected);
            dos.writeBoolean(isWeighted);
            dos.writeInt(vertexIDCounter);
            dos.writeDouble(Vertex.radius);
            dos.writeInt(vertexList.size());
            for (Vertex v : vertexList) {
                dos.write(v.toByteArray());
            }
            dos.writeInt(edgeList.size());
            for (Edge e : edgeList) {
                dos.write(e.toByteArray());
            }
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createFromFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(fis);
            isDirected = dis.readBoolean();
            isWeighted = dis.readBoolean();
            vertexIDCounter = dis.readInt();
            Vertex.radius = dis.readDouble();
            int vertexNum = dis.readInt();
            vertexList = FXCollections.observableArrayList();
            for (int i = 0; i < vertexNum; i++) {
                vertexList.add(new Vertex().fromByteArray(dis));
            }
            int edgeNum = dis.readInt();
            edgeList = FXCollections.observableArrayList();
            for (int i = 0; i < edgeNum; i++) {
                int v1id, v2id;
                v1id = dis.readInt();
                v2id = dis.readInt();
                if (isWeighted) {
                    double weight = dis.readDouble();
                    if (isDirected) {
                        edgeList.add(new DirectedEdge(getVertexById(v1id), getVertexById(v2id), weight));
                    } else {
                        edgeList.add(new Edge(getVertexById(v1id), getVertexById(v2id), weight));
                    }
                } else {
                    if (isDirected) {
                        edgeList.add(new DirectedEdge(getVertexById(v1id), getVertexById(v2id)));
                    } else {
                        edgeList.add(new Edge(getVertexById(v1id), getVertexById(v2id)));
                    }
                }

            }
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
