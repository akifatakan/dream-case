package com.example.dreamcase.service.impl;

import com.example.dreamcase.model.Tournament;
import com.example.dreamcase.model.User;
import com.example.dreamcase.repository.TournamentRepository;
import com.example.dreamcase.repository.UserRepository;
import com.example.dreamcase.request.CreateUserRequest;
import com.example.dreamcase.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    TournamentRepository tournamentRepository;

    public UserServiceImpl(UserRepository userRepository, TournamentRepository tournamentRepository) {
        this.userRepository = userRepository;
        this.tournamentRepository = tournamentRepository;
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User(createUserRequest);
        return userRepository.save(user);
    }

    @Override
    public User updateLevel(Long userId) {
        try {
            java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
            User user = getUserById(userId);
            user.setLevel(user.getLevel() + 1);
            user.setCoins(user.getCoins() + 25);

            if(user.isInTournament() && isGroupCompleted(userId)){
                Tournament tournament = tournamentRepository.findByIdDayAndIdUserId(today, userId);
                if (tournament.getId().getDay().toString().equals(today.toString()) && LocalTime.now().isBefore(LocalTime.of(20,00))){
                    tournament.setTournamentScore(tournament.getTournamentScore() + 1);
                    tournamentRepository.save(tournament);
                }
            }
            userRepository.save(user);

            return user;
        } catch (NoSuchElementException error) {
            throw new NoSuchElementException("User Not Found with id: " + userId);
        }
    }


    @Override
    public User getUserById(Long userId) {
        try {
            return userRepository.findById(userId).get();
        } catch ( NoSuchElementException error) {
            throw new NoSuchElementException("User Not Found with id: " + userId);
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private boolean isGroupCompleted(Long userId) {
        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        Long groupId = tournamentRepository.findByIdDayAndIdUserId(today, userId).getId().getGroupId();
        List<Tournament> usersGroup = tournamentRepository.findByIdDayAndIdGroupId(today, groupId);
        return usersGroup.size() == 5;
    }
}
