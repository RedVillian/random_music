package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public enum NoteLengthEnum {
    WHOLE(1.0),
    DOT_HALF(.75),
    HALF(.5),
    DOT_QUARTER(.375),
    QUARTER(.25),
    DOT_EIGHTH(.1875),
    EIGHTH(.125),
    DOT_SIXTEENTH(.09375),
    SIXTEENTH(.0625),
    DOT_THIRTY_SECOND(.046875),
    THIRTY_SECOND(.03125);

    private final double ratio;

    NoteLengthEnum(double ratio) {
        this.ratio = ratio;
    }

    public double getRatio() {
        return ratio;
    }
}
