package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.SixhatDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.Exception.AlreadyHasUserID;
import com.HALEEGO.meetin.Exception.AlreadyStartMeetException;
import com.HALEEGO.meetin.Exception.NotFoundException;
import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CreateController {

    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;
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

    @RequestMapping(value = "/create/signup" , method = RequestMethod.POST)
    public Object signup(@RequestBody UserDTO userDTO){ //회원가입
        log.info("signup start ");
        JSONObject jsonobj = new JSONObject();
        log.info("userID : "+userDTO.getUserID());
        log.info("userPW : "+userDTO.getUserPW());
        log.info("userNAME : "+userDTO.getUserNAME());
        User user = User.builder()
                .userID(userDTO.getUserID())
                .userPW(userDTO.getUserPW())
                .userNAME(userDTO.getUserNAME())
                .build();
        try {
            userRepository.save(user);
            log.info("signup success end");
            return new FixedreturnValue<>();
        }catch (Exception e){
              throw new AlreadyHasUserID("이미 있는 아이디입니다" , ErrorCode.DUPLICATE);
        }
    }

    @RequestMapping(value = "/create/createroom" , method = RequestMethod.POST)
    public FixedreturnValue createRoom(@RequestBody JSONObject jsonObject) {
        log.info("createRoom start");
        jsonObject.forEach((k,v)->{ log.info(k+" : "+v); });
        Room room;
        Sixhat sixhat;
        Tool tool;
        User_has_Room user_has_room;
        Date time = new Date();
        Long id = Long.parseLong(jsonObject.get("id").toString());
        MeetType meetType = MeetType.valueOf(
                jsonObject.get("meetType").toString()
        );

        User user = userRepository
                .findById(id)
                .get();

        room = new Room().builder()
                .roomID(
                        Math.abs(
                                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(time)
                                        .hashCode()
                        )
                )
                .title(jsonObject.get("title").toString())
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
                Room room1 = roomRepository.save(room);

                user_has_room = new User_has_Room().builder()
                        .user(user)
                        .room(room)
                        .build();
                user_has_roomRepository.save(user_has_room);
                RoomDTO roomDTO = new RoomDTO().builder()
                        .roomID(room1.getRoomID())
                        .title(room1.getTitle())
                        .hostUSER(new UserDTO().builder()
                                .userNAME(user.getUserNAME())
                                .build()
                        )
                        .meetType(MeetType.SIX_HAT)
                        .build();
                log.info("createRoom Success end");
                return new FixedreturnValue<RoomDTO>(roomDTO);

        }
        log.info("createRoom fail end");
        return new FixedreturnValue<>().builder()
                .status(ErrorCode.NOT_FOUND.getStatus())
                .message(ErrorCode.NOT_FOUND.getMessage())
                .customMessage(Return.FAIL.toString())
                .build();
    }


//    @MessageMapping(value = "/save/enterroom")
    @RequestMapping(value = "/create/enterroom" , method = RequestMethod.POST)
    @Transactional
    public Object enterRoom(@RequestBody JSONObject jsonObject) {
        log.info("enterRoom start");
        List<SixhatDTO> sixhatDTOS = new ArrayList<>();
        RoomDTO roomDTO;
        User guestUser = new User();
        jsonObject.forEach((k,v)->{log.info(k+" : "+v);});

        log.info(jsonObject.containsKey("id")?
                jsonObject.get("id").toString():
                "guest 이용자입니다");
        log.info("roomID : " + jsonObject.get("roomID").toString());
        if(!jsonObject.containsKey("id")){
            guestUser = userRepository.save(
                    new User().builder()
                    .userNAME(jsonObject.get("userNAME").toString())
                    .build()
            );
        }
        Long id = jsonObject.containsKey("id")?Long.parseLong(jsonObject.get("id").toString()):0L;
        int roomid = Integer.parseInt(jsonObject.get("roomID").toString());
        Room room  = roomRepository.findByRoomID(roomid).orElseThrow(()->
                new NotFoundException("해당 방(방 번호 : "+roomid+") 이 없습니다 ", ErrorCode.NOT_FOUND)
        );
        User user = userRepository.findById(id).orElse(guestUser);
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
        User_has_Room user_has_room =
                new User_has_Room().builder()
                    .room(room)
                    .user(user)
                    .build();
        user_has_roomRepository.save(user_has_room);
        List<User_has_Room> users = user_has_roomRepository.findByRoom(room);
        List<UserDTO> userDTOS = new ArrayList<>();
        for(User_has_Room u : users){
            userDTOS.add(
                    new UserDTO().builder()
                            .userNAME(u.getUser().getUserNAME())
                            .id(
                                    u.getUser().getUserNAME().equals(user.getUserNAME())?user.getId():0L
                            )
                            .build()
            );
        }
        roomDTO = new RoomDTO().builder()
                .roomID(roomid)
                .title(room.getTitle())
                .hostUSER(
                        new UserDTO().builder()
                                .userNAME(room.getHostUSER().getUserNAME())
                                .build()
                ).meetType(room.getMeetType())
                .users(userDTOS)
                .build();

        messagingTemplate.convertAndSend("/topic/enterroom/"+roomid,userDTOS);
        log.info("enterRoom success end");
        return new FixedreturnValue<RoomDTO>(roomDTO);
    }

}
