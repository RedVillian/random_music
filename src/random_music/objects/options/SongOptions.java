package random_music.objects.options;

import random_music.objects.Song;
import random_music.objects.enums.ProgressionEnum;
import random_music.objects.enums.ScaleEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 12:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class SongOptions {
    private int rootPitch = 60;
    private int measureMs = 992;
    private ScaleEnum scale = ScaleEnum.MAJOR;
    private ProgressionEnum progression = ProgressionEnum.MAJOR_FULL;
    private int numMeasures = 2;
    private int attributeVariance = 15;

    //attribute percentages
    private int complexity = 50;
    private int speed = 50;

    public int getNumMeasures() {
        return numMeasures;
    }

    public SongOptions setNumMeasures(int numMeasures) {
        this.numMeasures = numMeasures;
        return this;
    }

    public int getRootPitch() {
        return rootPitch;
    }

    public SongOptions setRootPitch(int rootPitch) {
        this.rootPitch = rootPitch;
        return this;
    }

    public int getMeasureMs() {
        return measureMs;
    }

    public SongOptions setMeasureMs(int measureMs) {
        //ensure that total measure time is always evenly divisible by the number of notes in the measure
        this.measureMs = measureMs - (measureMs % Song.NUM_NOTES_IN_MEASURE);
        return this;
    }

    public ScaleEnum getScale() {
        return scale;
    }

    public SongOptions setScale(ScaleEnum scale) {
        this.scale = scale;
        return this;
    }

    public ProgressionEnum getProgression() {
        return progression;
    }

    public SongOptions setProgression(ProgressionEnum progression) {
        this.progression = progression;
        return this;
    }

    public int getComplexity() {
        return complexity;
    }

    public SongOptions setComplexityPercent(int complexity) {
        this.complexity = complexity;
        return this;
    }

    public int getAttributeVariance() {
        return attributeVariance;
    }

    public SongOptions setAttributeVariance(int attributeVariance) {
        this.attributeVariance = attributeVariance;
        return this;
    }

    public SongOptions setComplexity(int complexity) {
        this.complexity = complexity;
        return this;
    }

    public int getSpeed() {
        return speed;
    }

    public SongOptions setSpeed(int speed) {
        this.speed = speed;
        return this;
    }

    public SongOptions shiftRootPitch(int octaveOffset) {
        setRootPitch(this.rootPitch + (octaveOffset * ScaleEnum.STEPS_IN_OCTAVE));
        return this;
    }
}
