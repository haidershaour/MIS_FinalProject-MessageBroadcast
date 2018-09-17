package com.example.shaour.myapplicationnew;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class morseplayer {
    private String TAG = "MorsePlayer";
    private final int SAMPLE_RATE = 90000;
    private double duration;  // in seconds
    private int wpmSpeed;
    private int toneHertz;
    private int numSamples;
    private double sample[];
    private byte ditSnd[];
    private byte dahSnd[];
    private byte pauseInnerSnd[];
    private MorseBit[] pattern;
    private AKASignaler signaler = AKASignaler.getInstance();

    // Constructor: prepare to play morse code at SPEED wpm and HERTZ frequency
    public morseplayer(int hertz, int speed) {
        setSpeed(speed);
        setTone(hertz);
        buildSounds();
    }
    // Generate 'dit','dah' and empty sinewave tones of the proper lengths.
    private void buildSounds() {
        // where (1200 / wpm) = element length in milliseconds
        duration = (double)((1200 / wpmSpeed) * .001);
        numSamples = (int)(duration * SAMPLE_RATE - 1);
        double sineMagnitude = 1; // starting with a dummy value for absolute normalized value of sine wave
        double CUTOFF = 0.1; // threshold for whether sine wave is near zero crossing

        double phaseAngle = 2 * Math.PI / (SAMPLE_RATE/toneHertz);

        while (sineMagnitude > CUTOFF){
            numSamples++;
            //check to see if  is near zero-crossing to avoid clicks when sound cuts off
            sineMagnitude = Math.abs(Math.sin(phaseAngle*numSamples));
        }


        sample = new double[numSamples];
        ditSnd = new byte[2 * numSamples];
        dahSnd = new byte[6 * numSamples];
        pauseInnerSnd = new byte[2 * numSamples];

        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(phaseAngle * i);
        }
        // convert to 16 bit pcm sound array; assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            final short val = (short) ((dVal * 32767)); // scale to maximum amplitude
            // in 16 bit wav PCM, first byte is the low order byte
            ditSnd[idx++] = (byte) (val & 0x00ff);
            ditSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        for (int i = 0; i < (dahSnd.length); i++) {
            dahSnd[i] = ditSnd[i % ditSnd.length];
        }
        for (int i = 0; i < (pauseInnerSnd.length); i++) {
            pauseInnerSnd[i] = 0; //Adjust for radio PTT
        }
    }

    private void setSpeed(int speed) {
        wpmSpeed = speed;
    }
    private void setTone(int hertz) {
        toneHertz = hertz;
    }

    // The main method of this class; runs exactly once in a standalone thread.
    public void playpreparing(int leng){
        int msgSize = leng;
        System.out.println("creat audiotrack");
        int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        signaler.audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
        signaler.msgSize = msgSize;
        // Start playing sound out of the buffer
        signaler.audioTrack.play();
    }
    public void playdot(){

        signaler.audioTrack.write(ditSnd, 0, ditSnd.length);
    }

    public void playdash(){

        signaler.audioTrack.write(dahSnd, 0, dahSnd.length);
    }
}

