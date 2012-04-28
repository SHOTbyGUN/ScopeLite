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
public class ChangeAmplifyFactor extends MenuItem {
    
    DecimalFormat decimalFormat = new DecimalFormat("#.#");

    
    public ChangeAmplifyFactor(MenuItem topMenu) {
        super(topMenu, "Amplify factor", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.amplitudeModifierFactor += 0.1f;
        updateValue();
    }

    @Override
    public void minusAction() {
        scopelite.ScopeLite.amplitudeModifierFactor -= 0.1f;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.amplitudeModifierFactor = scopelite.ScopeLite.amplitudeModifierFactorDefault;
        updateValue();
    }

    @Override
    protected void updateValue() {
        value = decimalFormat.format(scopelite.ScopeLite.amplitudeModifierFactor);
    }
    
}
