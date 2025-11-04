package inputs;

import java.awt.event.KeyListener;

import main.GameStates;

import java.awt.event.KeyEvent;

public class KeyboardListener  implements KeyListener{
    
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode()  == KeyEvent.VK_A) {
            GameStates.gameState = GameStates.MENU;
        } else if (e.getKeyCode()  == KeyEvent.VK_Z) {
            GameStates.gameState = GameStates.PLAYING;
        } else if (e.getKeyCode()  == KeyEvent.VK_E) {
            GameStates.gameState = GameStates.SETTINGS;
        }
        
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        
    }    
}