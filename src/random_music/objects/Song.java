package random_music.objects;

import com.sun.media.sound.SimpleInstrument;
import random_music.MusicPlayer;
import random_music.objects.enums.*;
import random_music.objects.options.MeasureOptions;
import random_music.objects.options.SongOptions;
import random_music.objects.options.TimingOptions;

import javax.sound.midi.Instrument;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class Song {
    public static final boolean RISING = true;
    public static final boolean FALLING = true;

    public static final int NUM_NOTES_IN_MEASURE = 32;
    private static final double CHORD_COMPLEX_MOD = 1.5;//chords are twice as likely as other complexity-based components
    public static final int PERCENT = 100;

    SongOptions songOptions;
    MeasureOptions defaultMeasureOptions;

    MeasureChannel[] measureChannels;

    public Song(SongOptions songOptions, Random r) {
        this.songOptions = songOptions;

        this.defaultMeasureOptions = generateDefaultMeasureOptions();
        measureChannels = generateMeasureChannels(r);
    }

    //TODO
    private MeasureOptions generateDefaultMeasureOptions() {
        MeasureOptions defaults = new MeasureOptions();
        return defaults;
    }

    private MeasureChannel[] generateMeasureChannels(Random r) {
        MeasureChannel[] mChannels = new MeasureChannel[2];

        MeasureOptions highMeasureOptions = defaultMeasureOptions.setChordChance(30).setPrimaryChance(70);
        highMeasureOptions.getTimingOptions().setTimeSignature(TimeSignatureEnum.EIGHT_EIGHT);

        mChannels[0] = new MeasureChannel(ChannelRangeEnum.HIGH, songOptions, highMeasureOptions);
        mChannels[1] = new MeasureChannel(ChannelRangeEnum.LOW, songOptions, defaultMeasureOptions.setChordChance(100).setVolume(300));

        for(MeasureChannel m : mChannels){
            m.setMeasures(generateMeasures(m.getSongUpdates(), m.getMeasureUpdates(), r));
        }

        return mChannels;
    }

    private Measure[] generateMeasures(SongOptions songUpdates, MeasureOptions options, Random r) {
        Measure[] m = new Measure[songUpdates.getNumMeasures()];

        for(int i = 0; i < songUpdates.getNumMeasures(); i++){
            m[i] = generateMeasure(songUpdates, options, r);
        }

        return m;
    }

    private int generateChordPercent(Random r) {
        return MusicPlayer.sanitizePercent(variate(((int) (songOptions.getComplexity() * CHORD_COMPLEX_MOD)), r));
    }

    private int variate(int percent, Random r) {
        int variance = r.nextInt(songOptions.getAttributeVariance());
        return percent + variance - (songOptions.getAttributeVariance() / 2);
    }

    private Measure generateMeasure(SongOptions songUpdates, MeasureOptions options, Random r) {
        Timing timing = new Timing(options.getTimingOptions(), songUpdates.getMeasureMs(), songUpdates.getSpeed(), r);
        Note[] notes = new Note[timing.getNumTimes()];
        Note lastNote = null;
        Note currNote;

        options.setChordChance(generateChordPercent(r));

        for(int i = 0; i < timing.getNumTimes(); i++){
            currNote = nextNote(lastNote, options, timing.get(i), r);

            notes[i] = currNote;
            lastNote = currNote;
        }

        return new Measure(notes);
    }

    private int generateSpeedPercent(Random r) {
        return MusicPlayer.sanitizePercent(variate(songOptions.getSpeed(), r));
    }

    public Note nextNote(Note lastNote, MeasureOptions options, NoteLengthEnum noteLength, Random r) {
        int percent = r.nextInt(100);
        int chance = r.nextInt(options.getTotalChance());
        int noteTone = 0;
        int runningChance = 0;
        Note note = null;

        if(null == lastNote){
            noteTone = songOptions.getRootPitch();
        } else if(chance < (runningChance+=options.getRepeatChance())){
            //repeat last note with new duration
            note = new Note(lastNote.getTone(), Note.NOT_REST, noteLength);
        } else if( chance < (runningChance+=options.getRestChance())){
            //insert a rest with new duration
            return new Note(lastNote.getTone(), Note.IS_REST, noteLength);
        } else if (chance < (runningChance+=options.getSecondaryChance())) {
            //play a diminished note...
            noteTone = randomNote(r, NoteSetEnum.DIMINISHED);
        } else if (chance < (runningChance+=options.getPrimaryChance())){
            //play a dominant note...
            noteTone = randomNote(r, NoteSetEnum.DOMINANT);
        }

        if(null != lastNote){
            //Only reach this point if changing to a replacement non-rest tone
            if(r.nextBoolean()){
                //...rising
                noteTone = ScaleEnum.nearestNote(noteTone, lastNote.getTone(), RISING);
            } else {
                //...falling
                noteTone =  ScaleEnum.nearestNote(noteTone, lastNote.getTone(), FALLING);
            }
        }

        if(null == note){
            note = new Note(noteTone, Note.NOT_REST, noteLength);
        }

        //Handle chords
        if(percent < options.getChordChance()){
            if(songOptions.getScale().isMajor()){
                note.setChord(ChordEnum.MAJOR_TRIAD);
            } else {
                note.setChord(ChordEnum.MINOR_TRIAD);
            }
        }

        return note;
    }

    static int randomNote(Random r, NoteSetEnum noteSet) {
        return noteSet.notes[r.nextInt(noteSet.notes.length)];
    }

    public int getMeasureMs() {
        return songOptions.getMeasureMs();
    }

    public ScaleEnum getScale() {
        return songOptions.getScale();
    }

    public MeasureChannel[] getMeasureChannels() {
        return measureChannels;
    }
}
