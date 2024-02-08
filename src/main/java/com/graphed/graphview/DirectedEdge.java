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

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class DirectedEdge extends Edge {
    public DirectedEdge(Vertex v1, Vertex v2) {
        super(v1, v2);
    }

    public DirectedEdge(Vertex v1, Vertex v2, double weight) {
        super(v1, v2, weight);
    }

    // returns the id of the vertex adjacent to the vertex v on the edge
    public Integer getAdjacentIfExists(Vertex v) {

        if (v1.equals(v)) {
            return v2.getId();
        }
        return null;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeLine(v1.posX, v1.posY, v2.posX, v2.posY);
        double X = v2.posX - v1.posX;
        double Y = v2.posY - v1.posY;
        double XY = Math.pow(X, 2) + Math.pow(Y, 2);
        double H = Math.sqrt(XY);
        double x = Vertex.radius * X / H;
        double y = Vertex.radius * Y / H;
        double pointx, pointy;
        pointx = v2.posX - x;
        pointy = v2.posY - y;
        gc.setFill(Color.GREEN);
        gc.beginPath();
        gc.setLineWidth(2.5);
        gc.arc(pointx, pointy, 7, 7, 0, 360);
        gc.closePath();
        gc.fill();
    }
}
