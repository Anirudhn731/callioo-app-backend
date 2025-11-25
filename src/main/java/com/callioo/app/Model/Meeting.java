package com.callioo.app.Model;

import java.time.Instant;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
// import jakarta.persistence.PrePersist;

@Entity
@IdClass(MeetingId.class)
public class Meeting {
    @Id
    private String meetingRoomId;

    @Id
    private String email;

    @Id
    @Column(updatable = false)
    private Instant startedAt;

    @Column(nullable = false, unique = false)
    private String createdByEmail;

    private Instant endedAt;

    // @PrePersist
    // public void generateId() {
    // if (meetingRoomId == null) {
    // meetingRoomId = UUID.randomUUID();
    // }
    // }

    public Meeting() {
    }

    // Can be used for immediate meetings
    public Meeting(String email, String createdByEmail) {
        this.meetingRoomId = NanoIdUtils.randomNanoId();
        this.email = email;
        this.createdByEmail = createdByEmail;
        this.startedAt = Instant.now();

    }

    // Can be used for scheduled meetings
    public Meeting(String email, String createdByEmail, Instant startedAt) {
        this.meetingRoomId = NanoIdUtils.randomNanoId();
        this.email = email;
        this.createdByEmail = createdByEmail;
        this.startedAt = startedAt;
    }

    public Meeting(String meetingRoomId, String email, String createdByEmail, Instant startedAt) {
        this.meetingRoomId = meetingRoomId;
        this.email = email;
        this.createdByEmail = createdByEmail;
        this.startedAt = startedAt;
    }

    // Getters and Setters
    public String getMeetingRoomId() {
        return this.meetingRoomId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setMeetingRoomID(String meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedByEmail() {
        return this.createdByEmail;
    }

    public Instant getStartedAt() {
        return this.startedAt;
    }

    public Instant getEndedAt() {
        return this.endedAt;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }

}
