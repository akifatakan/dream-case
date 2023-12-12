package com.example.dreamcase.response;

import com.example.dreamcase.model.Country;

import java.util.List;

public class CountryLeaderboardResponse {
    private Country country;

    private int tournamentScore;

    private List<GroupLeaderboardResponse> participants;

    public CountryLeaderboardResponse(Country country, int tournamentScore, List<GroupLeaderboardResponse> participants) {
        this.country = country;
        this.tournamentScore = tournamentScore;
        this.participants = participants;
    }

    public CountryLeaderboardResponse() {
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

    public List<GroupLeaderboardResponse> getParticipants() {
        return participants;
    }

    public void setParticipants(List<GroupLeaderboardResponse> groupLeaderboardResponseList) {
        this.participants = groupLeaderboardResponseList;
    }
}
