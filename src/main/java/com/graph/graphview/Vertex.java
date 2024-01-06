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

import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class Vertex {
    private int id;
    public double posX;
    public double posY;
    public static double radius = 20;

    public Vertex() {
    }

    public Vertex(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setPos(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(GraphicsContext gc) {

        gc.setFill(Color.rgb(20, 20, 20));
        gc.beginPath();
        gc.setLineWidth(3);
        gc.arc(posX, posY, radius, radius, 0, 360);
        gc.closePath();
        gc.fill();
        gc.setStroke(Color.WHITE);
        gc.stroke();
        // gc.setFill(Color.GREEN);
        // gc.setTextBaseline(VPos.CENTER);
        // gc.setTextAlign(TextAlignment.CENTER);
        // gc.fillText(String.valueOf(id), posX, posY);
    }
}
