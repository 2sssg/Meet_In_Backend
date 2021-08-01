package com.HALEEGO.meetin.repository;

import com.HALEEGO.meetin.model.MeetKind.Sixhat;
import com.HALEEGO.meetin.model.ToolKind.Tool;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToolRepository extends JpaRepository<Tool,Long> {

    Tool findBySixhat(Sixhat sixhat);
}