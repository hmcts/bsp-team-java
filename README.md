Run FullStreamingTest class to get memory usage and duration of signature verification of a small and a big blobs containing `signature` and `envelope.zip` files.

The size of the small zip file resources/small.zip is 3 KB, The size of the small zip file resources/big.zip is 105 MB.

Example logging output with ```-Xmx16m``` flag:

```
18:36:38.250 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
18:36:38.255 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Verifying blob: small.zip
18:36:38.255 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
18:36:38.268 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used before verification: 6043136
18:36:38.272 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Getting signature
18:36:38.273 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: envelope.zip
18:36:38.320 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: signature
18:36:38.325 [Thread-0] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 3286 bytes from input stream to output stream
18:36:38.333 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature has been read from small.zip in 53 ms
18:36:38.334 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature size: 256
18:36:38.334 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after reading signature: 7091712
18:36:38.335 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Getting envelope.zip
18:36:38.336 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: envelope.zip
18:36:38.337 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Envelope has been read from small.zip in 14 ms
18:36:38.338 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after reading envelope: 7091712
18:36:38.357 [Thread-2] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 2711 bytes from input stream to output stream
18:36:38.359 [Thread-1] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 3286 bytes from input stream to output stream
18:36:40.683 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.ZipProcessor - Signature verified
18:36:40.684 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature of small.zip has been verified in 2346 ms
18:36:40.684 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after signature verification: 8140288
18:36:40.684 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
18:36:40.684 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
18:36:40.685 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Verifying blob: big.zip
18:36:40.685 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
18:36:40.685 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used before verification: 8140288
18:36:40.688 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Getting signature
18:36:40.689 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: envelope.zip
18:50:00.726 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: signature
18:50:00.732 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature has been read from big.zip in 800067 ms
18:50:00.732 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature size: 256
18:50:00.732 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after reading signature: 9231904
18:50:00.726 [Thread-3] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 104749490 bytes from input stream to output stream
18:50:00.737 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Getting envelope.zip
18:50:00.738 [main] INFO  uk.gov.hmcts.reform.signatureverification.util.BlobUtility - Zip entry: envelope.zip
18:50:00.738 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Envelope has been read from big.zip in 6 ms
18:50:00.739 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after reading envelope: 9231904
19:03:14.541 [Thread-4] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 104749490 bytes from input stream to output stream
19:03:14.539 [Thread-5] INFO  uk.gov.hmcts.reform.signatureverification.service.buffer.StreamBuffer - Copied 104748915 bytes from input stream to output stream
19:03:16.868 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.ZipProcessor - Signature verified
19:03:16.869 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Signature of big.zip has been verified in 796149 ms
19:03:16.869 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - Heap used after signature verification: 8224880
19:03:16.869 [main] INFO  uk.gov.hmcts.reform.signatureverification.service.StreamingProcessor - --------------------
```
