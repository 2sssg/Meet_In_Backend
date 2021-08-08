package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.ToolKind.PostIt;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PostitRepository extends JpaRepository<PostIt, Long> {
    List<PostIt> findByTool_Sixhat_Room_RoomID(int roomID);
    List<PostIt> findByTool_Sixhat(Sixhat sixhat);
}
