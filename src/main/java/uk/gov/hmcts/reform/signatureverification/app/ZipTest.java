package uk.gov.hmcts.reform.signatureverification.app;

import uk.gov.hmcts.reform.signatureverification.service.SignatureVerifier;
import uk.gov.hmcts.reform.signatureverification.util.PublicKeyDecoder;

import java.security.PublicKey;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.toByteArray;

public class ZipTest {
    public static void main(String[] args) throws Exception {
        PublicKey publicKey =
                PublicKeyDecoder.decode(
                        toByteArray(
                                getResource("public_key.der")
                        )
                );

        new SignatureVerifier().verifySignature(publicKey, "small");
        new SignatureVerifier().verifySignature(publicKey, "big");
    }
}
