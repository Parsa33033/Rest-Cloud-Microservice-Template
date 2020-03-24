package com.template.oauth.config;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.template.oauth.model.Authority;
import com.template.oauth.model.Client;
import com.template.oauth.model.Role;
import com.template.oauth.model.User;
import com.template.oauth.repository.ClientRepository;
import com.template.oauth.repository.UserRepository;

@Component
public class AdminCreator {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserRepository userRepo;

	@Value("${admin.user.username}")
	private String userUsername;

	@Value("${admin.user.password}")
	private String userPassword;

	@Value("${admin.user.email}")
	private String userEmail;

	@Value("${admin.user.role}")
	private String userRole;

	@Autowired
	ClientRepository clientRepo;
	
	@Value("${admin.client.clientId}")
	private String clientId;

	@Value("${admin.client.clientSecret}")
	private String clientSecret;

	@Value("${admin.client.authority}")
	private String clientAuthority;
	
	@PostConstruct
	public void createAdminUser() {
		User user = userRepo.findUserByUsername(userUsername);
		if (user == null) {
			user = createUser();
			userRepo.saveAndFlush(user);
		}
	}

	public User createUser() {
		User user = new User();
		user.setUsername(userUsername);
		user.setEmail(userEmail);
		user.setEnabled(true);
		user.setPassword(passwordEncoder.encode(userPassword));
		Role role = new Role();
		role.setType(userRole);
		user.setRoles(Arrays.asList(role));
		return user;
	}
	
	@PostConstruct
	public void createAdminClient() {
		Client client = clientRepo.findClientByClientId(clientId);
		if (client == null) {
			client = createClient();
			clientRepo.saveAndFlush(client);
		} 
	}
	
	public Client createClient() {
		Client client = new Client();
		client.setClientId(clientId);
		client.setClientSecret(passwordEncoder.encode(clientSecret));
		client.setAccessTokenValiditySeconds(86400);
		client.setResourceIds(Arrays.asList("oauth2-server"));
		client.setScope(Arrays.asList("READ", "WRITE"));
		client.setAuthorizedGrantTypes(Arrays.asList("password", "client_credentials"));
		Authority authority = new Authority();
		authority.setType(clientAuthority);
		client.setAuthorities(Arrays.asList(authority));
		return client;
	}
}
