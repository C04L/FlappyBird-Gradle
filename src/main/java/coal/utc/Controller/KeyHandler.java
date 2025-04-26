package coal.utc.Controller;


import coal.utc.GameObjects.*;
import coal.utc.View.Renderer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyHandler {
    private Renderer renderer;
    private GameState gameState;
    private Bird bird;
    private Restart restart;
    private Difficulty difficulty;
    private SoundButton soundButton;

    public KeyHandler(Renderer renderer, double width, double height) {
        this.renderer = renderer;
        this.gameState = GameState.getInstance();
        this.bird = renderer.getBird();
        this.restart = renderer.getRestart();
        this.difficulty = renderer.getDifficulty();
        this.soundButton = renderer.getSoundButton();
    }

    public void handleKeyInput(KeyEvent e) {
        System.out.println("Key pressed: " + e.getCode());
        if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.UP) {
            handleInput(-1, -1);
        }

        if (!gameState.isGameStarted()) {
            if (e.getCode() == KeyCode.LEFT) {
                difficulty.decreaseDifficulty();
                renderer.refreshGameObjects();
                updateGameObjectReferences();
            } else if (e.getCode() == KeyCode.RIGHT) {
                difficulty.increaseDifficulty();
                renderer.refreshGameObjects();
                updateGameObjectReferences();
            }
        }
    }
    public void handleMouseInput(double posX, double posY) {
        if (soundButton.checkClick(posX, posY)) {
            soundButton.toggleSound();
            return;
        }

        handleInput(posX, posY);
    }

    private void handleInput(double posX, double posY) {
        if (!gameState.isGameEnded()) {
            bird.jumpHandler();
            gameState.setGameStarted(true);
        } else if (posX == -1 && posY == -1 || restart.checkClick(posX, posY)) {
            gameState.resetGame();
            renderer.refreshGameObjects();
            updateGameObjectReferences();
        }
    }

    public void updateGame(long now) {
        renderer.updateGameObjects(now);
    }


    /*
    * Sau khi xóa gameObject thông qua refreshGameObjects
    * thì keyHandler vẫn đang ở instance cũ, hàm này sẽ trỏ lại
    * handler đến instance mới được khởi tạo
    *
    * ĐỪNG SỬA - C04:L
    * */
    private void updateGameObjectReferences() {
        this.bird = renderer.getBird();
        this.restart = renderer.getRestart();
        this.difficulty = renderer.getDifficulty();
        this.soundButton = renderer.getSoundButton();
    }
}
