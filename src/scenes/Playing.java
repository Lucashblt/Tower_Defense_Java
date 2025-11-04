package scenes;

import java.awt.Graphics;

import helper.LevelBuild;
import main.Game;
import managers.EnemyManager;
import managers.TileManager;
import ui.ActionBar;

public class Playing extends GameScene implements SceneMethods {

    private int[][] level;
    private TileManager tileManager;
    private EnemyManager enemyManager;
    private ActionBar actionBar;
    private int mouseX, mouseY;

    public Playing(Game game) {
        super(game);

        level = LevelBuild.getLevelData();
        tileManager = new TileManager();
        actionBar = new ActionBar(0, 640, 640, 100, this);
        enemyManager = new EnemyManager(this);
        initButtons();
    }
 
    private void initButtons() {

    }

    public void update() {
        enemyManager.update();
    }

    @Override
    public void render(Graphics g) {
        drawLevel(g);
        actionBar.draw(g);
        enemyManager.draw(g);
    }

    private void drawLevel(Graphics g) {
        for ( int y = 0; y < level.length; y++) {
            for ( int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
    }

    public int getTileType(int x, int y) {
        int xCord = x / 32;
        int yCord = y / 32;

        if (xCord < 0 || xCord > 19)
            return 0;
        if (yCord < 0 || yCord > 19)
            return 0;

        int id = level[y / 32][x / 32];
        return game.getTileManager().getTile(id).getTileType();
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
