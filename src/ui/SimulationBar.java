package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class SimulationBar extends Bar {
    
    private MyButton btnMenu, btnPause;
    private long elapsedTime;
    private int aliveEnemies;
    private int totalSpawned; // Total d'ennemis spawnés
    private boolean isPaused;
    
    public SimulationBar(int x, int y, int width, int height) {
        super(x, y, width, height);
        initButtons();
    }
    
    private void initButtons() {
        btnMenu = new MyButton("MENU", x + 10, y + 35, 100, 30);
        btnPause = new MyButton("PAUSE", x + 120, y + 35, 100, 30);
    }
    
    public void draw(Graphics g) {
        // Fond de la barre
        g.setColor(new Color(220, 123, 15));
        g.fillRect(x, y, width, height);
        
        // Dessiner les boutons
        drawButtons(g);
        
        // Dessiner le timer
        drawTimer(g);
        
        // Dessiner le total d'ennemis spawnés
        drawTotalSpawned(g);
        
        // Dessiner le nombre d'ennemis
        drawEnemyCount(g);
        
        // Afficher PAUSE si le jeu est en pause
        if (isPaused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 640, 640);
            g.setColor(Color.WHITE);
            g.setFont(new Font("LucidaSans", Font.BOLD, 50));
            g.drawString("SIMULATION PAUSE", 100, 320);
        }
    }
    
    private void drawButtons(Graphics g) {
        btnMenu.draw(g);
        drawButtonFeedback(g, btnMenu);
        
        btnPause.draw(g);
        drawButtonFeedback(g, btnPause);
    }
    
    private void drawTimer(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        long secs = seconds % 60;
        
        String timeText = String.format("Temps: %02d:%02d / %02d:%02d", minutes, secs, 15, 0);
        g.drawString(timeText, x + 250, y + 25);
    }
    
    private void drawTotalSpawned(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        
        String spawnedText = "Total spawnés: " + totalSpawned;
        g.drawString(spawnedText, x + 250, y + 45);
    }
    
    private void drawEnemyCount(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 18));
        
        String enemyText = String.format("Ennemis: %d vivants", aliveEnemies);
        g.drawString(enemyText, x + 250, y + 65);
    }
    
    public void mouseClicked(int x, int y) {
        // Géré dans SimulationPerformance
    }
    
    public void mouseMoved(int x, int y) {
        btnMenu.setMouseOver(false);
        btnPause.setMouseOver(false);
        
        if (btnMenu.getBounds().contains(x, y)) {
            btnMenu.setMouseOver(true);
        }
        if (btnPause.getBounds().contains(x, y)) {
            btnPause.setMouseOver(true);
        }
    }
    
    public void mousePressed(int x, int y) {
        if (btnMenu.getBounds().contains(x, y)) {
            btnMenu.setMousePressed(true);
        }
        if (btnPause.getBounds().contains(x, y)) {
            btnPause.setMousePressed(true);
        }
    }
    
    public void mouseReleased(int x, int y) {
        btnMenu.resetBooleans();
        btnPause.resetBooleans();
    }
    
    public void setElapsedTime(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    
    public void setAliveEnemies(int aliveEnemies) {
        this.aliveEnemies = aliveEnemies;
    }
    
    public void setTotalSpawned(int totalSpawned) {
        this.totalSpawned = totalSpawned;
    }
    
    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }
    
    public MyButton getBtnMenu() {
        return btnMenu;
    }
    
    public MyButton getBtnPause() {
        return btnPause;
    }
}
