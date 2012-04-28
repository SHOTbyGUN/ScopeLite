/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class HighPassAdjust extends MenuItem {
    
    public HighPassAdjust(MenuItem topMenu) {
        super(topMenu, "High-Pass Adjust", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.soundCapturer.highPassAdjust++;
        updateValue();
    }

    @Override
    public void minusAction() {
        scopelite.ScopeLite.soundCapturer.highPassAdjust--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.soundCapturer.highPassAdjust = scopelite.ScopeLite.soundCapturer.highPassAdjustDefault;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.soundCapturer.highPassAdjust);
    }
    
}
