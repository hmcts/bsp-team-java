package uk.gov.hmcts.reform.signatureverification.exceptions;

public class StreamReadingException extends RuntimeException {
    public StreamReadingException(InterruptedException ex) {
        super(ex);
    }
}
