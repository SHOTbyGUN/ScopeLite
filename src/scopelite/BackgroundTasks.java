/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author SHOTbyGUN
 */
public class BackgroundTasks implements Runnable {
    
    public Thread thread;
    
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.setName("Background Tasks");
        thread.start();
    }
    
    
    @Override
    public void run() {
        Timer statusUpdate = new Timer("Status Update", true);
        statusUpdate.schedule(new TimerTask() {

            @Override
            public void run() {
                
                // Update screenWidth and screenHeight
                ScopeLite.screenWidth = ScopeLite.mainFrame.getWidth();
                ScopeLite.screenHeight = ScopeLite.mainFrame.getHeight();
                
            }
        }, 100, 100);
    }
    
}
