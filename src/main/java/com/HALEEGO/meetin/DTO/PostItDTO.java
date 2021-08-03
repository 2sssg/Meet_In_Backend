package com.HALEEGO.meetin.DTO;


import com.HALEEGO.meetin.Constant.Enum.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostItDTO {

    private Long id;

    private int postitID;

    private double locationX;

    private double locationY;

    private String postitCONTEXT;

    private double width;

    private double height;

    private UserDTO user;

    private Enum<Color> postitCOLOR;

    private ToolDTO tool;

    private int status;

    private String message;

    public void setPostitCOLOR(String postitCOLOR) {
        this.postitCOLOR = Color.valueOf(postitCOLOR);
    }
}
