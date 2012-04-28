/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;


/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarColorBlue extends MenuItem {
    
    public ChangeBarColorBlue(MenuItem topMenu) {
        super(topMenu, "Blue", true);
        updateValue();
    }
    @Override
    public void plusAction() {
        if(scopelite.ScopeLite.drawer.barColorBlue < 255)
            scopelite.ScopeLite.drawer.barColorBlue++;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.drawer.barColorBlue > 1)
            scopelite.ScopeLite.drawer.barColorBlue--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.barColorBlue = scopelite.ScopeLite.drawer.barColorBlueDefault;
        updateValue();
    }
    
    @Override
    public final void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.barColorBlue);
        scopelite.ScopeLite.drawer.updateBarColor();
    }
    
}
