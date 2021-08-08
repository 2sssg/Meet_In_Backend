package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.AOP.LogExecution;
import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.Exception.NoRoomIDException;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.PostIt;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import com.HALEEGO.meetin.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeleteController {
//    @Autowired
//    private final SimpMessageSendingOperations messagingTemplate;

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
    public Object deleteGuest_Endroom(@PathVariable  int roomid){
        List<User_has_Room> user_has_rooms = roomRepository.findByRoomID(roomid).orElseThrow(
                () -> new NoRoomIDException("룸 id가 없쪙 ㅠㅠ" , ErrorCode.NOT_FOUND)
        ).getUsers();
        for(User_has_Room u : user_has_rooms) {
            if(u.getUser().getUserID()==null) {
                log.info("여기 들어오긴함");
                User user = u.getUser();
                user_has_roomRepository.delete(u);
                user.getRooms().remove(u.getRoom());
                if(!user.getPostits().isEmpty()){
                    log.info("if문 : " + user.getPostits().size());
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
        return new FixedreturnValue<>();
    }
////////////////////////////안쓸거임//////////////////////////////////////





    @RequestMapping("/delete/deleteroom/{roomid}")
    @Transactional
    @LogExecution
    public Object deleteRoom(@PathVariable int roomid){
        Room room = roomRepository.findByRoomID(roomid).orElseThrow(()->
                new NoRoomIDException("방을 못찾겠음 ㅠㅠ", ErrorCode.NOT_FOUND)
                );
        List<User_has_Room> uhr = room.getUsers();
        for(User_has_Room u : uhr) {
            u.setUser(null);
            u.getRoom().setHostUSER(null);
            u.setRoom(null);
            user_has_roomRepository.delete(u);
        }
        roomRepository.delete(room);
        return new FixedreturnValue<>();
    }



//    @RequestMapping("delete/exituser")
//    @Transactional
//    public Object exituser(@RequestBody  JSONObject jsonObject){
//        Long id = Long.parseLong(jsonObject.get("id").toString());
//        int roomID = Integer.parseInt(jsonObject.get("roomID").toString());
////        User_has_Room user_has_room = user_has_roomRepository.findByRoom_RoomIDAndUser_Id(roomID,id);
//        user_has_room.setRoom(null);
//        if(user_has_room.getUser().getUserID() != null){
//            user_has_room.setUser(null);
//        }
//        user_has_roomRepository.delete(user_has_room);
//
//        return null;
//    }

    //////////////////////////////////////이것도 안쓸거임/////////////////////////////////////////

}
