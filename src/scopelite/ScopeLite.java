/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import gui.GUI;
import java.awt.*;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 *
 * @author SHOTbyGUN
 */
public class ScopeLite {
    
    // PUBLIC STATIC CLASS INSTANCES
    public static ScopeLite scopeLite;
    public static BackgroundTasks bgTasks;
    public static Drawer drawer;
    public static SoundCapturer soundCapturer;
    public static SignalModifier signalModifier;
    public static GUI gui;
    public static JFrame errorForm;
    public static TextArea errorText;
    
    // PUBLIC STATIC VARIABLES
    
    public static boolean showFps = false;
    public static boolean showHelp = false;
    public static boolean fullscreen = false;
    public static GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    public static JFrame mainFrame;
    public static BufferStrategy strategy;
    public static InputListener inputListener;
    public static int maxScreenWidth = graphicsDevice.getDefaultConfiguration().getBounds().width * 2;
    public static int screenWidth = graphicsDevice.getDefaultConfiguration().getBounds().width / 2;
    public static int screenHeight = graphicsDevice.getDefaultConfiguration().getBounds().height / 2;
    
    // Live modified values
    //public static int samplesPerPixel = 10, samplesPerPixelDefault = 10;
    //public static int delaySleep = 88, delaySleepDefault = 88;
    public static int maxFps = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    public static int maxFpsDefault = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
    public static float amplitudeModifierFactor = 1.0f, amplitudeModifierFactorDefault = 1.0f;
    public static float logarithmFactor = -0.01f, logarithmFactorDefault = -0.01f;
    
    public static String[] helpText = new String[] {
        "h - help", 
        "g - show menu",
        "f - show fps",
        "r - reset scope",
        "space - pause",
        "esc - exit",
        "press and hold mouse to see selected bar's indicated frequency",
        "double click mouse to toggle fullscreen",
        "",
        "Free for non-commercial use",
        "Author: Teemu Kauhanen",
        "Email: teemu.kauhanen@facebook.com",
        "Homepage: http://kaahane.viuhka.fi"};
    
    // PUBLIC VARIABLES
    
    
    //public static AtomicIntegerArray dots;
    //public static AtomicIntegerArray dots2;
    //public static int[] dots, dots2;
    
    
    //public long lastSystemTime, newSystemTime, delta;
    
    public static Canvas canvas;
    
    public ScopeLite() {
        
        mainFrame = new JFrame("Scope");
        canvas = new Canvas();
        mainFrame.getContentPane().add(canvas);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        canvas.setBounds(0,0,screenWidth * 3, screenHeight * 3);
        canvas.setIgnoreRepaint(true);
        mainFrame.setIgnoreRepaint(true);
        
        //toggleFullscreen
        if(fullscreen) {
            mainFrame.setUndecorated(true);
            mainFrame.setResizable(false);
            mainFrame.setPreferredSize(new Dimension(screenWidth * 2,screenHeight * 2));
        } else {
            mainFrame.setPreferredSize(new Dimension(screenWidth,screenHeight));
            mainFrame.setResizable(true);
        }
        
        mainFrame.pack();
        mainFrame.setVisible(true);
        
        canvas.requestFocusInWindow();
        
        canvas.createBufferStrategy(2);
        strategy = canvas.getBufferStrategy();
        
        inputListener = new InputListener();
        canvas.addKeyListener(inputListener);
        canvas.addMouseListener(inputListener);
    }
    
    
    public static void main(String[] args) {
        soundCapturer = new SoundCapturer();
        bgTasks = new BackgroundTasks();
        drawer = new Drawer();
        scopeLite = new ScopeLite();
        
        soundCapturer.start();
        bgTasks.start();
        drawer.start();
        
        //new Sleeper().start();
    }
    
    public void toggleFullscreen() {
        if(fullscreen) {
            //graphicsDevice.setFullScreenWindow(null);
            fullscreen = false;
            mainFrame.dispose();
            scopeLite = new ScopeLite();
        } else {
            fullscreen = true;
            mainFrame.dispose();
            scopeLite = new ScopeLite();
            graphicsDevice.setFullScreenWindow(mainFrame);
        }
    }
    
    public void showError(String message, Exception exception) {
        if(errorForm == null) {
            // First time error
            errorForm = new JFrame("Error");
            errorText = new TextArea();
            errorForm.getContentPane().add(errorText);
            errorForm.pack();
        }
        
        errorText.append(message);
        errorText.append("\n\n");
        if(exception != null) {
            errorText.append(exception.getMessage());
            errorText.append("\n\n");
            for(StackTraceElement stack : exception.getStackTrace()) {
                errorText.append(stack.toString() + "\n");
            }
        }
        
        if(!errorForm.isVisible())
            errorForm.setVisible(true);
        
    }
    
}