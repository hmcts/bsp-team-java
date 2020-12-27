package uk.gov.hmcts.reform.signatureverification.util;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class BlobUtility {

    private BlobUtility() {
        //
    }

    public static byte[] getSignature(InputStream is) throws IOException {
        System.out.println("Getting signature");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            System.out.println("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals("signature")) {
                return ByteStreams.toByteArray(zis);
            }
        }

        throw new RuntimeException();
    }

    public static InputStream getEnvelope(InputStream is) throws IOException {
        System.out.println("Getting envelope.zip");
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry zipEntry;
        while ((zipEntry = zis.getNextEntry()) != null) {
            System.out.println("Zip entry: " + zipEntry.getName());
            if (zipEntry.getName().equals("envelope.zip")) {
                return zis;
            }
        }

        throw new RuntimeException();
    }
}
