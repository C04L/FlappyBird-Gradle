package coal.utc.GameObjects;


import coal.utc.View.Asset;
import coal.utc.View.Sprite;
import javafx.scene.canvas.GraphicsContext;


public class Title implements GameObject {
    private final int WIDTH = 178;
    private final int HEIGHT = 48;
    private Asset asset = new Asset("/Assets/images/title.png", WIDTH, HEIGHT);
    private Sprite sprite;
    private GameState gameState = GameState.getInstance();

    public Title(double screenWidth, double screenHeight, GraphicsContext ctx) {
        sprite = new Sprite(asset);
        sprite.setPosX(screenWidth / 2 - WIDTH / 2);
        sprite.setPosY(40);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);
    }

    public void update(long now) {
    }

    public void render() {
        if (!gameState.isGameStarted() && !gameState.isGameEnded())
            sprite.render();
    }
}