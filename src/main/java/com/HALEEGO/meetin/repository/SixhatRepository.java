package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface SixhatRepository extends JpaRepository<Sixhat,Long> {

    List<Sixhat> findByRoom(Room room);
}
