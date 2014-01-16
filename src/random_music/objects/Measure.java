package random_music.objects;

import random_music.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 12:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Measure {
    Note[] notes;

    public Measure(Note[] notes) {
        this.notes = notes;
    }

    public Note[] getNotes() {
        return notes;
    }

    public void transposeNotes(int octaveOffset) {
        for(Note n : notes){
            n.setTone(n.getTone() + (octaveOffset * Constants.STEPS_IN_OCTAVE));
        }
    }
}