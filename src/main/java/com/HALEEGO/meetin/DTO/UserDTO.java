package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.model.Room;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDTO {
    private Long id;

    private String userID;

    private String userPW;

    private String userNAME;

    private List<Room> rooms = new ArrayList<>();
}
