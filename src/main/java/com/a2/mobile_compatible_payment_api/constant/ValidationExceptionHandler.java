package com.a2.mobile_compatible_payment_api.constant;

import com.a2.mobile_compatible_payment_api.model.CustomException;
import com.a2.mobile_compatible_payment_api.model.GenericResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResponseEntity<Void> validationInvalid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        StringBuilder error = new StringBuilder();
        var errors = ex.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return GenericResponseEntity.error(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public GenericResponseEntity<?> handleCustomException(CustomException ex) {
        var extraFlag = ex.getExtraFlags();
        if (extraFlag == null) {
            return GenericResponseEntity.error(List.of(ex.getMessage()), ex.getStatus());
        }else{
            return  GenericResponseEntity.errorWithErrorData(extraFlag,List.of(ex.getMessage()),ex.getStatus());
        }
    }


}
