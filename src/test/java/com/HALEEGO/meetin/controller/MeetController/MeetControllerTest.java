package com.HALEEGO.meetin.controller.MeetController;

import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MeetControllerTest {
    @Test
    public void test(){
        UserDTO u = new UserDTO();
        log.info(new FixedreturnValue<UserDTO>(u).toString());
    }
}