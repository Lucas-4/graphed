module com.graph {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.graph to javafx.fxml;

    exports com.graph;
}
