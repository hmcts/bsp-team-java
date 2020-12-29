package uk.gov.hmcts.reform.signatureverification.exceptions;

public class InvalidZipArchiveException extends RuntimeException {

    public InvalidZipArchiveException(String message) {
        super(message);
    }
}
