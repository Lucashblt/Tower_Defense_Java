package scenes;

import java.awt.Graphics;

import main.Game;
import ui.MyButton;
import static main.GameStates.*;

public class Settings extends GameScene implements SceneMethods {

    private MyButton bMenu;

    public Settings(Game game) {
        super(game);
        initButtons();
    }

    private void initButtons() {
        int w = 100;
        int h = 32;
        int x = 10;
        int y = 10;
        bMenu = new MyButton("MENU", x, y, w, h);
    }

    @Override
    public void render(Graphics g) {
        // Draw back-to-menu button
        bMenu.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            setGameState(MENU);
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        bMenu.setMouseOver(false);
        if (bMenu.getBounds().contains(x, y)) {
            bMenu.setMouseOver(true);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (bMenu.getBounds().contains(x, y)) {
            bMenu.setMousePressed(true);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        bMenu.resetBooleans();
    }
    
}
