package de.elbelife.metric.dynamic;

public class TimeWindowSupport {
    final long timeWindow;

    TimeWindowSupport(long timeWindow) {
        this.timeWindow = timeWindow;
    }

    long currentSlot() {
        return System.currentTimeMillis() / timeWindow;
    }

}
