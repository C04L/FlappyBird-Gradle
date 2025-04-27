package coal.utc.GameObjects;


import coal.utc.Controller.DatabaseController;
import coal.utc.Controller.SoundController;
import coal.utc.View.Renderer;
import coal.utc.View.Sprite;

public class GameState {
    private static GameState instance;
    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private int score = 0;
    private int highscore = 0;
    private Sprite[] activePipes;
    private int difficulty = 0;
    private SoundController sound = SoundController.getInstance();
    private DatabaseController db = DatabaseController.getInstance();

    private GameState() {
        updateHighScoreFromDB();
    }

    public static GameState getInstance() {
        if (instance == null) {
            instance = new GameState();
        }
        return instance;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
        if (gameStarted) {
            sound.playBackgroundMusic();
        }
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
        if (gameEnded) {
            sound.stopBackgroundMusic();
        }
        if (score > 0) {
            db.saveScore(score, difficulty);
            updateHighScoreFromDB();
        }
    }

    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore() {
        this.score++;
        if (this.score > this.highscore) {
            this.highscore = this.score;
        }
    }

    public int getHighscore() {
        return highscore;
    }

    public void setActivePipes(Sprite[] pipes) {
        this.activePipes = pipes;
    }

    public Sprite[] getActivePipes() {
        return activePipes;
    }

     public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        Renderer renderer = Renderer.getInstance();
        System.out.println("Difficulty set to: " + difficulty);
        this.difficulty = difficulty;
        updateHighScoreFromDB();
        renderer.updateGameObjects(0);
    }


    public void resetGame() {
        gameStarted = false;
        gameEnded = false;
        score = 0;
        updateHighScoreFromDB();
    }

    private void updateHighScoreFromDB() {
        this.highscore = db.getHighScore(difficulty);
    }
}
