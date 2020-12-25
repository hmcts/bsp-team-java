package uk.gov.hmcts.reform.signatureverification.service.buffer.streaming;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class StreamBuffer {
    private final BlockingQueue<Integer> buffer;

    public StreamBuffer(int capacity) {
        buffer = new ArrayBlockingQueue<>(capacity);
    }

    public InputStream getInputStream() {
        return new BufferInputStream(buffer);
    }

    public void copyFromInputStream(InputStream is) {
        new Thread(() -> {
            try {
                int cnt = 0;

                OutputStream os = new BufferOutputStream(buffer);
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
