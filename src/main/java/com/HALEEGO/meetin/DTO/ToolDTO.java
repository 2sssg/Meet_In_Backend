package com.HALEEGO.meetin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolDTO {
    private Long id;

    private SixhatDTO sixhat;

    private List<PostItDTO> postits =  new ArrayList<>();

    private int status;

    private String message;

}
