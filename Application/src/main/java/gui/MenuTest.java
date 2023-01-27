package gui;

import game.TitleGuessing;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuTest extends Application {
    private static String guess;
    public Button button;

    Stage window;
    Scene scene1, scene2;
    Button button1, button2;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.window = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("menu.fxml")));
        window.setTitle("TS Games");
        window.setOnCloseRequest(event -> {
            event.consume();
            if(ConfirmBox.display("Quit?", "Are you sure you want to quit?")) stage.close();
        });

        window.setScene(new Scene(root, 300, 300));
        window.show();
    }

    public void handleButtonClicked() {
        System.out.println("pressed");
    }

    private boolean checkTheGuess(String string) {
        return TitleGuessing.checkGuess("I Almost Do", string);
    }

    private void firstMenuTest(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Button b1 = new Button("PLAY");
        b1.setPrefSize(162, 100);

        Button b2 = new Button("SCORES");
        b2.setPrefSize(162, 100);

        Button b3 = new Button("SETTINGS");
        b3.setPrefSize(162, 100);

        Button quitButton = new Button("QUIT");
        quitButton.setPrefSize(162, 100);
        quitButton.setStyle("-fx-background-color: #BF616A");
        quitButton.setOnAction(event -> stage.close());

        GridPane.setConstraints(b1, 0, 0);
        GridPane.setConstraints(b2, 1, 0);
        GridPane.setConstraints(b3, 0, 1);
        GridPane.setConstraints(quitButton, 1, 1);
        gridPane.getChildren().addAll(b1, b2, b3, quitButton);

        BorderPane layout = new BorderPane(gridPane);
        layout.setPadding(new Insets(50, 50, 50, 50));
        Scene scene = new Scene(layout, 512, 512);
        scene.getStylesheets().add("styling.css");
        stage.setScene(scene);
        stage.show();
    }

    private void takeAndCheckInput(Stage stage) {
        TextField input = new TextField();
        input.setPromptText("enter guess");

        Button guessButton = new Button("Guess");

        guessButton.setOnAction(event -> {
            guess = input.getText();
            if(!guess.isBlank()) System.out.println(checkTheGuess(guess));
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20, 20, 20, 20));
        layout.getChildren().addAll(input, guessButton);
        Scene scene = new Scene(layout, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void testing1(Stage stage) {
        // scene 1
        Label label = new Label("first scene");
        button1 = new Button("go to scene 2");
        button1.setOnAction(event -> {
            stage.setScene(scene2);
        });

        VBox layout1 = new VBox(20);
        layout1.getChildren().addAll(label, button1);
        scene1 = new Scene(layout1, 300, 300);

        // scene 2
        button2 = new Button("go to scene 1");
        button2.setOnAction(event -> {
            stage.setScene(scene1);
        });

        StackPane layout2 = new StackPane();
        layout2.getChildren().add(button2);
        scene2 = new Scene(layout2, 500, 500);

        stage.setScene(scene1);
        stage.show();
    }
}
