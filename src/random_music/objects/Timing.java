package random_music.objects;

import random_music.MusicPlayer;
import random_music.objects.enums.NoteLengthEnum;
import random_music.objects.enums.TimeSignatureEnum;
import random_music.objects.options.TimingOptions;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: jelms
 * Date: 1/11/14
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Timing {
    private static final int REGULAR_TIMES_PERCENT = 10;
    private static final boolean ACCELERATION = true;
    private static final boolean DECELERATION = false;
    private static final int SPEED_CHANGE_MOD = 1;
    NoteLengthEnum[] times;

    public Timing(TimingOptions options, int maxTime, int speedPercent, Random r) {
        times = generateTimes(options.getTimeSignature(), maxTime, speedPercent, r);
    }

    private NoteLengthEnum[] generateTimes(TimeSignatureEnum timeSignature, int maxTime, int speedPercent, Random r) {
        //modify the percent "roll" assuming an average percentage of 50
        int percent = MusicPlayer.sanitizePercent(r.nextInt(Song.PERCENT) * (speedPercent/(Song.PERCENT/2)));

        if(percent < (REGULAR_TIMES_PERCENT)){
            return generateRegularTimes(timeSignature);
        }

        return generateIrregularTimes(timeSignature, maxTime, percent, r);
    }

    private NoteLengthEnum[] generateIrregularTimes(TimeSignatureEnum timeSignature, int maxTime, int speedPercent, Random r) {
        int beats = timeSignature.getBeats();
        NoteLengthEnum lastBeat = timeSignature.getOneBeat();
        NoteLengthEnum currBeat = null;
        double remaining = 1.0;
        ArrayList<NoteLengthEnum> times = new ArrayList<NoteLengthEnum>();

        int fasterChance = generateSpeedChangeChance(speedPercent, ACCELERATION);
        int slowerChance = generateSpeedChangeChance(speedPercent, DECELERATION);

        System.out.println("Speed chances: +" + fasterChance + "   -" + slowerChance);

        try{
            while (remaining > 0.0){
                int roll;
                if((roll = r.nextInt(Song.PERCENT)) < fasterChance){
                    //TODO: Remove debug text
                    System.out.println("Attempt to accelerate with a roll of " + roll + " under " + fasterChance);
                    //attempt to speed up by a non-dotted type (ergo the 2)
                    currBeat = shiftNoteLength(lastBeat, remaining, 2);
                    if(currBeat != lastBeat){
                        System.out.println("      ...success");
                    }
                } else if((roll = r.nextInt(Song.PERCENT)) < slowerChance) {
                    System.out.println("Attempt to decelerate with a roll of " + roll + " under " + slowerChance);
                    //attempt to slow down by a non-dotted type (ergo the 2)
                    currBeat = shiftNoteLength(lastBeat, remaining, -2);
                    if(currBeat != lastBeat){
                        System.out.println("      ...success");
                    }
                } else {
                    currBeat = lastBeat;
                }
                times.add(currBeat);
                remaining -= currBeat.getRatio();
                lastBeat = currBeat;
            }
        } catch (NullPointerException e){
            System.err.println("currBeat was null with " + remaining + " remaining and the last beat as " + lastBeat);
            return generateRegularTimes(timeSignature);
        }

        NoteLengthEnum[] timesArray = new NoteLengthEnum[times.size()];
        times.toArray(timesArray);
        return timesArray;
    }

    private NoteLengthEnum shiftNoteLength(NoteLengthEnum lastBeat, double remaining, int change) {
        NoteLengthEnum beat = lastBeat;
        NoteLengthEnum [] allNoteLengths = NoteLengthEnum.values();

        while(change > 0) {
            //if there is a noteLength you could increment to...
            if(beat.ordinal() + 1 < allNoteLengths.length){
                NoteLengthEnum potentialBeat = allNoteLengths[beat.ordinal() + 1];
                //...and this note will not block the rest of the measure
                if(canFinishMeasure(remaining, potentialBeat.getRatio())){
                    beat = potentialBeat;
                }
            }
            change--;
        }
        while(change < 0) {
            //if there is a noteLength you could increment to...
            if(beat.ordinal() - 1 >= 0){
                NoteLengthEnum potentialBeat = allNoteLengths[beat.ordinal() - 1];
                //...and this note will not block the rest of the measure
                if(canFinishMeasure(remaining, potentialBeat.getRatio())){
                    beat = potentialBeat;
                }
            }
            change++;
        }

        return beat;
    }

    private boolean canFinishMeasure(double remaining, double potentialRatio){
        //...and there is enough time left in the measure...
        return potentialRatio < remaining &&
                //...and repetitions of this beat could evenly finish the measure
                //TODO: This is a hacky failsafe for ensuring clean measure completion
                remaining % potentialRatio == 0;
    }

    private int generateSpeedChangeChance(int speedPercent, boolean isForAcceleration) {
        int chance = 0;
        if(isForAcceleration){
            chance = speedPercent - 50;
        } else {
            chance = 50 - speedPercent;
        }
        return MusicPlayer.sanitizePercent(chance * SPEED_CHANGE_MOD);
    }

    private NoteLengthEnum[] generateRegularTimes(TimeSignatureEnum timeSignature) {
        NoteLengthEnum[] times = new NoteLengthEnum[timeSignature.getBeats()];
        for(int i = 0; i < timeSignature.getBeats(); i++){
            times[i] = timeSignature.getOneBeat();
        }
        return times;
    }

    public int getNumTimes(){
        return times.length;
    }

    public NoteLengthEnum get(int i) {
        return times[i];
    }
}
