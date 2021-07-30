package com.HALEEGO.meetin.Exception;

import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private ErrorCode errorCode;

    String customMessage;
    public NotFoundException(String customMessage,  ErrorCode errorCode){
        super(errorCode.getMessage());
        this.customMessage = customMessage;
        this.errorCode = errorCode;
    }


}
