package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.Six_hatDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.MeetKind.Six_hat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class CreateController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateController.class);
    @Autowired
    User_has_RoomRepository user_has_roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    Six_hatRepository six_hatRepository;

    @RequestMapping(value = "/signup" , method = RequestMethod.POST)
    public String signup(@RequestBody UserDTO userDTO){
        LOGGER.info("userID : "+userDTO.getUserID());
        LOGGER.info("userPW : "+userDTO.getUserPW());
        LOGGER.info("userNAME : "+userDTO.getUserNAME());

        User user = User.builder()
                .userID(userDTO.getUserID())
                .userPW(userDTO.getUserPW())
                .userNAME(userDTO.getUserNAME())
                .build();
        try {
            userRepository.save(user);
            return Return.SUCCESS.toString();
        }catch (Exception e){
              return Return.FAIL.toString();
        }
    }

    @RequestMapping(value = "/createroom" , method = RequestMethod.POST)
    public RoomDTO createRoom(@RequestBody HashMap<String,Object> map) {
        Room room;
        Six_hat six_hat;
        Tool tool;
        User_has_Room user_has_room;
        String id = map.get("id").toString();
        MeetType meetType = MeetType.valueOf(map.get("meetType").toString());

        User user = userRepository.findByuserID(id).get();
        Date time = new Date();

        room = new Room().builder()
                .roomID(Math.abs(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time).hashCode()))
                .hostUSER(user)
                .build();


        switch (meetType){
            case SIX_HAT:
                room.setMeetType(MeetType.SIX_HAT);
                six_hat = new Six_hat().builder()
                        .meetSTEP(MeetStep.BEFORE_START)
                        .room(room)
                        .build();

                tool = new Tool().builder()
                        .six_hat(six_hat)
                        .build();
                toolRepository.save(tool);
                six_hatRepository.save(six_hat);
                roomRepository.save(room);

        }
        user_has_room = new User_has_Room().builder()
                .user(user)
                .room(room)
                .build();
        user_has_roomRepository.save(user_has_room);

        Room room1 = roomRepository.findByRoomID(room.getRoomID());
        List<Six_hat> six_hats =  six_hatRepository.findByRoom(room1);
        List<Six_hatDTO> six_hatDTOS = new ArrayList<>();
        for(Six_hat t : six_hats){
            six_hatDTOS.add(
                    new Six_hatDTO().builder()
                            .id(t.getId())
                            .meetSTEP(t.getMeetSTEP())
                            .build()
            );
        }
        return new RoomDTO().builder()
                .roomID(room.getRoomID())
                .hostUSER(new UserDTO().builder()
                        .id(user.getId())
                        .userID(user.getUserID())
                        .userPW(user.getUserPW())
                        .userNAME(user.getUserNAME())
                        .build()
                )
                .meetType(MeetType.SIX_HAT)
                .six_hats(six_hatDTOS)
                .status(ErrorCode.SUCCESS.getStatus())
                .message(ErrorCode.SUCCESS.getMessage())
                .id(roomRepository.findByRoomID(room.getRoomID()).getId())
                .build();
    }



}
