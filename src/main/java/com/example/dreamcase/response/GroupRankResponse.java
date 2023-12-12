package com.example.dreamcase.response;

import com.example.dreamcase.model.Country;

import java.sql.Date;

public class GroupRankResponse
{
    private int groupRank;
    private String username;
    private java.sql.Date day;
    private Long groupId;
    private Country country;

    public GroupRankResponse(int groupRank, String username, Date day, Long groupId, Country country) {
        this.groupRank = groupRank;
        this.username = username;
        this.day = day;
        this.groupId = groupId;
        this.country = country;
    }

    public GroupRankResponse() {
    }

    public int getGroupRank() {
        return groupRank;
    }

    public void setGroupRank(int groupRank) {
        this.groupRank = groupRank;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
