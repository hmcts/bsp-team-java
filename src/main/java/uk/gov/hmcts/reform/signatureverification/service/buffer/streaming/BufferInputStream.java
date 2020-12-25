package uk.gov.hmcts.reform.signatureverification.service.buffer.streaming;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BufferInputStream extends InputStream {
    private final BlockingQueue<Integer> buffer;
    int cnt = 0;
    int c = 0;

    public BufferInputStream(BlockingQueue<Integer> buffer) {
        this.buffer = buffer;
    }

    @Override
    public int available() {
        return buffer.size();
    }

    @Override
    public int read() {
        try {
            final Integer b = buffer.poll(1000, TimeUnit.MILLISECONDS);
            if (b == null) {
                return -1;
            }
//            if (b == -1) {
//                return Byte.MAX_VALUE + 1;
//            }
            cnt++;
            c++;
            if (c == 100000) {
                c = 0;
                System.out.println("cnt: " + cnt);
            }
            return b;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
}
