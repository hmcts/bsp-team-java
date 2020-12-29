package uk.gov.hmcts.reform.signatureverification.util;

import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class BlobUtility {
    private static final Logger logger = LogManager.getLogger(BlobUtility.class);

    private BlobUtility() {
        //
    }

    public static byte[] getSignature(InputStream is) throws IOException {
        logger.info("Getting signature");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            logger.info("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals("signature")) {
                return ByteStreams.toByteArray(zis);
            }
        }

        throw new RuntimeException();
    }

    public static InputStream getEnvelope(InputStream is) throws IOException {
        logger.info("Getting envelope.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            logger.info("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals("envelope.zip")) {
                return zis;
            }
        }

        throw new RuntimeException();
    }
}
