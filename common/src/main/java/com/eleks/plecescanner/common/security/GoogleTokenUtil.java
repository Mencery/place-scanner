package com.eleks.plecescanner.common.security;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class GoogleTokenUtil {
    public String getToken(String authorizationHeader, Principal principal) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            return authorizationHeader;
        } else {
            var oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;

            //var authorityOptional =  oAuth2AuthenticationToken.getAuthorities().stream().filter(grantedAuthority -> Objects.equals(grantedAuthority.getAuthority(), "OICD_USER")).findFirst();
            //var authority = (OidcUserAuthority) authorityOptional.orElseThrow(IllegalStateException::new);
            var authority = (OidcUserAuthority) oAuth2AuthenticationToken.getAuthorities().stream().findFirst().orElseThrow(IllegalStateException::new);
            return authority.getIdToken().getTokenValue();
        }

    }
}
