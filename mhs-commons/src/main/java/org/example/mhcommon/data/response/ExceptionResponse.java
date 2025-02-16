package org.example.mhcommon.data.response;

import lombok.Data;

@Data
public class ExceptionResponse {
    private int status = 400;
    private String message;

    public ExceptionResponse(int status, String message){
        this.status = status;
        this.message = message;
    }

    public ExceptionResponse() {

    }
}
