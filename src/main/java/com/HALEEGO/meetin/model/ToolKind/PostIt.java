package com.HALEEGO.meetin.model.ToolKind;


import com.HALEEGO.meetin.Constant.Enum.Color;
import com.HALEEGO.meetin.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostIt {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private double width;

    @Column
    private double height;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID" )
    private User user;

    @Column
    private int postitID;

    @Column(nullable = false)
    private double locationX;

    @Column(nullable = false)
    private double locationY;

    @Column
    private String postitCONTEXT;

    @Column(nullable = false)
    private Enum<Color> postitCOLOR;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn(name = "TOOL_ID")
    private Tool tool;
}
