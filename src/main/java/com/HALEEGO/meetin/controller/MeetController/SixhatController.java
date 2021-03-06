package com.HALEEGO.meetin.controller.MeetController;

import java.util.*;

import com.HALEEGO.meetin.AOP.LogExecution;
import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.PostItDTO;
import com.HALEEGO.meetin.DTO.ToolDTO;
import com.HALEEGO.meetin.Exception.NoRoomIDException;
import com.HALEEGO.meetin.Exception.NoUserIdException;
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
    @Autowired private final SimpMessageSendingOperations messagingTemplate;
    @Autowired RoomRepository roomRepository;
    @Autowired PostitRepository postitRepository;
    @Autowired SixhatRepository sixhatRepository;
    @Autowired ToolRepository toolRepository;
    @Autowired UserRepository userRepository;



    @RequestMapping("/save/sixhatStep/{roomid}")
    @LogExecution
    public Object SaveSix_hatStep(@PathVariable int roomid, @RequestBody ToolDTO toolDTO) { // sixhat단계마다 저장하기
        Sixhat sixhat;
        Tool tool;
        PostIt postIt;
        Room room = roomRepository.findByRoomID(roomid).orElseThrow(()->
                    new NoRoomIDException("RoomId가없음 ㅇㅇ", ErrorCode.NOT_FOUND)
                ); //room 찾기 // 룸 id잘못됐으면 오류
        Enum<MeetStep> meetStep = toolDTO.getSixhat().getMeetSTEP(); // meetStep받기
        sixhat = Sixhat.builder() //식스햇
                .room(room)
                .meetSTEP(meetStep)
                .build();
        tool = Tool.builder() //툴
                .sixhat(sixhat)
                .build();
        for (PostItDTO postitDTOS :toolDTO.getPostits()){ // 포스트잇 리스트로 받은 것들 for문으로 포스트잇 하나씩 만들어서 저장

            postIt = PostIt.builder()
                    .height(postitDTOS.getHeight())
                    .width(postitDTOS.getWidth())
                    .user(userRepository.findById(postitDTOS.getUser().getId()).orElseThrow(()->
                            new NoUserIdException("user를 못찾겠음 ㅠㅠ",ErrorCode.NOT_FOUND)
                    ))
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

        return new FixedreturnValue<>();
    }



    @MessageMapping(value = "/read/sixhat/whitehat/{roomid}")
    @Transactional
    @RequestMapping(value = "/read/sixhat/whitehat/{roomid}")
    @LogExecution
    public Object returnWhitehat(@PathVariable("roomid") int roomid) { //whitehat일때 1~5단계 포스트잇 반환
        JSONObject jsonObject = new JSONObject();
        for(Sixhat sixhat : roomRepository
                .findByRoomID(roomid).orElseThrow(()->
                        new NoRoomIDException("방이없음 ㅠㅠ 방아이디 확인해보셈 "+roomid,ErrorCode.NOT_FOUND))
                .getSixhats()){ //룸id로 식스햇 리스트 받아와서
            List<PostItDTO> postItDTOS = new ArrayList<>();
            for(PostIt postIt : sixhat.getTool().getPostIts()){ // 포스트잇 리스트 for돌려주기
                postItDTOS.add(postIt.toPostItDTO());
            }
            jsonObject.put(sixhat.getMeetSTEP().toString(),postItDTOS);
        }

        FixedreturnValue<JSONObject> fixedreturnValue = new FixedreturnValue<>(jsonObject);
        messagingTemplate.convertAndSend("/topic" + roomid + "/sixhat/whitehat/",fixedreturnValue); // 원래는 메세지 템플릿으로 전체 유저한테 보내줘야함 이거 주석풀기
        return fixedreturnValue; // 확인위해서 그냥 리턴
    }

    @Transactional
    @RequestMapping(value = "/read/sixhat/onestep/{meetStep}/{roomid}")
    @LogExecution
    public Object returnWhitehat(@PathVariable("roomid") int roomid ,@PathVariable("meetStep") MeetStep meetStep) {
        List<PostItDTO> postItDTOS = new ArrayList<>();
        for(Sixhat sixhat : roomRepository.findByRoomID(roomid).orElseThrow(
                ()->new NoRoomIDException("룸 못찾겠음 ㅠㅠ 룸아이디 다시확인해봐"+roomid,ErrorCode.NOT_FOUND))
                .getSixhats())
        {
            if(sixhat.getMeetSTEP() != meetStep) continue;
            for(PostIt postit : sixhat.getTool().getPostIts()){
                postItDTOS.add(postit.toPostItDTO());
            }
        }
        return new FixedreturnValue<List<PostItDTO>>(postItDTOS);
    }


    @RequestMapping("/read/sixhat/allstep")
    @LogExecution
    @Transactional
    public Object readAllStep(@RequestBody JSONObject jsonObject){
        int roomID = Integer.parseInt(jsonObject.get("roomID").toString());
        JSONObject jsonObject1 = new JSONObject();
        List<Sixhat> sixhats = sixhatRepository.findByRoom_RoomID(roomID);
        for(Sixhat sixhat : sixhats){
            List<PostItDTO> postItDTOS = new ArrayList<>();
            if(sixhat.getMeetSTEP().equals(MeetStep.BEFORE_START)) continue;
            postitRepository.findByTool_Sixhat(sixhat).forEach(p-> postItDTOS.add(p.toPostItDTO()));
            jsonObject1.put(sixhat.getMeetSTEP().toString(),postItDTOS);
        }
        return new FixedreturnValue<JSONObject>(jsonObject1);
    }
}
