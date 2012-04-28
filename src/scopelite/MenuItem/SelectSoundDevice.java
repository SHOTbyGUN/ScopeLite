/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite.MenuItem;

/**
 *
 * @author SHOTbyGUN
 */
public class SelectSoundDevice extends MenuItem {
    
    public SelectSoundDevice(MenuItem topMenu) {
        super(topMenu, "Sound Device", true);
        updateValue();
    }

    @Override
    public void plusAction() {
        if(scopelite.ScopeLite.soundCapturer.currentMixer < scopelite.ScopeLite.soundCapturer.mixers.size() - 1) {
            scopelite.ScopeLite.soundCapturer.currentMixer++;
            scopelite.ScopeLite.soundCapturer.restart();
        }
        updateValue();
    }

    @Override
    public void minusAction() {
        if(scopelite.ScopeLite.soundCapturer.currentMixer > 0) {
            scopelite.ScopeLite.soundCapturer.currentMixer--;
            scopelite.ScopeLite.soundCapturer.restart();
        }
        updateValue();
    }

    @Override
    public void resetDefault() {
        scopelite.ScopeLite.soundCapturer.currentMixer = 0;
        updateValue();
    }
    
    @Override
    public void updateValue() {
        value = Integer.toString(scopelite.ScopeLite.soundCapturer.currentMixer)
                + " "
                + scopelite.ScopeLite.soundCapturer.mixers.get(scopelite.ScopeLite.soundCapturer.currentMixer).getName();
    }
    
}
