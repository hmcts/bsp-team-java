package uk.gov.hmcts.reform.signatureverification.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer;
import uk.gov.hmcts.reform.signatureverification.util.BlobUtility;
import uk.gov.hmcts.reform.signatureverification.util.MemoryMonitor;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.PublicKey;

public class StreamingProcessor {
    private static final Logger logger = LogManager.getLogger(StreamingProcessor.class);

    private static final int STREAM_BUFFER_CAPACITY = 100000;
    private static final long DELAY_MS = 1000L;
    private static final long AVAILABILITY_DELAY_STEP_MS = 10L;

    private final ZipProcessor zipProcessor = new ZipProcessor();

    public void verifyBlob(PublicKey publicKey, String blobName) throws Exception {
        logger.info("--------------------");
        logger.info("Verifying blob: " + blobName);
        logger.info("--------------------");

        URL blobUrl = ClassLoader.getSystemResource(blobName);
        File blob = new File(blobUrl.toURI());

        StreamBuffer signatureReadingStreamBuffer = new StreamBuffer(
                STREAM_BUFFER_CAPACITY,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer envelopeReadingStreamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer envelopeVerificationStreamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        logger.info("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        var signatureReadingBlobInputStream = new FileInputStream(blob);
        signatureReadingStreamBuffer.copyToOutputStream(signatureReadingBlobInputStream);
        var signature = BlobUtility.getSignature(signatureReadingStreamBuffer.getInputStream());

        long t1 = System.nanoTime();
        logger.info("Signature has been read from " + blobName + " in " + ((t1 - t0) / 1_000_000) + " ms");
        logger.info("Signature size: " + signature.length);
        logger.info("Heap used after reading signature: " + memoryMonitor.getHeapUsed());

        var envelopeReadingBlobInputStream = new FileInputStream(blob);
        envelopeReadingStreamBuffer.copyToOutputStream(envelopeReadingBlobInputStream);
        var envelope = BlobUtility.getEnvelope(envelopeReadingStreamBuffer.getInputStream());
        envelopeVerificationStreamBuffer.copyToOutputStream(envelope);

        long t2 = System.nanoTime();
        logger.info("Envelope has been read from " + blobName + " in " + ((t2 - t1) / 1_000_000) + " ms");
        logger.info("Heap used after reading envelope: " + memoryMonitor.getHeapUsed());

        zipProcessor.verifySignature(publicKey, envelopeVerificationStreamBuffer.getInputStream(), signature);

        long t3 = System.nanoTime();
        logger.info("Signature of " + blobName + " has been verified in " + ((t3 - t2) / 1_000_000) + " ms");
        logger.info("Heap used after signature verification: " + memoryMonitor.getHeapUsed());

        logger.info("--------------------");
    }
}
