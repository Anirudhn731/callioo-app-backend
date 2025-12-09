package com.callioo.app.Controller;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callioo.app.Model.Meeting;
import com.callioo.app.Model.Schedule;
import com.callioo.app.Service.MeetingService;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "*")
public class MeetingRESTController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/createMeeting")
    public String createMeeting(@RequestParam String meetingRoomId, @RequestParam String email) {
        return meetingService.createMeeting(meetingRoomId, email);
    }

    @GetMapping("/getAllRooms")
    public List<String> getAllRooms() {
        return meetingService.getAllRooms();
    }

    @GetMapping("/getMeetingHistory")
    public List<Meeting> getMeetingHistory(@RequestParam String email) {
        return meetingService.getMeetingHistory(email);
    }

    @GetMapping("/getUpcomingMeetings")
    public List<Meeting> getUpcomingMeetings(@RequestParam String email) {
        return meetingService.getUpcomingMeetings(email);
    }

    @PutMapping("/updateEndedAt")
    public boolean updateEndedAt(@RequestParam String meetingRoomId, @RequestBody Map<String, Instant> timeStamps) {
        if (timeStamps.get("endedAt") == null)
            return false;
        final boolean res = meetingService.updateEndedAt(meetingRoomId,
                timeStamps.get("startedAt"), timeStamps.get("endedAt"));

        System.out.println("Returning updateEndedAt :- " + res);

        return res;
    }

    @PostMapping("/saveSchedule")
    public void saveSchedule(@RequestParam int browserTZOffset, @RequestBody Schedule schedule) {
        if (schedule != null)
            meetingService.saveSchedule(schedule, browserTZOffset);
    }

    @DeleteMapping("/deleteSchedule")
    public void deleteSchedule(@RequestParam String meetingRoomId) {
        meetingService.deleteSchedule(meetingRoomId);
    }

    @DeleteMapping("/deleteMeeting")
    public void deleteMeeting(@RequestParam String meetingRoomId) {
        meetingService.deleteMeeting(meetingRoomId);
    }

    @GetMapping("/getAllScheduleRooms")
    public List<String> getAllScheduleRooms() {
        return meetingService.getAllScheduleRooms();
    }

    @GetMapping("/getSchedule")
    public Schedule getSchedule(@RequestParam String meetingRoomId) {
        return meetingService.getSchedule(meetingRoomId);
    }

    @PostMapping("/getJwtToken")
    public String getJwtToken(@RequestParam String meetingRoomId, @RequestParam String email,
            @RequestBody Instant startedAt) {
        return meetingService.getJwtToken(meetingRoomId, email, startedAt);
    }

    @PostMapping("/joinImmediateMeeting")
    public String joinImmediateMeeting(@RequestParam String meetingRoomId, @RequestParam String email) {
        return meetingService.joinImmediateMeeting(meetingRoomId, email);
    }
}
