/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarFreq extends MenuItem {
    
    public ChangeBarFreq(MenuItem topMenu) {
        super(topMenu, "Max bar frequency", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.drawer.modifyBar(16, 0);
        updateValue();
    }

    @Override
    public void minusAction() {
        scopelite.ScopeLite.drawer.modifyBar(-16, 0);
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.resetBarSettings();
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.getMaxBarFreq()) + " Hz";
    }
    
}
