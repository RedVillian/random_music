package random_music;

import random_music.objects.*;
import random_music.objects.enums.ChordEnum;
import random_music.objects.enums.NoteLengthEnum;
import random_music.objects.enums.ScaleEnum;
import random_music.objects.options.SongOptions;

import javax.sound.midi.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 8:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MusicPlayer {

    private static final int VOLUME = 150;

    public static void main(String [ ] args)
    {
        long seed = System.currentTimeMillis();
        System.err.println("This seed is: " + seed);
        Random r = new Random(seed);
        try {
            Synthesizer synth = getSingleSynthesizer();
            synth.open();
            clearSynthBuffer(synth);
            SongOptions options = new SongOptions();

            options.setRootPitch(100);
            options.setComplexityPercent(20);
            options.setNumMeasures(4);
            playSong(synth, new Song(options, r), r);

            Thread.sleep(1000);

            options.setRootPitch(50);
            options.setScale(ScaleEnum.MINOR);
            options.setMeasureMs(3000);
            options.setComplexityPercent(50);
            playSong(synth, new Song(options, r), r);

            synth.close();
        }
        //MidiSystem.getSynthesizer() failure
        catch (MidiUnavailableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        //Thread.sleep() failure
        catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void clearSynthBuffer(Synthesizer synth) throws InterruptedException {
        playNote(synth, new Note(0, Note.IS_REST, NoteLengthEnum.QUARTER), 100, 0);
    }

    public static Synthesizer getSingleSynthesizer() throws MidiUnavailableException {
        return MidiSystem.getSynthesizer();
    }

    public static void playSong(Synthesizer synth, Song song, Random r) throws InterruptedException {{
        int numLoops = 8;
        MeasureChannel[] currMeasureChannels;

        while(numLoops > 0){
            currMeasureChannels = song.getMeasureChannels();
            playMeasureChannels(synth, currMeasureChannels, song.getMeasureMs(), r);
            numLoops--;
        }
    }}

    private static void playMeasureChannels(Synthesizer synth, MeasureChannel[] measureChannels, int measureMs, Random r) throws InterruptedException {
        MidiChannel[] channels = synth.getChannels();
        int remainingMs = measureMs;

        //TODO: randomize measures within each measureChannel?

        Queue<Note>[] currentNoteQueues;
        currentNoteQueues = new Queue[measureChannels.length];
        for(int i = 0; i < currentNoteQueues.length; i++){
            currentNoteQueues[i] = new LinkedList<Note>();
        }

        //Load current notes from current measure...
        for(int i = 0; i < measureChannels.length; i++){
            for(Note n : measureChannels[i].getRandMeasure(r).getNotes()){
                //Set the channel not will play on and calculate the total milleseconds it will be on
                n.setChannelNum(i).setChannelMs(measureMs);
                currentNoteQueues[i].add(n);
            }
        }

        //Initial noteOns...
        for(Queue<Note> q : currentNoteQueues){
            noteOn(channels, q.remove());
        }

        int minWaitingMs = measureMs;
        Queue<Note> offNotes = new LinkedList<Note>();
        Queue<Note> onNotes = new LinkedList<Note>();
        //for the rest of the measure...
        while(remainingMs > 0){
            //Find the minimum time until change
            for(Queue<Note> nQ : currentNoteQueues){
                Note currNote = nQ.peek();
                if(null != currNote){
                    minWaitingMs = Math.min(minWaitingMs, currNote.getChannelMs());
                }
            }

            //For every note queue head...
            for(Queue<Note> nQ : currentNoteQueues){
                //...that matches the currently changing milleseconds...
                Note currNote = nQ.peek();
                if(null != currNote){
                    if(currNote.getChannelMs() <= minWaitingMs){
                        //Remove the head and queue it for noteOff...
                        offNotes.add(nQ.remove());
                        //...and peek at the next note and queue it for noteOn
                        onNotes.add(nQ.peek());
                        //For all other notes...
                    } else {
                        //...reduce their remaining wait
                        nQ.peek().decrementChannelMs(minWaitingMs);
                    }
                }
            }

            //wait the previously assessed number of ms
            Thread.sleep(minWaitingMs);

            //noteOff expiring notes
            while(offNotes.size() > 0){
                noteOff(channels, offNotes.remove());
            }
            //...and noteOn new
            while(onNotes.size() > 0){
                Note currNote = onNotes.remove();
                if(null != currNote){
                    noteOn(channels, currNote);
                }
            }

            //decrement remaining ms in the measure and prep minWaitingMs for next iteration
            remainingMs -= minWaitingMs;
            minWaitingMs = remainingMs;
        }
    }

    public static void playMeasure(Synthesizer synth, Measure measure, int measureMs, Random r) throws InterruptedException {
        for(Note n : measure.getNotes()){

            playNote(synth, n, measureMs, 0);
            //TODO: Staccato?
        }
    }

    static void playNote(Synthesizer synth, Note n, int measureMs, int channel) throws InterruptedException {
        int ms = getNoteTime(n.getLength(), measureMs);
        if(n.isRest()){
            System.out.println(ms + "ms rest");
            Thread.sleep(ms);
        } else if(n.getChord() != ChordEnum.NOTE){
            System.out.println("Chord: " + n.getTone() + " for " + ms + "ms");
            playMidiNote(synth, n.getTone(), ms, channel, n.getChord());

        }else{
            System.out.println("Tone: " + n.getTone() + " for " + ms + "ms");
            playMidiNote(synth, n.getTone(), ms, channel, n.getChord());
        }
    }

    public static void playMidiNote(Synthesizer synth, int pitch, int length, int channel, ChordEnum chord) throws InterruptedException {
        MidiChannel[] channels = synth.getChannels();
        for(int note : chord.getNotes()){
            channels[channel].noteOn((pitch + note), VOLUME);
        }
        Thread.sleep(length);
        for(int note : chord.getNotes()){
            channels[channel].noteOff((pitch + note));
        }
    }

    private static void noteOn(MidiChannel[] channels, Note note) {
        int volume = VOLUME;
        if(!note.isRest()){
            for(int n : note.getChord().getNotes()){
                channels[note.getChannelNum()].noteOn((note.getTone() + n), volume);

                //debug reporting
                if(note.getChord() != ChordEnum.NOTE){
                    System.out.print("Chord ");
                }
                System.out.print(note.getTone() + ", ");
            }
            System.out.println("on.");
        }
    }

    private static void noteOff(MidiChannel[] channels, Note note) {
        if(!note.isRest()){
            //TODO: remove debug reporting
            if(note.getChord() != ChordEnum.NOTE){
                System.out.print("Chord ");
            }
            for(int n : note.getChord().getNotes()){
                channels[note.getChannelNum()].noteOff((note.getTone() + n));

                System.out.print((note.getTone()+n) + ", ");
            }
            System.out.println("on.");
        }
    }

    private static int getNoteTime(NoteLengthEnum nLength, int measureMs) {
        return (int)(measureMs * nLength.getRatio());
    }

    public static int sanitizePercent(int percent) {
        return Math.max(Math.min(percent, Song.PERCENT), 0);
    }
}
