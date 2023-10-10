package com.eleks.placescanner.common.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
public class GoogleTokenVerifier {

    @SneakyThrows
    public boolean isTokenValid(String token, String clientId) {
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();

        GoogleIdToken idToken = GoogleIdToken.parse(verifier.getJsonFactory(), token);
        return verifier.verify(idToken);
    }

    public boolean isAuthorizationHeaderEmpty(String token) {
        return token == null || token.isEmpty();
    }

    public boolean isAuthorizationHeaderNotEmpty(String token) {
        return !isAuthorizationHeaderEmpty(token);
    }
}
