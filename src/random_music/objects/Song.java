package random_music.objects;

import random_music.Constants;
import random_music.MusicPlayer;
import random_music.objects.enums.*;
import random_music.objects.options.MeasureOptions;
import random_music.objects.options.SongOptions;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 9:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class Song {
    private static final double CHORD_COMPLEX_MOD = 1.5;//chords are twice as likely as other complexity-based components

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

        Note[][] measuresChords = getMeasuresChords(songOptions, defaultMeasureOptions, r);

        for(int i = 0; i < mChannels.length; i++) {
            mChannels[i].setMeasuresTransposed(generateMeasures(mChannels[i].getSongUpdates(), mChannels[i].getMeasureUpdates(), measuresChords, r));
        }

        return mChannels;
    }

    private Note[][] getMeasuresChords(SongOptions songOptions, MeasureOptions measureOptions, Random r) {
        Note[][] measures = new Note[songOptions.getNumMeasures()][];
        Note[] currChordTones = null;

        //Set opening chord for each set of measures to root
        measureOptions.setPrimaryNotes(currChordTones);

        for (int i = 0; i < measures.length; i++){
            currChordTones = getMeasureChord(songOptions, measureOptions, currChordTones, r);
            measures[i] = currChordTones;

            currChordTones = getMeasureChord(songOptions, measureOptions, currChordTones, r);
            measureOptions.setPrimaryNotes(currChordTones);
        }

        return measures;
    }

    private Measure[] generateMeasures(SongOptions songUpdates, MeasureOptions options, Note[][] measuresChords, Random r) {
        Measure[] m = new Measure[songUpdates.getNumMeasures()];
        Note[] measureChord;

        for(int i = 0; i < songUpdates.getNumMeasures(); i++){
            measureChord = measuresChords[i];
            //populate the measure with current options
            m[i] = generateMeasure(songUpdates, options, measureChord, r);
        }

        return m;
    }

    private Note[] getMeasureChord(SongOptions songUpdates, MeasureOptions options, Note[] lastRootTone, Random r) {
        int chordRootTone = songUpdates.getRootPitch();
        if(null != lastRootTone && null != lastRootTone[0]){
            ScaleEnum scale = songUpdates.getScale();
            chordRootTone = scale.getNextScaleStep(songOptions, options, lastRootTone[0].getTone(), r);
        }

        return generateMeasureChordNotes(songUpdates, chordRootTone);
    }

    private Note[] generateMeasureChordNotes(SongOptions songUpdates, int chordRootTone) {
        //TODO: Procedurally generate based on variance & attributes
        ChordEnum shape = ChordEnum.MINOR_TRIAD;

        if(songUpdates.isMajor()){
            shape = ChordEnum.MAJOR_TRIAD;
        }

        Note[] notes = new Note[shape.getNotes().length];

        for(int i = 0; i < notes.length; i++){
            notes[i] = new Note(chordRootTone + shape.get(i), false, null);
        }

        return notes;
    }

    private int generateChordPercent(Random r) {
        return MusicPlayer.sanitizePercent(variate(((int) (songOptions.getComplexity() * CHORD_COMPLEX_MOD)), r));
    }

    private int variate(int percent, Random r) {
        int variance = r.nextInt(songOptions.getAttributeVariance());
        return percent + variance - (songOptions.getAttributeVariance() / 2);
    }

    private Measure generateMeasure(SongOptions songUpdates, MeasureOptions options, Note[] measureChord, Random r) {
        Timing timing = new Timing(options.getTimingOptions(), songUpdates.getMeasureMs(), songUpdates.getSpeed(), r);
        Note[] notes = new Note[timing.getNumTimes()];
        Note lastNote = null;
        Note currNote;

        options.setChordChance(generateChordPercent(r));

        for(int i = 0; i < timing.getNumTimes(); i++){
            currNote = nextNote(lastNote, options, timing.get(i), measureChord, r);

            notes[i] = currNote;
            lastNote = currNote;
        }

        return new Measure(notes);
    }

    private int generateSpeedPercent(Random r) {
        return MusicPlayer.sanitizePercent(variate(songOptions.getSpeed(), r));
    }

    public Note nextNote(Note lastNote, MeasureOptions options, NoteLengthEnum noteLength, Note[] elligibleNotes, Random r) {
        int percent = r.nextInt(100);
        int chance = r.nextInt(options.getTotalChance());
        int noteTone = 0;
        int runningChance = 0;
        Note note = null;

        if(null == lastNote) {
            noteTone = elligibleNotes[0].getTone();
        }else if(chance < (runningChance+=options.getRepeatChance())){
            //repeat last note with new duration
            note = new Note(lastNote.getTone(), Constants.NOT_REST, noteLength);
        } else if( chance < (runningChance+=options.getRestChance())){
            //insert a rest with new duration
            return new Note(lastNote.getTone(), Constants.IS_REST, noteLength);
        } else {
            noteTone = randTone(elligibleNotes, r);
        }

        //TODO: Remove if thoroughly handled elsewhere
//        if(null != lastNote){
//            //Only reach this point if changing to a replacement non-rest tone
//            if(r.nextBoolean()){
//                //...rising
//                noteTone = ScaleEnum.nearestNote(noteTone, lastNote.getTone(), RISING);
//            } else {
//                //...falling
//                noteTone =  ScaleEnum.nearestNote(noteTone, lastNote.getTone(), FALLING);
//            }
//        }

        if(null == note){
            note = new Note(noteTone, Constants.NOT_REST, noteLength);
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

    private int randTone(Note[] elligibleNotes, Random r) {
        return elligibleNotes[r.nextInt(elligibleNotes.length)].getTone();
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

    public int getNumMeasures() {
        return songOptions.getNumMeasures();
    }

    public MeasureChannel[][] getMeasureChannels(int numMeasuresToPlay, Random r) {
        MeasureChannel[][] allChannels = new MeasureChannel[numMeasuresToPlay][];
        int nextMeasureNum = 0;
        for(int i = 0; i < allChannels.length; i++){
            nextMeasureNum = r.nextInt(this.getNumMeasures());
            allChannels[i] = new MeasureChannel[measureChannels.length];
            for(int j = 0; j < measureChannels.length; j++){
                allChannels[i][j] = new MeasureChannel(measureChannels[j]);
                allChannels[i][j].setMeasures(new Measure[]{measureChannels[j].measures[nextMeasureNum]});
            }
        }
        return allChannels;
    }
}
