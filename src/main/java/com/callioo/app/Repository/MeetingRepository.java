package com.callioo.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.callioo.app.Model.Meeting;
import com.callioo.app.Model.MeetingId;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, MeetingId> {

}
