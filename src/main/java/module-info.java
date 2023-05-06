module gui {
    requires javafx.controls;
    requires javafx.fxml;

    exports game;
    exports gui;
    opens game to javafx.fxml;
    opens gui to javafx.fxml;
}