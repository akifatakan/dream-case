package com.example.dreamcase.repository;

import com.example.dreamcase.model.Tournament;
import com.example.dreamcase.model.TournamentId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, TournamentId> {
    List<Tournament> findByIdDay(Date day);
    Tournament findByIdDayAndIdUserId(Date day, Long userId);
    List<Tournament> findByIdDayAndIdGroupId(Date day, Long groupId);

}
