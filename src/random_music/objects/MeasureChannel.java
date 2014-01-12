package random_music.objects;

import random_music.objects.options.MeasureOptions;
import random_music.objects.options.SongOptions;

import javax.sound.midi.Instrument;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 8:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class MeasureChannel {
    Measure[] measures;
    ChannelRangeEnum range;
    Instrument instrument;

    SongOptions songUpdates;
    MeasureOptions measureUpdates;

    public MeasureChannel(ChannelRangeEnum range, SongOptions songUpdates, MeasureOptions measureUpdates) {
        this.range = range;
        this.songUpdates = songUpdates;
        this.measureUpdates = measureUpdates;

        songUpdates.shiftRootPitch(range.getOctaveOffset());
    }

    public void setMeasures(Measure[] measures) {
        this.measures = measures;
    }

    public MeasureOptions getMeasureUpdates() {
        return measureUpdates;
    }

    public SongOptions getSongUpdates() {
        return songUpdates;
    }

    public Measure getRandMeasure(Random r){
        return getMeasure(r.nextInt(measures.length));
    }

    public Measure getMeasure(int i) {
        return measures[i];
    }
}
