package fr.gabrielabgrall.javaengine.utils;

public class Clock {

    protected String name;
    protected long lastMean, clockCount;
    protected long clockStart, clockEnd;

    public Clock(String name) {
        this.name = name;
    }

    public Clock() {
        this("Unnamed Clock");
    }

    public void tick(double frequency) {
        clockStart = System.currentTimeMillis();

        long sleepDuration = (long) (1000/frequency) - (clockStart-clockEnd);

        try {
            Thread.sleep(sleepDuration);
        } catch (Exception ignored) {
        }

        clockCount++;
        if(lastMean==0) lastMean = clockStart;
        if((clockStart - lastMean) > 1000) Debug.log(this.name + " | Clock rate : " + getMeanRate() + "/s");

        clockEnd = System.currentTimeMillis();
    }

    public int getMeanRate() {
        long elapsed = System.currentTimeMillis() - lastMean;
        int mean = (int) (1000 * clockCount / elapsed);
        clockCount = 0;
        lastMean = 0;
        return mean;
    }
}
