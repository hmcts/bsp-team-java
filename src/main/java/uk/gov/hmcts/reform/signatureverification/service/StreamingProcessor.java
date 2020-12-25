package uk.gov.hmcts.reform.signatureverification.service;

import com.google.common.io.ByteStreams;
import uk.gov.hmcts.reform.signatureverification.service.buffer.streaming.StreamBuffer;
import uk.gov.hmcts.reform.signatureverification.util.MemoryMonitor;
import uk.gov.hmcts.reform.signatureverification.util.ZipUtility;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.PublicKey;

public class StreamingProcessor {
    private static final long DELAY = 1000L;
    private static final long AVAILABILITY_DELAY_STEP = 10L;

    public void verifySignature(PublicKey publicKey, String folder) throws Exception {
        System.out.println("--------------------");
        System.out.println("Verifying folder: " + folder);
        System.out.println("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        var fis = new FileInputStream(envelopeFile);

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY,
                AVAILABILITY_DELAY_STEP
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Verifying folder '" + folder + "' with streaming");
        System.out.println("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyFromInputStream(fis);

        var signature = ByteStreams.toByteArray(StreamingProcessor.class.getClassLoader().getResourceAsStream(folder + "/signature"));
        ZipUtility.verifySignatureStreaming(publicKey, streamBuffer.getInputStream(), signature);

        System.out.println("Heap used after verification: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        System.out.println("Verified folder '" + folder + "' with streaming in " + ((t1 - t0) / 1_000_000) + " ms");

        System.out.println("--------------------");
    }

    public void parseZip(String folder) throws Exception {
        System.out.println("--------------------");
        System.out.println("Parsing zip from folder: " + folder);
        System.out.println("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        var fis = new FileInputStream(envelopeFile);

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY,
                AVAILABILITY_DELAY_STEP
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Parsing zip file from folder '" + folder + "' with streaming");
        System.out.println("Heap used before parsing: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyFromInputStream(fis);

        ZipUtility.getFileNames(streamBuffer.getInputStream());

        System.out.println("Heap used after parsing: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        System.out.println("Parsed zip file from folder '" + folder + "' with streaming in " + ((t1 - t0) / 1_000_000) + " ms");

        System.out.println("--------------------");
    }
}
