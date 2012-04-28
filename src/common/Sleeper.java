/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author SHOTbyGUN
 */

// This thread is trying to force the Java VM interrupt speed higher
// To make other sleeps and time measurements more accurate

public class Sleeper implements Runnable {
    
    public Thread thread;
    
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("Sleeper");
        thread.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                // = 10 nanosecond less than millisecond
                Thread.sleep(0,99999);
            } catch (Exception ex) {
                
            }
        }
    }
    
}
