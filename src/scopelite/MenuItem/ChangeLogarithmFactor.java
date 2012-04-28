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
public class ChangeLogarithmFactor extends MenuItem {
    
    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    
    public ChangeLogarithmFactor(MenuItem topMenu) {
        super(topMenu, "Logarithm factor", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        if(scopelite.ScopeLite.logarithmFactor < 1)
            scopelite.ScopeLite.logarithmFactor += 0.01f;
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.logarithmFactor >= 0)
            scopelite.ScopeLite.logarithmFactor -= 0.01f;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.logarithmFactor = scopelite.ScopeLite.logarithmFactorDefault;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        if(scopelite.ScopeLite.logarithmFactor < 0) {
            value = "Disabled";
        } else {
            value = decimalFormat.format(scopelite.ScopeLite.logarithmFactor * 100) + "%";
        }
    }
    
}
