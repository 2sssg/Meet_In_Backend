package com.HALEEGO.meetin.controller.MeetController;

import com.HALEEGO.meetin.Constant.FixedreturnValue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class SixhatControllerTest {

    @Test
    public void returnvalTest(){
        FixedreturnValue a;
        a = new FixedreturnValue().builder()
                .status(200)
                .message("zz")
                .customMessage("오메야")
                .build();

        log.info(String.valueOf(a.getStatus()));
        log.info(a.getMessage());
        log.info(a.getCustomMessage());
    }
}