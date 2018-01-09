package com.oauth;

import com.google.api.client.util.Key;

public class User {

	@Key("login")
	private String login;
	@Key("id")
	private int id;
	@Key("avatar_url")
	private String avatarUrl;
	public String getLogin() {
		return login;
	}
	public int getId() {
		return id;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	
	
	
}
