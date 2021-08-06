package com.HALEEGO.meetin.Constant.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND(404,"NOT_FOUND"),
    DUPLICATE(400, "DUPLICATE"),
    INCONSISTENCY(404, "INCONSISTENCY"),
    SUCCESS(200,"SUCCESS")
    ;

    private int status;
    private String message;
}
