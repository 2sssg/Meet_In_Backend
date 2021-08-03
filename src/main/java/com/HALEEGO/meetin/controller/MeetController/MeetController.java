package com.HALEEGO.meetin.controller.MeetController;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.repository.RoomRepository;
import com.HALEEGO.meetin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.websocket.server.PathParam;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class MeetController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;

    @MessageMapping("/{roomID}/postit")
    @Transactional
    public void temp(PostItDTO postItDTO  , @PathParam("roomID") int roomID ){
        JSONObject returnObj = new JSONObject();

        returnObj.put("postit" , postItDTO);
        returnObj.put("status" , ErrorCode.SUCCESS.getStatus());
        returnObj.put("message" , ErrorCode.SUCCESS.getMessage());

        messagingTemplate.convertAndSend("/topic/"+roomID+"/postit" , returnObj);

    }


}

