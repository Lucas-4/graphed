package com.graphed.graphview.animation;

import javafx.scene.Node;

public abstract class GraphAnimation implements Runnable {
    private String name;
    private int delayDuration;
    public OnFinished onFinished;

    // code for the animation goes in this function
    public abstract void play();

    public final void run() {
        play();
        onFinished.execute();
    }

    public void setDelay(int delay) {
        this.delayDuration = delay;
    };

    public void delay() throws InterruptedException {
        Thread.sleep(delayDuration);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public abstract Node[] params();

}
