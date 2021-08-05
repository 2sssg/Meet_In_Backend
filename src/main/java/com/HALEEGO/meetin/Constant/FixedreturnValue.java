package com.HALEEGO.meetin.Constant;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FixedreturnValue<T> {
    private int status = ErrorCode.SUCCESS.getStatus();
    private String message = ErrorCode.SUCCESS.getMessage();
    private String customMessage = Return.SUCCESS.toString();
    private T object;

    public FixedreturnValue(T object) {
        this.object = object;
    }
}
