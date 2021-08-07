package com.HALEEGO.meetin.controller.MeetController;


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
    public void movepostit(List<PostItDTO> postItDTO  , @PathParam("roomid") int roomID ){ //포스트잇 웹소켓팅
        log.info("move postit start");

        FixedreturnValue<List<PostItDTO>> fixedreturnValue = new FixedreturnValue<>(postItDTO);
        log.info("returnValue : " + fixedreturnValue.toString());

        messagingTemplate.convertAndSend("/topic/move/postit"+roomID , fixedreturnValue);
        log.info("move postit end");
    }

    @MessageMapping("/move/nextstep/{roomid}")
    public void movenextstep(JSONObject jsonObject, @PathParam("roomid") int roomID ){// 다음단계로 넘어가는 웹소켓팅
        log.info("movenextstep start");

        MeetStep meetStep = MeetStep.valueOf(jsonObject.get("meetStep").toString());
        FixedreturnValue<MeetStep> fixedreturnValue = new FixedreturnValue<>(meetStep.meetstepnext(meetStep));
        log.info("returnValue : " +fixedreturnValue.toString());

        messagingTemplate.convertAndSend("/topic/move/nextstep"+roomID, fixedreturnValue);

        log.info("movenextstep end");
    }

}

