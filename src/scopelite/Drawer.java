/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import common.DeltaTime;
import common.IntCache;
import common.StatisticCounter;
import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 *
 * @author SHOTbyGUN
 */
public class Drawer extends GetX implements Runnable {
    
    public Thread thread;
    public int samplesPerLongScope = 50, samplesPerLongScopeDefault = samplesPerLongScope;
    
    private boolean keepRunning = true;
    private DeltaTime deltaTime, deltaTimeBG;
    private StatisticCounter bufferStat;
    private StatisticCounter readStat;
    private StatisticCounter fpsStat;
    private StatisticCounter fpsBGStat;
    private long delta, deltaBG;
    private AtomicIntegerArray dataLeft, dataRight;
    private AtomicIntegerArray dataLow, dataMid, dataHigh;
    //private int xRunner;
    private Graphics2D g;
    private int x, max = 0, value, absolute, num;
    private int lineOffset = 0;
    private int lineHeight = 0;
    
    private Color highLine = new Color(0, 255, 255);
    private Color midLine = new Color(255, 255, 150);
    private Color lowLine = new Color(255, 200, 200);
    private Color leftLine = new Color(255, 100, 100);
    private Color rightLine = new Color(50, 255, 50);
    
    private Color highArea = new Color(0, 200, 200, 130);
    private Color midArea = new Color(200, 200, 100, 130);
    private Color lowArea = new Color(200, 150, 150, 130);
    //private Color leftArea = new Color(255, 50, 50, 70);
    //private Color rightArea = new Color(0, 200, 0, 70);
    private int diff;
    private int wait;
    //private int sleepMillis;
    private int sleepNanos;
    
    private IntCache cache = new IntCache();
    private int cacheRunner, cacheRunnerDelta, xRunnerOffset;
    private int processNum;
    private int[] currentCache;
    
    private int lastHeight, highOffset, midOffset, lowOffset, stereoOffset;
    
    // Spectrogram
    private double[] spectrogram = new double[2048];
    private double barWidth;
    private int barBorder;
    private int barAmount = 200, barAmountDefault = barAmount;
    private int barFreq = 512, barFreqDefault = barFreq;
    private int barMaxSpot = 678, barMaxSpotDefault = barMaxSpot;
    public double barSensitivity = 2.0d, barSensitivityDefault = barSensitivity;
    public int redBarId = -1;
    /*
    public int barColorRed = 200, barColorRedDefault = barColorRed;
    public int barColorGreen = 120, barColorGreenDefault = barColorGreen;
    public int barColorBlue = 70, barColorBlueDefault = barColorBlue;
    * 
    */
    private Color barColor = new Color(200, 120, 70);
    
    // Line thickness
    public int lineThickness = 2;
    private int lineThicknessLoop;
    private int lineThicknessOffset;
    
    public void setBarColor(Color color) {
        barColor = color;
    }
    
    public Color getBarColor() {
        return barColor;
    }
    
    public void modifyBar(int barFreqMod, int barAmountMod) {
        int newBarMaxSpot = (int)((barFreq + barFreqMod) * java.lang.Math.log10(21))+2;
        if(newBarMaxSpot < spectrogram.length / 1.2 && barAmount + barAmountMod > 1 && barFreq + barFreqMod > 1) {
            barFreq += barFreqMod;
            barAmount += barAmountMod;
            barMaxSpot = newBarMaxSpot;
        }
    }
    
    public void resetBarSettings() {
        barAmount = barAmountDefault;
        barFreq = barFreqDefault;
        barMaxSpot = barMaxSpotDefault;
    }
    
    public int getMaxBarFreq() {
        //double spotFreq = 1.0d * ScopeLite.soundCapturer.getHerz() / spectrogram.length;
        return (int)(getSpot(1) * getSpotFreq());
    }
    
    private double getSpotFreq() {
        return 1.0d * ScopeLite.soundCapturer.getHerz() / 2 / spectrogram.length;
    }
    
    public int getBarAmount() {
        return barAmount;
    }
    
    public int viewBarFreq() {
        return (int)(getSpot(barAmount - redBarId) * getSpotFreq());
    }
    
    public int getSpot(int i) {
        return (int)(barMaxSpot - java.lang.Math.log10(20.0d/barAmount*i+1) * barFreq);
    }
    
    
    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.setPriority(6);
        thread.setName("Drawer");
        thread.start();
    }
    
    public void stop() {
        keepRunning = false;
    }
    
    private void drawAmplitudeScope(AtomicIntegerArray dataSource, Color color, int offset) {
        
        currentCache = cache.getCache(dataSource);
        g.setColor(color);
        
        for(x = 1; x < ScopeLite.screenWidth; x++) {
            

            try {
                max = cache.getValue(currentCache, x);
            } catch (Exception ex) {
                System.out.println("no cache value found!");
                max = 0;
            }
            
            
            g.drawLine(x, SignalModifier.modifyY(max) + offset,
                x, SignalModifier.modifyY(-max) + offset);
            //lastLongLowPass = max;
        }
    }
    
    private int getMaxAmplitude(AtomicIntegerArray dataSource, int x) {
        int maxValue = 0;
        for(num = 0; num < samplesPerLongScope; num++) {
            value = dataSource.get(getX(x*samplesPerLongScope+num+xRunnerOffset));
            absolute = java.lang.Math.abs(value);
            if(absolute > java.lang.Math.abs(maxValue)) {
                maxValue = value;
            }
        }
        return maxValue;
    }
    
    public void startWorkerThread() {
        Runnable workerRunnable = new Runnable() {
            @Override
            public void run() {
                
                double[] dataPool = new double[spectrogram.length*2];
                
                int i;
                
                // Old working FFT class
                //FFT fft = new FFT();
                
                // New JTransforms FFT
                DoubleFFT_1D fft = new DoubleFFT_1D(dataPool.length / 2);
                
                
                while (keepRunning) {
                    
                    deltaBG = deltaTimeBG.getDelta();
                    
                    try {
                        // Pre calculate amplitude history
                        
                        // Get xRunner difference since last update
                        if(cacheRunner == xRunner)
                            cacheRunnerDelta = 0;
                        else if(cacheRunner < xRunner)
                            cacheRunnerDelta = xRunner - cacheRunner;
                        else
                            cacheRunnerDelta = ScopeLite.soundCapturer.getLocalBufferSize() - cacheRunner + xRunner;

                        cacheRunner = xRunner;

                        // Calculate how many amplitudeSlides we need to calculate
                        processNum = cacheRunnerDelta / samplesPerLongScope;
                        xRunnerOffset += cacheRunnerDelta % samplesPerLongScope;
                        if(xRunnerOffset >= samplesPerLongScope) {
                            processNum++;
                            xRunnerOffset -= samplesPerLongScope;
                        }
                        
                        for(i = processNum; i > 0; i--) {
                            cache.addValue(cache.getCache(dataHigh), getMaxAmplitude(dataHigh, i));
                        }
                        for(i = processNum; i > 0; i--) {
                            cache.addValue(cache.getCache(dataMid), getMaxAmplitude(dataMid, i));
                        }
                        for(i = processNum; i > 0; i--) {
                            cache.addValue(cache.getCache(dataLow), getMaxAmplitude(dataLow, i));
                        }
                        
                        
                        
                        for(i = 0; i < dataPool.length; i++) {
                            dataPool[i] = (ScopeLite.soundCapturer.channelsData.get(0).get(getX(i))
                                    + ScopeLite.soundCapturer.channelsData.get(1).get(getX(i))) / ScopeLite.soundCapturer.getMaxValue();
                        }
                        
                        // Old FFT
                        //dataPool = fft.four1(dataPool, dataPool.length / 4, 1);
                        
                        // New FFT
                        fft.realForwardFull(dataPool);
                        
                        for(i = 0; i < dataPool.length / 2; i++) {
                            spectrogram[i] = java.lang.Math.sqrt(dataPool[i]*dataPool[i] + dataPool[i+dataPool.length/2] * dataPool[i+dataPool.length/2]);
                            //spectrogram[i] = data[i];
                        }
                        
                        
                        
                        Thread.sleep(3);
                        
                    } catch (Exception ex) {
                        ScopeLite.scopeLite.showError("Error in Drawer worker thread", ex);
                        //System.out.println(ex.toString());
                        
                        ex.printStackTrace();
                    }
                }
            }
        };
        Thread workerThread = new Thread(workerRunnable, "Drawer worker");
        workerThread.setDaemon(true);
        workerThread.start();
    }
    
    

    @Override
    public void run() {
        
        keepRunning = true;
        deltaTime = new DeltaTime(true);
        deltaTimeBG = new DeltaTime(true);
        bufferStat = new StatisticCounter(100);
        readStat = new StatisticCounter(100);
        fpsStat = new StatisticCounter(10);
        fpsBGStat = new StatisticCounter(10);
        
        dataLeft = ScopeLite.soundCapturer.channelsData.get(0);
        dataRight = ScopeLite.soundCapturer.channelsData.get(1);

        dataLow = ScopeLite.soundCapturer.channelsData.get(2);
        dataMid = ScopeLite.soundCapturer.channelsData.get(3);
        dataHigh = ScopeLite.soundCapturer.channelsData.get(4);
        
        int i;
        
        startWorkerThread();
        
        while(keepRunning) {
            
            delta = deltaTime.getDelta();
            
            try {
                
                if(lastHeight != ScopeLite.canvas.getHeight()) {
                    lastHeight = ScopeLite.canvas.getHeight();
                    lineHeight = ScopeLite.canvas.getHeight() / 6;
                    lineOffset = ScopeLite.canvas.getHeight() / 20;
                    highOffset = lineHeight - lineOffset;
                    midOffset = lineHeight * 2 - lineOffset;
                    lowOffset = lineHeight * 3 - lineOffset;
                    stereoOffset = lineHeight * 4;
                }
            
                do {

                    do {

                        xRunner = ScopeLite.soundCapturer.xRunner;

                        g = (Graphics2D) ScopeLite.strategy.getDrawGraphics();

                        g.setColor(Color.BLACK);
                        g.fillRect(0,0,ScopeLite.screenWidth, ScopeLite.canvas.getHeight());
                            
                            barWidth = 1.0d * ScopeLite.screenWidth / barAmount;
                            if(barWidth < 2) {
                                barWidth = 1;
                                barBorder = 0;
                            } else {
                                barBorder = (int)(barWidth / 4);
                            }
                            for(i = 0; i <= barAmount; i++) {
                                
                                if(redBarId == i)
                                    g.setColor(Color.RED);
                                else
                                    g.setColor(barColor);
                                
                                g.fillRect((int)(barWidth * i), ScopeLite.canvas.getHeight() - (int)(java.lang.Math.log10(spectrogram[getSpot(barAmount-i)] / barSensitivity) / 8 * ScopeLite.amplitudeModifierFactor * ScopeLite.screenHeight), 
                                        (int)(barWidth - barBorder), ScopeLite.canvas.getHeight());
                            }
                        
                            for(lineThicknessLoop = 0; lineThicknessLoop < lineThickness; lineThicknessLoop++) {
                                
                                if(lineThicknessLoop % 2 == 0) {
                                    lineThicknessOffset = lineThicknessLoop / 2 * -1;
                                } else {
                                    lineThicknessOffset = lineThicknessLoop / 2 + 1;
                                }
                        
                                for(x = 1; x < ScopeLite.screenWidth; x++) {


                                    // High
                                    g.setColor(highLine);
                                    g.drawLine(x, SignalModifier.modifyY(dataHigh.get(getX(x))) + highOffset + lineThicknessOffset,
                                            x + 1, SignalModifier.modifyY(dataHigh.get(getX(x+1))) + highOffset + lineThicknessOffset);


                                    // Mid
                                    g.setColor(midLine);
                                    g.drawLine(x, SignalModifier.modifyY(dataMid.get(getX(x))) + midOffset + lineThicknessOffset,
                                            x + 1, SignalModifier.modifyY(dataMid.get(getX(x+1))) + midOffset + lineThicknessOffset);


                                    // Low
                                    g.setColor(lowLine);
                                    g.drawLine(x, SignalModifier.modifyY(dataLow.get(getX(x))) + lowOffset + lineThicknessOffset,
                                            x + 1, SignalModifier.modifyY(dataLow.get(getX(x+1))) + lowOffset + lineThicknessOffset);

                                }

                                g.setColor(leftLine);
                                for(x = 0; x < ScopeLite.screenWidth; x++) {
                                    if(x != 0) {
                                        g.drawLine(x, SignalModifier.modifyY(dataLeft.get(getX(x))) + stereoOffset + lineThicknessOffset,
                                                x+1, SignalModifier.modifyY(dataLeft.get(getX(x+1))) + stereoOffset + lineThicknessOffset);
                                    }
                                }

                                g.setColor(rightLine);
                                for(x = 0; x < ScopeLite.screenWidth; x++) {
                                    if(x != 0) {
                                        g.drawLine(x, SignalModifier.modifyY(dataRight.get(getX(x))) + stereoOffset + lineThicknessOffset, 
                                                x+1, SignalModifier.modifyY(dataRight.get(getX(x+1))) + stereoOffset + lineThicknessOffset);
                                    }
                                }

                            }



                            drawAmplitudeScope(dataHigh, highArea, highOffset);
                            drawAmplitudeScope(dataMid, midArea, midOffset);
                            drawAmplitudeScope(dataLow, lowArea, lowOffset);

                            //drawAmplitudeScope(dataLeft, leftArea, lineHeight * 4);
                            //drawAmplitudeScope(dataRight, rightArea, lineHeight * 4);
                        //}




                        g.setColor(Color.WHITE);

                        if(ScopeLite.showFps) {
                            // Draw FPS

                            
                            bufferStat.setValue(ScopeLite.soundCapturer.buffer);
                            readStat.setValue((int)ScopeLite.soundCapturer.delta);
                            fpsStat.setValue((int)(1000000000 / delta));
                            fpsBGStat.setValue((int)(1000000000 / deltaBG));

                            g.drawString("Draw FPS: " + fpsStat.getAverage()
                                    + " Reads: " + 1000000000 / readStat.getAverage()
                                    + " buffer min: " + bufferStat.getMin() 
                                    + " max: " + bufferStat.getMax()
                                    + " of " + ScopeLite.soundCapturer.getBufferSize()
                                    + " pre-processor: " + fpsBGStat.getAverage(), 0, 10);
                            // Draw Menu
                            //g.drawString(InputListener.currentMenu.get(InputListener.currentMenuIndex).toString(), 
                            //        ScopeLite.screenWidth / 3, 10);
                            //g.drawString(InputListener.currentMenu.toString(), ScopeLite.screenWidth / 2, 10);
                        }

                        if(ScopeLite.showHelp) {
                            i = 0;
                            for(String str : ScopeLite.helpText) {
                                g.drawString(str, 0, 50 + i++*15);
                            }
                        }
                        
                        if(redBarId != -1) {
                            g.setColor(Color.RED);
                            g.drawString(Integer.toString(viewBarFreq()) + " Hz", ScopeLite.screenWidth / 2, 30);
                        }

                        g.setColor(Color.GRAY);
                        g.drawString("H for help - Copyright © 2012 Teemu Kauhanen - v1.8", ScopeLite.screenWidth - 310, ScopeLite.canvas.getHeight() - 5);

                        //ScopeLite.mainFrame.repaint();

                        g.dispose();

                    } while (ScopeLite.strategy.contentsRestored());

                    ScopeLite.strategy.show();


                } while (ScopeLite.strategy.contentsLost());
            
            } catch (Exception ex) {
                
            }
            
            //4000000 = 4ms = 250fps
            // 1 000 000 000 = one second
            if(ScopeLite.maxFps > 0) {
                wait = (1000000000 / ScopeLite.maxFps);
                diff = (int)(System.nanoTime() - deltaTime.getLastSystemTime());
                //sleepMillis = (wait - diff) / 1000000;
                //sleepNanos = (wait - diff) - (1000000 * sleepMillis);
                sleepNanos = wait - diff;
                if(diff < wait) {
                    try {
                        //java.lang.concurrent.locks.LockSupport.parkNanos();
                        java.util.concurrent.locks.LockSupport.parkNanos(sleepNanos);
                        //Thread.sleep(sleepMillis, sleepNanos);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
    
}
