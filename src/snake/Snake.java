package snake;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;


public class Snake {
    
    private final List<Sprite> pieces = new ArrayList<>();
    private final int unitSize;
    private final Image head = new ImageIcon("data/images/paddle.png").getImage();
    private final Image tail = new ImageIcon("data/images/paddle.png").getImage();
    private final Image piece = new ImageIcon("data/images/brick08.png").getImage();
    private final int centerX;
    private final int centerY;
    
    /**
     * Sets the pos of the snake when the game starts/restarts
     * @param centerX
     * @param centerY
     * @param unitSize 
     */
    public Snake(int centerX, int centerY, int unitSize) {
        this.unitSize = unitSize;
        this.centerX = centerX;
        this.centerY = centerY;
        pieces.add(new Sprite(centerX, centerY, unitSize, unitSize, head));
        pieces.add(new Sprite(centerX, centerY - unitSize, unitSize, unitSize, tail));
    }
    /**
     * When the game start/restarts the pos of the snake is set to the center again
     */
    public void reset() {
        pieces.clear();
        pieces.add(new Sprite(centerX, centerY, unitSize, unitSize, head));
        pieces.add(new Sprite(centerX, centerY - unitSize, unitSize, unitSize, tail));
    }
    
    /**
     * Checks if snake collides with an apple
     * @param apple
     * @param direction
     * @return 
     */
    public boolean checkColisionApple(Sprite apple, char direction) {
        if (pieces.get(0).collides(apple)) {
            feed(direction);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Checks if snake collides with an rock
     * @param rock
     * @return 
     */
    public boolean checkColisionWithRock(Sprite rock) {
        System.out.println(rock.toString() + " " + pieces.get(0).toString());
        return pieces.get(0).collides(rock);
    }
    /**
     * Checks if snake collides with itself
     * @return 
     */
    public boolean checkSelfColision() {
        Sprite head = pieces.get(0);
        for (int i = 1; i < pieces.size(); i++) {
            if (head.collides(pieces.get(i))) {
                return true;
            }
        }
        return false;
    }
    /**
     * Checks if snake collides with the boarder of the screen
     * @return 
     */
    public boolean checkEnd() {
        //System.out.println(pieces.get(0));
        Sprite head = pieces.get(0);
        
        if (head.getX() < 0) {
            return true;
        }
        
        if (head.getX() > GameEngine.SCREEN_WIDTH) {
            return true;
        }
        
        if (head.getY() < 0) {
            return true;
        }
        
        if (head.getY() > GameEngine.SCREEN_HEIGHT) {
            return true;
        }
        
        return false;
    }
    /**
     * Movement of the snake around the screen
     * @param direction 
     */
    public void move(char direction) {
        for (int i = pieces.size() - 1; i > 0; i--) {
            pieces.get(i).setX(pieces.get(i - 1).getX());
            pieces.get(i).setY(pieces.get(i - 1).getY());
        }
        
        Sprite head = pieces.get(0);
        movePiece(pieces.get(0), direction);
    }
    
    /**
     * Movement of the head of the snake around the screen
     * @param piece
     * @param direction 
     */
    private void movePiece(Sprite piece, char direction) {
        switch(direction) {
            case 'U': 
                piece.setY(piece.getY() - unitSize);
                break;
            case 'D': 
                piece.setY(piece.getY() + unitSize);
                break;
            case 'L': 
                piece.setX(piece.getX() - unitSize);
                break;
            case 'R': 
                piece.setX(piece.getX() + unitSize);
                break;
        } 
    }
    
    /**
     * If snake's head collided with an apple, adds one more unit to the snake's 
     * body and moves the head in front
     * @param direction 
     */
    public void feed(char direction) {
        /*Add new piece to the snake*/
        Sprite head = pieces.get(0);
        Sprite newPiece = new Sprite(head.getX(), head.getY(), unitSize, unitSize, piece);
        pieces.add(1, newPiece);
        
        for (int i = pieces.size() - 1; i > 1; i--) {
            pieces.get(i).setX(pieces.get(i - 1).getX());
            pieces.get(i).setY(pieces.get(i - 1).getY());
        }
        movePiece(newPiece, direction);
        movePiece(head, direction);
        movePiece(head, direction);

    }
    /**
     * Draws pieces of the snake
     * @param g 
     */
    public void draw(Graphics g) {
        for (Sprite piece: pieces) {
            piece.draw(g);
        }
    }
    
}
