package coal.utc.GameObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class Difficulty implements GameObject {

    private double posX, posY;
    private GraphicsContext ctx;
    private Font appFont;
    private Color appColor;
    private GameState gameState = GameState.getInstance();

    private final String[] DIFFICULTY_LEVELS = {"EZ", "MD", "EX"};
    private int currentDifficultyIndex = 0;

    public Difficulty(double screenWidth, double screenHeight, GraphicsContext ctx, Font appFont, Color appColor) {
        this.ctx = ctx;
        this.appFont = appFont;
        this.appColor = appColor;

        posX = screenWidth / 2 - appFont.getSize();
        posY = screenHeight - 180;

        ctx.setFont(appFont);
        ctx.setFill(appColor);

        this.currentDifficultyIndex = gameState.getDifficulty();
    }

    public void increaseDifficulty() {
        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            if (currentDifficultyIndex < DIFFICULTY_LEVELS.length - 1) {
                currentDifficultyIndex++;
            } else {
                currentDifficultyIndex = 0;
            }
            gameState.setDifficulty(currentDifficultyIndex);
        }
    }

    public void decreaseDifficulty() {
        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            if (currentDifficultyIndex > 0) {
                currentDifficultyIndex--;
            } else {
                currentDifficultyIndex = DIFFICULTY_LEVELS.length - 1;
            }

            gameState.setDifficulty(currentDifficultyIndex);
        }
    }

    @Override
    public void update(long now) {}

    @Override
    public void render() {
        if (!gameState.isGameStarted() && !gameState.isGameEnded()) {
            ctx.setFill(Color.WHITE);
            ctx.setFont(new Font(appFont.getName(), 24));

            String difficultyText = "< " + DIFFICULTY_LEVELS[currentDifficultyIndex] + " >";


            ctx.fillText(difficultyText, posX, posY);

            ctx.setStroke(appColor);
            ctx.strokeText(difficultyText, posX, posY);
        }
    }

}
