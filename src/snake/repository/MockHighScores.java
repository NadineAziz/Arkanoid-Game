package snake.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MockHighScores implements AbstractHighScoreRepository {

    private List<HighScore> repo = new ArrayList<>();
    
    @Override
    public void putHighScore(String name, int score) throws SQLException {
        repo.add(new HighScore(name, score));
    }

    @Override
    public List<HighScore> getHighScores() throws SQLException {
        return repo;
    }
    
}
