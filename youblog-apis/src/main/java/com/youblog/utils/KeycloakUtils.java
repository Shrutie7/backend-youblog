package com.youblog.utils;

import java.util.Map;

import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.youblog.Keyvalue;
import com.youblog.payloads.CredentialDTO;
import com.youblog.payloads.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KeycloakUtils {

	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private final RestTemplate restTemplate;

	@Autowired
	private Keyvalue keyValue;

	@Autowired
	public KeycloakUtils(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public Map<String, String> getAdminToken() {
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(keyValue.getClientId(), keyValue.getAdminCredentialsSecret());
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		HttpEntity<String> request = new HttpEntity<>("grant_type=" + keyValue.getGrantType(), headers);
		String accessTokenURL = keyValue.getAuthServerUrl() + "/realms/master/protocol/openid-connect/token";
		ResponseEntity<Object> response = restTemplate.exchange(accessTokenURL, HttpMethod.POST, request, Object.class,
				keyValue.getMasterRealm());
		return (Map<String, String>) response.getBody();
	}

	public Boolean checkPersonalNoInKeycloak(String username, String adminToken) {
		UserRepresentation[] users = fetchKeycloakUsers(adminToken);
		if (users != null && users.length > 0) {

			System.out.println(username);

			for (UserRepresentation user : users) {
				System.out.println(user.getUsername());
				if (username.equals(user.getUsername())) {
					log.info(user.toString());
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public Boolean checkEmailIdInKeycloak(String emailId, String adminToken) {
		UserRepresentation[] users = fetchKeycloakUsers(adminToken);
		if (users != null && users.length > 0) {
			for (UserRepresentation user : users) {
				if (emailId.equals(user.getEmail())) {
					log.info("KeycloakEmailIdFetcher--------------------->" + user.getEmail());
					return true;
				}
			}
			return false;
		}
		return false;
	}

	public UserRepresentation[] fetchKeycloakUsers(String adminToken) {
		log.info("admintoken=======>" + adminToken);
		String url = "/" + keyValue.getYtclonerealm() + "/users";
		final String KEYCLOAK_USERS_URL = keyValue.getAuthServerUrl() + "/admin/realms" + url + "?first=0&max=10000000";
		log.info("KEYCLOAK_USERS_URL======" + KEYCLOAK_USERS_URL);

		final String ADMIN_TOKEN = adminToken;

		HttpHeaders headers = new HttpHeaders();
		headers.set(AUTHORIZATION, BEARER + ADMIN_TOKEN);
		ResponseEntity<UserRepresentation[]> response = restTemplate.exchange(KEYCLOAK_USERS_URL, HttpMethod.GET,
				new HttpEntity<>(headers), UserRepresentation[].class, keyValue.getYtclonerealm());
		if (response.getStatusCode().is2xxSuccessful()) {
			UserRepresentation[] users = response.getBody();
			if (users != null && users.length > 0) {
				return users;
			} else {
				return null;
			}
		} else {
			log.info("Failed to fetch users. Response: " + response.getBody());
		}
		return null;
	}

	public ResponseEntity<Map<String, Object>> keycloakUserCreator(String username, String password, String firstName,
			String lastName, String email, boolean active, String adminToken) {
		String url = "/" + keyValue.getYtclonerealm() + "/users";
		final String KEYCLOAK_USERS_URL = keyValue.getAuthServerUrl() + "/admin/realms" + url;
		final String ADMIN_TOKEN = adminToken;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(AUTHORIZATION, BEARER + ADMIN_TOKEN);
		boolean user = checkPersonalNoInKeycloak(username, ADMIN_TOKEN);
		boolean emailUser = checkEmailIdInKeycloak(email, ADMIN_TOKEN);
		if (!user) {
			if (!emailUser) {
				UserDTO userDTO = new UserDTO();
				userDTO.setUsername(username);
				CredentialDTO credentialDTO = new CredentialDTO();
				credentialDTO.setType("password");
				credentialDTO.setValue(password);
				credentialDTO.setTemporary(true);
				userDTO.setCredentials(new CredentialDTO[] { credentialDTO });
				userDTO.setEnabled(active);
				userDTO.setFirstName(firstName);
				userDTO.setLastName(lastName);
				userDTO.setEmail(email);
				HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);

				ResponseEntity<String> response = restTemplate.exchange(KEYCLOAK_USERS_URL, HttpMethod.POST, request,
						String.class, keyValue.getYtclonerealm());

				if (response.getStatusCode().is2xxSuccessful()) {
					return ResponseHandler.response(null, "User created successfully", true);

				} else {
					return ResponseHandler.response(response.getBody(), "Failed to create user. Response: ", false);

				}
			} else {
				return ResponseHandler.response(null, "EmailId already taken", false);
			}
		} else {
			return ResponseHandler.response(null, "Email Id already taken", false);
		}
	}

	public ResponseEntity<Map<String, Object>> keycloakUserDelete(String username, String adminToken) {
		UserRepresentation users = fetchKeycloakId(username.toLowerCase(), adminToken);
		String url = "/" + keyValue.getYtclonerealm() + "/users/" + users.getId();
		final String keycloakUrl = keyValue.getAuthServerUrl() + "/admin/realms" + url;

		final String ADMIN_TOKEN = adminToken;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(AUTHORIZATION, BEARER + ADMIN_TOKEN);
		HttpEntity<UserDTO> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(keycloakUrl, HttpMethod.DELETE, request, String.class,
				keyValue.getYtclonerealm(), users.getId());

		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("User Deleted successfully");
			return ResponseHandler.response(null, "User Deleted successfully", true);
		} else {
			log.info("Failed to delete user. Response: " + response.getBody());
			return ResponseHandler.response(null, "Failed to delete user. Response: " + response.getBody(), false);

		}
	}

	public boolean validatePassword(String userName, String Password) {

		String url = keyValue.getAuthServerUrl() + "/realms/" + keyValue.getYtclonerealm()
				+ "/protocol/openid-connect/token";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> multi = new LinkedMultiValueMap<>();

		multi.add("grant_type", "password");
		multi.add("client_id", keyValue.getClientId());
		multi.add("username", userName);
		multi.add("password", Password);
		multi.add("client_secret", keyValue.getNonAdminCredentialsSecret());

		System.out.println(multi);
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(multi, headers);
		System.out.println(request);
		System.out.println(url);
		ResponseEntity<String> response = null;

		try {
			response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

			return response.getStatusCode() == HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public UserRepresentation fetchKeycloakId(String userName, String adminToken) {

		UserRepresentation[] keycloakusers = fetchKeycloakUsers(adminToken);
		if (keycloakusers.length > 0) {
			for (UserRepresentation userRepresentation : keycloakusers) {
				System.out.println(userRepresentation.getUsername());
				System.out.println(userName);
				if (userName.equals(userRepresentation.getUsername())) {

					return userRepresentation;
				}
			}
		}
		return null;
	}

	public ResponseEntity<Map<String, Object>> updatePassword(String email, String newpassword, String token) {

		String id = fetchKeycloakId(email, token).getId();

		String url = keyValue.getAuthServerUrl() + "/admin/realms/" + keyValue.getYtclonerealm() + "/users/" + id
				+ "/reset-password";
		System.out.println(url);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(AUTHORIZATION, BEARER + token);

		CredentialRepresentation cred = new CredentialRepresentation();
		cred.setType("password");
		cred.setValue(newpassword);
		cred.setTemporary(false);
		HttpEntity<CredentialRepresentation> request = new HttpEntity<>(cred, headers);
		System.out.println(request);
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, request, String.class,
				keyValue.getYtclonerealm(), id);

		if (response.getStatusCode().is2xxSuccessful()) {
			return ResponseHandler.response(null, "password reset successfully", true);
		}
		return ResponseHandler.response(null, "password cannot be updated", false);
	}

	public ResponseEntity<Map<String, Object>> keycloakAuthorizeUser(String token) {
		System.out.println("inside keycloak authorize user method..");
		final String keycloakUrl = keyValue.getAuthServerUrl() + "/realms/" + keyValue.getYtclonerealm()
				+ "/protocol/openid-connect/userinfo";
		System.out.println(keycloakUrl);
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Content-Type", "application/json");
		headers.add("Authorization", "Bearer " + token);
		HttpEntity<UserDTO> request = new HttpEntity<>(headers);
		try {
		ResponseEntity<String> response = restTemplate.exchange(keycloakUrl, HttpMethod.GET, request, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			log.info("Authorization Successful");
			return ResponseHandler.response(null, "Authorized successfully", true);
		} else {
			log.info("Failed to authorize user. Response: " + response.getBody());
			return ResponseHandler.response(null, response.getBody(), false);
		}
		}catch (HttpClientErrorException.Unauthorized unauthorizedException) {
			System.out.println(unauthorizedException.getLocalizedMessage());
			return ResponseHandler.response(null, unauthorizedException.getLocalizedMessage(), false);
		}
	}
}
