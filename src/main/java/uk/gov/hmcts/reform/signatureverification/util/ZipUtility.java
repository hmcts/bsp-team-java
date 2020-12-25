package uk.gov.hmcts.reform.signatureverification.util;

import uk.gov.hmcts.reform.signatureverification.exceptions.DocSignatureFailureException;
import uk.gov.hmcts.reform.signatureverification.exceptions.SignatureValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ZipUtility {

    public static final String ENVELOPE = "envelope.zip";
    public static final String SIGNATURE = "signature";

    private static final String INVALID_SIGNATURE_MESSAGE = "Zip signature failed verification";
    private static final int BUFFER_SIZE = 1024;

    private ZipUtility() {
        //
    }

    public static void verifySignatureStreaming(PublicKey publicKey, InputStream data, byte[] signed)
            throws IOException {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);

            while (data.available() > 0) {
                signature.update((byte) data.read());
            }

            if (!signature.verify(signed)) {
                throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE);
            } else {
                System.out.println("Signature verified");
            }
        } catch (SignatureException e) {
            throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE, e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureValidationException(e);
        }
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
