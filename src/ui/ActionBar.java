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
    
    private MyButton btnMenu, btnPause;
    private Playing playing;
    private MyButton[] towerButtons;
    private Tower selectedTower;
    private Tower displayedTower;
    private DecimalFormat formatter;
    private int gold = 100, towerCostType;
    private boolean showTowerCosts;
    private MyButton sellTower, upgradeTower;

    public ActionBar(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;
        formatter = new DecimalFormat("0.0");

        initButtons();
    }

    private void initButtons() {
        btnMenu = new MyButton("MENU", x + 5, y + 5, 100, 32);
        btnPause = new MyButton("PAUSE", x + 5, y + 35 + 5, 100, 32);

        towerButtons = new MyButton[3];
        int w = 50;
        int h = 50;
        int xStart = 115;
        int yStart = 645;
        int xOffset = (int) (w *1.1f);

        for (int i = 0; i < towerButtons.length; i++) {
            towerButtons[i] = new MyButton("" + (i + 1), xStart + i * xOffset, yStart, w, h, i);
        }

		sellTower = new MyButton("Sell", 420, 702, 90, 25);
		upgradeTower = new MyButton("Upgrade", 535, 702, 90, 25);
    }

    private void drawButtons(Graphics g) {
        btnMenu.draw(g);
        drawButtonFeedback(g, btnMenu);
        btnPause.draw(g);
        drawButtonFeedback(g, btnPause);

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
        
        if (showTowerCosts) {
            drawTowerCosts(g);
        }

        if(playing.isGamePaused()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, 640, 640);
            g.setColor(Color.WHITE);
            g.setFont(new Font("LucidaSans", Font.BOLD, 50));
            g.drawString("GAME PAUSED", 150, 320);
        }
    }

    private void drawTowerCosts(Graphics g) {
        g.setColor(Color.gray);
        g.fillRect(285, 645, 120, 50);
        g.setColor(Color.BLACK);
        g.drawRect(285, 645, 120, 50);

        g.drawString("" + getTowerCostName(), 290, 665);
        g.drawString("Cost: " + getTowerCost() + "g", 290, 685);

        if(isTowerCostMoreThanGold()) {
            g.setColor(new Color(139, 0, 0));
            g.drawString("Can't Afford", 290, 720);
        }
    }

    private boolean isTowerCostMoreThanGold() {
        int cost = Towers.getTowerCost(towerCostType);
        return gold < cost;
    }

    private String getTowerCostName() {
        return Towers.getName(towerCostType);
    }

    private int getTowerCost() {
        return Towers.getTowerCost(towerCostType);
    }

    private void drawGoldAmount(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("LucidaSans", Font.BOLD, 20));
        g.drawString("Gold: " + gold, 120, 720);
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
        g.drawString("Enemies left: " + enemiesLeft, 415, 660);
    }

    private void drawWavesLevelInfo(Graphics g) {
        int current = playing.getWaveManager().getWaveIndex() + 1;
        int size = playing.getWaveManager().getWaves().size();
        g.drawString("Wave " + current + " / " + size, 415, 690);
    }

    private void drawWaveTimerInfo(Graphics g) {
        if(playing.getWaveManager().isWaveTimerStarted()){
            float timeLeft = playing.getWaveManager().getTimeLeft();
            String formatedText = formatter.format(timeLeft);
            g.drawString("Time left: " + formatedText + "s", 415, 720);
        }
    }

    public void addGold(int amount) {
        this.gold += amount;
    }

    public void displayTowerInfo(Tower t) {
        this.displayedTower = t;
    }

    public void drawDisplayTowerInfo(Graphics g) {
        g.setColor(Color.gray);
		g.fillRect(415, 645, 215, 85);
		g.setColor(Color.black);
		g.drawRect(415, 645, 215, 85);
		g.drawRect(425, 650, 50, 50);
		g.drawImage(playing.getTowerManager().getTowerImgs()[displayedTower.getTowerType()], 425, 650, 50, 50, null);
		g.setFont(new Font("LucidaSans", Font.BOLD, 15));
		g.drawString("" + Towers.getName(displayedTower.getTowerType()), 485, 660);
		g.drawString("ID: " + displayedTower.getId(), 485, 678);
        g.drawString("Tiers: " + displayedTower.getTier(), 535, 678);

        drawSelectedTowerBorder(g);
        drawDisplayTowerRange(g);

        sellTower.draw(g);
        drawButtonFeedback(g, sellTower);

        if(displayedTower.getTier() < 3){
            upgradeTower.draw(g);
            drawButtonFeedback(g, upgradeTower);
        }

        g.setFont(new Font("LucidaSans", Font.BOLD, 15));
        if(sellTower.isMouseOver()){
            g.setColor(new Color(139, 0, 0));
            g.drawString("Sell for: " + getSellAmount(displayedTower) + "g", 485, 695);
        }else if (upgradeTower.isMouseOver()) {
            g.setColor(Color.BLUE);
            g.drawString("Upgrade for: " + getUpgradeAmount(displayedTower) + "g", 485, 695);
        }
    }

    private int getUpgradeAmount(Tower displayedTower) {
        return (int)(Towers.getTowerCost(displayedTower.getTowerType()) * 0.3f);
    }

    private int getSellAmount(Tower displayedTower) {
        return (int)(Towers.getTowerCost(displayedTower.getTowerType()) * 0.5f + (displayedTower.getTier() -1) * getUpgradeAmount(displayedTower) * 0.5f);
    }

    private void drawSelectedTowerBorder(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(displayedTower.getX(), displayedTower.getY(), 32, 32);
    }

    private void drawDisplayTowerRange(Graphics g){
        g.setColor(Color.WHITE);
        g.drawOval(displayedTower.getX()+ 16 - (int)displayedTower.getRange(), displayedTower.getY()+ 16 - (int)displayedTower.getRange(), (int)displayedTower.getRange() * 2, (int)displayedTower.getRange() * 2);
    }

    public void payForTower(int towerType) {
        int cost = Towers.getTowerCost(towerType);
        this.gold -= cost;
    }

    private boolean isGoldEnoughForTower(int towerType) {
        int cost = Towers.getTowerCost(towerType);
        return gold >= cost;
    }

    private void sellTowerClicked() {
        this.gold += getSellAmount(displayedTower);
        playing.getTowerManager().sellTower(displayedTower);
        displayedTower = null;
    }

    private void upgradeTowerClicked() {
        int upgradeCost = getUpgradeAmount(displayedTower);
        if(gold >= upgradeCost) {
            this.gold -= upgradeCost;
            playing.getTowerManager().upgradeTower(displayedTower);
        }
    }   

    private void gamePause() {
        playing.setGamePaused(!playing.isGamePaused());
        
        if(playing.isGamePaused()){
            btnPause.setText("RESUME");
        } else {
            btnPause.setText("PAUSE");
        }
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            setGameState(MENU);
        } else if(btnPause.getBounds().contains(mouseX, mouseY)){
            gamePause();
        } else {
            if(displayedTower != null){
                if(sellTower.getBounds().contains(mouseX, mouseY)){
                    sellTowerClicked();
                    return;
                }
                if(upgradeTower.getBounds().contains(mouseX, mouseY) && displayedTower.getTier() < 3){
                    upgradeTowerClicked();
                    return;
                }
            }
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(mouseX, mouseY)) {
                    if(isGoldEnoughForTower(b.getID())){
                        selectedTower = new Tower(0, 0, -1, b.getID());
                        playing.setSelectedTower(selectedTower);
                        return;
                    }
                }
            }
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        btnMenu.setMouseOver(false);
        sellTower.setMouseOver(false);
        upgradeTower.setMouseOver(false);
        btnPause.setMouseOver(false);
        showTowerCosts = false;

        for (MyButton b : towerButtons) {
            b.setMouseOver(false);
        }

        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMouseOver(true);
        } else if(btnPause.getBounds().contains(mouseX, mouseY)){
            btnPause.setMouseOver(true);        
        }else {
            if(displayedTower != null){
                if(sellTower.getBounds().contains(mouseX, mouseY)){
                    sellTower.setMouseOver(true);
                    return;
                }
                if(upgradeTower.getBounds().contains(mouseX, mouseY) && displayedTower.getTier() < 3){
                    upgradeTower.setMouseOver(true);
                    return;
                }
            }
            for (MyButton b : towerButtons) {
                if (b.getBounds().contains(mouseX, mouseY)) {
                    b.setMouseOver(true);
                    showTowerCosts = true;
                    towerCostType = b.getID();
                    return;
                }
            }
        }
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMousePressed(true);
        } else if(btnPause.getBounds().contains(mouseX, mouseY)){
            btnPause.setMousePressed(true);  
        } else {
            if(displayedTower != null){
                if(sellTower.getBounds().contains(mouseX, mouseY)){
                    sellTower.setMousePressed(true);
                    return;
                }
                if(upgradeTower.getBounds().contains(mouseX, mouseY) && displayedTower.getTier() < 3){
                    upgradeTower.setMousePressed(true);
                    return;
                }
            }

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
        btnPause.resetBooleans();
        for (MyButton b : towerButtons) {
            b.resetBooleans();
        }
        sellTower.resetBooleans();
        upgradeTower.resetBooleans();
    }

    public void resetDisplayedTower() {
        this.displayedTower = null;
    }
}
