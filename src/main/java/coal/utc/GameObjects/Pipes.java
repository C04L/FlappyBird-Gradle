package coal.utc.GameObjects;

import java.util.ArrayList;


import coal.utc.View.Asset;
import coal.utc.View.Sprite;
import javafx.scene.canvas.GraphicsContext;
import java.util.concurrent.ThreadLocalRandom;

public class Pipes implements GameObject {
    private final int WIDTH = 62;
    private final int HEIGHT = 2000;
    private final Asset assetUp = new Asset("/Assets/images/up_pipe.png", WIDTH, HEIGHT);
    private final Asset assetDown = new Asset("/Assets/images/down_pipe.png", WIDTH, HEIGHT);
    private final ArrayList<Sprite> spritesUp = new ArrayList<>();
    private final ArrayList<Sprite> spritesDown = new ArrayList<>();
    private final GameState gameState = GameState.getInstance();

    private final double screenHeight;
    private final double screenWidth;
    private final GraphicsContext ctx;

    private final int PIPE_GAP = 400;
    private final int MIN_VERTICAL_GAP = 160;
    private final int MAX_VERTICAL_GAP = 250;
    private final int FLOOR_HEIGHT = 112;
    private final int MIN_TOP_MARGIN = 50;

    public Pipes(double screenWidth, double screenHeight, GraphicsContext ctx) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.ctx = ctx;

        Sprite[] pipes = createPipes(screenWidth + 200);
        gameState.setActivePipes(pipes);
        spritesUp.add(pipes[0]);
        spritesDown.add(pipes[1]);
    }

    public void update(long now) {
        if (gameState.isGameStarted()) {
            for (int i = 0; i < spritesUp.size(); i++) {
                spritesUp.get(i).update();
                spritesDown.get(i).update();

                Sprite[] activePipes = gameState.getActivePipes();
                if (activePipes != null && activePipes[0].getPosX() + WIDTH < screenWidth / 2 - 56) {
                    gameState.setActivePipes(new Sprite[] { spritesUp.get(i), spritesDown.get(i) });
                }
            }
        }
    }

    public void render() {
        for (Sprite pipe : spritesUp)
            pipe.render();

        for (Sprite pipe : spritesDown)
            pipe.render();

        if (spritesUp.getLast().getPosX() < screenWidth) {
            Sprite[] pipes = createPipes(spritesUp.getLast().getPosX() + PIPE_GAP);

            spritesUp.add(pipes[0]);
            spritesDown.add(pipes[1]);
        }

        if (spritesUp.getFirst().getPosX() < -WIDTH) {
            spritesUp.removeFirst();
            spritesDown.removeFirst();
        }
    }

    private Sprite[] createPipes(double posX) {
        double availableHeight = screenHeight - FLOOR_HEIGHT - MIN_TOP_MARGIN;

        int verticalGapSize = ThreadLocalRandom.current().nextInt(MIN_VERTICAL_GAP, MAX_VERTICAL_GAP + 1);

        int maxGapPosition = (int)(availableHeight - verticalGapSize);
        int gapPosition = ThreadLocalRandom.current().nextInt(0, maxGapPosition + 1);

        /*
        * Tọa độ Y được tính 0 từ trên xuống dưới
        * nên phải tính từ đấy, tọa độ của ống dưới
        * được tính dựa trên ống trên
        * 
        * NOTE: C04:L
        * */

        double topPipePosition = -(HEIGHT - MIN_TOP_MARGIN - gapPosition);
        double bottomPipePosition = MIN_TOP_MARGIN + gapPosition + verticalGapSize;


        Sprite pipeUp = new Sprite(assetUp);
        pipeUp.setPos(posX, bottomPipePosition);
        pipeUp.setVel(-1.5, 0);
        pipeUp.setCtx(ctx);

        Sprite pipeDown = new Sprite(assetDown);
        pipeDown.setPos(posX, topPipePosition);
        pipeDown.setVel(-1.5, 0);
        pipeDown.setCtx(ctx);

        return new Sprite[] { pipeUp, pipeDown };
    }
}