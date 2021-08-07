package com.HALEEGO.meetin.controller.MeetController;


import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.repository.RoomRepository;
import com.HALEEGO.meetin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import java.util.*;

import javax.websocket.server.PathParam;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MeetController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;

    @MessageMapping("/move/postit/{roomid}")
    public void movepostit(List<PostItDTO> postItDTO  , @PathParam("roomid") int roomID ){ //포스트잇 웹소켓팅
        log.info("move postit start");
        messagingTemplate.convertAndSend("/topic/"+roomID+"/move/postit" , new FixedreturnValue<List<PostItDTO>>(postItDTO));
        log.info("move postit end");
    }

    @MessageMapping("/move/nextstep/{roomid}")
    public void movenextstep(MeetStep meetStep, @PathParam("roomid") int roomID ){
        log.info("movenextstep start");
        messagingTemplate.convertAndSend("/topic/"+roomID+"/move/nextstep", new FixedreturnValue<MeetStep>(meetStep.meetstepnext(meetStep)));
        log.info("movenextstep end");
    }



}

