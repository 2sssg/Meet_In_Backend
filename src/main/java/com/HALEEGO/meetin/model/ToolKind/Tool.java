package com.HALEEGO.meetin.model.ToolKind;

import com.HALEEGO.meetin.model.MeetKind.Six_hat;
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
    @JoinColumn(name = "SIX_HAT_ID")
    private Six_hat six_hat;

    @OneToMany(mappedBy = "tool" , cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    private List<PostIt> postIts = new ArrayList<>();
}
