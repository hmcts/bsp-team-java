package uk.gov.hmcts.reform.signatureverification.service.buffer.streaming;

import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferOutputStream extends OutputStream {
    private final BlockingQueue<Integer> buffer;

    public BufferOutputStream(BlockingQueue<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void write(int b) {
        try {
            buffer.offer(b, 100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
