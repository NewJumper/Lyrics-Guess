module gui {
    requires javafx.controls;
    requires javafx.fxml;

    opens gui to javafx.fxml;
    exports gui;
    exports game;
    opens game to javafx.fxml;
}