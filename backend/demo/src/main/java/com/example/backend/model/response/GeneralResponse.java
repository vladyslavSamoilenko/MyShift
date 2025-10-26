package com.example.backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GeneralResponse<P> implements Serializable {
    private String message;
    private P payload;
    private boolean success;

    public static <P> GeneralResponse<P> createSuccessful(P payload){
        return new GeneralResponse<>(StringUtils.EMPTY, payload, true);
    }
}
