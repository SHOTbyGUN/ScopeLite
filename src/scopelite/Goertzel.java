/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

/**
 *
 * @author SHOTbyGUN
 */
public class Goertzel extends GetX {
    
    double s, s_prev, s_prev2, normalized_frequency, coeff;
    int i;
    
    public Goertzel(int targetFrequency) {
        normalized_frequency = targetFrequency / 480;
        coeff = 2* java.lang.Math.cos(2*java.lang.Math.PI*normalized_frequency);
    }
    
    public double update() {
        
        xRunner = ScopeLite.soundCapturer.xRunner;
        
        s_prev = 0;
        s_prev2 = 0;
        
        for(i = 0; i < 480; i++) {
            //s = x[n] + coeff*s_prev - s_prev2;
            s = (ScopeLite.soundCapturer.channelsData.get(0).get(getX(i))
                    + ScopeLite.soundCapturer.channelsData.get(1).get(getX(i)))
                    + coeff*s_prev - s_prev2;
            s_prev2 = s_prev;
            s_prev = s; 
        }
        
        return java.lang.Math.sqrt(s_prev2*s_prev2 + s_prev*s_prev - coeff*s_prev*s_prev2);
        
    }
    
}
