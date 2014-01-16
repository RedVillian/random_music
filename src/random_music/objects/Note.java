package random_music.objects;

import random_music.objects.enums.ChordEnum;
import random_music.objects.enums.NoteLengthEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Note {

    int tone;
    NoteLengthEnum length;
    boolean isRest;
    ChordEnum chord;

    private int channelNum;
    private int channelMs;

    public Note(int tone, boolean isRest, NoteLengthEnum length) {
        this.tone = tone;
        this.isRest = isRest;
        this.length = length;

        chord = ChordEnum.NOTE;
    }

    public int getTone() {
        return tone;
    }

    public boolean isRest() {
        return isRest;
    }

    public NoteLengthEnum getLength() {
        return length;
    }

    public ChordEnum getChord() {
        return chord;
    }

    public Note setChord(ChordEnum chord) {
        this.chord = chord;
        return this;
    }

    public int getChannelNum() {
        return channelNum;
    }

    public Note setChannelNum(int channelNum) {
        this.channelNum = channelNum;
        return this;
    }

    public int getChannelMs() {
        return channelMs;
    }

    public Note setChannelMs(int measureMs) {
        this.channelMs = (int)(length.getRatio() * measureMs);
        return this;
    }

    public void decrementChannelMs(int msWaited) {
        setChannelMs(channelMs - msWaited);
    }


    public void setTone(int tone) {
        this.tone = tone;
    }
}