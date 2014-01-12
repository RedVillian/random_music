package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public enum ScaleEnum {
    MAJOR(2,2,1,2,2,2,1,true),
    MINOR(2,1,2,2,1,2,2,false);

    public static final int STEPS_IN_OCTAVE = 12;
    public static final int NOTES_IN_SCALE = 7;

    private final int[] steps;
    private boolean isMajor;

    ScaleEnum(int ii, int iii, int iv, int v, int vi, int vii, int i, boolean isMajor){
        this.steps = new int[NOTES_IN_SCALE];
        steps[0] = ii;
        steps[1] = iii;
        steps[2] = iv;
        steps[3] = v;
        steps[4] = vi;
        steps[5] = vii;
        steps[6] = i;

        this.isMajor = isMajor;
    }

    public static int nearestNote(int noteNum, int lastNote, boolean isRising) {
        int lastNoteNum = lastNote % NOTES_IN_SCALE;
        int lastNoteOctave = lastNote / NOTES_IN_SCALE;

        int octaveShift = 0;

        if(isRising){
            if(noteNum <= lastNoteNum){
                octaveShift++;
            }
        } else{
            if(noteNum >= lastNoteNum){
                octaveShift--;
            }
        }

        int nextNote = ((lastNoteOctave + octaveShift) * NOTES_IN_SCALE) + noteNum;

        return nextNote;
    }

    public int get(int i){
        if(i > 0){
            return getPositive(i);
        } else if (i < 0 ){
            return getNegative(Math.abs(i));
        } else{
            return 0;
        }
    }

    int getPositive(int i){
        int octaveOffset = i/NOTES_IN_SCALE;
        int mod = i%NOTES_IN_SCALE;
        int noteOffset = 0;

        if(mod > 0){
            switch(mod){
                case 6: noteOffset += steps[5];
                case 5: noteOffset += steps[4];
                case 4: noteOffset += steps[3];
                case 3: noteOffset += steps[2];
                case 2: noteOffset += steps[1];
                case 1: noteOffset += steps[0];
            }
        }

        return (octaveOffset*STEPS_IN_OCTAVE) + noteOffset;
    }

    int getNegative(int i){
        int octaveOffset = i/NOTES_IN_SCALE;
        int mod = i%NOTES_IN_SCALE;
        int noteOffset = 0;

        if(mod > 0){
            switch(mod){
                case 6: noteOffset += steps[1];
                case 5: noteOffset += steps[2];
                case 4: noteOffset += steps[3];
                case 3: noteOffset += steps[4];
                case 2: noteOffset += steps[5];
                case 1: noteOffset += steps[6];
            }
        }

        return -(octaveOffset*STEPS_IN_OCTAVE) - noteOffset;
    }

    public int[] getSteps() {
        return steps;
    }

    public boolean isMajor() {
        return isMajor;
    }
}
