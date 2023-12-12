package com.example.dreamcase.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.sql.Date;

@Embeddable
public
class TournamentId implements Serializable {

    @Column(name = "day", nullable = false)
    private java.sql.Date day;

    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    public TournamentId(Date day, Long groupId, Long userId) {
        this.day = day;
        this.groupId = groupId;
        this.userId = userId;
    }

    public TournamentId() {
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    // ...
}
