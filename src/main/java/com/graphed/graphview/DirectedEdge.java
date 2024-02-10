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
    private static final double ARROW_ANGLE = 20;
    private static final double ARROW_LENGTH = 10;
    private double intersectionX, intersectionY;

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

        drawArrow(gc);
    }

    public void drawArrow(GraphicsContext gc) {
        double angle = calculateAngle();
        calculateIntersection();

        drawArrowLine(gc, angle - ARROW_ANGLE);
        drawArrowLine(gc, angle + ARROW_ANGLE);
    }

    private void drawArrowLine(GraphicsContext gc, double angle) {
        double opposite = Math.sin(Math.toRadians(angle)) * ARROW_LENGTH;
        double adjacent = Math.cos(Math.toRadians(angle)) * ARROW_LENGTH;

        double x = intersectionX + adjacent;
        double y = intersectionY - opposite;

        gc.setStroke(Color.GREEN);
        gc.setLineWidth(3);
        gc.strokeLine(intersectionX, intersectionY, x, y);
    }

    // Calculates the angle of the edge formed by two vertices (v1 and v2) in
    // relation to the x-axis.
    // Returns the angle in degrees.
    public double calculateAngle() {
        double angle = Math.toDegrees(Math.atan2(v2.posY - v1.posY, v1.posX - v2.posX));
        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    // calculates the intersection point of the edge and the border of the vertex
    public void calculateIntersection() {
        double X = v2.posX - v1.posX;
        double Y = v2.posY - v1.posY;
        double XY = Math.pow(X, 2) + Math.pow(Y, 2);
        double H = Math.sqrt(XY);
        double x = Vertex.radius * X / H;
        double y = Vertex.radius * Y / H;
        intersectionX = v2.posX - x;
        intersectionY = v2.posY - y;
    }

}