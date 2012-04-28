/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;


/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarColorGreen extends MenuItem {
    
    public ChangeBarColorGreen(MenuItem topMenu) {
        super(topMenu, "Green", true);
        updateValue();
    }
    @Override
    public void plusAction() {
        if(scopelite.ScopeLite.drawer.barColorGreen < 255)
            scopelite.ScopeLite.drawer.barColorGreen++;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.drawer.barColorGreen > 1)
            scopelite.ScopeLite.drawer.barColorGreen--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.barColorGreen = scopelite.ScopeLite.drawer.barColorGreenDefault;
        updateValue();
    }
    
    @Override
    public final void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.barColorGreen);
        scopelite.ScopeLite.drawer.updateBarColor();
    }
    
}
