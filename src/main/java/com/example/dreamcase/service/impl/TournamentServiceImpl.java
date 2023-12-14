package com.example.dreamcase.service.impl;

import com.example.dreamcase.exception.DuplicateTournamentEntryException;
import com.example.dreamcase.exception.UnsatisfiedConstraintException;
import com.example.dreamcase.model.*;
import com.example.dreamcase.repository.TournamentRepository;
import com.example.dreamcase.repository.UserRepository;
import com.example.dreamcase.response.CountryLeaderboardResponse;
import com.example.dreamcase.response.GroupLeaderboardResponse;
import com.example.dreamcase.response.GroupRankResponse;
import com.example.dreamcase.service.TournamentService;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentServiceImpl implements TournamentService {

    TournamentRepository tournamentRepository;
    UserRepository userRepository;

    public TournamentServiceImpl(TournamentRepository tournamentRepository, UserRepository userRepository) {
        this.tournamentRepository = tournamentRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GroupLeaderboardResponse> enterTournament(Long userId) {
        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        User user = userRepository.findById(userId).get();
        if (!isUserEligibleForTournament(user)){
            throw new UnsatisfiedConstraintException("User must be at least level 20 and have more than 1000 coins");
        }
        if (user.isInTournament()) {
          if (user.getLastTournamentDate().toString().equals(today.toString())){
                throw new DuplicateTournamentEntryException("User has already entered a tournament today.");
            } else {
              throw new DuplicateTournamentEntryException("User must claim reward to enter a new tournament.");
          }
        }

        // Find a suitable group for the user
        List<Tournament> todaysTournament = tournamentRepository.findByIdDay(today);
        Long suitableGroupId = findSuitableGroup(todaysTournament, userId);

        // Create a new tournament entry
        TournamentId tournamentId = new TournamentId(today, suitableGroupId, userId);
        Tournament newTournament = new Tournament(tournamentId, 0);

        // Save the new tournament entry
        tournamentRepository.save(newTournament);

        // Update user
        user.setInTournament(true);
        user.setCoins(user.getCoins() - 1000);
        user.setLastTournamentDate(today);
        userRepository.save(user);

        return getGroupLeaderboard(today, suitableGroupId);
    }

    @Override
    public Map<String, Object> claimReward(Long userId) {
        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        User user = userRepository.findById(userId).get();
        Date lastTournamentDate = user.getLastTournamentDate();
        if (lastTournamentDate == null || !user.isInTournament()){
            throw new RuntimeException("There is no reward to claim");
        }
        if (lastTournamentDate.toString().equals(today.toString()) && LocalTime.now().isBefore(LocalTime.of(20, 00))){
            throw new RuntimeException("Reward claiming is only allowed after 8 pm.");
        }

        Long groupId = tournamentRepository.findByIdDayAndIdUserId(lastTournamentDate, userId).getId().getGroupId();
        List<GroupLeaderboardResponse> groupLeaderboardResponseList = getGroupLeaderboard(lastTournamentDate, groupId);
        int position = getLeaderboardPosition(userId, groupLeaderboardResponseList);
        String message;
        if(position == 1){
            user.setCoins(user.getCoins() + 10000);
            message = "Congrats! You won 10000 coins by completing the tournament in the first place.";
        } else if (position == 2) {
            user.setCoins(user.getCoins() + 5000);
            message = "Congrats! You won 5000 coins by completing the tournament in the second place.";
        } else {
            message = "Ooooopppppssss, you could not earn any reward in this tournament. Don't give up!";
        }

        user.setInTournament(false);
        userRepository.save(user);
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("Message", message);
        responseMap.put("User", user);
        return responseMap;
    }

    @Override
    public GroupRankResponse getGroupRank(java.sql.Date day,  Long userId) {
        User user = userRepository.findById(userId).get();
        Tournament tournament = tournamentRepository.findByIdDayAndIdUserId(day, userId);
        if (tournament == null) {
            throw new RuntimeException(user.getUsername() + " did not enter to any tournament on " + day);
        }
        List<GroupLeaderboardResponse> groupLeaderboardResponseList = getGroupLeaderboard(day, tournament.getId().getGroupId());
        int position = getLeaderboardPosition(userId, groupLeaderboardResponseList);
        GroupRankResponse groupRankResponse= new GroupRankResponse(position, user.getUsername(), day, tournament.getId().getGroupId(), user.getCountry());
        return groupRankResponse;
    }

    @Override
    public List<GroupLeaderboardResponse> getGroupLeaderboard(Date day, Long groupId) {
        List<Tournament> groupLeaderboard = tournamentRepository.findByIdDayAndIdGroupId(day, groupId);
        groupLeaderboard.sort(Comparator.comparingInt(Tournament::getTournamentScore).reversed());
        List<GroupLeaderboardResponse> groupLeaderboardResponseList = new ArrayList<>();
        
        groupLeaderboard.forEach(tournament -> {
            GroupLeaderboardResponse groupLeaderboardResponse = getGroupLeaderboardResponseByTournament(tournament);
            groupLeaderboardResponseList.add(groupLeaderboardResponse);
        });
        return groupLeaderboardResponseList;
    }

    @Override
    public List<CountryLeaderboardResponse> getCountryLeaderboard(Date day) {
        List<Tournament> todaysTournamentList = tournamentRepository.findByIdDay(day);
        List<CountryLeaderboardResponse> countryLeaderboard = new ArrayList<>();
        todaysTournamentList.forEach(tournament -> {
            Country country = userRepository.findById(tournament.getId().getUserId()).get().getCountry();
            if(countryLeaderboard.stream().anyMatch(countryTournamentInfo -> countryTournamentInfo.getCountry().equals(country))){
                countryLeaderboard.forEach(countryTournamentInfo -> {
                    if (countryTournamentInfo.getCountry().equals(country)){
                        countryTournamentInfo.setTournamentScore(countryTournamentInfo.getTournamentScore() + tournament.getTournamentScore());
                        List<GroupLeaderboardResponse> groupLeaderboardResponseList = countryTournamentInfo.getParticipants();
                        groupLeaderboardResponseList.add(getGroupLeaderboardResponseByTournament(tournament));
                        countryTournamentInfo.setParticipants(groupLeaderboardResponseList);
                    }
                });
            } else {
                List<GroupLeaderboardResponse> groupLeaderboardResponseList = new ArrayList<>();
                groupLeaderboardResponseList.add(getGroupLeaderboardResponseByTournament(tournament));
                countryLeaderboard.add(new CountryLeaderboardResponse(country, tournament.getTournamentScore(), groupLeaderboardResponseList));
            }
        });

        countryLeaderboard.sort(Comparator.comparingInt(CountryLeaderboardResponse::getTournamentScore).reversed());
        return countryLeaderboard;
    }

    private Long findSuitableGroup(List<Tournament> todaysTournament, Long userId) {
        if (todaysTournament.isEmpty()) {
            return (long) 1; // If the group is empty, the user can enter
        } else {
            // Check if there is a user from the same country in the group
            User user = userRepository.findById(userId).get();
            Country userCountry = user.getCountry();

            for (Tournament tournament : todaysTournament){
                Long groupId = tournament.getId().getGroupId();
                List<Tournament> groupsTournaments = filterTournamentsByGroupId(todaysTournament, groupId);

                boolean isGroupAvailable = groupsTournaments.stream().noneMatch(tournament1 ->
                    userRepository.findById(tournament1.getId().getUserId()).get().getCountry().equals(userCountry)
                );

                if(isGroupAvailable) {
                    return groupId;
                }
            }

            // if function reaches that point than it means we need a new group

            return findLargestGroupId(todaysTournament) + 1;
        }
    }

    private Long findLargestGroupId(List<Tournament> tournaments) {
        return tournaments.stream()
                .map(tournament -> tournament.getId().getGroupId())
                .max(Long::compare).get();
    }

    private List<Tournament> filterTournamentsByGroupId(List<Tournament> tournaments, Long groupId) {
        return tournaments.stream()
                .filter(tournament -> tournament.getId().getGroupId().equals(groupId))
                .collect(Collectors.toList());
    }

    private boolean isUserEligibleForTournament(User user) {
        // Check if the user meets the eligibility criteria
        return user.getLevel() >= 20 && user.getCoins() >= 1000;
    }

    private int getLeaderboardPosition(Long userId, List<GroupLeaderboardResponse> leaderboard) {
        for (int i = 0; i < leaderboard.size(); i++) {
            if (leaderboard.get(i).getUserId().equals(userId)) {
                return i + 1; // Adding 1 because positions are usually 1-indexed
            }
        }

        // User not found in the leaderboard
        return -1;
    }
    @Override
    public Long getGroupIdByDayAndUserId(Date day, Long userId){
        return tournamentRepository.findByIdDayAndIdUserId(day, userId).getId().getGroupId();
    }

    private GroupLeaderboardResponse getGroupLeaderboardResponseByTournament(Tournament tournament){
        GroupLeaderboardResponse groupLeaderboardResponse = new GroupLeaderboardResponse();
        User user = userRepository.findById(tournament.getId().getUserId()).get();
        groupLeaderboardResponse.setUserId(user.getUserId());
        groupLeaderboardResponse.setUsername(user.getUsername());
        groupLeaderboardResponse.setCountry(user.getCountry());
        groupLeaderboardResponse.setTournamentScore(tournament.getTournamentScore());
        return groupLeaderboardResponse;
    }
}
