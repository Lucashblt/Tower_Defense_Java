package scenes;

import java.awt.Graphics;

import helper.LevelBuild;
import main.Game;
import managers.TileManager;
import ui.MyButton;
import static main.GameStates.*;

public class Playing extends GameScene implements SceneMethods {

    private int[][] level;
    private TileManager tileManager;
    private MyButton bMenu;

    public Playing(Game game) {
        super(game);

        level = LevelBuild.getLevelData();
        tileManager = new TileManager();
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
        for ( int y = 0; y < level.length; y++) {
            for ( int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
        // UI overlay
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
