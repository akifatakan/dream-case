package com.example.dreamcase.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tournament_info")
public class Tournament {

    @EmbeddedId
    private TournamentId id;

    @Column(nullable = false)
    private int tournamentScore;



    public Tournament(TournamentId id, int tournamentScore) {
        this.id = id;
        this.tournamentScore = tournamentScore;
    }

    public Tournament() {
    }


    public TournamentId getId() {
        return id;
    }

    public void setId(TournamentId id) {
        this.id = id;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }

    // ...
}

