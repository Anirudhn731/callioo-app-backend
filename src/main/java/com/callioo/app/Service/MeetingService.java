package com.callioo.app.Service;

import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.callioo.app.Model.Meeting;
import com.callioo.app.Repository.MeetingRepository;
import com.callioo.app.Security.JaasJwtBuilder;

@Service
public class MeetingService {

    @Autowired
    private UserService userService;

    @Autowired
    private MeetingRepository meetingRepository;

    public Map<String, String> createMeeting(String email) {
        try {
            Meeting newMeeting = new Meeting(email, email);
            meetingRepository.save(newMeeting);
            final String meetingRoomId = newMeeting.getMeetingRoomId();

            RSAPrivateKey rsaPrivateKey = JaasJwtBuilder.getPemPrivateKey();
            String jaasJwtToken = JaasJwtBuilder.builder()
                    .withDefaults(true, meetingRoomId)
                    .withApiKey("")
                    .withUserName(userService.findByEmail(email).get().getFullName())
                    .withUserEmail(email)
                    .withOutboundEnabled(false)
                    .withTranscriptionEnabled(false)
                    .withAppID("")
                    .withUserAvatar("http://localhost:9000/api/avatar?email=" + email)
                    .signwith(rsaPrivateKey);

            return Map.of("room", meetingRoomId, "jaasJwttoken", jaasJwtToken);
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

}
