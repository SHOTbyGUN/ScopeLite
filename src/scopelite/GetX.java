/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

/**
 *
 * @author SHOTbyGUN
 */
public class GetX {
    
    protected int xRunner = 0;
    
    protected int getX(int n) {
        n = xRunner - n;
        if(n < 0)
            n = n + ScopeLite.soundCapturer.getLocalBufferSize();
        if(n >= ScopeLite.soundCapturer.getLocalBufferSize()) {
            n = n - ScopeLite.soundCapturer.getLocalBufferSize();
        }
            
        return n;
    }
    
}
