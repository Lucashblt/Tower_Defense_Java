package scenes;

import java.awt.Graphics;

import helper.LevelBuild;
import main.Game;
import managers.TileManager;

public class Playing extends GameScene implements SceneMethods {

    private int[][] level;
    private TileManager tileManager;

    public Playing(Game game) {
        super(game);

        level = LevelBuild.getLevelData();
        tileManager = new TileManager();
    }
 

    @Override
    public void render(Graphics g) {
        for ( int y = 0; y < level.length; y++) {
            for ( int x = 0; x < level[y].length; x++) {
                int id = level[y][x];
                g.drawImage(tileManager.getSprite(id), x * 32, y * 32, null);
            }
        }
    }
    
}
