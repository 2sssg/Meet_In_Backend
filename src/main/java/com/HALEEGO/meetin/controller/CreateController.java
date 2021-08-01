package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.SixhatDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.Exception.AlreadyHasUserID;
import com.HALEEGO.meetin.Exception.AlreadyStartMeetException;
import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    SixhatRepository sixhatRepository;

    @RequestMapping(value = "/save/signup" , method = RequestMethod.POST)
    public Object signup(@RequestBody UserDTO userDTO){
        JSONObject jsonobj = new JSONObject();
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
            jsonobj.put("status" , ErrorCode.SUCCESS.getStatus());
            jsonobj.put("message",ErrorCode.SUCCESS.getMessage());
            jsonobj.put("customMessage",Return.SUCCESS);
            return jsonobj;
        }catch (Exception e){
              throw new AlreadyHasUserID("이미 있는 아이디입니다" , ErrorCode.DUPLICATE);
        }
    }

    @RequestMapping(value = "/save/createroom" , method = RequestMethod.POST)
    public RoomDTO createRoom(@RequestBody JSONObject jsonObject) {
        jsonObject.forEach((k,v)->{ LOGGER.info(k+" : "+v); });
        Room room;
        Sixhat sixhat;
        Tool tool;
        User_has_Room user_has_room;
        Date time = new Date();
        String userid = jsonObject.get("userID").toString();
        MeetType meetType = MeetType.valueOf(jsonObject.get("meetType").toString());

        User user = userRepository.findByuserID(userid).get();

        room = new Room().builder()
                .roomID(Math.abs(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time).hashCode()))
                .hostUSER(user)
                .build();


        switch (meetType){
            case SIX_HAT:
                room.setMeetType(MeetType.SIX_HAT);
                sixhat = new Sixhat().builder()
                        .meetSTEP(MeetStep.BEFORE_START)
                        .room(room)
                        .build();

                tool = new Tool().builder()
                        .sixhat(sixhat)
                        .build();
                toolRepository.save(tool);
                sixhatRepository.save(sixhat);
                roomRepository.save(room);

                user_has_room = new User_has_Room().builder()
                        .user(user)
                        .room(room)
                        .build();
                user_has_roomRepository.save(user_has_room);

                Room room1 = roomRepository.findByRoomID(room.getRoomID());
                List<Sixhat> sixhats =  sixhatRepository.findByRoom(room1);
                List<SixhatDTO> sixhatDTOS = new ArrayList<>();
                for(Sixhat t : sixhats){
                    sixhatDTOS.add(
                            new SixhatDTO().builder()
                                    .id(t.getId())
                                    .meetSTEP(t.getMeetSTEP())
                                    .build()
                    );
                }
                return new RoomDTO().builder()
                        .roomID(room.getRoomID())
                        .hostUSER(new UserDTO().builder()
                                .userNAME(user.getUserNAME())
                                .build()
                        )
                        .meetType(MeetType.SIX_HAT)
                        .sixhats(sixhatDTOS)
                        .status(ErrorCode.SUCCESS.getStatus())
                        .message(ErrorCode.SUCCESS.getMessage())
                        .id(roomRepository.findByRoomID(room.getRoomID()).getId())
                        .build();

        }
        return new RoomDTO().builder()
                .status(ErrorCode.NOT_FOUND.getStatus())
                .message(ErrorCode.NOT_FOUND.getMessage())
                .build();
    }



    @RequestMapping(value = "/save/enterroom" , method = RequestMethod.POST)
    @Transactional
    public RoomDTO enterRoom(@RequestBody JSONObject jsonObject){
        List<SixhatDTO> sixhatDTOS = new ArrayList<>();
        jsonObject.forEach((k,v)->{LOGGER.info(k+" : "+v);});

        LOGGER.info(jsonObject.get("userID").toString());
        LOGGER.info(jsonObject.get("roomID").toString());

        String userid = jsonObject.get("userID").toString();
        int roomid = Integer.parseInt(jsonObject.get("roomID").toString());
        Room room  = roomRepository.findByRoomID(roomid);
        User user = userRepository.findByuserID(userid).get();
        switch (room.getMeetType()){
            case SIX_HAT:
                List<Sixhat> sixhats =  sixhatRepository.findByRoom(room);
                if(sixhats.get(sixhats.size()-1).getMeetSTEP() != MeetStep.BEFORE_START){
                    throw new AlreadyStartMeetException("이미 방이 시작됐습니다. ", ErrorCode.DUPLICATE);
                }
                for(Sixhat t : sixhats){
                    sixhatDTOS.add(
                            new SixhatDTO().builder()
                                    .id(t.getId())
                                    .meetSTEP(t.getMeetSTEP())
                                    .build()
                    );
                }
        }
        User_has_Room user_has_room = new User_has_Room().builder()
                .room(room)
                .user(user)
                .build();

        user_has_roomRepository.save(user_has_room);
        return new RoomDTO().builder()
                .status(ErrorCode.SUCCESS.getStatus())
                .message(ErrorCode.SUCCESS.getMessage())
                .id(room.getId())
                .roomID(roomid)
                .hostUSER(
                        new UserDTO().builder()
                        .userNAME(user.getUserNAME())
                        .build()
                ).meetType(room.getMeetType())
                .sixhats(sixhatDTOS)
                .build();
    }




}
