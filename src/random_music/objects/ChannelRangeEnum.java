package random_music.objects;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 8:33 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ChannelRangeEnum {
    LOWEST(-3),
    LOWER(-2),
    LOW(-1),
    MID(0),
    HIGH(1),
    HIGHER(2),
    HIGHEST(3);

    private final int octaveOffset;

    ChannelRangeEnum(int octaveOffset){
        this.octaveOffset = octaveOffset;
    }

    public int getOctaveOffset(){
        return octaveOffset;
    }
}
