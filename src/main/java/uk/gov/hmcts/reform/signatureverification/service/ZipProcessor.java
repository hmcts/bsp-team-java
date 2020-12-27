package uk.gov.hmcts.reform.signatureverification.service;

import uk.gov.hmcts.reform.signatureverification.exceptions.DocSignatureFailureException;
import uk.gov.hmcts.reform.signatureverification.exceptions.SignatureValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.zip.ZipException;

public class ZipProcessor {
    private static final String INVALID_SIGNATURE_MESSAGE = "Zip signature failed verification";
    private static final String SHA_256_WITH_RSA_ALGORYTHM = "SHA256withRSA";

    public void verifySignature(
            PublicKey publicKey,
            InputStream data,
            byte[] signed
    ) throws IOException {
        try {
            Signature signature = Signature.getInstance(SHA_256_WITH_RSA_ALGORYTHM);
            signature.initVerify(publicKey);

            int cnt = 0;
            int c = 0;
            while (data.available() > 0) {
                try {
                    byte b = (byte) data.read();
                    cnt++;
                    c++;
                    if (c == 1000000) {
                        System.out.println("Zip stream cnt: " + cnt);
                        c = 0;
                    }
                    if (b != -1 || data.available() > 0) {
                        signature.update(b);
                    } else {
                        System.out.println("End zip stream, cnt: " + cnt + ", b: " + b);
                    }
                } catch (ZipException ze) {
                    System.out.println("data.available(): " + data.available());
                    System.out.println("cnt: " + cnt);
                    ze.printStackTrace();
                    break;
                }
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
}
