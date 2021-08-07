package com.HALEEGO.meetin.controller.MeetController;

import java.util.*;

import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.DTO.ToolDTO;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.PostIt;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.HALEEGO.meetin.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SixhatController {
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



    @RequestMapping("/create/sixhatStep/{roomid}")
    public Object SaveSix_hatStep(@PathVariable int roomid, @RequestBody ToolDTO toolDTO) { // sixhat단계마다 저장하기
        log.info("saveSix_hatStep start");
        log.info("toolDTO : "+toolDTO.toString());
        log.info("meetStep : " + toolDTO.getSixhat().getMeetSTEP().toString());
        log.info("postits : "+toolDTO.getPostits().toString());
        Sixhat sixhat;
        Tool tool;
        PostIt postIt;
        Room room = roomRepository.findByRoomID(roomid).orElse(null); //room 찾기 // 룸 id잘못됐으면 오류
        Enum<MeetStep> meetStep = toolDTO.getSixhat().getMeetSTEP(); // meetStep받기
        sixhat = new Sixhat().builder() //식스햇
                .room(room)
                .meetSTEP(meetStep)
                .build();
        tool = new Tool().builder() //툴
                .sixhat(sixhat)
                .build();
        for (PostItDTO postitDTOS :toolDTO.getPostits()){ // 포스트잇 리스트로 받은 것들 for문으로 포스트잇 하나씩 만들어서 저장
            log.info("postitDTOS : " + postitDTOS.toString());

            postIt = new PostIt().builder()
                    .height(postitDTOS.getHeight())
                    .width(postitDTOS.getWidth())
                    .user(userRepository.findById(postitDTOS.getUser().getId()).orElse(null))
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

        log.info("saveSix_hatStep end");
        return new FixedreturnValue<>();
    }



    @MessageMapping(value = "/read/sixhat/whitehat/{roomid}")
    @Transactional
    @RequestMapping(value = "/read/sixhat/whitehat/{roomid}")
    public Object returnWhitehat(@PathVariable("roomid") int roomid) { //whitehat일때 1~5단계 포스트잇 반환
        log.info("whitehatRequirement start");
        JSONObject jsonObject = new JSONObject();
        for(Sixhat sixhat : roomRepository
                .findByRoomID(roomid).orElse(null).getSixhats()){ //룸id로 식스햇 리스트 받아와서
            List<PostItDTO> postItDTOS = new ArrayList<>();
            for(PostIt postIt : sixhat.getTool().getPostIts()){ // 포스트잇 리스트 for돌려주기
                postItDTOS.add(postIt.toPostItDTO());
            }
            jsonObject.put(sixhat.getMeetSTEP().toString(),postItDTOS);
        }
        log.info("whitehatRequirement end");
        messagingTemplate.convertAndSend("/topic" + roomid + "/sixhat/whitehat/",jsonObject); // 원래는 메세지 템플릿으로 전체 유저한테 보내줘야함 이거 주석풀기
        return new FixedreturnValue<JSONObject>(jsonObject); // 확인위해서 그냥 리턴
    }

    @Transactional
    @RequestMapping(value = "/read/sixhat/onestep/{meetStep}/{roomid}")
    public Object returnWhitehat(@PathVariable("roomid") int roomid ,@PathVariable("meetStep") MeetStep meetStep) {
        log.info(meetStep+"Requirement start");
        List<PostItDTO> postItDTOS = new ArrayList<>();
        for(Sixhat sixhat : roomRepository.findByRoomID(roomid).orElse(null).getSixhats()){
            if(sixhat.getMeetSTEP() != meetStep) continue;
            for(PostIt postit : sixhat.getTool().getPostIts()){
                postItDTOS.add(postit.toPostItDTO());
            }
        }
        log.info(meetStep+"Requirement end");
        return new FixedreturnValue<List<PostItDTO>>(postItDTOS);
    }



}
