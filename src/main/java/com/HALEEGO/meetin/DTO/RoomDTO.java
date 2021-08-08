package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RoomDTO {
    private Long id;

    private String title;

    private int roomID;

    private UserDTO hostUSER;

    private MeetType meetType;

    private UserDTO userME;

    private List<UserDTO> userPARTICIPANT;

    private List<SixhatDTO> sixhats = new ArrayList<>();

    private List<UserDTO> users = new ArrayList<>();

}
