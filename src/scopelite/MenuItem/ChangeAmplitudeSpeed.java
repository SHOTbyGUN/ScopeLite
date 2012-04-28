/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class ChangeAmplitudeSpeed extends MenuItem {
    
    public ChangeAmplitudeSpeed(MenuItem topMenu) {
        super(topMenu, "Amplitude scope speed", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.drawer.samplesPerLongScope++;
        updateValue();
    }

    @Override
    public void minusAction() {
        if (scopelite.ScopeLite.drawer.samplesPerLongScope > 1)
            scopelite.ScopeLite.drawer.samplesPerLongScope--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.drawer.samplesPerLongScope = scopelite.ScopeLite.drawer.samplesPerLongScopeDefault;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.drawer.samplesPerLongScope);
    }
    
    
}
