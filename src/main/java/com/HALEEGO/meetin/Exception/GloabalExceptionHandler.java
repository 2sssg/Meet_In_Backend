package com.HALEEGO.meetin.Exception;


import com.HALEEGO.meetin.Constant.ErrorResponse;
import com.HALEEGO.meetin.controller.CreateController;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@Slf4j
@RestControllerAdvice
public class GloabalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GloabalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final NotFoundException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .customMessage(e.getCustomMessage())
                        .build());
    }


    @ExceptionHandler(InconsistencyException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final InconsistencyException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .customMessage(e.getCustomMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final Exception e){
        LOGGER.info(e.getMessage());
        return ResponseEntity
                .status(404)
                .body(ErrorResponse.builder()
                        .status(404)
                        .message(e.getMessage())
                        .customMessage("알 수 없는 오류")
                        .build());
    }
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final NullPointerException e){
        LOGGER.info(e.getMessage());
        return ResponseEntity
                .status(404)
                .body(ErrorResponse.builder()
                        .status(404)
                        .message(e.getMessage())
                        .customMessage("헤으응 null값이 튀어나와 버렷...! 그렇게 빈건 들어가지 않는다구...!")
                        .build());
    }

    @ExceptionHandler(AlreadyStartMeetException.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final AlreadyStartMeetException e){
        LOGGER.info(e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .customMessage(e.getCustomMessage())
                        .build());
    }

    @ExceptionHandler(AlreadyHasUserID.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(HttpServletRequest request, final AlreadyHasUserID e){
        LOGGER.info(e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.builder()
                        .status(e.getErrorCode().getStatus())
                        .message(e.getErrorCode().getMessage())
                        .customMessage(e.getCustomMessage())
                        .build());
    }

}
