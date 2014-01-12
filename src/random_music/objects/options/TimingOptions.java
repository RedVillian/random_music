package random_music.objects.options;

import random_music.objects.enums.TimeSignatureEnum;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimingOptions {
    TimeSignatureEnum timeSignature = TimeSignatureEnum.FOUR_FOUR;

    public TimeSignatureEnum getTimeSignature() {
        return timeSignature;
    }

    public TimingOptions setTimeSignature(TimeSignatureEnum timeSignature) {
        this.timeSignature = timeSignature;
        return this;
    }
}
