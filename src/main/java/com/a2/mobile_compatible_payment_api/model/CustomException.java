package com.a2.mobile_compatible_payment_api.model;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class CustomException extends  Exception{
    private final HttpStatus status;
    private final Map extraFlags;

    public CustomException(String message) {
        super(message);
        status=HttpStatus.CONFLICT;
        extraFlags=null;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status=status;
        extraFlags=null;
    }

    public CustomException(String message, HttpStatus status, Map extraFlags) {
        super(message);
        this.status=status;
        this.extraFlags =extraFlags;
    }
}
