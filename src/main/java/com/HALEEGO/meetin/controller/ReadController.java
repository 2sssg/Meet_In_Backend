package com.HALEEGO.meetin.controller;


import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.DTO.UserDTO;
import com.HALEEGO.meetin.Exception.InconsistencyException;
import com.HALEEGO.meetin.Exception.NotFoundException;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.HashMap;
import java.util.function.Supplier;

@RestController
public class ReadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadController.class);

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/login" , method = RequestMethod.POST)
    public Object login(@RequestBody UserDTO userDTO) throws Throwable {
        UserDTO returnuserDTO;
        User user = userRepository.findByuserID(userDTO.getUserID()).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new NotFoundException("해당 id가 없습니다", ErrorCode.NOT_FOUND);
            }
        });
        if(userDTO.getUserPW().equals(user.getUserPW())) {
            returnuserDTO = UserDTO.builder()
                    .id(user.getId())
                    .userID(user.getUserID())
                    .userPW(user.getUserPW())
                    .userNAME(user.getUserNAME())
                    .status(ErrorCode.SUCCESS.getStatus())
                    .message(ErrorCode.SUCCESS.getMessage())
                    .build();
            return returnuserDTO;
        }
        else throw new InconsistencyException("password가 일치 하지 않음", ErrorCode.INCONSISTENCY);
    }





}
