package main;

import java.awt.Graphics;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Random;
import java.awt.image.BufferedImage;

public class GameScreen  extends JPanel{

    private Random random;
    private BufferedImage img;

    private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();

    public GameScreen(BufferedImage img) {
        this.img = img;

        LoadSprite();
    }

    private void LoadSprite() {
        
        for ( int y = 0; y < 3; y++) {
            for( int x = 0; x < 10; x++) {
                sprites.add(img.getSubimage(32*x, 32*y, 32, 32));
            }
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for ( int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                random = new Random();
                g.drawImage(sprites.get(random.nextInt(sprites.size())), i * 32, j * 32, null);
            }
        }
    }
}
