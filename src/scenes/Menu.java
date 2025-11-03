package scenes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Game;
import ui.MyButton;
import static main.GameStates.*;

public class Menu extends GameScene implements SceneMethods {

    private BufferedImage img, menuImg;
    
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
        int y = 350;
        int yOffset = 75;

        bPlaying = new MyButton("PLAY", x, y, w, h);
        bSettings = new MyButton("SETTINGS", x, y + yOffset, w, h);
        bQuit = new MyButton("QUIT", x, y + yOffset * 2, w, h);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(menuImg, 0, 0, null);

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
        // load sprite atlas
        InputStream is = getClass().getResourceAsStream("/spriteatlas2.png");
        if (is == null) {
            try {
                is = new FileInputStream("res/spriteatlas2.png");
            } catch (FileNotFoundException e) {
                is = null;
            }
        }
        try {
            if (is != null) {
                img = ImageIO.read(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (is != null) is.close(); } catch (Exception ignore) {}
        }

        // load menu background image (menuimg.png) from same locations
        InputStream is2 = getClass().getResourceAsStream("/menuimg.png");
        if (is2 == null) {
            try {
                is2 = new FileInputStream("res/menuimg.png");
            } catch (FileNotFoundException e) {
                is2 = null;
            }
        }
        try {
            if (is2 != null) {
                menuImg = ImageIO.read(is2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (is2 != null) is2.close(); } catch (Exception ignore) {}
        }
    }
}
