import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends Application {

    private static final Image BACKGROUND_IMAGE = new Image("assets/background/1.png");
    private ImageView backgroundImage;
    private ImageView crossImageView;
    ImageView foregroundImageView;

    public Game(ImageView backgroundImageView, ImageView crosshairImageView) {
        this.backgroundImage = backgroundImageView;
        this.crossImageView = crosshairImageView;
    }

    double volume = 0.025;
    Stage primaryStage;
    private boolean isTextVisible = true;
    private boolean levelCompleted;

    private int scale = 3; // Default scale factor
    public static int WIDTH = (int) BACKGROUND_IMAGE.getWidth();;
    public static int HEIGHT = (int) BACKGROUND_IMAGE.getHeight();
    public static final int DUCK_SIZE_WIDTH = (int) new Image("assets/duck_black/1.png").getWidth();
    public static final int DUCK_SIZE_HEIGHT = (int) new Image("assets/duck_black/1.png").getHeight();
    private int GUN_SIZE_WIDTH = (int) new Image("assets/crosshair/1.png").getWidth() * scale;
    private int GUN_SIZE_HEIGHT = (int) new Image("assets/crosshair/1.png").getHeight() * scale;
    private double x = 0;
    private double y = 0;
    /* I opeend the images of each directions.*/

    private static final Image[] DUCK_FLY_IMAGES_RIGHT_UP = {
            (new Image("assets/duck_black/1.png")),
            (new Image("assets/duck_black/2.png")),
            (new Image("assets/duck_black/3.png")),

    };
    private static final Image[] DUCK_FLY_IMAGES_LEFT_UP = {
            flipImageHorizontally(new Image("assets/duck_black/1.png")),
            flipImageHorizontally(new Image("assets/duck_black/2.png")),
            flipImageHorizontally(new Image("assets/duck_black/3.png")),
    };
    private static final Image[] DUCK_FLY_IMAGES_RIGHT_DOWN = {
            flipImageVertically(new Image("assets/duck_black/1.png")),
            flipImageVertically(new Image("assets/duck_black/2.png")),
            flipImageVertically(new Image("assets/duck_black/3.png")),

    };
    private static final Image[] DUCK_FLY_IMAGES_LEFT_DOWN = {
            flipImageHorizontally(flipImageVertically(new Image("assets/duck_black/1.png"))),
            flipImageHorizontally(flipImageVertically(new Image("assets/duck_black/2.png"))),
            flipImageHorizontally(flipImageVertically(new Image("assets/duck_black/3.png")))
    };

    private static final Image[] DUCK_FLY_IMAGES_RIGHT = {

            (new Image("assets/duck_black/4.png")),
            (new Image("assets/duck_black/5.png")),
            (new Image("assets/duck_black/6.png")),
    };

    private static final Image[] DUCK_FLY_IMAGES_LEFT = {

            Game.flipImageHorizontally(new Image("assets/duck_black/4.png")),
            Game.flipImageHorizontally(new Image("assets/duck_black/5.png")),
            Game.flipImageHorizontally(new Image("assets/duck_black/6.png")),

    };

    private static final String[] DUCK_DIE_IMAGES = {
            "assets/duck_black/7.png",
            "assets/duck_black/8.png"
    };
      /* Initilizing the sound effects.*/

    private static final String GUNSHOT_SOUND = "assets/effects/Gunshot.mp3";
    private static final String DUCK_FALL_SOUND = "assets/effects/DuckFalls.mp3";
    private static final String GAME_OVER_SOUND = "assets/effects/GameOver.mp3";
    private static final String LEVEL_COMPLETED_SOUND = "assets/effects/LevelCompleted.mp3";
    private static final String GAME_COMPLETED_SOUND = "assets/effects/GameCompleted.mp3";

    private static int currentLevel = 1;

    private int ammoLeft;
    private boolean gameOver;
    private boolean gameCompleted;

    private Image[] duckFlyImagesRight;
    private Image[] duckFlyImagesLeft;
    private Image[] duckDieImages;
    private Image gunImage;

    private static List<Duck> ducks;

    private double gunX;
    private double gunY;

    int levelTextSize = 10;
    private GraphicsContext gc;
    private GraphicsContext cg;

    private Timeline animation;

    private AudioClip gunshotClip;
    private AudioClip duckFallClip;
    private AudioClip gameOverClip;
    private AudioClip levelCompletedClip;
    private AudioClip gameCompletedClip;

    private Canvas canvas;
    private Canvas aCanvas;

    private int totalLevels = 6;
    public double windowWidth;
    public double windowHeight;
    StackPane root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Duck Hunt");
        primaryStage.setWidth(WIDTH);
        primaryStage.setHeight(HEIGHT);
        this.primaryStage = primaryStage;

        this.root = new StackPane();
        String path = null;

        this.windowWidth = primaryStage.getWidth();
        this.windowHeight = primaryStage.getHeight();
        backgroundImage.setFitWidth(windowWidth);
        backgroundImage.setFitHeight(windowHeight);
      
        root.getChildren().add(backgroundImage);
        String look = backgroundImage.getImage().impl_getUrl();

        if (look.contains("1.png")) {
            path = "assets/foreground/1.png";
        } else if (look.contains("2.png")) {
            path = "assets/foreground/2.png";
        } else if (look.contains("3.png")) {
            path = "assets/foreground/3.png";
        } else if (look.contains("4.png")) {
            path = "assets/foreground/4.png";
        } else if (look.contains("5.png")) {
            path = "assets/foreground/5.png";
        } else if (look.contains("6.png")) {
            path = "assets/foreground/6.png";
        }

        Image foreground = new Image(path);
        this.foregroundImageView = new ImageView(foreground);

        foregroundImageView.setFitWidth(windowWidth);
        foregroundImageView.setFitHeight(windowHeight);

        Canvas canvas = new Canvas(windowWidth, windowHeight);
        this.canvas = canvas;
        Canvas aCanvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        this.aCanvas = aCanvas;
        cg = aCanvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        root.getChildren().add(foregroundImageView);

    
        aCanvas.setCursor(Cursor.NONE);
        cg.drawImage(crossImageView.getImage(), gunX, gunY);

        loadResources();
        setupInputHandlers();
        initialize();
        startGameLoop();

        root.getChildren().add(aCanvas);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        setScale(scale, primaryStage);
        primaryStage.show();

    }

    private void setScale(int scale, Stage primaryStage) {
        this.scale = scale;
        windowHeight *= scale;
        windowWidth *= scale;

        primaryStage.setWidth(windowWidth);
        primaryStage.setHeight(windowHeight);
        canvas.setWidth(windowWidth);
        canvas.setHeight(windowHeight);
        aCanvas.setWidth(WIDTH * scale);
        aCanvas.setHeight(HEIGHT * scale);
        gc.scale(scale, scale);
        cg.scale(scale, scale);

        backgroundImage.setFitHeight(windowHeight);
        backgroundImage.setFitWidth(windowWidth);
        foregroundImageView.setFitHeight(windowHeight);
        foregroundImageView.setFitWidth(windowWidth);

    }

    private void loadResources() {
        gunImage = crossImageView.getImage();

        duckFlyImagesRight = new Image[DUCK_FLY_IMAGES_RIGHT.length];
        duckFlyImagesLeft = new Image[DUCK_FLY_IMAGES_LEFT.length];
        duckDieImages = new Image[DUCK_DIE_IMAGES.length];

        for (int i = 0; i < DUCK_DIE_IMAGES.length; i++) {
            duckDieImages[i] = new Image(DUCK_DIE_IMAGES[i]);
        }

        gunshotClip = new AudioClip(getClass().getResource(GUNSHOT_SOUND).toString());
        duckFallClip = new AudioClip(getClass().getResource(DUCK_FALL_SOUND).toString());
        gameOverClip = new AudioClip(getClass().getResource(GAME_OVER_SOUND).toString());
        levelCompletedClip = new AudioClip(getClass().getResource(LEVEL_COMPLETED_SOUND).toString());
        gameCompletedClip = new AudioClip(getClass().getResource(GAME_COMPLETED_SOUND).toString());

        /*  Adjust the volume for the sound effects
         Set the volume between 0.0 and 1.0 (0.0 being mute, 1.0 being maximum volume) */

        gunshotClip.setVolume(volume);
        duckFallClip.setVolume(volume);
        gameOverClip.setVolume(volume);
        levelCompletedClip.setVolume(volume);
        gameCompletedClip.setVolume(volume);
    }

    private void setupInputHandlers() {

        aCanvas.setFocusTraversable(true);
        canvas.setOnKeyPressed(this::handleKeyPress);
        aCanvas.setOnKeyPressed(this::handleKeyPress);
        root.setOnKeyPressed(this::handleKeyPress);
        root.setOnMouseMoved(this::handleMouseMove);
        root.setFocusTraversable(true);
        root.setOnMouseClicked(this::handleMouseClick);

    }

    private void initialize() {
        currentLevel = 1;

        gameOver = false;
        gameCompleted = false;

        ducks = new ArrayList<>();
        spawnDuck();
        gunX = crossImageView.getX();
        gunY = crossImageView.getY();
    }
    /*Game is playing in that loop. */
    private void startGameLoop() {
        animation = new Timeline(new KeyFrame(Duration.seconds(1.0 / 60.0), event -> {
            if (gameOver) {
                startFlashingAnimation();
                animation.stop();

            } else if (gameCompleted) {
                startFlashingAnimation();
                animation.stop();

            } else if (levelCompleted) {
                startFlashingAnimation();
                animation.stop();
            } else {

                update();
                render();

            }
        }));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }
    /*I updated ducks and games position and situation. */
    private void update() {
        Iterator<Duck> iterator = ducks.iterator();
        while (iterator.hasNext()) {
            Duck duck = iterator.next();
            duck.update();

            if (duck.isDead()) {
                if (!duck.isFalling()) {
                    duckFallClip.play();

                    duck.setFalling(true);
                } else if (duck.isFalling() && duck.getY() >= HEIGHT) {
                    iterator.remove();

                }
            } else if (duck.getX() > WIDTH || duck.getX() + duck.getWidth() < 0) {
                iterator.remove();
            }
            if (ducks.isEmpty() && ammoLeft >= 0) {
                if (!gameCompleted && !gameOver) {
                    duck.setDead(true);

                    if (currentLevel < totalLevels) {
                        if (currentLevel < totalLevels) {
                            levelCompletedClip.play();
                            levelCompleted = true;

                        }
                        currentLevel++;
                        spawnDuck();
                        if (currentLevel == totalLevels + 1) {
                            currentLevel--;
                        }

                    } else {
                        gameCompletedClip.play();
                        renderGameCompletedScreen();
                        gameCompleted = true;
                    }
                }
            }

            if (ammoLeft < 0 && !ducks.isEmpty() && currentLevel <= totalLevels) {
                gameOverClip.play();
                iterator.remove();
                gameOver = true;
            }
        }

    }

    private void render() {
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        cg.clearRect(0, 0, WIDTH, HEIGHT);

        for (Duck duck : ducks) {
            if (duck.isFalling()) {
                gc.drawImage(duck.image, duck.getX(), duck.getY());
            } else {
                gc.drawImage(duck.image, duck.getX(), duck.getY());
            }
        }

        cg.drawImage(gunImage, gunX, gunY);

        cg.setFill(Color.ORANGE);
        cg.setFont(Font.font(5 * scale + "px Arial")); /*  Set the desired font size */

        cg.fillText("Level: " + currentLevel, scale * (WIDTH / 2 - 20) / 3, 5 * scale);

        if (ammoLeft >= 0) {
            cg.fillText("Ammo: " + ammoLeft, scale * (WIDTH - 60) / 3, 5 * scale);
        }

    }

    private void renderGameOverScreen() {
        cg.clearRect(0, 0, WIDTH, HEIGHT);

        cg.fillText("GAME OVER!", WIDTH / 2 - 40, HEIGHT / 2);
        cg.setFill(Color.ORANGE);
        if (isTextVisible) {
            cg.fillText("Press ENTER to play again\n\tPress ESC to exit", WIDTH / 2 - 75, HEIGHT / 2 + 20);

        }
    }

    private void renderLevelCompletedScreen() {
        cg.clearRect(0, 0, WIDTH, HEIGHT);

        cg.fillText("LEVEL COMPLETED!", WIDTH / 2 - 60, HEIGHT / 2);
        cg.setFill(Color.ORANGE);
        if (isTextVisible) {
            cg.fillText("Press ENTER to play again\n\tPress ESC to exit", WIDTH / 2 - 75, HEIGHT / 2 + 20);

        }
    }

    private void renderGameCompletedScreen() {
        cg.clearRect(0, 0, WIDTH, HEIGHT);
      
        cg.fillText("GAME COMPLETED!", WIDTH / 2 - 60, HEIGHT / 2);
        cg.setFill(Color.ORANGE);
        if (isTextVisible) {
            cg.fillText("Press ENTER to play again\n\tPress ESC to exit", WIDTH / 2 - 75, HEIGHT / 2 + 20);

        }
    }

    private void startFlashingAnimation() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    isTextVisible = !isTextVisible;
                    if (gameOver) {
                        renderGameOverScreen();
                    } else if (gameCompleted) {
                        renderGameCompletedScreen();
                    } else if (levelCompleted) {
                        renderLevelCompletedScreen();
                    }

                }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

/*Levels are in that method: */
    private void spawnDuck() {

        if (currentLevel == 1) {
            ammoLeft = 3;
            this.x = 0 * scale;
            this.y = 0 * scale;
            ducks.add(new Duck(x, y, false, true, 10 * scale, 15 * scale));

        } else if (currentLevel == 2) {
            ammoLeft = 6;
            this.x = 55 * scale;
            this.y = 10 * scale;
            double a = 50 * scale;
            double b = 30;
            ducks.add(new Duck(x, y, true, true, -10 * scale, 12 * scale));
            ducks.add(new Duck(a, b, false, true, 10 * scale, -15 * scale));

        } else if (currentLevel == 3) {
            ammoLeft = 6;
            this.x = 60 * scale;
            this.y = 20 * scale;
            double a = 50 * scale;
            double b = 32 * scale;
            ducks.add(new Duck(x, y, false, true, 10 * scale, 0 * scale));
            ducks.add(new Duck(a, b, false, true, -10 * scale, 0 * scale));

        } else if (currentLevel == 4) {
            ammoLeft = 9;
            this.x = 60 * scale;
            this.y = 20 * scale;
            double a = 50 * scale;
            double b = 32 * scale;
            double c = 20 * scale;
            double d = 12 * scale;
            ducks.add(new Duck(x, y, false, true, -10 * scale, -15 * scale));
            ducks.add(new Duck(a, b, false, true, -10 * scale, 20 * scale));
            ducks.add(new Duck(c, d, false, true, 12 * scale, 16 * scale));

        } else if (currentLevel == 5) {
            ammoLeft = 9;
            this.x = 50 * scale;
            this.y = 30 * scale;
            double a = 50 * scale;
            double b = 30 * scale;
            double c = 45 * scale;
            double d = 40 * scale;
            ducks.add(new Duck(x, y, false, true, -12 * scale, -17 * scale));
            ducks.add(new Duck(a, b, false, true, -13 * scale, 16 * scale));
            ducks.add(new Duck(c, d, false, true, 10 * scale, 16 * scale));

        } else if (currentLevel == 6) {
            ammoLeft = 12;
            this.x = 70 * scale;
            this.y = 40 * scale;
            double a = 25 * scale;
            double b = 30 * scale;
            double c = 20 * scale;
            double d = 15 * scale;
            double e = 40 * scale;
            double f = 16 * scale;
            ducks.add(new Duck(x, y, false, true, -10 * scale, 18 * scale));
            ducks.add(new Duck(a, b, false, true, 11 * scale, -20 * scale));
            ducks.add(new Duck(c, d, false, true, 12 * scale, -20 * scale));
            ducks.add(new Duck(e, f, false, true, -13 * scale, 20 * scale));

        }

    }

    private void handleMouseClick(MouseEvent event) {
        if (!gameOver && !gameCompleted && !levelCompleted && ammoLeft >= 0
                && event.getButton() == MouseButton.PRIMARY) {
            if (ammoLeft >= 0) {
                double mouseX = event.getX();
                double mouseY = event.getY();
                gunX = mouseX;
                gunY = mouseY;

                if (mouseX >= gunX && mouseX <= gunX + GUN_SIZE_WIDTH && mouseY >= gunY
                        && mouseY <= gunY + GUN_SIZE_HEIGHT) {
                    gunshotClip.play();
                    ammoLeft--;
                    if (ammoLeft == -1) {
                        ammoLeft--;
                    }
                    for (Duck duck : ducks) {

                        if (!duck.isDead() && duck.contains(mouseX, mouseY)) {
                            duck.setDead(true);
                            duck.setDieFrame(0);
                            break;
                        }

                    }
                }
            }

        }

    }

    private void handleMouseMove(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        gunX = mouseX;
        gunY = mouseY;
    }

    private void handleKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && gameOver) {
            /* Restart the game from level 1 */ 
            initialize();
            startGameLoop();
        } else if (event.getCode() == KeyCode.ESCAPE && gameOver) {
            /*  Go back to the title screen */
            DuckHunt duckhunt = new DuckHunt();
            duckhunt.start(primaryStage);
        } else if (event.getCode() == KeyCode.ENTER && gameCompleted) {
         /* Restart the game from level 1 */ 
            initialize();
            startGameLoop();
        } else if (event.getCode() == KeyCode.ESCAPE && gameCompleted) {
              /*  Go back to the title screen */
            DuckHunt duckhunt = new DuckHunt();
            duckhunt.start(primaryStage);
        } else if (event.getCode() == KeyCode.ESCAPE && levelCompleted) {
              /*  Go back to the title screen */
            DuckHunt duckhunt = new DuckHunt();
            duckhunt.start(primaryStage);
        } else if (event.getCode() == KeyCode.ENTER && levelCompleted) {
            update();
            render();
            levelCompleted = false;
            startGameLoop();
        }
    }

    private static Image flipImageHorizontally(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        WritableImage flippedImage = new WritableImage(width, height);
        PixelWriter writer = flippedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(width - x - 1, y, reader.getColor(x, y));
            }
        }

        return flippedImage;
    }

    private static Image flipImageVertically(Image image) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        PixelReader reader = image.getPixelReader();
        WritableImage flippedImage = new WritableImage(width, height);
        PixelWriter writer = flippedImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, height - y - 1, reader.getColor(x, y));
            }
        }

        return flippedImage;
    }

    private class Duck {
        private double x;
        private double y;
        private boolean movingLeft;
        private double frame;
        private boolean dead;
        private boolean falling;
        private int dieFrame;
        private double velocityX;
        private double velocityY;
        private boolean movingUp;
        private int count = 0;

        private Image image;

        private static final double FALL_SPEED = 50;
        public Duck(double x, double y, boolean movingLeft, boolean movingUp, double speedX, double speedY) {
            this.x = x;
            this.y = y;
            this.movingLeft = movingLeft;
            this.frame = 0;
            this.dead = false;
            this.falling = false;
            this.dieFrame = 0;
            this.movingUp = movingUp;

            velocityX = speedX; 
            velocityY = speedY;

        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public boolean isDead() {
            return dead;
        }

        public void setDead(boolean dead) {
            this.dead = dead;
        }

        public boolean isFalling() {
            return falling;
        }

        public void setFalling(boolean falling) {
            this.falling = falling;
        }

        public void setDieFrame(int dieFrame) {
            this.dieFrame = dieFrame;
        }

        public double getWidth() {
            return DUCK_SIZE_WIDTH;
        }

        public double getHeight() {
            return DUCK_SIZE_HEIGHT;
        }

        public boolean contains(double mouseX, double mouseY) {
            return mouseX >= x && mouseX <= x + getWidth() && mouseY >= y && mouseY <= y + getHeight();
        }

        public void update() {
            frame += 0.15;
            Image[] duckImages = null;

            if (dead) {
                count++;
                dieFrame++;
                duckImages = duckDieImages;
                if (count == 1) {
                    image = duckImages[0];
                } else {
                    image = duckImages[1];
                }

            } else {
                x += velocityX * 0.02;
                y += velocityY * 0.02;
                if (velocityX < 0) {
                    movingLeft = true; /*Update the direction flag */ 
                } else {
                    movingLeft = false; /*Update the direction flag */ 
                }
                if (velocityY < 0) {
                    movingUp = true;
                } else {
                    movingUp = false;
                }

                /*Check if the duck hits the window */ 
                if (x <= 0 || x + DUCK_SIZE_WIDTH >= Game.WIDTH) {
                    velocityX = -velocityX; /* Reflect horizontally */ 

                    if (velocityX < 0) {
                        movingLeft = true; /*Update the direction flag */ 
                    } else {
                        movingLeft = false; /*Update the direction flag */ 
                    }
                }
                if (y <= 0 || y + DUCK_SIZE_HEIGHT >= Game.HEIGHT) {
                    velocityY = -velocityY; /*Reflect vertically */
                    if (velocityY < 0) {
                        movingUp = true; /*Update the direction flag */ 
                    } else {
                        movingUp = false; /*Update the direction flag */ 
                    }
                }

                if (movingLeft && velocityY == 0) {
                    duckImages = DUCK_FLY_IMAGES_LEFT;
                } else if (!movingLeft && velocityY == 0) {
                    duckImages = DUCK_FLY_IMAGES_RIGHT;
                } else if (!movingLeft && movingUp) {
                    duckImages = DUCK_FLY_IMAGES_RIGHT_UP;
                } else if (movingLeft && movingUp) {
                    duckImages = DUCK_FLY_IMAGES_LEFT_UP;
                } else if (!movingLeft && !movingUp) {
                    duckImages = DUCK_FLY_IMAGES_RIGHT_DOWN;
                } else if (movingLeft && !movingUp) {
                    duckImages = DUCK_FLY_IMAGES_LEFT_DOWN;
                }

                int frameIndex = (int) (frame % duckImages.length);
                image = duckImages[frameIndex];
            }

            if (falling) {

                y += FALL_SPEED * 0.02;
            }
        }

    }
}
