/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class ChangeMaxFps extends MenuItem {
    
    public ChangeMaxFps(MenuItem topMenu) {
        super(topMenu, "Max fps", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.maxFps++;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.maxFps > 0)
            scopelite.ScopeLite.maxFps--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.maxFps = scopelite.ScopeLite.maxFpsDefault;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        if(scopelite.ScopeLite.maxFps == 0) {
            value = "No limit";
        } else {
            value = Integer.toString(scopelite.ScopeLite.maxFps);
        }
    }
    
}
