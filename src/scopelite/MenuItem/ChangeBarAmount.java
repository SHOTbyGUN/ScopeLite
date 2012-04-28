/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarAmount extends MenuItem {
    
    public ChangeBarAmount(MenuItem topMenu) {
        super(topMenu, "Bar amount", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.drawer.modifyBar(0, 1);
        updateValue();
    }

    @Override
    public void minusAction() {
        scopelite.ScopeLite.drawer.modifyBar(0, -1);
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.resetBarSettings();
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.getBarAmount());
    }
    
}
