package org.example.mhcommon.core.exception;

import lombok.Getter;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Getter
public class DBException extends RuntimeException {
    static final long serialVersionUID = 1651914954614L;
    private int code = 400;

    protected int statusCode = INTERNAL_SERVER_ERROR.value();

    public DBException() {
    }

    public DBException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    protected DBException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
