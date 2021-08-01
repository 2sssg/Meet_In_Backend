package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SixhatDTO {
    private Long id;

    private Enum<MeetStep> meetSTEP;

    private ToolDTO tool;

    private RoomDTO room;

    private int status;

    private String message;

    public void setMeetSTEP(String meetSTEP) {
        this.meetSTEP = MeetStep.valueOf(meetSTEP);
    }
}
