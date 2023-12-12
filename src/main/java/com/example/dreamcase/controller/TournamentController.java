package com.example.dreamcase.controller;

import com.example.dreamcase.exception.DuplicateTournamentEntryException;
import com.example.dreamcase.response.CountryLeaderboardResponse;
import com.example.dreamcase.response.GroupLeaderboardResponse;
import com.example.dreamcase.response.GroupRankResponse;
import com.example.dreamcase.response.ResponseHandler;
import com.example.dreamcase.service.TournamentService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/tournament/")
public class TournamentController {
    @Autowired
    TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @PostMapping("/enter-tournament/{userId}")
    public ResponseEntity<Object> enterTournamentRequest(@PathVariable Long userId) {
        // Your logic to enter the tournament for the specified user
        try{
            List<GroupLeaderboardResponse> groupLeaderboardResponseList = tournamentService.enterTournament(userId);
            java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
            Long groupId = tournamentService.getGroupIdByDayAndUserId(today, userId);
            Map<String, Object> responseObject = createGroupLeaderboardResponse(groupLeaderboardResponseList, today, groupId);
            return ResponseHandler.responseSuccessBuilder("User entered to tournament.", HttpStatus.CREATED, responseObject);
        } catch (DuplicateTournamentEntryException error){
            return ResponseHandler.responseErrorBuilder(error.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            return ResponseHandler.responseErrorBuilder(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/group-leaderboard/{groupId}")
    public ResponseEntity<Object> getGroupLeaderboardRequest(@PathVariable Long groupId) {
        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        List<GroupLeaderboardResponse> groupLeaderboardResponseList = tournamentService.getGroupLeaderboard(today, groupId);
        Map<String, Object> responseObject = createGroupLeaderboardResponse(groupLeaderboardResponseList, today, groupId);
        return ResponseHandler.responseSuccessBuilder("Group Leaderboard of group " + groupId + " is retrieved successfully.", HttpStatus.OK, responseObject);
    }

    @PostMapping("/claim-reward/{userId}")
    public ResponseEntity<Object> claimRewardRequest(@PathVariable Long userId){
        try {
            Map<String, Object> claimRewardMap = tournamentService.claimReward(userId);
            return ResponseHandler.responseSuccessBuilder(claimRewardMap.get("Message").toString(), HttpStatus.OK, claimRewardMap.get("User"));
        } catch (Exception exception) {
            return ResponseHandler.responseErrorBuilder(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/country-leaderboard")
    public ResponseEntity<Object> getCountryLeaderboardRequest(@RequestParam(name = "day", required = false) java.sql.Date day) {
        if (day == null){
            day = new java.sql.Date(new java.util.Date().getTime()); // day is today
        }
        try {
            List<CountryLeaderboardResponse> countryLeaderboardResponseList = tournamentService.getCountryLeaderboard(day);
            Map<String, Object> responseObject = createCountryLeaderboardResponse(countryLeaderboardResponseList, day);
            return ResponseHandler.responseSuccessBuilder("Country Leaderboard for " + day + " is retrieved successfully.", HttpStatus.OK, responseObject);
        } catch (Exception e) {
            return ResponseHandler.responseErrorBuilder(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/group-rank")
    public ResponseEntity<Object> getGroupRankRequest(@RequestParam(name = "userId") Long userId, @RequestParam(name = "day", required = false) java.sql.Date day) {
        if (day == null) {
            day = new java.sql.Date(new java.util.Date().getTime()); // day is today
        }
        try {
            GroupRankResponse groupRankResponse = tournamentService.getGroupRank(day, userId);
            return ResponseHandler.responseSuccessBuilder("Group Rank for " + groupRankResponse.getUsername() + " on " + groupRankResponse.getDay() + "  retrieved successfully.",
                    HttpStatus.OK, groupRankResponse);
        } catch (NoSuchElementException exception) {
            return ResponseHandler.responseErrorBuilder(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private Map<String, Object> createGroupLeaderboardResponse(List<GroupLeaderboardResponse> groupLeaderboardResponseList, Date day, Long groupId){
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("Date", day);
        responseObject.put("GroupId", groupId);
        responseObject.put("GroupLeaderboard", groupLeaderboardResponseList);
        return responseObject;
    }

    private Map<String, Object> createCountryLeaderboardResponse(List<CountryLeaderboardResponse> countryLeaderboardResponseList, Date day){
        Map<String, Object> responseObject = new HashMap<>();
        responseObject.put("Date", day);
        responseObject.put("CountryLeaderboard", countryLeaderboardResponseList);
        return responseObject;
    }

}
