package com.example.dreamcase.response;

import com.example.dreamcase.model.Country;

public class GroupLeaderboardResponse {
    private Long userId;
    private String username;
    private Country country;
    private int tournamentScore;

    public GroupLeaderboardResponse() {
    }

    public GroupLeaderboardResponse(Long userId, String username, Country country, int tournamentScore) {
        this.userId = userId;
        this.username = username;
        this.country = country;
        this.tournamentScore = tournamentScore;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getTournamentScore() {
        return tournamentScore;
    }

    public void setTournamentScore(int tournamentScore) {
        this.tournamentScore = tournamentScore;
    }
}
