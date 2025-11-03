package ui;

import java.awt.Color;
import java.awt.Graphics;

import scenes.Playing;
import static main.GameStates.*; 

public class ActionBar extends Bar {
    
    private MyButton btnMenu;
    private Playing playing;

    public ActionBar(int x, int y, int width, int height, Playing playing) {
        super(x, y, width, height);
        this.playing = playing;

        initButtons();
    }

    private void initButtons() {
        btnMenu = new MyButton("MENU", x + 10, y + 10, 100, 32);
    }

    private void drawButtons(Graphics g) {
        btnMenu.draw(g);
    }
    
    public void draw(Graphics g) {
        g.setColor(new Color(220, 123,15));
        g.fillRect(x, y, width, height);
        drawButtons(g);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            setGameState(MENU);
        }
    }

    public void mouseMoved(int mouseX, int mouseY) {
        btnMenu.setMouseOver(false);
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMouseOver(true);
        }
    }

    public void mousePressed(int mouseX, int mouseY) {
        if (btnMenu.getBounds().contains(mouseX, mouseY)) {
            btnMenu.setMousePressed(true);
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        btnMenu.resetBooleans();
    }
}
