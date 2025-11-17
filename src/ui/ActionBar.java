package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.awt.Font;

import helper.Constants.Towers;
import objects.Tower;
import scenes.Playing;
import static main.GameStates.*; 

public class ActionBar extends Bar {
    
    private MyButton btnMenu;
    private Playing playing;
    private MyButton[] towerButtons;
    private Tower selectedTower;
    private Tower displayedTower;
    private DecimalFormat formatter;
    private int gold = 100;

    public ActionBar(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        formatter = new DecimalFormat("0.0");

        initButtons();
    }

    private void initButtons() {
        btnMenu = new MyButton("MENU", x + 5, y + 5, 100, 32);
        towerButtons = new MyButton[3];
        int w = 50;
        int h = 50;
        int xStart = 120;
        int yStart = 645;
        int xOffset = (int) (w *1.1f);

        for (int i = 0; i < towerButtons.length; i++) {
            towerButtons[i] = new MyButton("" + (i + 1), xStart + i * xOffset, yStart, w, h, i);
        }
    }

    private void drawButtons(Graphics g) {
        btnMenu.draw(g);

        for (MyButton b : towerButtons) {
            g.setColor(Color.GRAY);
            g.fillRect(b.x, b.y, b.width, b.height);
            g.drawImage(playing.getTowerManager().getTowerImgs()[b.getID()], b.getX(), b.getY(), b.getWidth(), b.getHeight(), null);

            drawButtonFeedback(g, b);
        }
    }

    public void draw(Graphics g) {
        g.setColor(new Color(220, 123,15));
        g.fillRect(x, y, width, height);
        drawButtons(g);

        if (displayedTower != null) {
            drawDisplayTowerInfo(g);
        } else {
            drawWavesInfo(g);
        }

        drawGoldAmount(g);
    }

    private void drawGoldAmount(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        g.drawString("Gold: " + gold, 150, 720);
    }

    private void drawWavesInfo(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        drawWaveTimerInfo(g);
        drawEnemyLeftInfo(g);
        drawWavesLevelInfo(g);
    }

    private void drawEnemyLeftInfo(Graphics g) {
        int enemiesLeft = playing.getEnemyManager().getAmountOfAliveEnemies();
        g.drawString("Enemies left: " + enemiesLeft, 410, 660);
    }

    private void drawWavesLevelInfo(Graphics g) {
        int current = playing.getWaveManager().getWaveIndex() + 1;
        int size = playing.getWaveManager().getWaves().size();
        g.drawString("Wave " + current + " / " + size, 410, 690);
    }

    private void drawWaveTimerInfo(Graphics g) {
        if(playing.getWaveManager().isWaveTimerStarted()){
            float timeLeft = playing.getWaveManager().getTimeLeft();
            String formatedText = formatter.format(timeLeft);
            g.drawString("Time left: " + formatedText + "s", 410, 720);
        }
    }

    public void displayTowerInfo(Tower t) {
        this.displayedTower = t;
    }

    public void drawDisplayTowerInfo(Graphics g) {
        g.setColor(Color.gray);
		g.fillRect(410, 645, 220, 85);
		g.setColor(Color.black);
		g.drawRect(410, 645, 220, 85);
		g.drawRect(420, 650, 50, 50);
		g.drawImage(playing.getTowerManager().getTowerImgs()[displayedTower.getTowerType()], 420, 650, 50, 50, null);
		g.setFont(new Font("LucidaSans", Font.BOLD, 15));
		g.drawString("" + Towers.getName(displayedTower.getTowerType()), 480, 660);
		g.drawString("ID: " + displayedTower.getId(), 480, 675);
		//g.drawString("Tier: " + displayedTower.getTier(), 560, 660);

        drawSelectedTowerBorder(g);
        drawDisplayTowerRange(g);
    }

    private void drawSelectedTowerBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);
    }

    private void drawDisplayTowerRange(Graphics g){
        g.setColor(Color.WHITE);
        g.drawOval(displayedTower.getX()+ 16 - (int)displayedTower.getRange(), displayedTower.getY()+ 16 - (int)displayedTower.getRange(), (int)displayedTower.getRange() * 2, (int)displayedTower.getRange() * 2);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            setGameState(MENU);
        } else {
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(mouseX, mouseY)) {
                    selectedTower = new Tower(0, 0, -1, b.getID());
                    playing.setSelectedTower(selectedTower);
                    return;
                }
            }
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        btnMenu.setMouseOver(false);
        for (MyButton b : towerButtons) {
            b.setMouseOver(false);
        }
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMouseOver(true);
        } else {
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(mouseX, mouseY)) {
                    b.setMouseOver(true);
                    break;
                }
            }
        }
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMousePressed(true);
        } else {
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(mouseX, mouseY)) {
                    b.setMousePressed(true);
                    break;
                }
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        btnMenu.resetBooleans();
        for (MyButton b : towerButtons) {
            b.resetBooleans();
        }
    }

    public void resetDisplayedTower() {
        this.displayedTower = null;
    }
}
