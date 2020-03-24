package com.template.oauth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.template.oauth.auth.AddResourceToClientResponse;
import com.template.oauth.auth.ClientCreationResponse;
import com.template.oauth.auth.DeleteClientResponse;
import com.template.oauth.auth.DisableUserResponse;
import com.template.oauth.auth.LogoutRequest;
import com.template.oauth.auth.LogoutResponse;
import com.template.oauth.auth.UserRegistrationResponse;
import com.template.oauth.model.Client;
import com.template.oauth.model.User;
import com.template.oauth.repository.ClientRepository;
import com.template.oauth.repository.UserRepository;

@RestController
public class MainController {

	@Autowired
	ClientRepository clientRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	RedisTemplate<String, String> redis;

	@GetMapping("/")
	public String hello() {
		return "hello";
	}

	/**
	 * creating client by user Admin
	 * 
	 * @param client
	 * @return
	 */
	@PostMapping(value = "/create-client", consumes = { "application/json" })
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<ClientCreationResponse> createClient(@RequestBody Client client) {

		Client temp = clientRepo.findClientByClientId(client.getClientId());
		if (temp != null) {
			return ResponseEntity.ok(clientCreationResponse(client.getClientId(), false, "client already exists!"));
		}
		client.setClientSecret(passwordEncoder.encode(client.getClientSecret()));
		clientRepo.saveAndFlush(client);
		return ResponseEntity.ok(clientCreationResponse(client.getClientId(), true, "client created!"));
	}

	/**
	 * creating a response for client creation
	 * 
	 * @param clientId
	 * @param created
	 * @param message
	 * @return
	 */
	public ClientCreationResponse clientCreationResponse(String clientId, boolean created, String message) {
		ClientCreationResponse response = new ClientCreationResponse();
		response.setClientId(clientId);
		response.setCreated(created);
		response.setMessage(message);
		return response;
	}

	/**
	 * remove client by client id
	 * @param clientId
	 * @return
	 */
	@PostMapping(value = "/remove-client", consumes = "application/x-www-form-urlencoded")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<DeleteClientResponse> removeClient(@RequestParam("client-id") String clientId) {
		Client client = clientRepo.findClientByClientId(clientId);
		DeleteClientResponse response = new DeleteClientResponse();
		if (client == null) {
			response.setClientId(clientId);
			response.setDeleted(false);
			response.setMessage("client does not exist!");
			return ResponseEntity.ok(response);
		}
		clientRepo.delete(client);
		response.setClientId(clientId);
		response.setDeleted(true);
		response.setMessage("client deleted!");
		return ResponseEntity.ok(response);
	}

	/**
	 * add resource id to a client using client id
	 * 
	 * @param clientId
	 * @param resourceId
	 * @return
	 */
	@PostMapping(value = "/add-resources-to-client")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<AddResourceToClientResponse> addResourceToClient(@RequestParam("client-id") String clientId,
			@RequestParam("resource-id") String resourceId) {
		Client client = clientRepo.findClientByClientId(clientId);
		AddResourceToClientResponse response = new AddResourceToClientResponse();
		if (client == null) {
			response.setClientId(clientId);
			response.setResourceAdded(false);
			response.setMessage("clinet does not exist!");
			return ResponseEntity.ok(response);
		}
		Set<String> resourceIds = client.getResourceIds();
		resourceIds.add(resourceId);
		client.setResourceIds(new ArrayList<String>(resourceIds));
		clientRepo.saveAndFlush(client);
		response.setClientId(clientId);
		response.setResourceAdded(true);
		response.setMessage("resource " + resourceId + " added to " + clientId);
		return ResponseEntity.ok(response);
	}
	
	/**
	 * get client info by client id
	 * @param clientId
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/get-client")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Client> getClient(@RequestParam("client-id") String clientId) throws Exception{
		Client client = clientRepo.findClientByClientId(clientId);
		if (client == null) {
			throw new Exception("client does not exist!");
		}
		return ResponseEntity.ok(client);
	}

	/**
	 * disable user by username and jwt
	 * 
	 * @param username
	 * @param authorizationHeader
	 * @return
	 */
	@PostMapping(value = "/disable-user")
	public ResponseEntity<DisableUserResponse> disableUser(@RequestParam("username") String username,
			@RequestHeader("Authorization") String authorizationHeader) {
		User user = userRepo.findUserByUsername(username);
		DisableUserResponse response = new DisableUserResponse();
		response.setUsername(username);
		if (user == null) {
			response.setDisabled(false);
			response.setMessage("user does not exist!");
			return ResponseEntity.ok(response);
		}
		String jwt = authorizationHeader.substring(7);
		Jwt jwtContent = JwtHelper.decode(jwt);
		String claims = jwtContent.getClaims();
		JSONObject json = new JSONObject(claims);
		String jwtUsername = json.getString("user_name");
		if (jwtUsername.equals(username)) {
			user.setEnabled(false);
			userRepo.saveAndFlush(user);
		}
		response.setDisabled(true);
		response.setMessage("user has been disabled!");
		return ResponseEntity.ok(response);
	}

	/**
	 * registering user
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping(value = "/register-user", consumes = { "application/json" })
	public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody User user) {
		List<User> temp = userRepo.getUsernameOrEmail(user.getUsername(), user.getEmail());
		if (temp.iterator().hasNext()) {
			return ResponseEntity.ok(userRegistrationResponse(user.getUsername(), false, "user already exists!"));
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(true);
		userRepo.saveAndFlush(user);
		return ResponseEntity.ok(userRegistrationResponse(user.getUsername(), true, "user registered!"));
	}

	/**
	 * creating a reponse for user registration
	 * 
	 * @param username
	 * @param registered
	 * @param message
	 * @return
	 */
	public UserRegistrationResponse userRegistrationResponse(String username, boolean registered, String message) {
		UserRegistrationResponse response = new UserRegistrationResponse();
		response.setUsername(username);
		response.setRegistered(registered);
		response.setMessage(message);
		return response;
	}

	/**
	 * logout user by addin its JWT token to blacklist in redis
	 * 
	 * @param request
	 * @param authorizationHeader
	 * @return
	 */
	@PostMapping(value = "/logout-user", consumes = { "application/json" })
	public ResponseEntity<LogoutResponse> userLogout(@RequestBody LogoutRequest request,
			@RequestHeader("Authorization") String authorizationHeader) {

		String jwtTmp = authorizationHeader.substring(7);
		String jwt = request.getJwt();
		String username = request.getUsername();
		if (!jwtTmp.equals(jwt)) {
			return ResponseEntity.ok(logoutResponse(username, false, "wrong jwt entered!"));
		}

		// add Jwt to blacklist
		redis.opsForSet().add(jwt, username);
		return ResponseEntity.ok(logoutResponse(username, true, "user logged out!"));
	}

	/**
	 * response for logging out user
	 * 
	 * @param username
	 * @param loggedOut
	 * @param message
	 * @return
	 */
	public LogoutResponse logoutResponse(String username, boolean loggedOut, String message) {
		LogoutResponse response = new LogoutResponse();
		response.setUsername(username);
		response.setLoggedOut(loggedOut);
		response.setMessage(message);
		return response;
	}
}
