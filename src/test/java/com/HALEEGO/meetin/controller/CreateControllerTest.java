package com.HALEEGO.meetin.controller;

import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.repository.RoomRepository;
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
class CreateControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateController.class);
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
        LOGGER.info("여기");
//        User user = roomRepository.getById(1L).getHostUSER();
        LOGGER.info("여기");
        UserDTO userDTO = new UserDTO().builder()
                .userID(room.getHostUSER().getUserID())
                .userPW(room.getHostUSER().getUserPW())
                .userNAME(room.getHostUSER().getUserNAME())
                .id(room.getHostUSER().getId())
                .build();
        LOGGER.info("여기");
        RoomDTO roomDTO = new RoomDTO().builder()
                .roomID(room.getRoomID())
                .id(room.getId())
                .hostUSER(userDTO)
                .build();
        LOGGER.info(String.valueOf(roomDTO.getRoomID()));
        LOGGER.info(String.valueOf(roomDTO.getId()));
        LOGGER.info(String.valueOf(roomDTO.getHostUSER().getUserID()));


    }

}