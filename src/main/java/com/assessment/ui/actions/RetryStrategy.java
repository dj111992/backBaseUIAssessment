package com.assessment.ui.actions;

import java.util.logging.Logger;

public class RetryStrategy {

    private static Logger logger = Logger.getLogger("Retry Strategy for APIs and UIs");

    public static final int DEFAULT_RETRIES = 3;
    public static final long DEFAULT_WAIT_TIME_IN_MILLI = 4000;

    private int numberOfRetries;
    private int numberOfTriesLeft;
    private long timeToWait;

    public RetryStrategy() {
        this(DEFAULT_RETRIES, DEFAULT_WAIT_TIME_IN_MILLI);
    }

    public RetryStrategy(int numberOfRetries,
                         long timeToWait) {
        this.numberOfRetries = numberOfRetries;
        numberOfTriesLeft = numberOfRetries;
        this.timeToWait = timeToWait;
    }

    /**
     * @return true if there are tries left
     */
    public boolean shouldRetry() {
        return numberOfTriesLeft > 0;
    }

    public void errorOccured() throws Exception {
        if (!shouldRetry()) {
            throw new Exception("Retry Failed: Total " + numberOfRetries
                    + " attempts made at interval " + getTimeToWait()
                    + "ms");
        }
        numberOfTriesLeft--;
        logger.info("Number of retry counts left is: " + numberOfTriesLeft);
        waitUntilNextTry();
    }

    public void noErrorOccurred() {
        numberOfTriesLeft = 0;
        numberOfRetries = 0;
        logger.info("Since no error occurred, making number of retry counts left as: " + numberOfTriesLeft);
    }

    public long getTimeToWait() {
        return timeToWait;
    }

    private void waitUntilNextTry() {
        try {
            Thread.sleep(getTimeToWait());
        } catch (InterruptedException ignored) {
            ignored.printStackTrace();
        }
    }
}
