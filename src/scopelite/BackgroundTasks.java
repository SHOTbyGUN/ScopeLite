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
                
                try {
                    // Update screenWidth and screenHeight
                    ScopeLite.screenWidth = ScopeLite.canvas.getWidth();
                    ScopeLite.screenHeight = ScopeLite.canvas.getHeight();

                    // New volatileImage if screen size changed
                    /*
                    if(scopelite.Drawer.volatileImage.getWidth() != ScopeLite.screenWidth || scopelite.Drawer.volatileImage.getHeight() != ScopeLite.screenHeight)
                        scopelite.Drawer.volatileImage = ScopeLite.getCurrentGraphicsDevice().getDefaultConfiguration().createCompatibleVolatileImage(ScopeLite.screenWidth, ScopeLite.screenHeight);
                        * 
                        */
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, 100, 100);
    }
    
}
