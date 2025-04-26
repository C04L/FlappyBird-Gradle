package coal.utc.GameObjects;


import coal.utc.Controller.SoundController;
import coal.utc.View.Asset;
import coal.utc.View.Sprite;
import javafx.scene.canvas.GraphicsContext;


public class Bird implements GameObject {
    private final int WIDTH = 56;
    private final int HEIGHT = 40;
    private double rotation = 0;
    private final double MAX_ROTATION_UP = -30;
    private final double MAX_ROTATION_DOWN = 55;
    private final double ROTATION_SPEED = 1;
    private Asset assets[] = {
        new Asset("/Assets/images/bird1.png", WIDTH, HEIGHT),
        new Asset("/Assets/images/bird2.png", WIDTH, HEIGHT),
        new Asset("/Assets/images/bird3.png", WIDTH, HEIGHT)
    };
    private Sprite sprite;
    private int currentAssetIndex = 0;
    private long prevTime = 0;
    private float terminalVel; //Tốc độ rơi tối đa của con bird
    private float jumpVel; //Tốc độ nhảy của con bird
    private double screenHeight;
    private GameState gameState = GameState.getInstance();
    private SoundController soundController = SoundController.getInstance();

    public Bird(double screenWidth, double screenHeight, GraphicsContext ctx) {
        this.screenHeight = screenHeight;

        sprite = new Sprite(assets[currentAssetIndex]);
        sprite.setPosX(screenWidth / 2 - WIDTH / 2);
        sprite.setPosY(gameState.isGameEnded() ? screenHeight - 112 - HEIGHT : (screenHeight - 112) / 2);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);
    }

    public void jumpHandler() {
        System.out.println("Bird jumping! Current vel: " + sprite.getVelY() + ", Setting to: " + jumpVel);
        sprite.setVelY(jumpVel);
    }

    public void update(long now) {
        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            updateBirdHovering();
        } else if (gameState.isGameEnded()) {
            updateBirdFalldown();
        } else if (gameState.isGameStarted()) {
            if (now - prevTime > 90000000) {
                updateSprite();
                prevTime = now;
            }


            Sprite[] activePipes = gameState.getActivePipes();
            if ((sprite.getPosY() + HEIGHT) > (screenHeight - 112) ||
                (activePipes != null && activePipes.length >= 2 &&
                 (sprite.intersects(activePipes[0]) || sprite.intersects(activePipes[1])))) {

                gameState.setGameStarted(false);
                gameState.setGameEnded(true);
            }

            applyGravity();
        }

        updateRotation();

        sprite.update();
    }

//    Chim đứng yên
    public void updateBirdHovering() {
        double centerPosY = (screenHeight - 112) / 2;
        double amplitude = 10;
        double period = 0.05;


        double offset = amplitude * Math.sin(period * System.currentTimeMillis() / 10000000);
        sprite.setPosY(centerPosY + offset);
        sprite.setVelY(0);

        rotation = 0;
    }

    public void applyGravity() {
        double vel = sprite.getVelY();

        if (vel < terminalVel)
            sprite.setVelY(vel + 0.2);
        else
            sprite.setVelY(terminalVel);
    }

    public void updateBirdFalldown() {
        if (sprite.getPosY() + HEIGHT >= screenHeight - 112) {
            sprite.setVel(0, 0);
            sprite.setPosY(screenHeight - 112 - HEIGHT);
        } else {
            applyGravity();
        }
    }

    public void updateSprite() {
        currentAssetIndex = (currentAssetIndex + 1) % 3;
        sprite.changeImage(assets[currentAssetIndex]);
    }


    /*
    * Render con chim + xoay tạo cảm giác vật lý :>>
    * */
    public void render() {
        GraphicsContext ctx = sprite.getContext();

        ctx.save();

//        Tọa độ tâm của con chim
        double centerX = sprite.getPosX() + WIDTH / 2;
        double centerY = sprite.getPosY() + HEIGHT / 2;

        ctx.translate(centerX, centerY);

        ctx.rotate(rotation);

        // Ném con chim ra giữa màn hình
        ctx.drawImage(
            sprite.getImage(),
            -WIDTH / 2,
            -HEIGHT / 2,
            WIDTH,
            HEIGHT
        );

        ctx.restore();
    }

    public double getPosX() {
        return sprite.getPosX();
    }

    public double getPosY() {
        return sprite.getPosY();
    }

    private void updateRotation() {
        double velY = sprite.getVelY();

        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            rotation = 0;
            return;
        }

        if (velY < 0) {
            rotation = MAX_ROTATION_UP;
        } else {
            double targetRotation = MAX_ROTATION_DOWN;
            if (rotation < targetRotation) {
                rotation += ROTATION_SPEED;
                if (rotation > targetRotation) rotation = targetRotation;
            }
        }
    }

    public void setTerminalVel(float vel) {
        this.terminalVel = vel;
    }

    public void setJumpVel(float vel) {
        this.jumpVel = vel;
    }
}