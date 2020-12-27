package uk.gov.hmcts.reform.signatureverification.service;

import com.google.common.io.ByteStreams;
import uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer;
import uk.gov.hmcts.reform.signatureverification.util.BlobUtility;
import uk.gov.hmcts.reform.signatureverification.util.MemoryMonitor;
import uk.gov.hmcts.reform.signatureverification.util.ZipUtility;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.PublicKey;

public class StreamingProcessor {
    private static final long DELAY_MS = 1000L;
    private static final long AVAILABILITY_DELAY_STEP_MS = 10L;

    private final ZipProcessor zipProcessor = new ZipProcessor();

    public void verifyBlob(PublicKey publicKey, String blobName) throws Exception {
        System.out.println("--------------------");
        System.out.println("Verifying blob: " + blobName);
        System.out.println("--------------------");

        URL blobUrl = ClassLoader.getSystemResource(blobName);
        File blob = new File(blobUrl.toURI());

        StreamBuffer streamBuffer = new StreamBuffer(
                1000000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer streamBuffer1 = new StreamBuffer(
                1000000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer streamBuffer2 = new StreamBuffer(
                1000000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        var fis = new FileInputStream(blob);
        streamBuffer.copyToOutputStream(fis);
        var signature = BlobUtility.getSignature(streamBuffer.getInputStream());
        System.out.println("Signature size: " + signature.length);

        var fis1 = new FileInputStream(blob);
        streamBuffer1.copyToOutputStream(fis1);
        var envelope = BlobUtility.getEnvelope(streamBuffer1.getInputStream());
        streamBuffer2.copyToOutputStream(envelope);
        System.out.println("Envelope has been got");

        zipProcessor.verifySignature(publicKey, streamBuffer2.getInputStream(), signature);

        System.out.println("Heap used after verification: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        System.out.println("Verified blob " + blobName + " in " + ((t1 - t0) / 1_000_000) + " ms");

        System.out.println("--------------------");
    }

    public void verifySignature(PublicKey publicKey, String folder) throws Exception {
        System.out.println("--------------------");
        System.out.println("Verifying folder: " + folder);
        System.out.println("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        var fis = new FileInputStream(envelopeFile);

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Verifying folder '" + folder + "' with streaming");
        System.out.println("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyToOutputStream(fis);

        var signature = ByteStreams.toByteArray(StreamingProcessor.class.getClassLoader().getResourceAsStream(folder + "/signature"));
        zipProcessor.verifySignature(publicKey, streamBuffer.getInputStream(), signature);

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
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Parsing zip file from folder '" + folder + "' with streaming");
        System.out.println("Heap used before parsing: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyToOutputStream(fis);

        ZipUtility.getFileNames(streamBuffer.getInputStream());

        System.out.println("Heap used after parsing: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        System.out.println("Parsed zip file from folder '" + folder + "' with streaming in " + ((t1 - t0) / 1_000_000) + " ms");

        System.out.println("--------------------");
    }
}
