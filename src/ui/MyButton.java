package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Font;

public class MyButton {

    public int x, y, width, height;
    private String text;     
    private Rectangle bounds;
    private boolean mouseOver, mousePressed;

    private final Color baseColor = new Color(85, 60, 40);     
    private final Color hoverColor = new Color(115, 90, 60);   
    private final Color pressedColor = new Color(65, 45, 30);
    private final Color borderColor = new Color(30, 20, 10);
    private final Color textColor = new Color(240, 220, 180);

    public MyButton(String text, int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        initBounds();
    }

    private void initBounds() {
        bounds = new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g){

        drawBody(g);
        drawBorder(g);
        drawText(g);
    }

    private void drawBody(Graphics g) {
        if (mousePressed) {
            g.setColor(pressedColor);
        } else if (mouseOver) {
            g.setColor(hoverColor);
        } else {
            g.setColor(baseColor);
        }
        g.fillRect(x, y, width, height);
    }

    private void drawBorder(Graphics g) {
        g.setColor(borderColor);
        g.drawRect(x, y, width, height);
        g.drawRect(x + 1, y + 1, width - 2, height - 2);
    }

    private void drawText(Graphics g) {
        g.setFont(new Font("Monospaced", Font.BOLD, 20));
        g.setColor(textColor);

        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getAscent();

        g.drawString(text, x + (width - textWidth) / 2, y + (height + textHeight) / 2 - 3);
    }
    public void resetBooleans() {
        this.mouseOver = false;
        this.mousePressed = false;
    }

    public boolean isMouseOver() {
		return mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
