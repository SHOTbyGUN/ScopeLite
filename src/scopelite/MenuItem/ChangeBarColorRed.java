/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;


/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarColorRed extends MenuItem {
    
    public ChangeBarColorRed(MenuItem topMenu) {
        super(topMenu, "Red", true);
        updateValue();
    }
    @Override
    public void plusAction() {
        if(scopelite.ScopeLite.drawer.barColorRed < 255)
            scopelite.ScopeLite.drawer.barColorRed++;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.drawer.barColorRed > 1)
            scopelite.ScopeLite.drawer.barColorRed--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.barColorRed = scopelite.ScopeLite.drawer.barColorRedDefault;
        updateValue();
    }
    
    @Override
    public final void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.barColorRed);
        scopelite.ScopeLite.drawer.updateBarColor();
    }
    
}
