package com.HALEEGO.meetin.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@RequiredArgsConstructor
@RestController
public class MeetController {
    @Autowired
    private final SimpMessageSendingOperations messagingTemplate;


}
