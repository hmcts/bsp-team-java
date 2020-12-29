package uk.gov.hmcts.reform.signatureverification.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(ZipProcessor.class);

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
                        logger.debug("Zip stream cnt: " + cnt);
                        c = 0;
                    }
                    if (b != -1 || data.available() > 0) {
                        signature.update(b);
                    } else {
                        logger.debug("End zip stream, cnt: " + cnt + ", b: " + b);
                    }
                } catch (ZipException ex) {
                    logger.error("data.available(): " + data.available());
                    logger.error("cnt: " + cnt);
                    logger.error(ex);
                    break;
                }
            }

            if (!signature.verify(signed)) {
                throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE);
            } else {
                logger.info("Signature verified");
            }
        } catch (SignatureException e) {
            throw new DocSignatureFailureException(INVALID_SIGNATURE_MESSAGE, e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SignatureValidationException(e);
        }
    }
}
