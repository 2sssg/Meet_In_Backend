package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.AOP.LogExecution;
import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.FixedreturnValue;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.Exception.InconsistencyException;
import com.HALEEGO.meetin.Exception.NotFoundException;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class ReadController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/read/login" , method = RequestMethod.POST)
    @LogExecution
    public Object signIn(@RequestBody UserDTO userDTO)   {
        UserDTO returnuserDTO;
        User user = userRepository.findByUserID(userDTO.getUserID()).orElseThrow(
                () -> new NotFoundException("해당 id가 없습니다", ErrorCode.NOT_FOUND));
        if (userDTO.getUserPW().equals(user.getUserPW())) {
            returnuserDTO = UserDTO.builder()
                    .id(user.getId())
                    .userID(user.getUserID())
                    .userPW(user.getUserPW())
                    .userNAME(user.getUserNAME())
                    .build();
            return new FixedreturnValue<>(returnuserDTO);
        } else {
            log.info("enterRoom fail end");
            throw new InconsistencyException("password가 일치 하지 않음", ErrorCode.INCONSISTENCY);
        }
    }
}
