

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import scopelite.MenuItem.MenuItem;

/**
 *
 * @author SHOTbyGUN
 * 
 * Check keyCodes from http://download.oracle.com/javase/tutorial/uiswing/events/keylistener.html
 * 
 */

public class InputListener implements KeyListener, MouseListener {
    
    public static MenuItem currentMenu = MenuItem.buildMenuTree().getSubItem();
    //public static ArrayList<MenuItem> currentMenu = MenuItem.buildMenuTree().subMenu();
    //public static int currentMenuIndex = 0;
    public static int rePressDelay = 25;
    public static long lastMouseClick = System.currentTimeMillis();
    
    private long keyPressed = 0;

    @Override
    public void keyTyped(KeyEvent e) {
        return;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
        if(keyPressed == 0) {
            keyPressed = System.currentTimeMillis();
            processKeyCode(e.getKeyCode());
        } else if (System.currentTimeMillis() - keyPressed > rePressDelay) {
            keyPressed = System.currentTimeMillis();
            processKeyCode(e.getKeyCode());
        }
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyPressed = 0;
    }
    
    private void processKeyCode(int keyCode) {
        switch(keyCode) {
            case 107: // Numpad plus
            case 521: // Normal plus
                currentMenu.plusAction();
                break;
                
            case 109: // Numpad minus
            case 45:  // Normal minus
                currentMenu.minusAction();
                break;
                
            case 104: // Numpad 8
            case 38:  // Up arrow
                if(currentMenu.getPrevious() != null)
                    currentMenu = currentMenu.getPrevious();
                break;
                
            case 98:  // Numpad 2
            case 40:  // Down arrow
                if(currentMenu.getNext() != null)
                    currentMenu = currentMenu.getNext();
                break;
                
                
            case 102: // Numpad 6
            case 39:  // Right arrow
                if(currentMenu.getSubItem() != null)
                    currentMenu = currentMenu.getSubItem();
                break;
                
                
            case 100: // Numpad 4
            case 37:  // Left arrow
                if(currentMenu.getTopItem() != null)
                    currentMenu = currentMenu.getTopItem();
                break;
                
            case 72:  // H
                ScopeLite.showHelp = !ScopeLite.showHelp;
                break;
                
            case 68:  // D
                currentMenu.resetDefault();
                break;
                
            case 71:  // G
                ScopeLite.showGui = !ScopeLite.showGui;
                break;
                
            case 70:  // F
                ScopeLite.scopeLite.toggleFullscreen();
                break;
                
            case 82: // R
                ScopeLite.soundCapturer.restart();
                break;
                
            case 32: // Space
                ScopeLite.soundCapturer.togglePause();
                break;
                
            case 27: // Escape
                if(ScopeLite.fullscreen)
                    ScopeLite.scopeLite.toggleFullscreen();
                else
                    System.exit(0);
                break;
                
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        long newClick = System.currentTimeMillis();
        if(newClick - lastMouseClick < 300) {
            ScopeLite.scopeLite.toggleFullscreen();
            
            // This is to prevent triple click to undo the togglescreen
            newClick -= 1000;
        }
        lastMouseClick = newClick;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ScopeLite.drawer.redBarId = (int)(e.getX() / (1.0d * ScopeLite.screenWidth / ScopeLite.drawer.getBarAmount()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        ScopeLite.drawer.redBarId = -1;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
    
}