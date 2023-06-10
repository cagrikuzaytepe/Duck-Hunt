import java.awt.Color;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class BackgroundSelectionScreen extends Application {
    private int currentBackgroundIndex = 0;
    private int currentCrosshairIndex = 0;
    private int scale = 3; // Default scale factor
    private AudioClip audioClip;

    private Image[] backgrounds = {
            new Image("assets/background/1.png"),
            new Image("assets/background/2.png"),
            new Image("assets/background/3.png"),
            new Image("assets/background/4.png"),
            new Image("assets/background/5.png"),
            new Image("assets/background/6.png")
    };

    private Image[] crosshairs = {
            new Image("assets/crosshair/1.png"),
            new Image("assets/crosshair/2.png"),
            new Image("assets/crosshair/3.png"),
            new Image("assets/crosshair/4.png"),
            new Image("assets/crosshair/5.png"),
            new Image("assets/crosshair/6.png"),
            new Image("assets/crosshair/7.png")
    };

    public ImageView backgroundImageView;
    public ImageView crosshairImageView;
    private double backgroundWidth = new Image("assets/background/1.png").getWidth()*scale;
    private double backgroundHeight = new Image("assets/background/1.png").getHeight()*scale;
    private double crosshairWidth = new Image("assets/crosshair/1.png").getWidth()*scale;
    private double crosshairHeight = new Image("assets/crosshair/1.png").getHeight()*scale;

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, backgroundWidth, backgroundHeight);

        

        backgroundImageView = new ImageView(backgrounds[currentBackgroundIndex]);
        crosshairImageView = new ImageView(crosshairs[currentCrosshairIndex]);
        backgroundImageView.setFitWidth(backgroundWidth);
        backgroundImageView.setFitHeight(backgroundHeight);
        crosshairImageView.setFitWidth(crosshairWidth);
        crosshairImageView.setFitHeight(crosshairHeight);

        root.getChildren().addAll(backgroundImageView, crosshairImageView);

        Label instructionLabel = new Label("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");
        instructionLabel.setWrapText(true);
        instructionLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        
        instructionLabel.setTextFill(Paint.valueOf("ORANGE"));
        instructionLabel.setFont(Font.font("Arial",8*scale));


        instructionLabel.setTranslateY(-100*scale);
        root.getChildren().add(instructionLabel);

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                changeBackground(true);
            } else if (event.getCode() == KeyCode.LEFT) {
                changeBackground(false);
            } else if (event.getCode() == KeyCode.UP) {
                changeCrosshair(true);
            } else if (event.getCode() == KeyCode.DOWN) {
                changeCrosshair(false);
            } else if (event.getCode() == KeyCode.ENTER) {
                // Start the game
                primaryStage.setScene(scene);
                primaryStage.setTitle("HUBBM Duck Hunt"); 
                DuckHunt.audioClip.stop();       
                Game game = new Game(backgroundImageView,crosshairImageView);
                game.start(primaryStage);

                //game.
            } else if (event.getCode() == KeyCode.ESCAPE) {
                // Go back to the title screen
                DuckHunt.audioClip.stop();   
                DuckHunt duckHunt = new DuckHunt();
                duckHunt.start(primaryStage);    
            }
           
        });

        primaryStage.setScene(scene);
        primaryStage.setTitle("HUBBM Duck Hunt");
        primaryStage.show();
    }

    private void changeBackground(boolean next) {
        if (next) {
            currentBackgroundIndex = (currentBackgroundIndex + 1) % backgrounds.length;
        } else {
            currentBackgroundIndex = (currentBackgroundIndex - 1 + backgrounds.length) % backgrounds.length;
        }
        backgroundImageView.setImage(backgrounds[currentBackgroundIndex]);
    }

    private void changeCrosshair(boolean next) {
        if (next) {
            currentCrosshairIndex = (currentCrosshairIndex + 1) % crosshairs.length;
        } else {
            currentCrosshairIndex = (currentCrosshairIndex - 1 + crosshairs.length) % crosshairs.length;
        }
        crosshairImageView.setImage(crosshairs[currentCrosshairIndex]);
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
    }
}
