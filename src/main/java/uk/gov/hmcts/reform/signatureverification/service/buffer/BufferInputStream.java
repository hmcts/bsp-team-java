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
    private final long delay;
    private final long availabilityDelayStep;

    private int cnt = 0;
    private int c = 0;

    public BufferInputStream(
            BlockingQueue<Integer> buffer,
            long delay,
            long availabilityDelayStep
    ) {
        this.buffer = buffer;
        this.delay = delay;
        this.availabilityDelayStep = availabilityDelayStep;
    }

    @Override
    public int available() {
        long currentDelay = 0;
        while (buffer.size() == 0 && currentDelay < delay) {
            try {
                Thread.sleep(availabilityDelayStep);
                currentDelay += availabilityDelayStep;
            } catch (InterruptedException ex) {
                logger.error(ex);
            }
        }
        return buffer.size();
    }

    @Override
    public int read() {
        try {
            final Integer b = buffer.poll(delay, TimeUnit.MILLISECONDS);
            cnt++;
            c++;
            if (c == 1000000) {
                logger.debug("Input stream cnt: " + cnt);
                c = 0;
            }
            if (b == null) {
                logger.debug("End input stream, cnt: " + cnt);
                return -1;
            } else {
                return b;
            }
        } catch (InterruptedException ex) {
            throw new StreamReadingException(ex);
        }
    }
}
