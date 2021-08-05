package com.HALEEGO.meetin.model.MeetKind;

import java.util.*;
import com.HALEEGO.meetin.Constant.Enum.MeetStep;
import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Sixhat {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Enum<MeetStep> meetSTEP;


    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL , mappedBy = "sixhat")
    @JoinColumn(name = "TOOL_ID")
    private Tool tool;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "ROOM_ID")
    private Room room;

}
