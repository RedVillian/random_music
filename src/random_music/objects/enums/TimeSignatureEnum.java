package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
public enum TimeSignatureEnum {
    FOUR_FOUR(4, NoteLengthEnum.QUARTER),
    EIGHT_EIGHT(8, NoteLengthEnum.EIGHTH);

    int beats;
    NoteLengthEnum oneBeat;

    TimeSignatureEnum(int beats, NoteLengthEnum oneBeat){
        this.beats = beats;
        this.oneBeat = oneBeat;
    }

    public int getBeats() {
        return beats;
    }

    public NoteLengthEnum getOneBeat() {
        return oneBeat;
    }
}
