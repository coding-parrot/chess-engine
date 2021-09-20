public class Timer {
    private long currentTime = System.nanoTime();

    public long getTimeElapsed() {
        final long start = currentTime;
        currentTime = System.nanoTime();
        return currentTime - start;
    }
}
