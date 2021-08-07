package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    @Autowired
    RoomRepository roomRepository;

    @MessageMapping("/update/roomtitle")
    @RequestMapping("/update/roomtitle")
    @Transactional
    public Object updateRoomTitle(@RequestBody Room room){
        roomRepository.findByRoomID(room.getRoomID()).orElse(null).setTitle(room.getTitle());
        messagingTemplate.convertAndSend("/topic/"+room.getRoomID()+"/update/roomtitle" ,new FixedreturnValue<Room>(room));
        return new FixedreturnValue<Room>(room);
    }
}
