package com.HALEEGO.meetin.Exception;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import lombok.Getter;

@Getter
public class InconsistencyException extends RuntimeException{
    private ErrorCode errorCode;
    String customMessage;

    public InconsistencyException(String customMessage,  ErrorCode errorCode){
        super(errorCode.getMessage());
        this.customMessage = customMessage;
        this.errorCode = errorCode;
    }
}
