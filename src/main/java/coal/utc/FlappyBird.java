package coal.utc;

import coal.utc.Controller.KeyHandler;
import coal.utc.GameObjects.GameState;
import coal.utc.View.Renderer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;

public class FlappyBird extends Application {

    private Scene scene;
    private GraphicsContext ctx;
    private Canvas canvas;
    private double width = 450;
    private double height = 600;
    private double minWidth = 365;
    private double minHeight = 412;

    private final Font appFont = Font.loadFont(FlappyBird.class.getResource("/Assets/fonts/04b_19.ttf").toExternalForm(), 42);
    private final Color appColor = Color.web("#543847");

    private Renderer renderer;
    private KeyHandler controller;
    private GameState gameState;

    private AnimationTimer timer;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Gái alimi tập bay");
        stage.getIcons().add(new Image("/Assets/images/Loading.gif"));
        stage.setMinWidth(minWidth);
        stage.setMinHeight(minHeight);

        setupScene(stage);

        gameState = GameState.getInstance();

        renderer = Renderer.getInstance();
        renderer.initialize(width, height, ctx, appFont, appColor);

        controller = new KeyHandler(renderer, width, height);

        setupInputHandlers();

        startGameLoop();

        stage.show();
    }

    private void setupScene(Stage stage) {
        Pane pane = new Pane();
        canvas = new Canvas();
        ctx = canvas.getGraphicsContext2D();

        canvas.heightProperty().bind(pane.heightProperty());
        canvas.widthProperty().bind(pane.widthProperty());

        canvas.widthProperty().addListener((obs, oldVal, newVal) -> {
            width = newVal.doubleValue();
            handleResize();
        });

        canvas.heightProperty().addListener((obs, oldVal, newVal) -> {
            height = newVal.doubleValue();
            handleResize();
        });

        pane.getChildren().addAll(canvas);
        scene = new Scene(pane, width, height);
        stage.setScene(scene);
    }

    private void setupInputHandlers() {
        scene.setOnKeyPressed(controller::handleKeyInput);
        scene.setOnMousePressed(e -> controller.handleMouseInput(e.getX(), e.getY()));
    }

    private void handleResize() {
        if (renderer != null) {
            renderer.setDimensions(width, height);
            renderer.refreshGameObjects();
        }
    }

    private void startGameLoop() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                controller.updateGame(now);
                renderer.render();
            }
        };
        timer.start();
    }
}