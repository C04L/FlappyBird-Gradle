package coal.utc.Controller;

import java.sql.*;

public class DatabaseController {

    private static DatabaseController instance;
    private Connection connection;
    private final String url = "jdbc:sqlite:src/main/resources/database/score.db";


    private DatabaseController() {
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            System.out.println("Connecting to database...");
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection(url);

            String createTable = "create table if not exists scores (" +
                    "id integer primary key autoincrement," +
                    "score integer not null," +
                    "difficulty integer not null," +
                    "created_at datetime default current_timestamp)";

            try (Statement statement = connection.createStatement()) {
                statement.execute(createTable);
                System.out.println("Database initialized successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getHighScore(int difficulty) {
        int highScore = 0;
        String query = "SELECT MAX(score) as score FROM scores WHERE difficulty = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, difficulty);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    highScore = resultSet.getInt("score");
                }
            } catch (SQLException e) {
                System.out.println("Error retrieving high score: " + e.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return highScore;
    }

    public boolean isHighScore(int score, int difficulty) {
        int highScore = getHighScore(difficulty);
        return score > highScore;
    }

    public void saveScore(int score, int difficulty) {

        if (!isHighScore(score, difficulty)) return;

        String insertScore = "INSERT INTO scores (score, difficulty) VALUES (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertScore)) {
            statement.setInt(1, score);
            statement.setInt(2, difficulty);
            statement.executeUpdate();
            System.out.println("Saving new high score: " + score + " for difficulty: " + difficulty);
        } catch (SQLException e) {
            System.out.println("Error saving score: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing database connection: " + e.getMessage());
        }
    }

    public static DatabaseController getInstance() {
        if (instance == null) {
            instance = new DatabaseController();
        }
        return instance;
    }

}
