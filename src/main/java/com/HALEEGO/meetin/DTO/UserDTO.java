package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.model.Room;
import lombok.*;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class UserDTO {
    private Long id;

    private String userID;

    private String userPW;

    private String userNAME;

    private List<Room> rooms = new ArrayList<>();

    private int status;
    private String message;
}
