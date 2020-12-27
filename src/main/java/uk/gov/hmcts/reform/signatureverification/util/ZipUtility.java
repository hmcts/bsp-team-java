package uk.gov.hmcts.reform.signatureverification.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtility {

    private ZipUtility() {
        //
    }

    public static List<String> getFileNames(InputStream is) throws IOException {
        List<String> zipEntries = new ArrayList<>();

        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            System.out.println("Zip entry: " + zipEntry.getName());
            System.out.println("Size: " + zipEntry.getSize());
            System.out.println("Compressed size: " + zipEntry.getCompressedSize());
            zipEntries.add(zipEntry.getName());
        }

        return zipEntries;
    }
}
