package com.HALEEGO.meetin.controller.MeetController;

import java.util.*;
import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.Enum.Return;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.DTO.ToolDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.PostIt;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.repository.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

@RestController
@Transactional
@RequiredArgsConstructor
public class SixhatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SixhatController.class);
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    PostitRepository postitRepository;
    @Autowired
    SixhatRepository sixhatRepository;
    @Autowired
    ToolRepository toolRepository;
    @Autowired
    UserRepository userRepository;



    @RequestMapping("/save/sixhatStep/{roomid}")
    public Object SaveSix_hatStep(@PathVariable int roomid, @RequestBody ToolDTO toolDTO)  {
        LOGGER.info("toolDTO : "+toolDTO.toString());
        LOGGER.info(toolDTO.getSixhat().getMeetSTEP().toString());
        LOGGER.info(toolDTO.getPostits().toString());
        JSONObject returnjsonObj = new JSONObject();
        Sixhat sixhat;
        Tool tool;
        PostIt postIt;
        Room room = roomRepository.findByRoomID(roomid);
        Enum<MeetStep> meetStep = toolDTO.getSixhat().getMeetSTEP();
        sixhat = new Sixhat().builder()
                .room(room)
                .meetSTEP(meetStep)
                .build();
        tool = new Tool().builder()
                .sixhat(sixhat)
                .build();
        for (PostItDTO postitDTOS :toolDTO.getPostits()){
            LOGGER.info(postitDTOS.toString());

            postIt = new PostIt().builder()
                    .height(postitDTOS.getHeight())
                    .width(postitDTOS.getWidth())
                    .user(userRepository.findByuserID(postitDTOS.getUser().getUserID()).get())
                    .postitID(postitDTOS.getPostitID())
                    .locationX(postitDTOS.getLocationX())
                    .locationY(postitDTOS.getLocationY())
                    .postitCONTEXT(postitDTOS.getPostitCONTEXT())
                    .postitCOLOR(postitDTOS.getPostitCOLOR())
                    .tool(tool)
                    .build();
            postitRepository.save(postIt);
        }
        toolRepository.save(tool);
        sixhatRepository.save(sixhat);

        returnjsonObj.put("status" , ErrorCode.SUCCESS.getStatus());
        returnjsonObj.put("message" , ErrorCode.SUCCESS.getMessage());
        returnjsonObj.put("customMessage" , Return.SUCCESS);
        return returnjsonObj;
    }



//    @MessageMapping(value = "/read/whitehat/{roomid}")
    @Transactional
    @RequestMapping(value = "/read/whitehat/{roomid}")
    public Object sixthStepRequirement(@PathVariable("roomid") int roomid){
        JSONObject jsonObject = new JSONObject();
        for(Sixhat sixhat : roomRepository.findByRoomID(roomid).getSixhats()){
            List<PostIt> postItList = toolRepository.findBySixhat(sixhat).getPostIts();
            List<PostItDTO> postItDTOS = new ArrayList<>();
            for(PostIt postIt : postItList){
                postItDTOS.add(
                        new PostItDTO().builder()
                                .id(postIt.getId())
                                .width(postIt.getWidth())
                                .height(postIt.getHeight())
                                .user(
                                        new UserDTO().builder()
                                        .userNAME(postIt.getUser().getUserNAME())
                                        .build()
                                )
                                .postitID(postIt.getPostitID())
                                .locationX(postIt.getLocationX())
                                .locationY(postIt.getLocationY())
                                .postitCOLOR(postIt.getPostitCOLOR())
                                .postitCONTEXT(postIt.getPostitCONTEXT())
                                .build()
                );
            }
            jsonObject.put(sixhat.getMeetSTEP().toString(),postItDTOS);
        }
        jsonObject.put("status", ErrorCode.SUCCESS.getStatus());
        jsonObject.put("message", ErrorCode.SUCCESS.getMessage());
        jsonObject.put("customMessage" , Return.SUCCESS);
        return jsonObject;
//        messagingTemplate.convertAndSend("/topic/whitehat/"+roomid,jsonObject);

    }


}
