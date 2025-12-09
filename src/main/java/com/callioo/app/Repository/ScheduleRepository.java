package com.callioo.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.callioo.app.Model.Schedule;

import jakarta.transaction.Transactional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {

    @Transactional
    public void deleteByMeetingRoomId(String meetingRoomId);

    public Schedule findByMeetingRoomId(String meetingRoomId);

}
