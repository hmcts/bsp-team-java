package uk.gov.hmcts.reform.signaturevalidation;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

public class ZipVerifiers {

    public static final String ENVELOPE = "envelope.zip";
    public static final String SIGNATURE = "signature";

    public static final String INVALID_SIGNATURE_MESSAGE = "Zip signature failed verification";

    private ZipVerifiers() {
    }

    public static void verifyZip(InputStream envelope, byte[] signature, PublicKey publicKey) throws Exception {

        verifySignature(
                publicKey,
                envelope,
                signature
        );
    }

    public static void verifySignature(PublicKey publicKey, InputStream data, byte[] signed) throws Exception {
        System.out.println("Data: " + data.available());
        System.out.println("Signed: " + signed.length);
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);
            while (data.available() != 0) {
                var b = (byte) data.read();
                signature.update(b);
            }
            if (!signature.verify(signed)) {
                throw new RuntimeException(INVALID_SIGNATURE_MESSAGE);
            }
        } catch (SignatureException e) {
            throw new RuntimeException(INVALID_SIGNATURE_MESSAGE, e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
