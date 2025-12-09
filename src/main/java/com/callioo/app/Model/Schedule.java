package com.callioo.app.Model;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Schedule {
    @Id
    private String meetingRoomId;
    @Column(nullable = false, unique = false)
    private String createdByEmail;
    private List<String> emails = new ArrayList<>();
    @Column(columnDefinition = "DATE")
    private LocalDate startDate;
    @Column(columnDefinition = "DATE")
    private LocalDate endDate;
    private LocalTime startTime;

    private boolean everyMonday;
    private boolean everyTuesday;
    private boolean everyWednesday;
    private boolean everyThursday;
    private boolean everyFriday;
    private boolean everySaturday;
    private boolean everySunday;

    // -------------------------
    // Getters & Setters
    // -------------------------

    public String getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(String meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public String getCreatedByEmail() {
        return createdByEmail;
    }

    public void setCreatedByEmail(String createdByEmail) {
        this.createdByEmail = createdByEmail;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails.addAll(emails);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public boolean getEveryMonday() {
        return everyMonday;
    }

    public void setEveryMonday(boolean everyMonday) {
        this.everyMonday = everyMonday;
    }

    public boolean getEveryTuesday() {
        return everyTuesday;
    }

    public void setEveryTuesday(boolean everyTuesday) {
        this.everyTuesday = everyTuesday;
    }

    public boolean getEveryWednesday() {
        return everyWednesday;
    }

    public void setEveryWednesday(boolean everyWednesday) {
        this.everyWednesday = everyWednesday;
    }

    public boolean getEveryThursday() {
        return everyThursday;
    }

    public void setEveryThursday(boolean everyThursday) {
        this.everyThursday = everyThursday;
    }

    public boolean getEveryFriday() {
        return everyFriday;
    }

    public void setEveryFriday(boolean everyFriday) {
        this.everyFriday = everyFriday;
    }

    public boolean getEverySaturday() {
        return everySaturday;
    }

    public void setEverySaturday(boolean everySaturday) {
        this.everySaturday = everySaturday;
    }

    public boolean getEverySunday() {
        return everySunday;
    }

    public void setEverySunday(boolean everySunday) {
        this.everySunday = everySunday;
    }

    // Helper function expand a schedule into individual meetings

    public List<Meeting> expandToMeetings(int browserTZOffset) {
        List<Meeting> meetings = new ArrayList<>();

        if (startDate == null || endDate == null || startTime == null) {
            return meetings;
        }

        LocalDate current = startDate;
        while (!current.isAfter(endDate)) {
            DayOfWeek dow = current.getDayOfWeek();

            boolean allowed = (dow == DayOfWeek.MONDAY && everyMonday) ||
                    (dow == DayOfWeek.TUESDAY && everyTuesday) ||
                    (dow == DayOfWeek.WEDNESDAY && everyWednesday) ||
                    (dow == DayOfWeek.THURSDAY && everyThursday) ||
                    (dow == DayOfWeek.FRIDAY && everyFriday) ||
                    (dow == DayOfWeek.SATURDAY && everySaturday) ||
                    (dow == DayOfWeek.SUNDAY && everySunday);

            if (allowed || !isRecurring()) {
                Instant startedAt = current.atTime(startTime)
                        .toInstant(ZoneOffset.ofTotalSeconds(browserTZOffset * -60));

                for (String email : emails) {
                    Meeting m = new Meeting(meetingRoomId, email, createdByEmail, startedAt);

                    meetings.add(m);
                }

                if (!isRecurring())
                    return meetings;
            }

            current = current.plusDays(1);
        }

        return meetings;
    }

    // Helper function to check if a schedule has recurring meetings
    public boolean isRecurring() {
        return this.everyMonday || this.everyTuesday || this.everyWednesday
                || this.everyThursday || this.everyFriday || this.everySaturday
                || this.everySunday;
    }

}
