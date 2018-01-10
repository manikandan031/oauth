package com.oauth;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

public class AuthorizationCodeFlowFactory {
	
	//Data Store
	private static DataStoreFactory FILE_DATA_STORE_FACTORY;

	public static AuthorizationCodeFlow getInstance() throws IOException{
		
		FILE_DATA_STORE_FACTORY = new FileDataStoreFactory(new File("c:/store"));
		
		return new AuthorizationCodeFlow.Builder(
				BearerToken.queryParameterAccessMethod(),
				new NetHttpTransport(),
				new JacksonFactory(),
				new GenericUrl("https://github.com/login/oauth/access_token"),
				new ClientParametersAuthentication("/*Client ID*/", "/*Client Secret*/"),
				"bf2f42153ab5f8f9da91",
				"https://github.com/login/oauth/authorize").setScopes(Collections.singletonList("user:email")).
				setDataStoreFactory(FILE_DATA_STORE_FACTORY).
				build();
	}
}
