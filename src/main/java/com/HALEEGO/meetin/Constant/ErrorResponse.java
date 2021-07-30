package com.HALEEGO.meetin.Constant;

import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;

    private String customMessage;

    public ErrorResponse(String customMessage, ErrorCode errorCode){
        this.status = errorCode.getStatus();
        this.message = errorCode.getMessage();
        this.customMessage = customMessage;
    }

}
