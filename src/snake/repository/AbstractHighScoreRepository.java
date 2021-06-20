package snake.repository;

import java.sql.SQLException;
import java.util.List;

/**
 * Saves for the high scores of the players and saves them in a list
 */
public interface AbstractHighScoreRepository {
    
    void putHighScore(String name, int score) throws SQLException;
    
    List<HighScore> getHighScores() throws SQLException;
    
}
