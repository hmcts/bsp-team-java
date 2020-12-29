package uk.gov.hmcts.reform.signatureverification.service.buffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StreamBuffer {
    private static final Logger logger = LogManager.getLogger(StreamBuffer.class);

    private final BlockingQueue<Integer> buffer;
    private final long delayMs;
    private final long availabilityDelayStepMs;

    public StreamBuffer(int capacity, long delayMs, long availabilityDelayStepMs) {
        buffer = new ArrayBlockingQueue<>(capacity);
        this.delayMs = delayMs;
        this.availabilityDelayStepMs = availabilityDelayStepMs;
    }

    public InputStream getInputStream() {
        return new BufferInputStream(buffer, delayMs, availabilityDelayStepMs);
    }

    public void copyToOutputStream(InputStream is) {
        new Thread(() -> {
            int cnt = 0;
            try {

                OutputStream os = new BufferOutputStream(buffer, delayMs);
                while (is.available() > 0) {
                    os.write(is.read());
                    cnt++;
                }
                logger.info("Copied " + cnt + " bytes from input stream to output stream");
            } catch (IOException ex) {
                logger.error(ex);
            }
        }).start();
    }
}
