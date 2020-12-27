package uk.gov.hmcts.reform.signatureverification.service.buffer;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferOutputStream extends OutputStream {
    private final BlockingQueue<Integer> buffer;
    private final long delay;

    public BufferOutputStream(BlockingQueue<Integer> buffer, long delay) {
        this.buffer = buffer;
        this.delay = delay;
    }

    @Override
    public void write(int b) {
        try {
            buffer.offer(b, delay, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
