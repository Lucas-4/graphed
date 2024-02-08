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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

class Edge {

    public boolean isWeighted;
    public double weight;
    public Vertex v1, v2;

    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public Edge(Vertex v1, Vertex v2, double weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
        this.isWeighted = true;

    }

    public Edge() {
    }

    // returns the id of the vertex adjacent to the vertex v on the edge
    public Integer getAdjacentIfExists(Vertex v) {
        if (v1.equals(v)) {
            return v2.getId();
        }
        if (v2.equals(v)) {
            return v1.getId();
        }
        return null;
    }

    public String toString() {
        String s = String.valueOf(v1.getId()) + "-" + String.valueOf(v2.getId());
        if (isWeighted) {
            s += "   w: " + weight;
        }
        return s;
    }

    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(v1.posX, v1.posY, v2.posX, v2.posY);
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        try {
            dos.writeInt(v1.getId());
            dos.writeInt(v2.getId());
            if (isWeighted) {
                dos.writeDouble(weight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

}
