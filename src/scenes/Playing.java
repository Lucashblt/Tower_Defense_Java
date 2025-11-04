package scenes;

import java.awt.Graphics;

import helper.LevelBuild;
import main.Game;
import managers.TileManager;
import ui.ActionBar;

public class Playing extends GameScene implements SceneMethods {

    private int[][] level;
    private TileManager tileManager;
    private ActionBar actionBar;
    private int mouseX, mouseY;

    public Playing(Game game) {
        super(game);

        level = LevelBuild.getLevelData();
        tileManager = new TileManager();
        actionBar = new ActionBar(0, 640, 640, 100, this);
        initButtons();
    }
 
    private void initButtons() {

    }


    @Override
    public void render(Graphics g) {
        drawLevel(g);
        actionBar.draw(g);
    }

    private void drawLevel(Graphics g) {
        for ( int y = 0; y < level.length; y++) {
            for ( int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640)
			actionBar.mouseClicked(x, y);
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640)
            actionBar.mouseMoved(x, y);
        else {
			mouseX = (x / 32) * 32;
			mouseY = (y / 32) * 32;
		}
    }  
    @Override
    public void mousePressed(int x, int y) {
        if (y >= 640)
            actionBar.mousePressed(x, y);
    }  
    @Override
    public void mouseReleased(int x, int y) {
        actionBar.mouseReleased(x, y);
    }
}
