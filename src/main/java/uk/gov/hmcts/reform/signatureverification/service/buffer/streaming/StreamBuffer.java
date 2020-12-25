package uk.gov.hmcts.reform.signatureverification.service.buffer.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StreamBuffer {
    private final BlockingQueue<Integer> buffer;
    private final long delay;
    private final long availabilityDelayStep;

    public StreamBuffer(int capacity, long delay, long availabilityDelayStep) {
        buffer = new ArrayBlockingQueue<>(capacity);
        this.delay = delay;
        this.availabilityDelayStep = availabilityDelayStep;
    }

    public InputStream getInputStream() {
        return new BufferInputStream(buffer, delay, availabilityDelayStep);
    }

    public void copyFromInputStream(InputStream is) {
        new Thread(() -> {
            try {
                int cnt = 0;

                OutputStream os = new BufferOutputStream(buffer, delay);
                while(is.available() > 0) {
                    os.write(is.read());
                    cnt++;
                }
//                cnt = IOUtils.copy(is, os);
                System.out.println("Copied " + cnt + " bytes from file to output stream");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
