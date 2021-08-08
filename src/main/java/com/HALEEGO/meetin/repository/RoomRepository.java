package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Optional<Room> findByRoomID(int roomid);

}