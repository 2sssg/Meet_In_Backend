package com.HALEEGO.meetin.model.ToolKind;

import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tool {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY  , cascade = CascadeType.ALL)
    @JoinColumn(name = "SIXHAT_ID")
    private Sixhat sixhat;

    @OneToMany(mappedBy = "tool" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<PostIt> postIts = new ArrayList<>();
}
