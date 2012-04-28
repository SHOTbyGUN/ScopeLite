/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package scopelite;

import common.DeltaTime;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicIntegerArray;
import javax.sound.sampled.*;

/**
 *
 * @author SHOTbyGUN
 */
public class SoundCapturer extends GetX implements Runnable {
    
    public Thread thread;
    public long delta = 100;
    public volatile int buffer;
    public boolean isPaused = false;
    public ArrayList<AtomicIntegerArray> channelsData = new ArrayList<AtomicIntegerArray>();
    public int highPassAdjust = -41, highPassAdjustDefault = highPassAdjust;
    public int midPassAdjust = - 39, midPassAdjustDefault = midPassAdjust;
    public ArrayList<Mixer.Info> mixers = new ArrayList<Mixer.Info>();
    public int currentMixer = 0;
    
    private TargetDataLine line;
    private boolean keepRunning;
    private DeltaTime deltaTime;
    private boolean restarting = false;
    
    private int bitRate = 16, channels = 2, herz = 48000, bytesPerChannel;
    private int localBufferSize = 100000; // xRunner pool
    private int lineBufferSize;
    private int value;
    private float maxValue;
    
    public volatile int monoRunner;
    
    public SoundCapturer() {
        
        // Create local buffers
        for(int i = 0; i < channels + 3; i++) {
            channelsData.add(new AtomicIntegerArray(localBufferSize));
        }
        
        // Get system recording devices
        
        Mixer.Info[] mixerInfo;
        Line.Info targetDataLine = new Line.Info(TargetDataLine.class);
        mixerInfo = AudioSystem.getMixerInfo();
        for(int i = 0; i < mixerInfo.length; i++) {
            Mixer selectedMixer = AudioSystem.getMixer(mixerInfo[i]);
            if(selectedMixer.isLineSupported(targetDataLine) ) {
                mixers.add(mixerInfo[i]);
            }
        }
        
        // Select recording device, or leave default index 0
        for(int i = 0; i < mixers.size(); i++) {
            if(mixers.get(i).getName().startsWith("Stereo Mix")) {
                currentMixer = i;
                break;
            }
            if(mixers.get(i).getName().startsWith("What U Hear")) {
                currentMixer = i;
                break;
            }
        }
    }
    
    public void restart() {
        ScopeLite.drawer.stop();
        
        if(keepRunning) {
            stop();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        if(bitRate != 0 && channels != 0 && herz != 0)
            start();
        
        ScopeLite.drawer.start();
    }
    
    public int getBitRate() {
        return bitRate;
    }
    
    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
        restart();
    }
    
    public int getChannels() {
        return channels;
    }
    
    public void setChannels(int channels) {
        this.channels = channels;
        restart();
    }
    
    public int getHerz() {
        return herz;
    }

    public void setHerz(int herz) {
        this.herz = herz;
        restart();
    }

    public float getMaxValue() {
        return maxValue;
    }
    
    public boolean isRunning() {
        return keepRunning;
    }
    
    public int getLocalBufferSize() {
        return localBufferSize;
    }
    
    public int getBufferSize() {
        return lineBufferSize;
    }

    public void start() {
        keepRunning = true;
        //this.bitRate = bitRate;
        //this.channels = channels;
        //this.herz = herz;
        maxValue = (int) java.lang.Math.pow(2, bitRate);
        bytesPerChannel = bitRate / 8;
        thread = new Thread(this);
        thread.setName("Sound Capturer");
        thread.setDaemon(true);
        thread.setPriority(6);
        thread.start();
        
    }

    public void stop() {
        keepRunning = false;
    }
    
    public void togglePause() {
        isPaused = !isPaused;
    }

    @Override
    public void run() {

        AudioFormat format = new AudioFormat(herz, bitRate, channels, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        
        
        if (!AudioSystem.isLineSupported(info)) {
            //System.out.println("Line matching " + info + " not supported.");
            ScopeLite.scopeLite.showError("Line matching " + info + " not supported.", null);
            return;
        }

        try {
            //line = (TargetDataLine) AudioSystem.getLine(info);
            line = (TargetDataLine) AudioSystem.getMixer(mixers.get(currentMixer)).getLine(info);
            
            line.open(format, format.getFrameSize() * (int)format.getSampleRate() / 20);
        } catch (LineUnavailableException ex) { 
            //System.out.println("Unable to open the line: " + ex);
            ScopeLite.scopeLite.showError("Unable to open the line", ex);
            return;
        } catch (SecurityException ex) { 
            //System.out.println(ex.toString());
            ScopeLite.scopeLite.showError("SecurityException", ex);
            return;
        } catch (Exception ex) { 
            ScopeLite.scopeLite.showError("Exception", ex);
            return;
        }

        //int readSize = format.getFrameSize();
        int readSize = format.getFrameSize() * (line.getBufferSize() / 4000);
        byte[] data = new byte[readSize];
        int numBytesRead;
        
        lineBufferSize = line.getBufferSize();
        System.out.println("Frame size in bytes: " + format.getFrameSize());
        System.out.println("Line buffer size: " + line.getBufferSize());
        System.out.println("ReadSize: " + readSize);

        line.start();
        
        int bytePointer = 0, channelPointer = 0;
        int i;
        byte[] newByte = new byte[bytesPerChannel];
        
        deltaTime = new DeltaTime(true);
        
        int lowPass, lastLowPass = 0, midPass, lastMidPass = 0, highPass, lastHighPass = 0;
        AtomicIntegerArray dataLow, dataMid, dataHigh;
        dataLow = ScopeLite.soundCapturer.channelsData.get(2);
        dataMid = ScopeLite.soundCapturer.channelsData.get(3);
        dataHigh = ScopeLite.soundCapturer.channelsData.get(4);

        while (keepRunning) {
            
            delta = deltaTime.getDelta();
            
            buffer = line.available();
            
            if((numBytesRead = line.read(data, 0, readSize)) == -1) {
                keepRunning = false;
                System.out.println("Cannot read from line, Bytes read: " + numBytesRead 
                        + " BufferSize:" + line.getBufferSize());
                break;
            }
            if(!isPaused) {
                for(i = 0; i < data.length; i++) {
                    newByte[bytePointer] = data[i];
                    if(bytePointer >= bytesPerChannel - 1) {
                        value = (new BigInteger(newByte).intValue());
                        channelsData.get(channelPointer).set(xRunner, value);
                        channelPointer++;
                        bytePointer = 0;
                    } else {
                        bytePointer++;
                    }
                    if(channelPointer >= channels) {
                        // Calculate lowPass midPass highPass filtered data combined from all channels
                        
                        lowPass = (channelsData.get(0).get(getX(0)) + channelsData.get(1).get(getX(0)) + lastLowPass * 100) / 102;
                        midPass = (channelsData.get(0).get(getX(-midPassAdjust)) + channelsData.get(1).get(getX(-midPassAdjust))) / 2 - lowPass;
                        midPass = (midPass + (lastMidPass * 3)) / 4;
                        highPass = (channelsData.get(0).get(getX(-highPassAdjust)) + channelsData.get(1).get(getX(-highPassAdjust))) / 2 - lowPass - midPass;

                        lastMidPass = midPass;
                        lastHighPass = highPass;
                        lastLowPass = lowPass;
                        
                        dataLow.set(xRunner, lowPass);
                        dataMid.set(xRunner, midPass);
                        dataHigh.set(xRunner, highPass);
                        
                        if(++xRunner >= localBufferSize)
                            xRunner = 0;
                        bytePointer = 0;
                        channelPointer = 0;
                        
                        //line.flush();
                    }
                }
            }
        }
        line.stop();
        line.close();
    }
    
}
