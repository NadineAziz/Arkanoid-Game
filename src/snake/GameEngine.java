package snake;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import snake.repository.AbstractHighScoreRepository;

public class GameEngine extends JPanel {

    public static final int SCREEN_WIDTH = 600;
    public static final int SCREEN_HEIGHT = 600;
    public static final int UNIT_SIZE = 25;
    private static final int DELAY = 250;

    private final Image background;
    private final Timer newFrameTimer;
    private final Random random;
    private final AbstractHighScoreRepository highScores;

    private char direction = 'D';    // UP, DOWN, LEFT, RIGHT (U, D, L, R)
    private boolean running = true;
    private int applesEaten = 0;

    private Sprite apple;
    private final Snake snake;
    private final java.util.List<Sprite> rocks;
    private final int numberOfRocks;

    /**
     * Saves the game gui and places random rocks and apples when the game starts
     * @param highScores 
     */
    public GameEngine(AbstractHighScoreRepository highScores) {
        super();
        this.rocks = new ArrayList<>();
        background = new ImageIcon("data/images/background.jpg").getImage();
        random = new Random();
        numberOfRocks = random.nextInt(15) + 5;
        newFrameTimer = new Timer(DELAY, new NewFrameListener());
        snake = new Snake(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, UNIT_SIZE);
        newApple();

        this.highScores = highScores;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setFocusable(true);
        //this.addKeyListener(new MyKeyAdapter());

    }
    
    /**
     * Starts the game
     */
    public void start() {
        System.out.println("GameEngine start");
       
        restart();
        newFrameTimer.start();
        running = true;
    }

    /**
     * Restarts the game
     */
    private void restart() {
        newApple();
        this.addKeyListener(new MyKeyAdapter());
        applesEaten = 0;
        rocks.clear();
        snake.reset();
        direction = 'D';

        for (int i = 0; i < numberOfRocks; i++) {
            newRock();
        }
    }

    /**
     * Draws the game gui with the screen size
     * Draws the rock
     * Draws the apple
     * @param g 
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.orange);

        for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
            g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        snake.draw(g);

        rocks.forEach(rock -> {
            rock.draw(g);
        });
        apple.draw(g);

        if (!running) {
            gameOver(g);
        }

    }
    /**
     * Moves the snake around
     */
    private void move() {
        snake.move(direction);
    }

    /**
     * Places a new apple anywhere on the screen randomly
     */
    public void newApple() {
        int appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        apple = new Sprite(appleX, appleY, UNIT_SIZE, UNIT_SIZE, new ImageIcon("data/images/apple.jpg").getImage());

    }

    /**
     * Places a new rock anywhere on the screen randomly
     */
    private void newRock() {
        int rockX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int rockY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        Sprite rock = new Sprite(rockX, rockY, UNIT_SIZE, UNIT_SIZE, new ImageIcon("data/images/rock.png").getImage());
        rocks.add(rock);
    }

    /**
     * Moves the snake around the screen according to the keyboard buttons
     */
    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    /**
     * Displays a gameover when the player loses and saves the player's score
     * @param g 
     */
    public void gameOver(Graphics g) {
        newFrameTimer.stop();
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 45));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String text = "Game Over, score: " + applesEaten;
        g.drawString(text,
                (SCREEN_WIDTH - metrics.stringWidth(text)) / 2,
                SCREEN_HEIGHT / 2);

        saveScore();
    }

    /**
     * Saves username and score of player then restarts game
     */
    private void saveScore() {
        String showInputDialog = JOptionPane.showInputDialog("Username: ", "");

        try {
            highScores.putHighScore(showInputDialog, applesEaten);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        restart();

    }

    /**
     * Checks if the snake hit a rock
     * @return true if hit
     */
    private boolean checkColisionWithRock() {
        if (rocks.stream().anyMatch(rock -> (snake.checkColisionWithRock(rock)))) {
            return true;
        }
        return false;
    }

    /**
     * Makes sure that while the game is going on the snake didn't hit any rock 
     * or collide with itself
     */
    class NewFrameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            if (running) {
                move();

                if (snake.checkColisionApple(apple, direction)) {
                    applesEaten++;
                    newApple();
                }

                if (checkColisionWithRock() || snake.checkSelfColision() || snake.checkEnd()) {
                    running = false;
                }

            }
            repaint();
        }

    }
}
