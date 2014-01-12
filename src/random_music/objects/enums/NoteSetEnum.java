package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 11:28 AM
 * To change this template use File | Settings | File Templates.
 */
public enum NoteSetEnum {
    DOMINANT(0),
    DIMINISHED(1);

    public int[] notes;

    NoteSetEnum(int i){
        switch (i){
            case 0: //DOMINANT
                notes = new int[3];
                notes[0] = 1;
                notes[1] = 4;
                notes[2] = 5;
                break;
            case 1: //DIMINISHED
                notes = new int[4];
                notes[0] = 2;
                notes[1] = 3;
                notes[2] = 6;
                notes[3] = 7;
        }
    }
}
