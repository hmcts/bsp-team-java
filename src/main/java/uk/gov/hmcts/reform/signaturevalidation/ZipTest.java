package uk.gov.hmcts.reform.signaturevalidation;

import com.google.common.io.ByteStreams;

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

        var fis = ZipTest.class.getClassLoader().getResourceAsStream("98902762223112414856442_08-10-2018-05-50-07/envelope.zip");
        var signature = ByteStreams.toByteArray(ZipTest.class.getClassLoader().getResourceAsStream("98902762223112414856442_08-10-2018-05-50-07/signature"));

        ZipVerifiers.verifyZip(fis, signature, publicKey);
    }
}
