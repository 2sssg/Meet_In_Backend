package com.HALEEGO.meetin.controller.MeetController;


import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.repository.RoomRepository;
import com.HALEEGO.meetin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

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
    @Transactional
    public void movepostit(PostItDTO postItDTO  , @PathParam("roomID") int roomID ){
        log.info("move postit start");
        messagingTemplate.convertAndSend("/topic/"+roomID+"/postit" , new FixedreturnValue<PostItDTO>(postItDTO));
        log.info("move postit end");
    }


}

