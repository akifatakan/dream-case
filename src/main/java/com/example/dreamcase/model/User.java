package com.example.dreamcase.model;

import com.example.dreamcase.request.CreateUserRequest;
import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;
import java.util.Random;

@Entity
@Table(name = "user_info")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private Country country;

    private int level;
    private int coins;

    private boolean inTournament;

    private java.sql.Date lastTournamentDate;

    // Constructors, getters, setters, and other methods


    public User(String username, Country country) {
        this.username = username;
        this.country = country;
        this.level = 1;
        this.coins = 5000;
        this.inTournament = false;
        this.lastTournamentDate = null;
    }

    public User() {
    }

    public User(CreateUserRequest createUserRequest) {
        this.username = createUserRequest.getUserName();
        this.country = selectRandomCountry();
        this.level = 1;
        this.coins = 5000;
        this.inTournament = false;
        this.lastTournamentDate = null;
    }

    private static Country selectRandomCountry() {
        Country[] countries = Country.values();
        Random random = new Random();
        int index = random.nextInt(countries.length);
        return countries[index];
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isInTournament() {
        return inTournament;
    }

    public void setInTournament(boolean inTournament) {
        this.inTournament = inTournament;
    }

    public Date getLastTournamentDate() {
        return lastTournamentDate;
    }

    public void setLastTournamentDate(Date lastTournamentDate) {
        this.lastTournamentDate = lastTournamentDate;
    }

    // Add other getters and setters for additional fields

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) &&
                Objects.equals(username, user.username) &&
                country == user.country &&
                level == user.level &&
                coins == user.coins;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, country, level, coins);
    }

}

