package com.HALEEGO.meetin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 20 , unique = true)
    private String userID;

    @Column( length = 20)
    private String userPW;

    @Column(nullable = false , length = 20)
    private String userNAME;

    @OneToMany(cascade = CascadeType.ALL , mappedBy = "user" , fetch = FetchType.LAZY)
    private List<User_has_Room> rooms = new ArrayList<>();



}
