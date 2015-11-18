package util;

/**
 * Simple stopwatch class to measure performance
 * @author Kairi Kozuma
 * @version 1.0
 */
public class Stopwatch {
    private long timeStart;
    private long timeEnd;

    private Stopwatch(long timeStart) {
        this.timeStart = timeStart;
    }

    /**
     * Stop the stopwatch
     */
    public void stop() {
        timeEnd = System.nanoTime();
    }

    /**
     * Get the elapsed time in nanoseconds
     * @return long time elapsed in nanoseconds
     */
    public long elapsed() {
        return timeEnd - timeStart;
    }

    /**
     * Get the elapsed time in seconds
     * @return double time in seconds
     */
    public double elapsedSeconds() {
        return (timeEnd - timeStart) / (double) 1000000000;
    }

    /**
     * Create new stopwatch that is started by default
     * @return [description]
     */
    public static Stopwatch createStarted() {
        return new Stopwatch(System.nanoTime());
    }
}
