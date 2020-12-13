package uk.gov.hmcts.reform.signatureverification.service;

import com.google.common.io.ByteStreams;
import uk.gov.hmcts.reform.signatureverification.app.ZipTest;
import uk.gov.hmcts.reform.signatureverification.util.MemoryMonitor;
import uk.gov.hmcts.reform.signatureverification.util.zipverification.ZipVerifiers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.security.PublicKey;

public class SignatureVerifier {
    public void verifySignature(PublicKey publicKey, String folder) throws Exception {
        System.out.println("--------------------");
        System.out.println("Verifying folder: " + folder);
        System.out.println("--------------------");

        URL envelopeUrl = ClassLoader.getSystemResource(folder + "/envelope.zip");
        File envelopeFile = new File(envelopeUrl.toURI());

        MemoryMonitor memoryMonitor = new MemoryMonitor();

        System.out.println("Verifying folder '" + folder + "' with memory usage optimization");
        System.out.println("Heap used before verification: " + memoryMonitor.getHeapUsed());

        var signature = ByteStreams.toByteArray(ZipTest.class.getClassLoader().getResourceAsStream(folder + "/signature"));
        var fis = ZipTest.class.getClassLoader().getResourceAsStream(folder + "/envelope.zip");

        long t0 = System.nanoTime();

        ZipVerifiers.verifySignatureMemoryUsageOptimized(publicKey, fis, signature);

        System.out.println("Heap used after verification: " + memoryMonitor.getHeapUsed());

        long t1 = System.nanoTime();
        System.out.println("Verified folder '" + folder + "' with memory usage optimization in " + ((t1 - t0) / 1_000_000) + " ms");

        System.out.println("====================");

        System.out.println("Verifying folder '" + folder + "' without memory usage optimization");
        System.out.println("Heap used before verification: " + memoryMonitor.getHeapUsed());

        byte[] data = Files.readAllBytes(envelopeFile.toPath());

        long t2 = System.nanoTime();

        ZipVerifiers.verifySignature(publicKey, data, signature);

        System.out.println("Heap used after verification: " + memoryMonitor.getHeapUsed());
        long t3 = System.nanoTime();
        System.out.println("Verified folder '" + folder + "' without memory usage optimization in " + ((t3 - t2) / 1_000_000) + " ms");

        System.out.println("--------------------");
    }
}
