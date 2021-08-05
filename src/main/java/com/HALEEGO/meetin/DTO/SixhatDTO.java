package com.HALEEGO.meetin.DTO;

import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SixhatDTO {
    private Long id;

    private Enum<MeetStep> meetSTEP;

    private ToolDTO tool;

    private RoomDTO room;

    public void setMeetSTEP(String meetSTEP) {
        this.meetSTEP = MeetStep.valueOf(meetSTEP);
    }
}
