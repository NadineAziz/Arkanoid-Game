package snake;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static snake.GameEngine.SCREEN_HEIGHT;
import static snake.GameEngine.SCREEN_WIDTH;
import static snake.GameEngine.UNIT_SIZE;
import snake.repository.AbstractHighScoreRepository;
import snake.repository.*;

public class SnakePanel extends JFrame {

    private GameEngine gameArea;
    private AbstractHighScoreRepository scoreRepo;

    public SnakePanel() throws SQLException {
        super("Snake");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //scoreRepo = new HighScores(10);
        scoreRepo = new MockHighScores();

        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem startMenuItem, highScoresMenuItem;

        menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        startMenuItem = new JMenuItem("Start");
        highScoresMenuItem = new JMenuItem("Scores");

        menu.add(startMenuItem);
        menu.add(highScoresMenuItem);
        menuBar.add(menu);

        setJMenuBar(menuBar);
        gameArea = new GameEngine(scoreRepo);
        getContentPane().add(gameArea);
        
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        
        container.setVisible(false);
        container.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        

        startMenuItem.addActionListener(e -> {
            gameArea.setVisible(true);
            gameArea.start();
            container.setVisible(false);
           
        });

        /**
         * Saves the high scores from the game and insert it into a "table"
         */
        highScoresMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                container.setVisible(true);
                gameArea.setVisible(false);
                String[] captions = {"Username", "Score"};
                
                List<HighScore> scores = new ArrayList<>();
                
                try {
                    scores = scoreRepo.getHighScores();
                } catch (SQLException ex) {
                    Logger.getLogger(SnakePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Object[][] data = new Object[scores.size()][2];
                for (int i = 0; i < scores.size(); i++) {
                    data[i][0] = scores.get(i).getName();
                    data[i][1] = scores.get(i).getScore();
                }
                
                JTable table = new JTable(data, captions);
                container.removeAll();
                container.add(table.getTableHeader(), BorderLayout.PAGE_START);
                container.add(table, BorderLayout.CENTER);
                table.setFillsViewportHeight(true);
                getContentPane().add(container);
            }
        });

        setPreferredSize(new Dimension(SCREEN_WIDTH + UNIT_SIZE, SCREEN_HEIGHT + 75));
        setResizable(false);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

}
