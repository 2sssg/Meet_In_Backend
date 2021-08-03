package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface User_has_RoomRepository extends JpaRepository<User_has_Room,Long> {

    List<User> findByRoom(Room room);
}
