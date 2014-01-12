package random_music.objects.options;

import random_music.objects.Note;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeasureOptions {
    private int restChance = 10;
    private int repeatChance = 50;
    private int secondaryChance = 5;
    private int primaryChance = 45;
    private Note[] primaryNotes;

    private int chordChance = 100;
    private TimingOptions timingOptions = new TimingOptions();
    private int volume;

    public int getTotalChance() {
        return repeatChance +
                secondaryChance +
                primaryChance;
    }

    public int getRestChance() {
        return restChance;
    }

    public MeasureOptions setRestChance(int restChance) {
        this.restChance = restChance;
        return this;
    }

    public int getRepeatChance() {
        return repeatChance;
    }

    public MeasureOptions setRepeatChance(int repeatChance) {
        this.repeatChance = repeatChance;
        return this;
    }

    public int getSecondaryChance() {
        return secondaryChance;
    }

    public MeasureOptions setSecondaryChance(int secondaryChance) {
        this.secondaryChance = secondaryChance;
        return this;
    }

    public int getPrimaryChance() {
        return primaryChance;
    }

    public MeasureOptions setPrimaryChance(int primaryChance) {
        this.primaryChance = primaryChance;
        return this;
    }

    public int getChordChance() {
        return chordChance;
    }

    public MeasureOptions setChordChance(int chordChance) {
        this.chordChance = chordChance;
        return this;
    }

    public TimingOptions getTimingOptions() {
        return timingOptions;
    }

    public void setTimingOptions(TimingOptions timingOptions) {
        this.timingOptions = timingOptions;
    }

    public MeasureOptions setVolume(int volume) {
        this.volume = volume;
        return this;
    }

    public int getVolume() {
        return volume;
    }

    public Note[] getPrimaryNotes() {
        return primaryNotes;
    }

    public void setPrimaryNotes(Note[] primaryNotes) {
        this.primaryNotes = primaryNotes;
    }
}
