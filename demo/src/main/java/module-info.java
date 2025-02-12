module com.basheer.os {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.basheer.os to javafx.fxml;
    exports com.basheer.os;
}
