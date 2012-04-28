/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

import java.util.ArrayList;

/**
 *
 * @author SHOTbyGUN
 */
public abstract class MenuItem {
    
    private static String plusMinusCharacter = "±";
    private static String leftArrowCharacter = "←";
    private static String rightArrowCharacter = "→";
    private static String upDownArrowCharacter = "↕";
    
    private String text;
    protected String value;
    public MenuItem topMenuItem;
    private boolean actionsEnabled;
    private ArrayList<MenuItem> subMenuList = new ArrayList<MenuItem>();
    
    public MenuItem(MenuItem topMenuItem, String text, boolean actionsEnabled) {
        this.topMenuItem = topMenuItem;
        this.text = text;
        this.actionsEnabled = actionsEnabled;
    }
    
    
    public MenuItem getSubItem() {
        if(subMenuList.size() > 0)
            return subMenuList.get(0);
        else
            return null;
    }
    
    public MenuItem getTopItem() {
        if(topMenuItem != null && !topMenuItem.text.equals("root"))
            return topMenuItem;
        else
            return null;
    }
    
    public MenuItem getNext() {
        try {
            return topMenuItem.subMenuList.get(topMenuItem.subMenuList.indexOf(this)+1);
        } catch (IndexOutOfBoundsException ioobEx) {
            return null;
        }
        
    }
    
    public MenuItem getPrevious() {
        try {
            return topMenuItem.subMenuList.get(topMenuItem.subMenuList.indexOf(this)-1);
        } catch (IndexOutOfBoundsException ioobEx) {
            return null;
        }
    }
    
    
    public abstract void plusAction();
    
    public abstract void minusAction();
    
    public abstract void resetDefault();
    
    @Override
    public String toString() {
        String out = "";
        
        if(topMenuItem.topMenuItem != null)
            out += leftArrowCharacter + " ";
        
        if(topMenuItem.subMenuList.size() > 1) {
            out += upDownArrowCharacter + " ";
        }
        
        out += text + " ";
        
        updateValue();
        
        if(value != null) {
            out += "(" + value + ") ";
        }
        
        if(actionsEnabled)
            out += plusMinusCharacter + " ";
        
        if(!subMenuList.isEmpty())
            out += rightArrowCharacter;
        
        return out;
    }
    
    
    public static MenuItem buildMenuTree() {
        
        MenuItem root = new RootMenu();
        MenuItem item, item2;
        
        /*
        item = new TextMenuItem(root, "Filter settings");
        root.subMenuList.add(item);
        
        item.subMenuList.add(new HighPassAdjust(item));
        item.subMenuList.add(new MidPassAdjust(item));
        * 
        */
        
        
        
        item = new TextMenuItem(root, "Visual settings");
        item.subMenuList.add(new ChangeAmplitudeSpeed(item));
        item.subMenuList.add(new ChangeLogarithmFactor(item));
        item.subMenuList.add(new ChangeAmplifyFactor(item));
        root.subMenuList.add(item);
        
        item = new TextMenuItem(root, "Spectrum bar settings");
        item.subMenuList.add(new ChangeBarFreq(item));
        item.subMenuList.add(new ChangeBarAmount(item));
        item.subMenuList.add(new ChangeBarSensitivity(item));
        item2 = new TextMenuItem(item, "Change Bar color");
        item2.subMenuList.add(new ChangeBarColorRed(item2));
        item2.subMenuList.add(new ChangeBarColorGreen(item2));
        item2.subMenuList.add(new ChangeBarColorBlue(item2));
        item.subMenuList.add(item2);
        
        root.subMenuList.add(item);
        
        item = new TextMenuItem(root, "Screen settings");
        item.subMenuList.add(new ChangeMaxFps(item));
        root.subMenuList.add(item);
        
        item = new TextMenuItem(root, "Sound device settings");
        item.subMenuList.add(new SelectSoundDevice(item));
        root.subMenuList.add(item);
        
        
        return root;
        
    }
    
    protected abstract void updateValue();
    
}
