module com.graphed {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.graphed to javafx.fxml;

    exports com.graphed;
}
