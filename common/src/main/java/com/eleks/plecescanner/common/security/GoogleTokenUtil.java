package com.eleks.plecescanner.common.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

@Component
public class GoogleTokenUtil {

    public String getToken(String authorizationHeader, Principal principal) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            return authorizationHeader;
        } else if (principal != null) {
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;

            var authority = (OidcUserAuthority) oAuth2AuthenticationToken.getAuthorities().stream().findFirst().orElseThrow(IllegalStateException::new);
            return authority.getIdToken().getTokenValue();
        }
        else {
            return null;
        }
    }

    public String getEmail(String token) throws IOException {
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdToken idToken = GoogleIdToken.parse(jsonFactory, token);
        return idToken.getPayload().getEmail();
    }
}
