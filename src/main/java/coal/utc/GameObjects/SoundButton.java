package coal.utc.GameObjects;


import coal.utc.Controller.SoundController;
import coal.utc.View.Asset;
import coal.utc.View.Renderer;
import coal.utc.View.Sprite;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;

public class SoundButton implements GameObject {
    private final int WIDTH = 32;
    private final int HEIGHT = 32;
    private Asset soundOnAsset = new Asset("/Assets/images/soundOn.png", WIDTH, HEIGHT);
    private Asset soundOffAsset = new Asset("/Assets/images/soundOff.png", WIDTH, HEIGHT);
    private Sprite sprite;
    private SoundController soundController = SoundController.getInstance();
    private double posX, posY;

    public SoundButton(double screenWidth, GraphicsContext ctx) {


        posX = screenWidth - WIDTH - 10;
        posY = 10;

        sprite = new Sprite(soundController.isSoundEnabled() ? soundOnAsset : soundOffAsset);
        sprite.setPos(posX, posY);
        sprite.setVel(0, 0);
        sprite.setCtx(ctx);
    }

    public boolean checkClick(double posX, double posY) {
        return sprite.intersects(new Rectangle2D(posX, posY, 1, 1));
    }

    public void toggleSound() {
        System.out.println("Sound button clicked! Current sound state: " + soundController.isSoundEnabled());
        soundController.toggleSound();
        sprite.changeImage(soundController.isSoundEnabled() ? soundOnAsset : soundOffAsset);
        Renderer renderer = Renderer.getInstance();
        renderer.refreshGameObjects();
    }

    public void repositionButton(double screenWidth) {
        posX = screenWidth - WIDTH - 10;
        sprite.setPosX(posX);
        sprite.render();
    }

    @Override
    public void update(long now) {
    }

    @Override
    public void render() {
        sprite.render();
    }
}