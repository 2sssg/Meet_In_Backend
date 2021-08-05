package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.Room;
import com.HALEEGO.meetin.model.User;
import com.HALEEGO.meetin.model.User_has_Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
public interface User_has_RoomRepository extends JpaRepository<User_has_Room,Long> {

    List<User_has_Room> findByRoom(Room room);

}
