package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.MeetKind.Six_hat;
import com.HALEEGO.meetin.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface Six_hatRepository extends JpaRepository<Six_hat,Long> {

    List<Six_hat> findByRoom(Room room);
}
