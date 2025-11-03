package scenes;

import java.awt.Graphics;

import main.Game;

public class Playing extends GameScene implements SceneMethods {

    public Playing(Game game) {
        super(game);
    }

    @Override
    public void render(Graphics g) {
        // Rendering logic for the playing scene
        g.drawString("Playing Scene", 100, 100);
    }
    
}
