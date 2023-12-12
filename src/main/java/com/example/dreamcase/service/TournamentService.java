package com.example.dreamcase.service;

import com.example.dreamcase.response.CountryLeaderboardResponse;
import com.example.dreamcase.response.GroupLeaderboardResponse;
import com.example.dreamcase.response.GroupRankResponse;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface TournamentService {
    List<GroupLeaderboardResponse> enterTournament(Long userId);
    Map<String, Object> claimReward(Long userId);
    GroupRankResponse getGroupRank(java.sql.Date day, Long userId);

    List<GroupLeaderboardResponse> getGroupLeaderboard(java.sql.Date day, Long groupId);

    List<CountryLeaderboardResponse> getCountryLeaderboard (Date date);
    Long getGroupIdByDayAndUserId(Date day, Long userId);

    }
