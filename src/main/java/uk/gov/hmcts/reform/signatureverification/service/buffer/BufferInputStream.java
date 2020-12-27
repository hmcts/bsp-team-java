package uk.gov.hmcts.reform.signatureverification.service.buffer;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferInputStream extends InputStream {
    private final BlockingQueue<Integer> buffer;
    private final long delay;
    private final long availabilityDelayStep;
    int cnt = 0;
    int c = 0;

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
            } catch (InterruptedException e) {
                e.printStackTrace();
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
                System.out.println("Input stream cnt: " + cnt);
                c = 0;
            }
            if (b == null) {
                System.out.println("End input stream, cnt: " + cnt);
                return -1;
            } else {
                return b;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
