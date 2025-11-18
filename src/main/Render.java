package main;

import java.awt.Graphics;

public class Render {

    private Game game;

    public Render(Game game) {
        this.game = game;
    }

    public void render(Graphics g) {
        
        switch (GameStates.gameState) { 
            case MENU:
                game.getMenu().render(g);
                break;
            case PLAYING:
                game.getPlaying().render(g);
                break;
            case SIMULATION_PERFORMANCE:
                game.getSimulationPerformance().render(g);
                break;
            case GAME_OVER:
                game.getGameOver().render(g);
                break;
            case GAME_WIN:
                game.getGameWin().render(g);
                break;
            default:
                break;
        }
    }
}
