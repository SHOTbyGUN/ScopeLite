/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

import java.text.DecimalFormat;

/**
 *
 * @author SHOTbyGUN
 */
public class ChangeBarSensitivity extends MenuItem {
    
    DecimalFormat decimalFormat = new DecimalFormat("#.#");
    
    public ChangeBarSensitivity(MenuItem topMenu) {
        super(topMenu, "Change bar sensitivity", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.drawer.barSensitivity += 0.1d;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.drawer.barSensitivity > 0.2d)
            scopelite.ScopeLite.drawer.barSensitivity -= 0.1d;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.barSensitivity = scopelite.ScopeLite.drawer.barSensitivityDefault;
        updateValue();
    }
    
    @Override
    public final void updateValue() {
        value = "y / " + decimalFormat.format(scopelite.ScopeLite.drawer.barSensitivity);
    }
    
}
