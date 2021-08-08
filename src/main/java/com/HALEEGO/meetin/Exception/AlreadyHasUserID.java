package com.HALEEGO.meetin.Exception;

import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class AlreadyHasUserID extends RuntimeException{
    private ErrorCode errorCode;
    String customMessage;

    public AlreadyHasUserID(String customMessage,  ErrorCode errorCode){
        super(errorCode.getMessage());
        this.customMessage = customMessage;
        this.errorCode = errorCode;
    }
}
