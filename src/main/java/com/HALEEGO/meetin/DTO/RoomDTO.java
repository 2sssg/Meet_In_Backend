package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.Constant.Enum.ErrorCode;
import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private List<Six_hatDTO> six_hats = new ArrayList<>();
    private int status;
    private String message;
}
