package com.callioo.app.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.callioo.app.Service.MeetingService;

@RestController
@RequestMapping("/api/meetings")
@CrossOrigin(origins = "http://localhost:5173")
public class MeetingRESTController {

    @Autowired
    private MeetingService meetingService;

    @PostMapping("/createMeeting")
    public Map<String, String> createMeeting(@RequestParam String email) {
        return meetingService.createMeeting(email);

    }
}
