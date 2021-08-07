package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.RoomDTO;
import com.HALEEGO.meetin.Exception.NoRoomIDException;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UpdateController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired RoomRepository roomRepository;

    @MessageMapping("/update/roomtitle")
    @RequestMapping("/update/roomtitle")
    @Transactional
    public Object updateRoomTitle(@RequestBody Room room){
        log.info("updateRommTile is start");
        roomRepository.findByRoomID(room.getRoomID()).orElseThrow(()->
                new NoRoomIDException("roomID가 없는듯 ㅋㅋ",ErrorCode.NOT_FOUND)
                ).setTitle(room.getTitle());

        RoomDTO roomDTO = RoomDTO.builder()
                .title(room.getTitle())
                .build();
        FixedreturnValue<RoomDTO> fixedreturnValue = new FixedreturnValue<>(roomDTO);

        log.info("returnValue : "  + fixedreturnValue);
        messagingTemplate.convertAndSend("/topic/"+room.getRoomID()+"/update/roomtitle" ,fixedreturnValue);
        log.info("updateRommTile is end");
        return fixedreturnValue;
    }

    @RequestMapping("update/roomend")
    @Transactional
    public Object roomEnd(@RequestBody JSONObject jsonObject){
        log.info("roomend is start");
        jsonObject.forEach((k,v)-> log.info(k+" : "+v));
        int roomID = Integer.parseInt(jsonObject.get("roomID").toString());
        roomRepository.findByRoomID(roomID).orElseThrow(()->
           new NoRoomIDException("roomID가 없는듯 ㅋㅋ",ErrorCode.NOT_FOUND)
        ).setEnd(false);

        FixedreturnValue<Object> fixedreturnValue = new FixedreturnValue<>();
        log.info("returnValue : "+ fixedreturnValue);

        return fixedreturnValue;
    }



}
