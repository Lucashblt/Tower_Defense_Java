package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GameScreen  extends JPanel{

    private Dimension size;
    private Render render;
    private Game game;

    public GameScreen(Game game) {
        this.game = game;
        render = new Render(this);
        setPreferredSize();    
    }

    private void setPreferredSize() {
        size = new Dimension(640, 640);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        render .render(g);
    }
}
