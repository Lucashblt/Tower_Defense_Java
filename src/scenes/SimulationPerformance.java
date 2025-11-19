package scenes;

import java.awt.Graphics;

import main.Game;
import managers.SimulationPerformanceManager;
import ui.SimulationBar;
import static main.GameStates.*;

public class SimulationPerformance extends GameScene implements SceneMethods {

    private SimulationBar simulationBar;
    private SimulationPerformanceManager manager;
    private boolean simulationStarted = false;

    public SimulationPerformance(Game game) {
        super(game);
        simulationBar = new SimulationBar(0, 640, 640, 100);
        manager = new SimulationPerformanceManager(this, game.getTileManager());
    }
    
    /**
     * Appelé quand on entre dans cette scène pour réinitialiser l'état
     */
    public void onEnter() {
        System.out.println("Entrée dans SimulationPerformance");
        if (simulationStarted) {
            // Réinitialiser pour permettre un nouveau démarrage
            manager.reset();
            simulationStarted = false;
        }
    }

    public void update() {
        // Démarrer la simulation au premier update
        if (!simulationStarted) {
            System.out.println("Démarrage de la simulation...");
            manager.startSimulation();
            simulationStarted = true;
        }
        
        if (manager.isSimulationRunning()) {
            manager.update();
            
            // Mettre à jour l'UI
            simulationBar.setElapsedTime(manager.getElapsedTime());
            simulationBar.setAliveEnemies(manager.getAliveEnemiesCount());
            simulationBar.setTotalSpawned(manager.getTotalEnemiesSpawned());
            simulationBar.setPaused(manager.isPaused());
        }
    }

    @Override
    public void render(Graphics g) {
        // Dessiner le jeu
        manager.draw(g);
        
        // Dessiner la barre d'interface
        simulationBar.draw(g);
    }

    @Override
    public void mouseClicked(int x, int y) {
        if (y >= 640) {
            if (simulationBar.getBtnMenu().getBounds().contains(x, y)) {
                manager.reset();
                simulationStarted = false;
                setGameState(MENU);
            } else if (simulationBar.getBtnPause().getBounds().contains(x, y)) {
                manager.togglePause();
            }
        }
    }

    @Override
    public void mouseMoved(int x, int y) {
        if (y >= 640) {
            simulationBar.mouseMoved(x, y);
        }
    }

    @Override
    public void mousePressed(int x, int y) {
        if (y >= 640) {
            simulationBar.mousePressed(x, y);
        }
    }

    @Override
    public void mouseReleased(int x, int y) {
        simulationBar.mouseReleased(x, y);
    }
    
}
