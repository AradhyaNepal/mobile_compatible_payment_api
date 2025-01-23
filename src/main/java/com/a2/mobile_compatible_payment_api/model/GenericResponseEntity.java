package com.a2.mobile_compatible_payment_api.model;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class GenericResponseEntity<T> extends ResponseEntity<GenericResponse<T>> {
    public GenericResponseEntity(GenericResponse<T> response, HttpStatusCode status) {
        super(response, status);
    }

    static public <T> GenericResponseEntity<T> successWithData(T data, String message) {
        return new GenericResponseEntity<>(GenericResponse.<T>builder().data(data).message(message).build(), HttpStatus.OK);
    }

    static public GenericResponseEntity<Void> successWithMessage(String message) {
        return new GenericResponseEntity<>(GenericResponse.<Void>builder().message(message).build(), HttpStatus.OK);
    }

    static public GenericResponseEntity<Void> error(List<String> errors, HttpStatus status) {
        return new GenericResponseEntity<>(GenericResponse.<Void>builder().errors(errors).build(), status);
    }

    static public <T> GenericResponseEntity<T> errorWithErrorData(T errorData, List<String> errors, HttpStatus status) {
        return new GenericResponseEntity<>(GenericResponse.<T>builder().data(errorData).errors(errors).build(), status);
    }

}
