package scenes;

import static main.GameStates.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;
import ui.MyButton;

public class GameWin extends GameScene implements SceneMethods {

    private MyButton btnMenu, btnReplay;
    private BufferedImage img;

    public GameWin(Game game) {
        super(game);
        initButtons();  
        importImg();
    }

    private void initButtons() {
        int w = 200;
        int h = 50;
        int x = 640 / 2 - w / 2;
        int y = 400;
        int yOffset = 75;

        btnMenu = new MyButton("Menu", x, y, w, h);
        btnReplay = new MyButton("Replay", x, y + yOffset, w, h);
    }

    private void importImg() {
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
                img = ImageIO.read(is2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (is2 != null) is2.close(); } catch (Exception ignore) {}
        }
    }

    private void drawGameWinText(Graphics g) {
        g.setColor(java.awt.Color.GREEN);
        g.setFont(new Font("LucidaSans", Font.BOLD, 50));
        g.drawString("YOU WIN!", 195, 370);
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img, 0, 0, null);

        drawGameWinText(g);
         
        btnMenu.draw(g);
        btnReplay.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (btnMenu.getBounds().contains(x, y)) {
            game.getPlaying().resetAll();
            setGameState(main.GameStates.MENU);
        } else if (btnReplay.getBounds().contains(x, y)) {
            game.getPlaying().resetAll();
            setGameState(main.GameStates.PLAYING);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        btnMenu.setMouseOver(false);
        btnReplay.setMouseOver(false);

        if (btnMenu.getBounds().contains(x, y)) {
            btnMenu.setMouseOver(true);
        } else if (btnReplay.getBounds().contains(x, y)) {
            btnReplay.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (btnMenu.getBounds().contains(x, y)) {
            btnMenu.setMousePressed(true);
        } else if (btnReplay.getBounds().contains(x, y)) {
            btnReplay.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        btnMenu.resetBooleans();
        btnReplay.resetBooleans();
    }    
}
