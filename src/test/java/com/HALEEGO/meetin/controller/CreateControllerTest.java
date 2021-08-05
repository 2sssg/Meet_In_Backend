package com.HALEEGO.meetin.controller;

import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CreateControllerTest {

    @Test
    public void signup(){
        User user = User.builder()
                .userID(null)
                .userPW(null)
                .userNAME("이석진")
                .build();

        System.out.println(user.getUserID());
        assertThat(user.getId(), is(1L));
        assertThat(user.getUserID(), is(nullValue()));
        assertThat(user.getUserPW(), is(nullValue()));
        assertThat(user.getUserNAME(), is("이석진"));

    }

    @Autowired
    RoomRepository roomRepository;

    @Test
    @Transactional
    public void hashtest(){

        Room room = roomRepository.getById(1L);
        UserDTO userDTO = new UserDTO().builder()
                .userID(room.getHostUSER().getUserID())
                .userPW(room.getHostUSER().getUserPW())
                .userNAME(room.getHostUSER().getUserNAME())
                .id(room.getHostUSER().getId())
                .build();
        log.info("여기");
        RoomDTO roomDTO = new RoomDTO().builder()
                .roomID(room.getRoomID())
                .id(room.getId())
                .hostUSER(userDTO)
                .build();
        log.info(String.valueOf(roomDTO.getRoomID()));
        log.info(String.valueOf(roomDTO.getId()));
        log.info(String.valueOf(roomDTO.getHostUSER().getUserID()));


    }

    @Test
    public void test(){
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("roomID",123213);
       log.info(jsonObject.containsKey("userID")?"TRUE":"FALSE");
    }

}