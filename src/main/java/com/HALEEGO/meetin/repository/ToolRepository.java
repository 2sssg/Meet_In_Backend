package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.MeetKind.Six_hat;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ToolRepository extends JpaRepository<Tool,Long> {


}