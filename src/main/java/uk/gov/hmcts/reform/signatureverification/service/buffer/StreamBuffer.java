package uk.gov.hmcts.reform.signatureverification.service.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StreamBuffer {
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
                System.out.println("Copied " + cnt + " bytes from input stream to output stream");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}
