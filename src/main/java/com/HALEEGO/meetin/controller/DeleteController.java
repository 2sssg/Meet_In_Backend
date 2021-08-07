package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.PostIt;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeleteController {
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


    @RequestMapping("/delete/endroom/{roomid}")
    @Transactional
    public FixedreturnValue deleteGuest_Endroom(@PathVariable  int roomid){
        log.info("endroom start" );
        List<User_has_Room> user_has_rooms = roomRepository.findByRoomID(roomid).orElse(null).getUsers();
        for(User_has_Room u : user_has_rooms) {
            if(u.getUser().getUserID()==null) {
                log.info("여기 들어오긴함");
                User user = u.getUser();
                user_has_roomRepository.delete(u);
                user.getRooms().remove(u.getRoom());
                if(!user.getPostits().isEmpty()){
                    log.info("if문 : " + String.valueOf(user.getPostits().size()));
                    for(PostIt p : user.getPostits()){
                        p.setUser(null);
                    }
                }else{
                    log.info("else문 : " );
                }
                userRepository.delete(user);
            }
        }
        log.info("enterRoom end");
        return new FixedreturnValue();
    }





    @RequestMapping("/delete/deleteroom/{roomid}")
    @Transactional
    public Object deleteRoom(@PathVariable int roomid){
        Room room = roomRepository.findByRoomID(roomid).orElse(null);
        List<User_has_Room> uhr;
        if(room != null){
            uhr = room.getUsers();
        }else{
            throw new NullPointerException();
        }
        if(uhr !=null){
            for(User_has_Room u : uhr){
                u.setUser(null);
                u.getRoom().setHostUSER(null);
                u.setRoom(null);
                user_has_roomRepository.delete(u);
            }
        }else{
            throw new NullPointerException();
        }
        roomRepository.delete(room);
        return new FixedreturnValue<>();
    }

}
