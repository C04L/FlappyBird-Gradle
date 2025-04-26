package coal.utc.GameObjects;


import coal.utc.View.Asset;
import coal.utc.View.Sprite;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Floor implements GameObject {
    private final int WIDTH = 336;
    private final int HEIGHT = 112;
    private final Asset asset = new Asset("/Assets/images/floor.png", WIDTH, HEIGHT);
    private final ArrayList<Sprite> sprites = new ArrayList<>();
    private final GameState gameState = GameState.getInstance();

    public Floor(double screenWidth, double screenHeight, GraphicsContext ctx) {
        int floorWidth = 0;
        do {
            Sprite floor = new Sprite(asset);
            floor.setPos(floorWidth, screenHeight - HEIGHT);
            floor.setVel(-1.5, 0);
            floor.setCtx(ctx);

            sprites.add(floor);
            floorWidth += WIDTH;
        } while (floorWidth < (screenWidth + WIDTH));
    }

    public void update(long now) {
        if (gameState.isGameStarted()) {
            for (Sprite floor : sprites)
                floor.update();

            if (sprites.getFirst().getPosX() < -WIDTH) {
                Sprite firstFloor = sprites.getFirst();

                sprites.removeFirst();
                firstFloor.setPosX(sprites.getLast().getPosX() + WIDTH);
                sprites.add(firstFloor);
            }
        }
    }

    public void render() {
        for (Sprite floor : sprites)
            floor.render();
    }
}