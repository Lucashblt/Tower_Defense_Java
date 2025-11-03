package scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Game;
import ui.MyButton;
import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods {

    private BufferedImage img;
    
    private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();

    private MyButton bPlaying, bSettings, bQuit;

    public Menu(Game game) {
        super(game);

        importImg();
        LoadSprite();
        initButtons();
    }

    private void initButtons() {
        int w = 150;
        int h = w / 3;
        int x = 640 / 2 - w / 2;
        int y = 150;
        int yOffset = 100;

        bPlaying = new MyButton("PLAY", x, y, w, h);
        bSettings = new MyButton("SETTINGS", x, y + yOffset, w, h);
        bQuit = new MyButton("QUIT", x, y + yOffset * 2, w, h);
    }

    @Override
    public void render(Graphics g) {
        bPlaying.draw(g);
        bSettings.draw(g);
        bQuit.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {    
        if(bPlaying.getBounds().contains(x, y)) {
            setGameState(PLAYING);
        } else if (bSettings.getBounds().contains(x, y)) {
            setGameState(SETTINGS);
        } else if (bQuit.getBounds().contains(x, y)) {
            System.exit(0);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        bPlaying.setMouseOver(false);
        bSettings.setMouseOver(false);
        bQuit.setMouseOver(false);

        if(bPlaying.getBounds().contains(x, y)) {
            bPlaying.setMouseOver(true);
        } else if (bSettings.getBounds().contains(x, y)) {
            bSettings.setMouseOver(true);
        } else if (bQuit.getBounds().contains(x, y)) {
            bQuit.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if(bPlaying.getBounds().contains(x, y)) {
            bPlaying.setMousePressed(true);
        } else if (bSettings.getBounds().contains(x, y)) {
            bSettings.setMousePressed(true);
        } else if (bQuit.getBounds().contains(x, y)) {
            bQuit.setMousePressed(true);
        }
    }
    @Override
    public void mouseReleased(int x, int y) {
        resetButtons();
    }

    private void resetButtons() {
        bPlaying.resetBooleans();
        bSettings.resetBooleans();
        bQuit.resetBooleans();
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
