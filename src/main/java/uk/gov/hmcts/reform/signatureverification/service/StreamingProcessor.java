package uk.gov.hmcts.reform.signatureverification.service;

import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer;
import uk.gov.hmcts.reform.signatureverification.util.BlobUtility;
import uk.gov.hmcts.reform.signatureverification.util.MemoryMonitor;
import uk.gov.hmcts.reform.signatureverification.util.ZipUtility;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.security.PublicKey;

public class StreamingProcessor {
    private static final Logger logger = LogManager.getLogger(StreamingProcessor.class);

    private static final long DELAY_MS = 1000L;
    private static final long AVAILABILITY_DELAY_STEP_MS = 10L;

    private final ZipProcessor zipProcessor = new ZipProcessor();

    public void verifyBlob(PublicKey publicKey, String blobName) throws Exception {
        logger.info("--------------------");
        logger.info("Verifying blob: " + blobName);
        logger.info("--------------------");

        URL blobUrl = ClassLoader.getSystemResource(blobName);
        File blob = new File(blobUrl.toURI());

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer streamBuffer1 = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );
        StreamBuffer streamBuffer2 = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        logger.info("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        var fis = new FileInputStream(blob);
        streamBuffer.copyToOutputStream(fis);
        var signature = BlobUtility.getSignature(streamBuffer.getInputStream());
        logger.info("Signature size: " + signature.length);

        var fis1 = new FileInputStream(blob);
        streamBuffer1.copyToOutputStream(fis1);
        var envelope = BlobUtility.getEnvelope(streamBuffer1.getInputStream());
        streamBuffer2.copyToOutputStream(envelope);
        logger.info("Envelope has been got");

        zipProcessor.verifySignature(publicKey, streamBuffer2.getInputStream(), signature);

        logger.info("Heap used after verification: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        logger.info("Verified blob " + blobName + " in " + ((t1 - t0) / 1_000_000) + " ms");

        logger.info("--------------------");
    }

    public void verifySignature(PublicKey publicKey, String folder) throws Exception {
        logger.info("--------------------");
        logger.info("Verifying folder: " + folder);
        logger.info("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        var fis = new FileInputStream(envelopeFile);

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        logger.info("Verifying folder '" + folder + "' with streaming");
        logger.info("Heap used before verification: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyToOutputStream(fis);

        var signature = ByteStreams.toByteArray(StreamingProcessor.class.getClassLoader().getResourceAsStream(folder + "/signature"));
        zipProcessor.verifySignature(publicKey, streamBuffer.getInputStream(), signature);

        logger.info("Heap used after verification: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        logger.info("Verified folder '" + folder + "' with streaming in " + ((t1 - t0) / 1_000_000) + " ms");

        logger.info("--------------------");
    }

    public void parseZip(String folder) throws Exception {
        logger.info("--------------------");
        logger.info("Parsing zip from folder: " + folder);
        logger.info("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        var fis = new FileInputStream(envelopeFile);

        StreamBuffer streamBuffer = new StreamBuffer(
                100000,
                DELAY_MS,
                AVAILABILITY_DELAY_STEP_MS
        );

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        logger.info("Parsing zip file from folder '" + folder + "' with streaming");
        logger.info("Heap used before parsing: " + memoryMonitor.getHeapUsed());

        long t0 = System.nanoTime();

        streamBuffer.copyToOutputStream(fis);

        ZipUtility.getFileNames(streamBuffer.getInputStream());

        logger.info("Heap used after parsing: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        logger.info("Parsed zip file from folder '" + folder + "' with streaming in " + ((t1 - t0) / 1_000_000) + " ms");

        logger.info("--------------------");
    }
}
