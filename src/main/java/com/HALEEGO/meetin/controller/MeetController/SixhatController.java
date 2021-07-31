package com.HALEEGO.meetin.controller.MeetController;


import com.HALEEGO.meetin.controller.CreateController;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SixhatController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateController.class);


    @RequestMapping("/save/six_hatStep/{roomid}")
    public void SaveSix_hatStep(@PathVariable int roomid, @RequestBody JSONObject jsonObjectj){
        jsonObjectj.forEach(((k,v)->{ LOGGER.info(k + " : "+v); }));
        
        //TODO : 식스햇 단계마다 저장하는 로직 짜기

    }
}
