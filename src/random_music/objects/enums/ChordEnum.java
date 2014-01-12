package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ChordEnum {
    NOTE(0),
    MAJOR_TRIAD(0,4,7),
    MINOR_TRIAD(0,3,7);

    int[] notes;

    ChordEnum(int...notes){
        this.notes = notes;
    }

    public int[] getNotes() {
        return notes;
    }

    public int get(int i) {
        return notes[i];
    }
}
