package com.callioo.app.Repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.callioo.app.Model.Meeting;
import com.callioo.app.Model.MeetingId;

import jakarta.transaction.Transactional;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, MeetingId> {

    public List<Meeting> findByMeetingRoomId(String meetingRoomId);

    public List<Meeting> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("""
                UPDATE Meeting m
                SET m.endedAt = :endedAt
                WHERE m.meetingRoomId = :meetingRoomId
                AND m.startedAt BETWEEN :startedAtMinus AND :startedAtPlus
            """)
    public int updateEndedAt(@Param("meetingRoomId") String meetingRoomId,
            @Param("startedAtMinus") Instant startedAtMinus,
            @Param("startedAtPlus") Instant startedAtPlus,
            @Param("endedAt") Instant endedAt);

    // @Query("""
    // SELECT m.jwtToken
    // FROM Meeting m
    // WHERE m.meetingRoomId = :meetingRoomId
    // AND m.email = :email
    // AND m.startedAt BETWEEN :startedAtMinus AND :startedAtPlus
    // """)
    // public String getJwtToken(@Param("meetingRoomId") String meetingRoomId,
    // @Param("email") String email,
    // @Param("startedAtMinus") Instant startedAtMinus,
    // @Param("startedAtPlus") Instant startedAtPlus);

    @Transactional
    public void deleteByMeetingRoomId(String meetingRoomId);
}
