package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.Constant.Enum.MeetType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;

    private int roomID;

    private UserDTO hostUSER;

    private MeetType meetType;

    private List<SixhatDTO> sixhats = new ArrayList<>();

    private List<UserDTO> users = new ArrayList<>();

    private int status;

    private String message;
}
