package random_music.objects.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 5:21 PM
 * To change this template use File | Settings | File Templates.
 */
public enum ProgressionEnum {
    MAJOR_FULL(MajMinSymbolEnum.I, MajMinSymbolEnum.IV, MajMinSymbolEnum.vii, MajMinSymbolEnum.iii, MajMinSymbolEnum.vi, MajMinSymbolEnum.ii, MajMinSymbolEnum.V),
    MAJOR_FOUR(MajMinSymbolEnum.I, MajMinSymbolEnum.V, MajMinSymbolEnum.vi, MajMinSymbolEnum.IV);

    private final MajMinSymbolEnum[] progression;

    ProgressionEnum(MajMinSymbolEnum...progression){
        this.progression = progression;
    }

    public MajMinSymbolEnum[] getProgression() {
        return progression;
    }

    public MajMinSymbolEnum getRand(Random r, boolean wantMajor){
        //TODO: Variance here?

        List<MajMinSymbolEnum> elligibleSymbols = new ArrayList<MajMinSymbolEnum>();
        for(MajMinSymbolEnum s : progression){
            if(s.isMajor() == wantMajor){
                elligibleSymbols.add(s);
            }
        }

        //TODO: Weight returning the root note?
        return elligibleSymbols.get(r.nextInt(elligibleSymbols.size()));
    }
}
