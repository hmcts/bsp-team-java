package uk.gov.hmcts.reform.signatureverification.service.buffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.hmcts.reform.signatureverification.exceptions.StreamReadingException;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferInputStream extends InputStream {
    private static final Logger logger = LogManager.getLogger(BufferInputStream.class);

    private final BlockingQueue<Integer> buffer;
    private final long delayMs;
    private final long availabilityDelayStepMs;

    private int cnt = 0;
    private int c = 0;

    public BufferInputStream(
            BlockingQueue<Integer> buffer,
            long delayMs,
            long availabilityDelayStepMs
    ) {
        this.buffer = buffer;
        this.delayMs = delayMs;
        this.availabilityDelayStepMs = availabilityDelayStepMs;
    }

    @Override
    public int available() {
        long currentDelayMs = 0;
        while (buffer.size() == 0 && currentDelayMs < delayMs) {
            try {
                Thread.sleep(availabilityDelayStepMs);
                currentDelayMs += availabilityDelayStepMs;
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
        return buffer.size();
    }

    @Override
    public int read() {
        try {
            final Integer b = buffer.poll(delayMs, TimeUnit.MILLISECONDS);
            cnt++;
            c++;
            if (c == 1000000) {
                logger.debug("Input stream reading, cnt: " + cnt);
                c = 0;
            }
            if (b == null) {
                logger.debug("End input stream reading, cnt: " + cnt);
                return -1;
            } else {
                return b;
            }
        } catch (InterruptedException ex) {
            throw new StreamReadingException(ex);
        }
    }
}
