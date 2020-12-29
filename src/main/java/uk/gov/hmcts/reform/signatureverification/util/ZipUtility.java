package uk.gov.hmcts.reform.signatureverification.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtility {
    private static final Logger logger = LogManager.getLogger(ZipUtility.class);

    private ZipUtility() {
        //
    }

    public static List<String> getFileNames(InputStream is) throws IOException {
        List<String> zipEntries = new ArrayList<>();

        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            logger.info("Zip entry: " + zipEntry.getName());
            logger.info("Size: " + zipEntry.getSize());
            logger.info("Compressed size: " + zipEntry.getCompressedSize());
            zipEntries.add(zipEntry.getName());
        }

        return zipEntries;
    }
}
