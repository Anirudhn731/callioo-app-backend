package com.callioo.app.Security;

// import java.io.FileNotFoundException;
// import java.io.InputStream;
// import java.security.KeyFactory;
// import java.security.interfaces.RSAPrivateKey;
// import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
// import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

@Service
public class JaasJwtBuilder {
    // private static final String BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE
    // KEY-----";
    // private static final String END_PRIVATE_KEY = "-----END PRIVATE KEY-----";
    private static final long EXP_TIME_DELAY_SEC = 7200;
    private static final long NBF_TIME_DELAY_SEC = 10;
    private static final String JITSI_APP_ID = "callioo-jitsi-app";
    @Value("${jitsi.domain}")
    private String JITSI_DOMAIN;

    @Value("${jitsi.secret}")
    private String JITSI_SECRET;

    private JWTCreator.Builder jwtBuilder;
    private Algorithm algorithm;
    private Map<String, Object> userClaims;
    private Map<String, Object> featureClaims;

    private JaasJwtBuilder() {
        userClaims = new HashMap<>();
        featureClaims = new HashMap<>();
        jwtBuilder = JWT.create();
    }

    public JaasJwtBuilder withApiKey(String apiKey) {
        jwtBuilder.withKeyId(apiKey);
        return this;
    }

    public JaasJwtBuilder withUserAvatar(String url) {
        userClaims.put("avatar", url);
        return this;
    }

    public JaasJwtBuilder withModerator(boolean isModerator) {
        userClaims.put("moderator", isModerator);
        return this;
    }

    public JaasJwtBuilder withUserName(String userName) {
        userClaims.put("name", userName);
        return this;
    }

    public JaasJwtBuilder withUserEmail(String userEmail) {
        userClaims.put("email", userEmail);
        return this;
    }

    public JaasJwtBuilder withLiveStreamingEnabled(boolean isEnabled) {
        featureClaims.put("livestreaming", String.valueOf(isEnabled));
        return this;
    }

    public JaasJwtBuilder withRecordingEnabled(boolean isEnabled) {
        featureClaims.put("recording", String.valueOf(isEnabled));
        return this;
    }

    public JaasJwtBuilder withOutboundEnabled(boolean isEnabled) {
        featureClaims.put("outbound-call", String.valueOf(isEnabled));
        return this;
    }

    public JaasJwtBuilder withTranscriptionEnabled(boolean isEnabled) {
        featureClaims.put("transcription", String.valueOf(isEnabled));
        return this;
    }

    public JaasJwtBuilder withExpTime(long expTime) {
        jwtBuilder.withClaim("exp", expTime);
        return this;
    }

    public JaasJwtBuilder withNbfTime(long nbfTime) {
        jwtBuilder.withClaim("nbf", nbfTime);
        return this;
    }

    public JaasJwtBuilder withMeetingRoomId(String meetingRoomId) {
        jwtBuilder.withClaim("room", meetingRoomId);
        return this;
    }

    public JaasJwtBuilder withStartedAt(Instant startedAt) {
        jwtBuilder.withClaim("startedAt", startedAt);
        return this;
    }

    public JaasJwtBuilder withAppID(String appId) {
        jwtBuilder.withClaim("sub", appId);
        return this;
    }

    public JaasJwtBuilder withUserId(String userId) {
        userClaims.put("id", userId);
        return this;
    }

    public JaasJwtBuilder withDefaults(boolean isModerator, String meetingRoomId) {
        return this.withExpTime(Instant.now().getEpochSecond() + EXP_TIME_DELAY_SEC)
                .withNbfTime(Instant.now().getEpochSecond() - NBF_TIME_DELAY_SEC)
                .withLiveStreamingEnabled(false)
                .withRecordingEnabled(false)
                .withModerator(isModerator)
                .withMeetingRoomId(meetingRoomId)
                .withAppID(JITSI_DOMAIN);
    }

    public String signWith() {
        System.out.println("Jitsi secret :- " + JITSI_SECRET);
        System.out.println("Jitsi domain :- " + JITSI_DOMAIN);
        this.algorithm = Algorithm.HMAC256(JITSI_SECRET);
        Map<String, Object> context = new HashMap<>() {
            {
                put("user", userClaims);
                put("features", featureClaims);
            }
        };

        return jwtBuilder
                .withClaim("iss", JITSI_APP_ID)
                .withClaim("aud", "jitsi")
                .withClaim("context", context)
                .sign(this.algorithm);
    }

    // public String signwith(RSAPrivateKey privateKey) {
    // algorithm = Algorithm.RSA256(null, privateKey);
    // Map<String, Object> context = new HashMap<>() {
    // {
    // put("user", userClaims);
    // put("features", featureClaims);
    // }
    // };

    // return jwtBuilder.withClaim("iss", JITSI_APP_ID)
    // .withClaim("aud", "jitsi")
    // .withClaim("context", context)
    // .sign(this.algorithm);
    // }

    // public static RSAPrivateKey getPemPrivateKey() throws Exception {
    // final InputStream PRIVATE_KEY_STREAM =
    // JaasJwtBuilder.class.getResourceAsStream("/keys/jaas-key.pk");
    // if (PRIVATE_KEY_STREAM == null)
    // throw new FileNotFoundException("Private key not found!");

    // String pem = new String(PRIVATE_KEY_STREAM.readAllBytes());
    // System.out.println("Key :- " + pem);
    // String privKey = pem.replace(BEGIN_PRIVATE_KEY, "")
    // .replace(END_PRIVATE_KEY, "")
    // .replaceAll("\\s", "");
    // Base64.Decoder b64 = Base64.getDecoder();
    // byte[] decoded = b64.decode(privKey);

    // PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
    // KeyFactory kf = KeyFactory.getInstance("RSA");

    // return (RSAPrivateKey) kf.generatePrivate(spec);
    // }

}
