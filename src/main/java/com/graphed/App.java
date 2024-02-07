
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
package com.graphed;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import com.graphed.screens.Main;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private Main mainScreen;

    @Override
    public void start(Stage stage) throws IOException {
        BorderPane appPane = new BorderPane();
        appPane.getStyleClass().add("border-pane");
        MenuBar menubar = new MenuBar();
        Menu fileMenu = new Menu("File");
        MenuItem newProjectMenuItem = new MenuItem("New Project...");

        menubar.setMaxWidth(Double.MAX_VALUE);

        fileMenu.getItems().addAll(newProjectMenuItem);
        menubar.getMenus().add(fileMenu);

        appPane.setTop(menubar);

        scene = new Scene(appPane);
        scene.getStylesheets().add(App.class.getResource("/styles/main.css").toExternalForm());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Graphed");
        scene.setFill(Color.RED);
        stage.show();
        newProjectMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GridPane popupPane = new GridPane();
                Stage newProjectStage = new Stage();
                newProjectStage.initModality(Modality.APPLICATION_MODAL);
                Scene newProjectScene = new Scene(popupPane);
                newProjectScene.getStylesheets().add(App.class.getResource("/styles/main.css").toExternalForm());

                Button createProjectBtn = new Button("Create Project");
                popupPane.setPrefWidth(300);
                popupPane.setPrefHeight(150);
                popupPane.setAlignment(Pos.CENTER);
                ToggleGroup isDirected = new ToggleGroup();
                ToggleGroup isWeighted = new ToggleGroup();

                RadioButton directed = new RadioButton("Directed");
                RadioButton undirected = new RadioButton("Undirected");
                directed.setToggleGroup(isDirected);
                undirected.setToggleGroup(isDirected);
                directed.setSelected(true);

                RadioButton weighted = new RadioButton("Weighted");
                RadioButton unweighted = new RadioButton("Unweighted");
                weighted.setToggleGroup(isWeighted);
                unweighted.setToggleGroup(isWeighted);
                weighted.setSelected(true);

                GridPane.setRowIndex(directed, 0);
                GridPane.setRowIndex(undirected, 1);
                GridPane.setRowIndex(weighted, 2);
                GridPane.setRowIndex(unweighted, 3);
                GridPane.setRowIndex(createProjectBtn, 4);

                popupPane.getChildren().addAll(directed, undirected, weighted, unweighted, createProjectBtn);
                newProjectStage.setScene(newProjectScene);

                createProjectBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent e) {

                        mainScreen = new Main(isDirected.getSelectedToggle().equals(directed),
                                isWeighted.getSelectedToggle().equals(weighted));

                        appPane.setCenter(mainScreen);
                        newProjectStage.close();
                    }
                });
                newProjectStage.showAndWait();

            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}