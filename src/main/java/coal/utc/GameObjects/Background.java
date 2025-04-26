// Modified Background.java
package coal.utc.GameObjects;

import java.util.ArrayList;


import coal.utc.View.Asset;
import coal.utc.View.Sprite;
import javafx.scene.canvas.GraphicsContext;

public class Background implements GameObject {
    private final int WIDTH = 288;
    private final int HEIGHT = 512;
    private Asset asset;
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private double screenWidth;
    private double screenHeight;
    private GraphicsContext ctx;

    public Background(double screenWidth, double screenHeight, GraphicsContext ctx) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.ctx = ctx;
        this.asset = new Asset("/Assets/images/background.png", WIDTH, HEIGHT);

        createBackgroundSprites();
    }

    private void createBackgroundSprites() {
        sprites.clear();
        int backgroundWidth = 0;

        do {
            Sprite background = new Sprite(asset);

            if ((screenHeight - 112) < HEIGHT)
                background.resizeImage(WIDTH, HEIGHT);
            else
                background.resizeImage(WIDTH, screenHeight - 112);

            if (screenHeight > HEIGHT)
                background.setPos(backgroundWidth, 0);
            else
                background.setPos(backgroundWidth, screenHeight - HEIGHT);

            background.setVel(0, 0);
            background.setCtx(ctx);

            sprites.add(background);
            backgroundWidth += WIDTH;
        } while (backgroundWidth < (screenWidth + WIDTH));
    }

    public void resize(double screenWidth, double screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        createBackgroundSprites();
    }

    public void update(long now) {
    }

    public void render() {
        for (Sprite background : sprites)
            background.render();
    }

    public void changeImage(String path) {
        this.asset = new Asset(path, WIDTH, HEIGHT);
        for (Sprite background : sprites)
            background.changeImage(asset);
        createBackgroundSprites();
    }
}