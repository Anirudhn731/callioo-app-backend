package com.callioo.app.Model;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class MeetingId implements Serializable {
    private String meetingRoomId;
    private String email;
    private Instant startedAt;

    public MeetingId() {
    }

    // Used for immediate meetings
    public MeetingId(String meetingRoomId, String email) {
        this.meetingRoomId = meetingRoomId;
        this.email = email;
        this.startedAt = Instant.now();
    }

    // Used for scheduled meetings
    public MeetingId(String meetingRoomId, String email, Instant startedAt) {
        this.meetingRoomId = meetingRoomId;
        this.email = email;
        this.startedAt = startedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof MeetingId))
            return false;

        MeetingId that = (MeetingId) o;
        return Objects.equals(this.email, that.email)
                && Objects.equals(this.meetingRoomId, that.meetingRoomId)
                && Objects.equals(this.startedAt, that.startedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingRoomId, email, startedAt);
    }
}
