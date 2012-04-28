/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

/**
 *
 * @author SHOTbyGUN
 */
public class SignalModifier {
    
    public static int modifyY(int input) {
        float value;
        value = input;
        if(ScopeLite.logarithmFactor > 0)
            value = logarithmic(value);
        value = fitToScreen(value);
        value = amplitudeModifier(value);
        
        return (int)value;
    }
    
    public static float fitToScreen(float input) {
        return input / ScopeLite.soundCapturer.getMaxValue() * ScopeLite.screenHeight / 3;
    }
    
    public static float logarithmic(float input) {
        
        float returnValue;
        
        if(input == 0)
            return 0;
        
        returnValue = (float)java.lang.Math.log10(java.lang.Math.abs(input) / 10 + 1)
                * (ScopeLite.soundCapturer.getMaxValue() / 10) 
                * ScopeLite.logarithmFactor;
        
        /*
        // =A1/SQRT(A1^2+A1)
        returnValue =  java.lang.Math.abs(input) / (float)java.lang.Math.sqrt(java.lang.Math.pow(java.lang.Math.abs(input), 2) + java.lang.Math.abs(input) + 1)
                * (ScopeLite.soundCapturer.getMaxValue() / 5) 
                * ScopeLite.logarithmFactor;
                * 
                */
        
        if(input < 0)
            returnValue = returnValue * -1;
        
        returnValue += (1 - ScopeLite.logarithmFactor) * input;
        
        //System.out.println((float)java.lang.Math.log(java.lang.Math.abs(input)));
        
        //return (float)java.lang.Math.log(java.lang.Math.abs(input)) * ScopeLite.logarithmFactor
                //+ input * (1-ScopeLite.logarithmFactor);
        return returnValue;
    }
    
    public static float amplitudeModifier(float input) {
        return input * ScopeLite.amplitudeModifierFactor;
    }
    
}
