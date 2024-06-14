package fr.gabrielabgrall.gnpengine.utils;

/**
 * Clock est une classe utilitaire permettant la gestion du temps de sommeil dans les boucles d'affichage et de calculs physiques, pour maintenir un écart constant entre chaque itération des deux boucles principales.
 */
public class Clock {

    protected String name;
    protected long lastMean, clockCount;
    protected long clockStart, clockEnd;
    protected double deltaTime;

    public Clock(String name) {
        this.name = name;
    }

    public Clock() {
        this("Unnamed Clock");
    }

    public void tick(double frequency) {
        clockStart = System.currentTimeMillis();
        if (clockEnd==0) clockEnd = clockStart;

        long elapsed = clockStart - clockEnd;
        long sleepDuration = (long) (1000/frequency) - elapsed;
        clockEnd = clockStart + sleepDuration;

        try {
            if(sleepDuration > 0) Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        clockCount++;
        if(lastMean==0) lastMean = clockStart;
        if((clockStart - lastMean) > 1000) Debug.log(this.name + " | Clock rate : " + getMeanRate() + "/s");

        deltaTime = elapsed + System.currentTimeMillis() - clockStart;
    }

    public int getMeanRate() {
        long elapsed = System.currentTimeMillis() - lastMean;
        int mean = (int) (1000 * clockCount / elapsed);
        clockCount = 0;
        lastMean = 0;
        return mean;
    }

    public double getDeltaTime() {
        return deltaTime;
    }
}
