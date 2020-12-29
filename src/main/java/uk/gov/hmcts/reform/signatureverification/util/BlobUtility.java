package uk.gov.hmcts.reform.signatureverification.util;

import com.google.common.io.ByteStreams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.gov.hmcts.reform.signatureverification.exceptions.InvalidZipArchiveException;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class BlobUtility {
    private static final Logger logger = LogManager.getLogger(BlobUtility.class);

    private static final String SIGNATURE = "signature";
    private static final String ENVELOPE_ZIP = "envelope.zip";

    private BlobUtility() {
        //
    }

    public static byte[] getSignature(InputStream is) throws IOException {
        logger.info("Getting signature");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            logger.info("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals(SIGNATURE)) {
                return ByteStreams.toByteArray(zis);
            }
        }

        throw new InvalidZipArchiveException("signature not found in blob");
    }

    public static InputStream getEnvelope(InputStream is) throws IOException {
        logger.info("Getting envelope.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            logger.info("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals(ENVELOPE_ZIP)) {
                return zis;
            }
        }

        throw new InvalidZipArchiveException("envelope.zip not found in blob");
    }
}
