package random_music.objects.enums;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public enum MajMinSymbolEnum {
    I(true),II(true),III(true),IV(true),V(true),VI(true),VII(true),
    i(false),ii(false),iii(false),iv(false),v(false),vi(false),vii(false);


    private boolean isMajor;

    MajMinSymbolEnum(boolean isMajor){
        this.isMajor = isMajor;
    }
}
