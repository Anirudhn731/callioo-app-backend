package com.callioo.app.Service;

// import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.callioo.app.Model.Meeting;
import com.callioo.app.Model.Schedule;
import com.callioo.app.Repository.MeetingRepository;
import com.callioo.app.Repository.ScheduleRepository;
import com.callioo.app.Security.JaasJwtBuilder;
import com.callioo.app.Security.SecurityConfig;

import jakarta.transaction.Transactional;

@Service
public class MeetingService {

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private JaasJwtBuilder jaasJwtBuilder;

    public String createMeeting(String meetingRoomId, String email) {
        try {
            System.out.println("Creating a meeting...");
            Meeting newMeeting = new Meeting(meetingRoomId, email, email);
            meetingRepository.save(newMeeting);
            System.out.println("Meeting Created :- " + newMeeting.getMeetingRoomId());

            System.out.println("Creating Jitsi jwt token...");
            // RSAPrivateKey rsaPrivateKey = JaasJwtBuilder.getPemPrivateKey();
            String jwtToken = jaasJwtBuilder
                    .withDefaults(true, newMeeting.getMeetingRoomId())
                    .withUserName(userService.findByEmail(email).get().getFullName())
                    .withUserEmail(email)
                    .withUserAvatar(SecurityConfig.BACKEND_URL + "api/avatar?email=" + email)
                    .withStartedAt(newMeeting.getStartedAt())
                    // .signwith(rsaPrivateKey);
                    .signWith();
            System.out.println("Jitsi jwt token created :- " + jwtToken);

            return jwtToken;
        } catch (Exception e) {
            System.out.println("Exception occurred while creating meeting...");
            System.out.println(e.getMessage());

            return null;
        }
    }

    public List<Meeting> getMeetingHistory(String email) {
        return meetingRepository.findByEmail(email).stream()
                .filter((meeting) -> (meeting.getEndedAt() != null
                        || Instant.now().minus(Duration.ofHours(24)).isAfter(meeting.getStartedAt())))
                .sorted(Comparator.comparing(Meeting::getStartedAt).reversed()).toList();
    }

    public List<Meeting> getUpcomingMeetings(String email) {
        List<Meeting> upcomingMeetings = meetingRepository.findByEmail(email).stream()
                .filter((meeting) -> (meeting.getEndedAt() == null
                        || !Instant.now().minus(Duration.ofHours(24)).isAfter(meeting.getStartedAt())))
                .sorted(Comparator.comparing(Meeting::getStartedAt)).toList();

        List<String> scheduleRooms = getAllScheduleRooms();
        List<String> upComingRooms = upcomingMeetings.stream().map(meeting -> meeting.getMeetingRoomId()).toList();
        for (String room : scheduleRooms) {
            if (!upComingRooms.contains(room)) {
                scheduleRepository.deleteByMeetingRoomId(room);
            }
        }

        return upcomingMeetings;
    }

    public List<String> getAllRooms() {
        return meetingRepository.findAll().stream().map((meeting) -> meeting.getMeetingRoomId()).distinct().toList();
    }

    @Transactional
    public boolean updateEndedAt(String meetingRoomId, Instant startedAt, Instant endedAt) {
        Instant startedAtMinus = startedAt.minusMillis(1);
        Instant startedAtPlus = startedAt.plusSeconds(1);

        int count = meetingRepository.updateEndedAt(meetingRoomId, startedAtMinus, startedAtPlus, endedAt);

        return count > 0;
    }

    public void saveSchedule(@NonNull Schedule schedule, int browserTZOffset) {
        deleteSchedule(schedule.getMeetingRoomId());
        System.out.println("Saving Schedule...");
        scheduleRepository.save(schedule);
        List<Meeting> meetings = schedule.expandToMeetings(browserTZOffset);
        try {
            if (meetings != null && meetings.size() > 0) {
                for (Meeting meeting : meetings) {
                    if (meeting != null)
                        meetingRepository.save(meeting);
                }

            } else {
                throw new RuntimeException("Schedule Expanded no meetings");
            }
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.println("Deleting Schedule...");
            deleteSchedule(schedule.getMeetingRoomId());
        } catch (Exception e) {
            System.out.println("Exception occurred while retrieving key :- " + e.getMessage());
            System.out.println("Deleting Schedule...");
            deleteSchedule(schedule.getMeetingRoomId());
        }
    }

    public void deleteSchedule(String meetingRoomId) {
        meetingRepository.deleteByMeetingRoomId(meetingRoomId);
        scheduleRepository.deleteByMeetingRoomId(meetingRoomId);
    }

    public void deleteMeeting(String meetingRoomId) {
        meetingRepository.deleteByMeetingRoomId(meetingRoomId);
    }

    public List<String> getAllScheduleRooms() {
        return scheduleRepository.findAll().stream()
                .map((s) -> s.getMeetingRoomId()).toList();
    }

    public Schedule getSchedule(String meetingRoomId) {
        return scheduleRepository.findByMeetingRoomId(meetingRoomId);

    }

    public String getJwtToken(String meetingRoomId, String email, Instant startedAt) {
        System.out.println("Creating Jitsi jwt token...");
        String createdByEmail = meetingRepository.findByMeetingRoomId(meetingRoomId).getFirst().getCreatedByEmail();
        try {
            // RSAPrivateKey rsaPrivateKey = JaasJwtBuilder.getPemPrivateKey();
            String jwtToken = jaasJwtBuilder
                    .withDefaults(createdByEmail == email, meetingRoomId)
                    .withUserName(userService.findByEmail(email).get().getFullName())
                    .withUserEmail(email)
                    .withUserAvatar(SecurityConfig.BACKEND_URL + "api/avatar?email=" + email)
                    .withStartedAt(startedAt)
                    // .signwith(rsaPrivateKey);
                    .signWith();
            System.out.println("Jitsi jwt token created :- " + jwtToken);
            return jwtToken;
        } catch (Exception e) {
            System.out.println("Excpetion occurred while creating token :- " + e.getMessage());
            return null;
        }
    }

    public String joinImmediateMeeting(String meetingRoomId, String email) {
        Meeting createdMeeting = meetingRepository.findByMeetingRoomId(meetingRoomId).stream()
                .sorted(Comparator.comparing(Meeting::getStartedAt)).findFirst().orElse(null);
        if (createdMeeting == null)
            return null;

        meetingRepository.save(
                new Meeting(meetingRoomId, email, createdMeeting.getCreatedByEmail(), createdMeeting.getStartedAt()));
        return getJwtToken(meetingRoomId, email, createdMeeting.getStartedAt());
    }

}
