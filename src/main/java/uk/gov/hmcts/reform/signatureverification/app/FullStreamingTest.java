package uk.gov.hmcts.reform.signatureverification.app;

import uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor;
import uk.gov.hmcts.reform.signatureverification.util.PublicKeyDecoder;

import java.security.PublicKey;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.toByteArray;

public class FullStreamingTest {
    public static void main(String[] args) throws Exception {
        PublicKey publicKey =
                PublicKeyDecoder.decode(
                        toByteArray(
                                getResource("public_key.der")
                        )
                );

        final StreamingProcessor streamingProcessor = new StreamingProcessor();

        streamingProcessor.verifyBlob(publicKey, "small.zip");
        streamingProcessor.verifyBlob(publicKey, "big.zip");
//        streamingProcessor.verifySignature(publicKey, "small");
//        streamingProcessor.parseZip("small");
//        streamingProcessor.verifySignature(publicKey, "big");
//        streamingProcessor.parseZip("big");
    }
}
