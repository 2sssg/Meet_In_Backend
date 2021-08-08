package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.AOP.LogExecution;
import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.Exception.AlreadyHasUserID;
import com.HALEEGO.meetin.Exception.AlreadyStartMeetException;
import com.HALEEGO.meetin.Exception.NoRoomIDException;
import com.HALEEGO.meetin.Exception.NotFoundException;
import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    ObjectMapper objectMapper = new ObjectMapper();
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

    @LogExecution
    @RequestMapping(value = "/create/signup" , method = RequestMethod.POST)
    public Object signup(@RequestBody UserDTO userDTO){ //회원가입
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
            return new FixedreturnValue<>();
        }catch (Exception e){
              throw new AlreadyHasUserID("이미 있는 아이디입니다" , ErrorCode.DUPLICATE);
        }
    }

    @RequestMapping(value = "/create/createroom" , method = RequestMethod.POST)
    @LogExecution
    public Object createRoom(@RequestBody JSONObject jsonObject) {
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
                .orElseThrow(() ->
                        new NoRoomIDException("id가 없음 ㅋㅋ 다시줭 ㅎㅎ",ErrorCode.NOT_FOUND)
                );

        room = Room.builder()
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
                sixhat = Sixhat.builder()
                        .meetSTEP(MeetStep.BEFORE_START)
                        .room(room)
                        .build();

                tool = Tool.builder()
                        .sixhat(sixhat)
                        .build();
                toolRepository.save(tool);
                sixhatRepository.save(sixhat);
                Room room1 = roomRepository.save(room);

                user_has_room = User_has_Room.builder()
                        .user(user)
                        .room(room)
                        .build();

                user_has_roomRepository.save(user_has_room);
                RoomDTO roomDTO = RoomDTO.builder()
                        .roomID(room1.getRoomID())
                        .title(room1.getTitle())
                        .hostUSER(UserDTO.builder()
                                .userNAME(user.getUserNAME())
                                .build()
                        )
                        .meetType(MeetType.SIX_HAT)
                        .build();
                return new FixedreturnValue<RoomDTO>(roomDTO);

        }

        return FixedreturnValue.builder()
                .status(ErrorCode.NOT_FOUND.getStatus())
                .message(ErrorCode.NOT_FOUND.getMessage())
                .customMessage(Return.FAIL.toString())
                .build();
    }


//    @MessageMapping(value = "/save/enterroom")
    @RequestMapping(value = "/create/enterroom" , method = RequestMethod.POST)
    @Transactional
    @LogExecution
    public Object enterRoom(@RequestBody JSONObject jsonObject) throws JsonProcessingException {
        RoomDTO roomDTO;
        User guestUser = new User();

        log.info(jsonObject.containsKey("id")?
                jsonObject.get("id").toString():
                "guest 이용자입니다");

        if(!jsonObject.containsKey("id")){
            guestUser = userRepository.save(
                    User.builder()
                    .userNAME(jsonObject.get("userNAME").toString())
                    .build()
            );
        }else{
            log.info("이미 참가한 사용자입니다. DB저장없이 리턴만 해줍니당~ ");
            int roomID = Integer.parseInt(jsonObject.get("roomID").toString());
            Long id = Long.parseLong(jsonObject.get("id").toString());
            if(user_has_roomRepository.findByRoom_RoomIDAndUser_Id(roomID, id).isPresent()){
                Room room = roomRepository.findByRoomID(roomID).orElseThrow(() ->
                            new NoRoomIDException("룸없음" , ErrorCode.NOT_FOUND)
                        );
                List<UserDTO> userDTOList = new ArrayList<>();
                UserDTO userDTO = new UserDTO();
                for(User_has_Room user_has_room : room.getUsers()){
                    if(user_has_room.getUser().getId().equals(id)){
                        userDTO.setUserNAME(user_has_room.getUser().getUserNAME());
                    }
                    userDTOList.add(
                            UserDTO.builder()
                            .userNAME(user_has_room.getUser().getUserNAME())
                            .build()
                    );
                }
                return new FixedreturnValue<RoomDTO>(
                        RoomDTO.builder()
                                .roomID(room.getRoomID())
                                .title(room.getTitle())
                                .hostUSER(
                                        UserDTO.builder()
                                                .userNAME(room.getHostUSER().getUserNAME())
                                                .build()
                                ).meetType(room.getMeetType())
                                .userPARTICIPANT(userDTOList)
                                .userME(userDTO)
                                .build()
                );
            }
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
                if(sixhatRepository.findByRoom(room).size() != 1){
                    throw new AlreadyStartMeetException("이미 방이 시작됐습니다. ", ErrorCode.DUPLICATE);
                }
        }
        User_has_Room user_has_room =
                User_has_Room.builder()
                    .room(room)
                    .user(user)
                    .build();
        user_has_roomRepository.save(user_has_room);
        List<User_has_Room> users = user_has_roomRepository.findByRoom(room);
        List<UserDTO> userDTOS = new ArrayList<>();
        UserDTO usermeDTO = new UserDTO();
        JSONObject jsonObjects = new JSONObject();
        for(User_has_Room u : users){
            if(u.getUser().getUserNAME().equals(user.getUserNAME())){
                usermeDTO = UserDTO.builder()
                        .userNAME(u.getUser().getUserNAME())
                        .id(user.getId())
                        .build();
            }
            userDTOS.add(
                    UserDTO.builder()
                            .userNAME(u.getUser().getUserNAME())
                            .build()
            );
        }
        roomDTO = RoomDTO.builder()
                .roomID(roomid)
                .title(room.getTitle())
                .hostUSER(
                        UserDTO.builder()
                                .userNAME(room.getHostUSER().getUserNAME())
                                .build()
                ).meetType(room.getMeetType())
                .userPARTICIPANT(userDTOS)
                .userME(usermeDTO)
                .build();

        log.info("WebSocketReturnValue : \n"+
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userDTOS));
        messagingTemplate.convertAndSend(
                "/topic/enterroom/"+roomid
                ,new FixedreturnValue<UserDTO>(
                        UserDTO.builder()
                                .userNAME(user.getUserNAME())
                                .build()
                )
        );
        return new FixedreturnValue<RoomDTO>(roomDTO);
    }
}
