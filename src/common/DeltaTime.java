/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author SHOTbyGUN
 */
public class DeltaTime {
    
    private long newSystemTime, delta, lastSystemTime;
    private boolean nanos_true_millis_false;
    
    public DeltaTime(boolean nanos_true_millis_false) {
        this.nanos_true_millis_false = nanos_true_millis_false;
        
        if(nanos_true_millis_false)
            lastSystemTime = System.nanoTime();
        else
            lastSystemTime = System.currentTimeMillis();
    }
    
    public long getDelta() {
        
        if(nanos_true_millis_false)
            newSystemTime = System.nanoTime();
        else
            newSystemTime = System.currentTimeMillis();
        
        delta = newSystemTime - lastSystemTime;
        lastSystemTime = newSystemTime;
        
        return delta;
    }
    
    public long getLastSystemTime() {
        return lastSystemTime;
    }
    
}
