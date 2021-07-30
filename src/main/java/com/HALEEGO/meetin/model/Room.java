package com.HALEEGO.meetin.model;

import com.HALEEGO.meetin.Constant.Enum.MeetType;
import com.HALEEGO.meetin.model.MeetKind.Six_hat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false , unique = true, length = 20)
    private int roomID;

    @Column
    private MeetType meetType;

    @ManyToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    @JoinColumn(name = "HOSTUSER_ID")
    private User hostUSER;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "room" , fetch = FetchType.LAZY)
    private List<User_has_Room> users = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "room")
    private List<Six_hat> six_hats = new ArrayList<>();

}
