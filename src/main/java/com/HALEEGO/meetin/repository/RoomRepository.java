package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {

    Room findByRoomID(int roomid);
}