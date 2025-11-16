package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import main.Game;
import main.GameStates;

public class MyMouseListener implements MouseListener, MouseMotionListener{
    
    private Game game;

    public MyMouseListener(Game game) {
        this.game = game;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {

            switch (GameStates.gameState) {
                case MENU:
                    game.getMenu().mouseClicked(e.getX(), e.getY());
                    break;

                case PLAYING:
                    game.getPlaying().mouseClicked(e.getX(), e.getY());
                    break;

                case SIMULATION_PERFORMANCE:
                    game.getSimulationPerformance().mouseClicked(e.getX(), e.getY());
                    break;
                
                default:
                    break;
            }

        }        
    }

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        switch (GameStates.gameState) {
                case MENU:
                    game.getMenu().mousePressed(e.getX(), e.getY());
                    break;

                case PLAYING:
                    game.getPlaying().mousePressed(e.getX(), e.getY());
                    break;

                case SIMULATION_PERFORMANCE:
                    game.getSimulationPerformance().mousePressed(e.getX(), e.getY());
                    break;
                
                default:
                    break;
            }        
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        switch (GameStates.gameState) {
                case MENU:
                    game.getMenu().mouseReleased(e.getX(), e.getY());
                    break;

                case PLAYING:
                    game.getPlaying().mouseReleased(e.getX(), e.getY());
                    break;

                case SIMULATION_PERFORMANCE:
                    game.getSimulationPerformance().mouseReleased(e.getX(), e.getY());
                    break;
                
                default:
                    break;
            }
    }

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {
         switch (GameStates.gameState) {
                case MENU:
                    game.getMenu().mouseMoved(e.getX(), e.getY());
                    break;

                case PLAYING:
                    game.getPlaying().mouseMoved(e.getX(), e.getY());
                    break;

                case SIMULATION_PERFORMANCE:
                    game.getSimulationPerformance().mouseMoved(e.getX(), e.getY());
                    break;
                
                default:
                    break;
            }        
    }
}
