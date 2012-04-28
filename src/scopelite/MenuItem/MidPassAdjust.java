/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class MidPassAdjust extends MenuItem {
    
    public MidPassAdjust(MenuItem topMenu) {
        super(topMenu, "Mid-Pass Adjust", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        scopelite.ScopeLite.soundCapturer.midPassAdjust++;
        updateValue();
    }

    @Override
    public void minusAction() {
        scopelite.ScopeLite.soundCapturer.midPassAdjust--;
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.soundCapturer.midPassAdjust = scopelite.ScopeLite.soundCapturer.midPassAdjustDefault;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.soundCapturer.midPassAdjust);
    }
    
}