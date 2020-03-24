package com.template.oauth.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Client implements ClientDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String clientId;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> resourceIds;

	private String clientSecret;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> scope;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> AuthorizedGrantTypes;

	@ElementCollection(fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private List<String> registeredRedirectUri;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "clients_authorities", joinColumns = {
			@JoinColumn(name = "client_id", referencedColumnName = "clientId") }, inverseJoinColumns = {
					@JoinColumn(name = "authority_id", referencedColumnName = "id") })
	private List<Authority> authorities;

	private int accessTokenValiditySeconds;

	@Override
	public String getClientId() {
		// TODO Auto-generated method stub
		return this.clientId;
	}

	@Override
	public Set<String> getResourceIds() {
		// TODO Auto-generated method stub
		return new HashSet<String>(this.resourceIds);
	}

	@Override
	public boolean isSecretRequired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getClientSecret() {
		// TODO Auto-generated method stub
		return this.clientSecret;
	}

	@Override
	public boolean isScoped() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Set<String> getScope() {
		// TODO Auto-generated method stub
		return new HashSet<String>(this.scope);
	}

	@Override
	public Set<String> getAuthorizedGrantTypes() {
		// TODO Auto-generated method stub
		return new HashSet<String>(this.AuthorizedGrantTypes);
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		// TODO Auto-generated method stub
		return new HashSet<String>(this.registeredRedirectUri);
	}

	@JsonIgnore
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
		for (Authority authority : this.authorities) {
			grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + authority.getType()));
		}
		return grantedAuthoritySet;
	}

	@Override
	public Integer getAccessTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return this.accessTokenValiditySeconds;
	}

	@Override
	public Integer getRefreshTokenValiditySeconds() {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public boolean isAutoApprove(String scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Map<String, Object> getAdditionalInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public void setResourceIds(List<String> resourceIds) {
		this.resourceIds = resourceIds;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public void setScope(List<String> scope) {
		this.scope = scope;
	}

	public void setAuthorizedGrantTypes(List<String> authorizedGrantTypes) {
		AuthorizedGrantTypes = authorizedGrantTypes;
	}

	public void setRegisteredRedirectUri(List<String> registeredRedirectUri) {
		this.registeredRedirectUri = registeredRedirectUri;
	}

	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

}
