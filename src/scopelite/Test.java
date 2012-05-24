/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author kaahane
 */
public class Test {
    public static void main(String[] args) {
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        System.out.println(graphicsDevice.getDefaultConfiguration().getBounds().width);
        System.out.println(graphicsDevice.getDefaultConfiguration().getBounds().height);
        System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width);
        System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height);
    }
}
