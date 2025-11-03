package main;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class Render {
    
    private GameScreen gameScreen;
    private Random random;
    private BufferedImage img;
    
    private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();

    public Render(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        random = new Random();
        importImg();
        LoadSprite();
    }

    public void render(Graphics g) {
        
        switch (GameStates.gameState) { 
            case MENU:
                for ( int i = 0; i < 20; i++) {
                    for (int j = 0; j < 20; j++) {
                        random = new Random();
                        g.drawImage(sprites.get(random.nextInt(sprites.size())), i * 32, j * 32, null);
                    }
                }
                break;
            case PLAYING:

                break;
            case SETTINGS:

                break;
        
            default:
                break;
        }
    }

    private void LoadSprite() {
        for ( int y = 0; y < 3; y++) {
            for( int x = 0; x < 10; x++) {
                sprites.add(img.getSubimage(32*x, 32*y, 32, 32));
            }
        }

    }

    private void importImg() {
		InputStream is = getClass().getResourceAsStream("/spriteatlas.png");

		// If not found on the classpath, try loading from a common 'res' folder on the filesystem (useful for simple setups)
		if (is == null) {
			try {
				is = new FileInputStream("res/spriteatlas.png");
			} catch (FileNotFoundException e) {
			}
		}

		try {
			img = ImageIO.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try { is.close(); } catch (Exception ignore) {}
		}
	}
}
