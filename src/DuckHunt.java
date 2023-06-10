import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DuckHunt extends Application {
    private static final String TITLE_SCREEN_MUSIC = "assets/effects/Title.mp3";
    private static double VOLUME = 0.025;
    private int scale = 3; // Default scale factor
    public static AudioClip audioClip;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Image icon = new Image("assets/welcome/1.png");
        double width2 = icon.getWidth() * scale;
        double height2 = icon.getHeight() * scale;

        Scene scene = new Scene(root, width2, height2);

        // Play the title screen music in a loop
        audioClip = new AudioClip(getClass().getResource(TITLE_SCREEN_MUSIC).toString());
        audioClip.setVolume(VOLUME);
        audioClip.setCycleCount(AudioClip.INDEFINITE);
        audioClip.play();

        primaryStage.getIcons().add(icon);

        // Create the background image
        BackgroundImage backgroundImage = new BackgroundImage(
                icon,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(width2, height2, false, false, true, true)
        );

        // Create the background
        Background background = new Background(backgroundImage);

        // Set the background to the root layout
        root.setBackground(background);

        // Handle key events
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // Go to the background selection screen
                BackgroundSelectionScreen backgroundSelectionScreen = new BackgroundSelectionScreen();
                backgroundSelectionScreen.start(primaryStage);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Exit the game
                primaryStage.close();
            }
        });

        Text text = new Text(20, 50, "PRESS ENTER TO PLAY \n PRESS ESC TO EXIT");
        text.setStyle("-fx-fill: orange;");
        text.setFont(Font.font("Arial", 15 * scale));
        text.setLayoutX(10 * scale);
        text.setLayoutY(10 * scale);

        // Change the position of the text node using translation
        text.setTranslateX(0); 
        text.setTranslateY(35 * scale); 

        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (text.getText().length() != 0) {
                    text.setText("");
                } else {
                    text.setText("PRESS ENTER TO PLAY\nPRESS ESC TO EXIT");
                    text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
                }
            }
        };
        Timeline animation = new Timeline(new KeyFrame(Duration.millis(1000), eventHandler));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        root.getChildren().add(text);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public DuckHunt(){

    }

    public static void main(String[] args) {
        launch(args);
    }

    public void stopMusic() {
        audioClip.stop();
    }

    public static void setVolume(double volume) {
        if (volume < 0 || volume > 1) {
            throw new IllegalArgumentException("Volume must be between 0 and 1");
        }

        VOLUME = volume;
    }
}
