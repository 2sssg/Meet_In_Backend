package com.HALEEGO.meetin.controller.MeetController;


import com.HALEEGO.meetin.AOP.LogExecution;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.repository.RoomRepository;
import com.HALEEGO.meetin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
    @LogExecution
    public Object movepostit(List<PostItDTO> postItDTO  , @PathParam("roomid") int roomID ){ //포스트잇 웹소켓팅
        FixedreturnValue<List<PostItDTO>> fixedreturnValue = new FixedreturnValue<>(postItDTO);
        messagingTemplate.convertAndSend("/topic/move/postit"+roomID , fixedreturnValue);
        return fixedreturnValue;
    }

    @MessageMapping("/move/nextstep/{roomid}")
    @LogExecution
    public Object movenextstep(JSONObject jsonObject, @PathParam("roomid") int roomID ){// 다음단계로 넘어가는 웹소켓팅
        MeetStep meetStep = MeetStep.valueOf(jsonObject.get("meetStep").toString());
        FixedreturnValue<MeetStep> fixedreturnValue = new FixedreturnValue<>(meetStep.meetstepnext(meetStep));
        messagingTemplate.convertAndSend("/topic/move/nextstep"+roomID, fixedreturnValue);
        return fixedreturnValue;
    }

}

