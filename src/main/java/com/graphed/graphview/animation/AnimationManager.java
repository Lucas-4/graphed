package com.graphed.graphview.animation;

import java.util.ArrayList;

import com.graphed.graphview.GraphView;
import com.graphed.graphview.search.DFS;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;

public class AnimationManager {
    private GraphAnimation currentAnimation;
    private Thread t;
    private ArrayList<GraphAnimation> animations = new ArrayList<>();
    private ComboBox<String> animationPicker = new ComboBox<String>();
    private HBox params = new HBox();
    private Button start = new Button("Start");
    private Button stop = new Button("Stop");

    public AnimationManager(GraphView gv) {
        animations.add(new DFS(gv));
        animationPicker.setItems(getAnimationNames());
        initControls();
    }

    public Node[] getControls() {
        Node[] n = new Node[4];
        n[0] = animationPicker;
        n[1] = start;
        n[2] = stop;
        n[3] = params;
        return n;
    }

    private ObservableList<String> getAnimationNames() {
        ObservableList<String> animationNames = FXCollections.observableArrayList();
        for (GraphAnimation a : animations) {
            animationNames.add(a.getName());
        }
        return animationNames;
    }

    private void initControls() {
        start.setDisable(true);
        stop.setDisable(true);

        animationPicker.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                String current = animationPicker.getSelectionModel().getSelectedItem();
                for (GraphAnimation ga : animations) {
                    if (ga.getName() == current) {
                        start.setDisable(false);
                        currentAnimation = ga;
                        params.getChildren().setAll(ga.params());
                    }
                }
            }

        });
        start.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                System.out.println("Starting");
                start.setDisable(true);
                stop.setDisable(false);
                animationPicker.setDisable(true);
                t = new Thread(currentAnimation);
                currentAnimation.onFinished = new OnFinished() {

                    @Override
                    public void execute() {
                        start.setDisable(false);
                        stop.setDisable(true);
                        animationPicker.setDisable(false);

                    }

                };
                t.start();
            }

        });
        stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {

                t.interrupt();
                stop.setDisable(true);
                start.setDisable(false);
                animationPicker.setDisable(false);

            }

        });
    }
}
