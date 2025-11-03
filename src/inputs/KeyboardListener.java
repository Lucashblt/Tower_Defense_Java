package inputs;

import java.awt.RenderingHints.Key;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class KeyboardListener  implements KeyListener{
    
    @Override
    public void keyTyped(java.awt.event.KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        if (e.getKeyCode()  == KeyEvent.VK_A) {
            System.out.println("A key pressed");

        }
        
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        // TODO Auto-generated method stub
        
    }    
}