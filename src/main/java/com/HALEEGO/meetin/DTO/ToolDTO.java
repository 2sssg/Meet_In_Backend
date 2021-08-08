package com.HALEEGO.meetin.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ToolDTO {
    private Long id;

    private SixhatDTO sixhat;

    private List<PostItDTO> postits =  new ArrayList<>();

}
