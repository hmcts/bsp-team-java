package uk.gov.hmcts.reform.signatureverification.util.zipverification;

import uk.gov.hmcts.reform.signatureverification.exceptions.DocSignatureFailureException;
import uk.gov.hmcts.reform.signatureverification.exceptions.SignatureValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class ZipVerifiers {

    public static final String ENVELOPE = "envelope.zip";
    public static final String SIGNATURE = "signature";

    private static final String INVALID_SIGNATURE_MESSAGE = "Zip signature failed verification";
    private static final int BUFFER_SIZE = 1024;

    private ZipVerifiers() {
    }

    public static void verifyZip(InputStream envelope, byte[] signature, PublicKey publicKey) throws Exception {

        verifySignatureMemoryUsageOptimized(
                publicKey,
                envelope,
                signature
        );
    }

    public static void verifySignature(PublicKey publicKey, byte[] data, byte[] signed) {
        System.out.println("Data size: " + data.length);

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            signature.update(data);
            if (!signature.verify(signed)) {
                throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE);
            }
        } catch (SignatureException e) {
            throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE, e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureValidationException(e);
        }
    }

    public static void verifySignatureMemoryUsageOptimized(PublicKey publicKey, InputStream data, byte[] signed)
            throws IOException {
        System.out.println("Data size: " + data.available());

        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);

            byte[] envelopeData = new byte[BUFFER_SIZE];
            while (data.available() != 0) {
                int numBytesRead = data.readNBytes(envelopeData, 0, envelopeData.length);
                signature.update(envelopeData, 0, numBytesRead);
            }

            if (!signature.verify(signed)) {
                throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE);
            }
        } catch (SignatureException e) {
            throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE, e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureValidationException(e);
        }
    }
}
