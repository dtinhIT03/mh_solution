package org.example.mhcommon.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class DfResponse<T> {

    private Integer code;
    private String message;
    private T result;
    @JsonIgnore
    private Boolean formCache;

    public DfResponse() {
        code = 0;
        message = "OK";
    }

    public DfResponse(String message) {
        this.code = 0;
        this.message = message;
    }

    public DfResponse(String message, T result) {
        this.code = 0;
        this.message = message;
        this.result = result;
    }

    public DfResponse(String message, Integer code) {
        this.code = code;
        this.message = message;
    }

    public static <T> ResponseEntity<DfResponse<T>> okEntity(T body) {
        return ResponseEntity.ok(ok(body));
    }

    public static <T> DfResponse<T> ok(T body) {
        DfResponse<T> response = new DfResponse<>();
        response.setResult(body);
        return response;
    }

    public static DfResponse badRequest(String message) {
        DfResponse response = new DfResponse<>(message, HttpStatus.BAD_REQUEST.value());
        return response;
    }

    public static DfResponse customRequest(String message, Integer code){
        DfResponse response = new DfResponse(message, code);
        return response;
    }
}