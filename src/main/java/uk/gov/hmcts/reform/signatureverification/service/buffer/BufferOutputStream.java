package uk.gov.hmcts.reform.signatureverification.service.buffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferOutputStream extends OutputStream {
    private static final Logger logger = LogManager.getLogger(BufferOutputStream.class);

    private final BlockingQueue<Integer> buffer;
    private final long delayMs;

    public BufferOutputStream(BlockingQueue<Integer> buffer, long delayMs) {
        this.buffer = buffer;
        this.delayMs = delayMs;
    }

    @Override
    public void write(int b) {
        try {
            buffer.offer(b, delayMs, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ex) {
            logger.error(ex);
        }
    }
}
