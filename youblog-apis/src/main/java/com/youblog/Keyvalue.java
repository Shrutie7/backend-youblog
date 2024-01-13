package com.youblog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component

public class Keyvalue {
	
@Value("${keycloak.realm}")
private String masterRealm;
@Value("${keycloak.resource}")
private String clientId;
@Value("${keycloak-grant-type}")
private String grantType;
@Value("${keycloak.credentials.secret}")
private String adminCredentialsSecret;
@Value("${keycloak-credentials-nonadmin}")
private String nonAdminCredentialsSecret;
@Value("${keycloak-ytclone-realm}")
private String ytclonerealm;
@Value("${keycloak.auth-server-url}")
private String authServerUrl;
}
