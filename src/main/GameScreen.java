package main;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import inputs.KeyboardListener;
import inputs.MyMouseListener;

public class GameScreen  extends JPanel{

    private Dimension size;
    private Game game;

	private MyMouseListener myMouseListener;
	private KeyboardListener keyboardListener;

    public GameScreen(Game game) {
        this.game = game;
        setPreferredSize();    
    }
    
    public void initInputs() {
		myMouseListener = new MyMouseListener(game);
		keyboardListener = new KeyboardListener(game);

		this.addMouseListener(myMouseListener);
		this.addMouseMotionListener(myMouseListener);
		this.addKeyListener(keyboardListener);

		requestFocus();
	}

    private void setPreferredSize() {
        size = new Dimension(640, 740);
        setMinimumSize(size);
        setMaximumSize(size);
        setPreferredSize(size);
    }    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        game.getRender().render(g);
    }
}
