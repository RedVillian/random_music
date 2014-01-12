package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MajMinSymbolEnum {
    I(true, 1),II(true, 2),III(true, 3),IV(true, 4),V(true, 5),VI(true, 6),VII(true, 7),
    i(false, 1),ii(false, 2),iii(false, 3),iv(false, 4),v(false, 5),vi(false, 6),vii(false, 7);

    private boolean isMajor;
    private final int num;

    MajMinSymbolEnum(boolean isMajor, int num){
        this.isMajor = isMajor;
        this.num = num;
    }

    public boolean isMajor(){
        return isMajor;
    }

    public int getNum(){
        return num;
    }
}
