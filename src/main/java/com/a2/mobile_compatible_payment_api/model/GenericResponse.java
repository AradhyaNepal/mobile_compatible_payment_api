package com.a2.mobile_compatible_payment_api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields during serialization
public class GenericResponse<T> {
    private T data;
    private List<String> errors;
    private String message;


}
